package mina.pool;

import org.apache.log4j.Logger;

public class ThreadPool {
	private static final Logger logger = Logger.getLogger(ThreadPool.class);
	
	private static ThreadPool instance;
	
	/**
	 * 单例模式，整个服务器只使用一个线程池
	 * @return
	 */
	public static ThreadPool getInstance(){
		if(instance == null){
			instance = new ThreadPool();
			instance.start();
		}
		return instance;
	}

	/*
	 * Default values ...
	 */
	public static final int MAX_THREADS       = 300;// maxThreads的最大值
	public static final int MAX_THREADS_MIN   = 10;// maxThreads的最小值
	public static final int MAX_SPARE_THREADS = 50;// 最大空闲线程
	public static final int MIN_SPARE_THREADS = 4;// 最小空闲线程（当线程池初始化时就启动这么多线程） 当要追加开启线程时，追加开启这么多线程
	public static final int WORK_WAIT_TIMEOUT = 60 * 1000;// 最大等待时间（1分钟），Monitor每隔这么长时间检查一次空闲线程

	/*
	 * Where the threads are held.
	 */
	protected ControlRunnable[] pool = null;

	/*
	 * A monitor thread that monitors the pool for idel threads.
	 */
	protected MonitorRunnable monitor;

	/*
	 * Max number of threads that you can open in the pool.
	 */
	protected int maxThreads;

	/*
	 * Min number of idel threads that you can leave in the pool.
	 */
	protected int minSpareThreads;

	/*
	 * Max number of idel threads that you can leave in the pool.
	 */
	protected int maxSpareThreads;

	/*
	 * Number of threads in the pool.
	 */
	protected int currentThreadCount;

	/*
	 * Number of busy threads in the pool.
	 */
	protected int currentThreadsBusy;

	/*
	 * Flag that the pool should terminate all the threads and stop.
	 */
	protected boolean stopThePool;

	/* Flag to control if the main thread is 'daemon' */
	protected boolean isDaemon = true;
	
	public String toString(){
		return "busy=" + currentThreadsBusy + " currentCount=" + currentThreadCount ;
	}

	/**
	 * Name of the threadpool
	 */
	protected String name = "TP";

	/**
	 * Sequence.
	 */
	protected int sequence = 1;

	/**
	 * Thread priority.
	 */
	protected int threadPriority = Thread.NORM_PRIORITY;

	/**
	 * Constructor
	 * 整个服务器可以配置多个线程池
	 */
	public ThreadPool() {
		maxThreads = MAX_THREADS;
		maxSpareThreads = MAX_SPARE_THREADS;
		minSpareThreads = MIN_SPARE_THREADS;
		currentThreadCount = 0;
		currentThreadsBusy = 0;
		stopThePool = false;
	}
	
	public ThreadPool(int maxThread){
		maxThreads = maxThread;
		maxSpareThreads = MAX_SPARE_THREADS;
		minSpareThreads = MIN_SPARE_THREADS;
		currentThreadCount = 0;
		currentThreadsBusy = 0;
		stopThePool = false;
	}
	/**
	 * 启动线程池
	 *
	 */
	public synchronized void start() {
		stopThePool = false;
		currentThreadCount = 0;
		currentThreadsBusy = 0;

		adjustLimits();

		pool = new ControlRunnable[maxThreads];

		openThreads(minSpareThreads);
		if (maxSpareThreads < maxThreads) {
			monitor = new MonitorRunnable(this);
		}
	}

	public MonitorRunnable getMonitor() {
		return monitor;
	}

	/**
	 * Returns the priority level of current and future threads in this pool.
	 * 
	 * @return The priority
	 */
	public int getThreadPriority() {
		return threadPriority;
	}

	public void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
	}

	public int getMaxThreads() {
		return maxThreads;
	}

	public void setMinSpareThreads(int minSpareThreads) {
		this.minSpareThreads = minSpareThreads;
	}

	public int getMinSpareThreads() {
		return minSpareThreads;
	}

	public void setMaxSpareThreads(int maxSpareThreads) {
		this.maxSpareThreads = maxSpareThreads;
	}

	public int getMaxSpareThreads() {
		return maxSpareThreads;
	}

	public int getCurrentThreadCount() {
		return currentThreadCount;
	}

	public int getCurrentThreadsBusy() {
		return currentThreadsBusy;
	}

	public boolean isDaemon() {
		return isDaemon;
	}

	/**
	 * The default is true - the created threads will be in daemon mode. If set
	 * to false, the control thread will not be daemon - and will keep the
	 * process alive.
	 */
	public void setDaemon(boolean b) {
		isDaemon = b;
	}

	public boolean getDaemon() {
		return isDaemon;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	private int getSequence() {
		return sequence++;
	}

	/**
	 * 运行一个Runnable 对象，这是线程池运行一个 Runnable对象的接口
	 * 
	 * @param target
	 *            要运行的对象
	 */
	public void run(Runnable target) {
		ControlRunnable c = findControlRunnable();// 找到一个空闲的ControlRunnable对象，用它来运行target
		c.runIt(target);
	}

	private ControlRunnable findControlRunnable() {
		ControlRunnable c = null;

		if (stopThePool) {
			throw new IllegalStateException();
		}

		// Obtain a free thread from the pool.
		synchronized (this) {

			while (currentThreadsBusy == currentThreadCount) {
				// All threads are busy
				if (currentThreadCount < maxThreads) {
					// Not all threads were open,
					// Open new threads up to the max number of idel threads
					int toOpen = currentThreadCount + minSpareThreads;
					openThreads(toOpen);
				} else {
					// Wait for a thread to become idel.
					try {
						this.wait();
					}
					// was just catch Throwable -- but no other
					// exceptions can be thrown by wait, right?
					// So we catch and ignore this one, since
					// it'll never actually happen, since nowhere
					// do we say pool.interrupt().
					catch (InterruptedException e) {
					}

					// Pool was stopped. Get away of the pool.
					if (stopThePool) {
						break;
					}
				}
			}
			// Pool was stopped. Get away of the pool.
			if (0 == currentThreadCount || stopThePool) {
				throw new IllegalStateException();
			}

			// If we are here it means that there is a free thread. Take it.
			int pos = currentThreadCount - currentThreadsBusy - 1;
			c = pool[pos];
			pool[pos] = null;
			currentThreadsBusy++;

		}
		return c;
	}



	/**
	 * Stop the thread pool
	 */
	public synchronized void shutdown() {
		if (!stopThePool) {
			stopThePool = true;
			if (monitor != null) {
				monitor.terminate();
				monitor = null;
			}
			for (int i = 0; i < currentThreadCount - currentThreadsBusy; i++) {
				try {
					pool[i].terminate();
				} catch (Throwable t) {
				}
			}
			currentThreadsBusy = currentThreadCount = 0;
			pool = null;
			notifyAll();
		}
	}

	/**
	 * Called by the monitor thread to harvest idle threads.
	 */
	protected synchronized void checkSpareControllers() {

		if (stopThePool) {
			return;
		}

		if ((currentThreadCount - currentThreadsBusy) > maxSpareThreads) {
			int toFree = currentThreadCount - currentThreadsBusy
					- maxSpareThreads;

			for (int i = 0; i < toFree; i++) {
				ControlRunnable c = pool[currentThreadCount
						- currentThreadsBusy - 1];
				c.terminate();
				pool[currentThreadCount - currentThreadsBusy - 1] = null;
				currentThreadCount--;
			}

		}

	}

	/**
	 * Returns the thread to the pool. Called by threads as they are becoming
	 * idel.
	 */
	protected synchronized void returnController(ControlRunnable c) {

		if (0 == currentThreadCount || stopThePool) {
			c.terminate();
			return;
		}

		// atomic
		currentThreadsBusy--;

		pool[currentThreadCount - currentThreadsBusy - 1] = c;
		notify();
	}

	/**
	 * Inform the pool that the specific thread finish.
	 * 
	 * Called by the ControlRunnable.run() when the runnable throws an
	 * exception.
	 */
	protected synchronized void notifyThreadEnd(ControlRunnable c) {
		currentThreadsBusy--;
		currentThreadCount--;
		notify();
	}

	/*
	 * Checks for problematic configuration and fix it. The fix provides
	 * reasonable settings for a single CPU with medium load.
	 */
	protected void adjustLimits() {
		if (maxThreads <= 0) {
			maxThreads = MAX_THREADS;
		} else if (maxThreads < MAX_THREADS_MIN) {
			maxThreads = MAX_THREADS_MIN;
		}

		if (maxSpareThreads >= maxThreads) {
			maxSpareThreads = maxThreads;
		}

		if (maxSpareThreads <= 0) {
			if (1 == maxThreads) {
				maxSpareThreads = 1;
			} else {
				maxSpareThreads = maxThreads / 2;
			}
		}

		if (minSpareThreads > maxSpareThreads) {
			minSpareThreads = maxSpareThreads;
		}

		if (minSpareThreads <= 0) {
			if (1 == maxSpareThreads) {
				minSpareThreads = 1;
			} else {
				minSpareThreads = maxSpareThreads / 2;
			}
		}
	}

	/**
	 * Create missing threads.
	 * 
	 * @param toOpen
	 *            Total number of threads we'll have open
	 */
	protected void openThreads(int toOpen) {

		if (toOpen > maxThreads) {
			toOpen = maxThreads;
		}

		for (int i = currentThreadCount; i < toOpen; i++) {
			pool[i - currentThreadsBusy] = new ControlRunnable(this);
		}

		currentThreadCount = toOpen;
	}

	/**
	 * Periodically execute an action - cleanup in this case
	 */
	public static class MonitorRunnable implements Runnable {
		ThreadPool p;
		Thread t;
		int interval = WORK_WAIT_TIMEOUT;
		boolean shouldTerminate;

		MonitorRunnable(ThreadPool p) {
			this.p = p;
			this.start();
		}

		public void start() {
			shouldTerminate = false;
			t = new Thread(this);
			t.setDaemon(p.getDaemon());
			t.setName(p.getName() + "-Monitor");
			t.start();
		}

		public void setInterval(int i) {
			this.interval = i;
		}

		public void run() {
			while (true) {
				try {

					// Sleep for a while.
					synchronized (this) {
						this.wait(interval);
					}

					// Check if should terminate.
					// termination happens when the pool is shutting down.
					if (shouldTerminate) {
						break;
					}

					// Harvest idle threads.
					p.checkSpareControllers();

				} catch (Throwable t) {
					logger.error("tp run error", t);
				}
			}
		}

		public void stop() {
			this.terminate();
		}

		/**
		 * Stop the monitor
		 */
		public synchronized void terminate() {
			shouldTerminate = true;
			this.notify();
		}
	}

	/**
	 * A Thread object that executes various actions (Runnable ) under control
	 * of ThreadPool
	 */
	public class ControlRunnable implements Runnable {
		/**
		 * ThreadPool where this thread will be returned
		 */
		private ThreadPool p;

		private Thread t;

		private Runnable toRunRunnable;

		/**
		 * Stop this thread
		 */
		private boolean shouldTerminate;

		/**
		 * Activate the execution of the action
		 */
		private boolean shouldRun;

		/**
		 * Start a new thread, with no method in it
		 */
		ControlRunnable(ThreadPool p) {
			shouldTerminate = false;
			shouldRun = false;
			this.p = p;
			t = new Thread(this);
			t.setDaemon(true);
			t.setName(p.getName() + "-Processor" + p.getSequence());
			t.setPriority(p.getThreadPriority());
			t.start();
		}

		public void run() {
			boolean _shouldRun = false;
			boolean _shouldTerminate = false;
			try {
				while (true) {
					try {
						/* Wait for work. */
						synchronized (this) {
							while (!shouldRun && !shouldTerminate) {
								this.wait();
							}
							_shouldRun = shouldRun;
							_shouldTerminate = shouldTerminate;
						}

						if (_shouldTerminate) {
							break;
						}

						/* Check if should execute a runnable. */
						try {

							if (_shouldRun) {
								if (toRunRunnable != null) {
									toRunRunnable.run();
								} else {
								}
							}
						} catch (Throwable t) {
							logger.error("tp inner run error", t);
							_shouldTerminate = true;
							_shouldRun = false;
							p.notifyThreadEnd(this);
						} finally {
							if (_shouldRun) {
								shouldRun = false;
								/*
								 * Notify the pool that the thread is now idle.
								 */
								p.returnController(this);
							}
						}

						/*
						 * Check if should terminate. termination happens when
						 * the pool is shutting down.
						 */
						if (_shouldTerminate) {
							break;
						}
					} catch (InterruptedException ie) { /*
														 * for the wait
														 * operation
														 */
						logger.error("tp interrup Errpr", ie);
					}
				}
			} finally {
//				p.removeThread(Thread.currentThread());
			}
		}
		/**
		 * Run a task
		 * 
		 * @param toRun
		 */
		public synchronized void runIt(Runnable toRun) {
			t.setName(toRun.getClass().getName());
			this.toRunRunnable = toRun;
			// Do not re-init, the whole idea is to run init only once per
			// thread - the pool is supposed to run a single task, that is
			// initialized once.
			// noThData = true;
			shouldRun = true;
			this.notify();
		}

		public void stop() {
			this.terminate();
		}

		public synchronized void terminate() {
			shouldTerminate = true;
			this.notify();
		}
	}

}

package mina.pool;

import java.util.concurrent.TimeUnit;

public class LoginThreadPool implements TestExecutor {
	
	private static final int LOGINTHREADPOOL_MAX_THREAD = 100;//最多多少同时人登陆
	
	private static final LoginThreadPool instance = new LoginThreadPool();
	public static LoginThreadPool getInstance(){
		return instance;
	}
	
	private ThreadPool tp;
	
	private LoginThreadPool(){
		tp = new ThreadPool(LOGINTHREADPOOL_MAX_THREAD);
		start();
	}
	
	public void execute(Runnable target){
		this.tp.run(target);
	}
	
	@Override
	public boolean start() {
		tp.start();
		return true;
	}

	@Override
	public boolean stop() {
		tp.shutdown();
		return true;
	}

	@Override
	public void execute(Runnable command, long timeout, TimeUnit unit) {
		// TODO Auto-generated method stub
	}

}

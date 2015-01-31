package mina.pool;

import java.util.concurrent.TimeUnit;

public class TestThreadExecutor implements TestExecutor {
    
    private ThreadPool tp = new ThreadPool();
    
    public TestThreadExecutor() {
    }

    @Override
    public boolean start(){
    	tp.start();
        return true;
    }

    @Override
    public boolean stop(){
    	tp.shutdown();
        return true;
    }
    
    public void execute(Runnable command, long timeout, TimeUnit unit) {
    	tp.run(command);
    }
    
    
    public void execute(Runnable command) {
    	tp.run(command);
    }

}

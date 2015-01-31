package mina.pool;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * TestExecutor
 * @author Administrator
 * @example
 * TestExecutor executor = new TestThreadExecutor();<br/>
 * executor.start();<br/>
 * ...<br/>
 * executor.execute(new Runnable(){<br/>
 *     public void run(){<br/>
 *     }<br/>
 * });<br/>
 */
public interface TestExecutor extends Executor{
	
    public boolean start();
    
    public boolean stop();
    
    public void execute(Runnable command, long timeout, TimeUnit unit);
    
}
package mina.server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;


public class MessageThread implements Runnable{
	private static final Logger logger = Logger.getLogger(MessageThread.class);
	
	private BlockingQueue<ReceivedMessage> messageQueue = 
		new LinkedBlockingQueue<ReceivedMessage>();

	private MessageParser parser = null;
	
	public MessageThread(MessageParser parser){
		this.parser = parser;
	}
	
	public int getRemainMessage(){
		return this.messageQueue.size();
	}
	
	public void add(ReceivedMessage message){
		this.messageQueue.add(message);
	}
	
	@Override
	public void run() {
		while(true){
			ReceivedMessage message = null;
			try{
				message = this.messageQueue.take();
				try{
//					String cpName = this.parser.getClass().getName();
//					Object object = message.getSession().getAttribute("");
					this.parser.parse(message);
				}
				catch(Exception e){
					if(this.parser != null){
						logger.error(this.parser.getClass().getName() + "|" + message, e);
						this.parser.catchException(message, e);
					}
				}
			}
			catch(Throwable t){
				logger.error("MessageThread Error! " + message, t);
			}
		}		
	}
	
}
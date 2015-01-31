package mina.server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

public class MessageDispatcher {

	private static final Logger logger = Logger.getLogger(MessageDispatcher.class);

	private BlockingQueue<ReceivedMessage> messageQueue = new LinkedBlockingQueue<ReceivedMessage>();

	private MessageThread[] parseThreads = new MessageThread[100];
	private Executor executor;
	
	private MessageDispatcherWorker worker = new MessageDispatcherWorker();
	
	public MessageDispatcher(Executor executor){
		this.executor = executor;
		this.executor.execute(worker);
	}

	public void addReceivedMessage(ReceivedMessage receivedMessage) {
		messageQueue.add(receivedMessage);
	}

	public class MessageDispatcherWorker implements Runnable {

		public void run() {
			while (true) {
				try {
					ReceivedMessage message = messageQueue.take();
					JSONObject jsonObject = message.getJsonObject();
					
					int index = 0;
					index = jsonObject.getInteger("parseId");
					if (index == 0) {
						logger.debug("No Command Parse Id.");
						continue;
					}
					// TODO 解密

					if (index < 0 || index >= parseThreads.length) {
						logger.debug("Error Command Parse Id [" + index + "].");
						continue;
					}

					MessageThread parseThread = parseThreads[index];
					if (parseThread == null) {
						logger.debug("UnRegister Command Parse Id [" + index + "].");
						continue;
					}

					parseThread.add(message);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public boolean addParse(MessageParser messageParser) {
		int index = messageParser.getParseId();
		if(index < 0 || index >= parseThreads.length){
			logger.error("error MessageDispatcher.addParse index:" + index);
			return false;
		}
		
		if(parseThreads[index] != null){
			logger.error("error MessageDispatcher.addParse index:" + index);
			return false;
		}
		
		this.parseThreads[index] = new MessageThread(messageParser);
		this.executor.execute(parseThreads[index]);
		return true;
	}

}

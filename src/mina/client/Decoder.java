package mina.client;

import mina.server.MessageDispatcher;
import mina.server.ReceivedMessage;
import mina.util.ByteArray;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class Decoder extends CumulativeProtocolDecoder {

	private static final int HEAD_SIZE = 4;
	

	@Override
	protected boolean doDecode(IoSession session, IoBuffer buf, ProtocolDecoderOutput out) throws Exception {
		if (buf.remaining() < HEAD_SIZE) {
			return false;
		}
		if (buf.remaining() > 1) {
			buf.mark();
			int length = buf.getInt(buf.position());
			if (length > (buf.remaining() - 4)) {
				System.out.println("package notenough  left=" + buf.remaining() + " length=" + length);
				buf.reset();
				return false;
			} else {
				// System.out.println("package ="+buf.toString());
				buf.getInt();
				byte[] bytes = new byte[length];
				buf.get(bytes, 0, length);
//				ReceivedMessage receivedMessage = new ReceivedMessage(session, new ByteArray(bytes));
//				messageDispatcher.addReceivedMessage(receivedMessage);
				// String str = new String(bytes,"UTF-8");
				// if(str != null && str.length() > 0){
				// out.write(str);
				// }
				// 还有数据就继续调用，没有就退出。
				return true;
			}
		}
		return false;
	}

}

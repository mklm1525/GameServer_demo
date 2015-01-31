package mina.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class Encoder extends ProtocolEncoderAdapter {

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		//message为byte[];
		IoBuffer buf = IoBuffer.allocate(100).setAutoExpand(true);
		byte[] bytes = (byte[]) message;
		buf.putInt(bytes.length);
		buf.put(bytes);
		buf.flip();
		out.write(buf);
	}

}

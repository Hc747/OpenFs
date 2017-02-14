package beta.openfs.filestore.codec.encoder.generic;

import beta.openfs.filestore.Constants;
import beta.openfs.filestore.codec.encoder.Encoder;
import io.netty.buffer.ByteBuf;

/**
 * @author Harrison, Alias: Hc747, Contact: harrisoncole05@gmail.com
 * @version 1.0
 * @since 14/2/17
 */
public final class TerminatorEncoder<T> implements Encoder<T> {

	@Override
	public void encode(T reference, ByteBuf buffer) {
		buffer.writeByte(Constants.TERMINATOR_OPCODE);
	}
}

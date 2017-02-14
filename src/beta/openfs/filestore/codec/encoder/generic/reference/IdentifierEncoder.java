package beta.openfs.filestore.codec.encoder.generic.reference;

import beta.openfs.filestore.Constants;
import beta.openfs.filestore.codec.encoder.Encoder;
import beta.openfs.filestore.reference.Reference;
import io.netty.buffer.ByteBuf;

/**
 * @author Harrison, Alias: Hc747, Contact: harrisoncole05@gmail.com
 * @version 1.0
 * @since 14/2/17
 */
public final class IdentifierEncoder<T extends Reference<T>> implements Encoder<T> {

	@Override
	public void encode(T reference, ByteBuf buffer) {
		buffer.writeByte(Constants.IDENTIFIER_OPCODE);
		buffer.writeInt(reference.getIdentifier());
	}

}

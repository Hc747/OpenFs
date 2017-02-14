package beta.openfs.filestore.codec.encoder.generic.reference;

import beta.openfs.filestore.Constants;
import beta.openfs.filestore.codec.encoder.Encoder;
import beta.openfs.filestore.reference.Reference;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

/**
 * @author Harrison, Alias: Hc747, Contact: harrisoncole05@gmail.com
 * @version 1.0
 * @since 14/2/17
 */
public final class NameEncoder<T extends Reference<T>> implements Encoder<T> {

	@Override
	public void encode(T reference, ByteBuf buffer) {
		buffer.writeByte(Constants.NAME_OPCODE);
		final byte[] name = reference.getName().getBytes(StandardCharsets.UTF_8);
		buffer.writeInt(name.length);
		buffer.writeBytes(name);
	}

}

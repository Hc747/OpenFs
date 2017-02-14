package beta.openfs.filestore.codec.encoder.generic.container;

import beta.openfs.filestore.Constants;
import beta.openfs.filestore.codec.encoder.Encoder;
import beta.openfs.filestore.container.Container;
import beta.openfs.filestore.reference.Reference;
import io.netty.buffer.ByteBuf;

/**
 * @author Harrison, Alias: Hc747, Contact: harrisoncole05@gmail.com
 * @version 1.0
 * @since 14/2/17
 */
public final class PayloadEncoder<R extends Reference<R>, T extends Container<R, T>> implements Encoder<T> {

	@Override
	public void encode(T container, ByteBuf buffer) {
		buffer.writeByte(Constants.PAYLOAD_OPCODE);
		buffer.writeInt(container.size());
		container.elements().forEach(it -> buffer.writeBytes(it.encode()));
	}

}

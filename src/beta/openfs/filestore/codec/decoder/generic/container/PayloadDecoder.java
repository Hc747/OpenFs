package beta.openfs.filestore.codec.decoder.generic.container;

import beta.openfs.filestore.codec.decoder.Decoder;
import beta.openfs.filestore.container.Container;
import beta.openfs.filestore.reference.Reference;
import io.netty.buffer.ByteBuf;

/**
 * @author Harrison, Alias: Hc747, Contact: harrisoncole05@gmail.com
 * @version 1.0
 * @since 14/2/17
 */
public final class PayloadDecoder<R extends Reference<R>, T extends Container<R, T>> implements Decoder<T> {

	@Override
	public void decode(T container, ByteBuf buffer) {
		final int length = buffer.readInt();
		for (int i = 0; i < length; i++) {
			final R reference = container.createFromBuffer(buffer);
			container.add(reference);
		}
	}

}

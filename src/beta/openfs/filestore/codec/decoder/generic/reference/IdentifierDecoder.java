package beta.openfs.filestore.codec.decoder.generic.reference;

import beta.openfs.filestore.codec.decoder.Decoder;
import beta.openfs.filestore.reference.Reference;
import io.netty.buffer.ByteBuf;

/**
 * @author Harrison, Alias: Hc747, Contact: harrisoncole05@gmail.com
 * @version 1.0
 * @since 14/2/17
 */
public final class IdentifierDecoder<T extends Reference<T>> implements Decoder<T> {

	@Override
	public void decode(T reference, ByteBuf buffer) {
		final int identifier = buffer.readInt();
		reference.setIdentifier(identifier);
	}

}

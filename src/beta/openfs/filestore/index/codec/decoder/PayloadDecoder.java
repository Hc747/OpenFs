package beta.openfs.filestore.index.codec.decoder;

import beta.openfs.filestore.codec.decoder.Decoder;
import beta.openfs.filestore.index.Index;
import io.netty.buffer.ByteBuf;

/**
 * @author Harrison, Alias: Hc747, Contact: harrisoncole05@gmail.com
 * @version 1.0
 * @since 14/2/17
 */
public final class PayloadDecoder implements Decoder<Index> {

	@Override
	public void decode(Index reference, ByteBuf buffer) {
		final int length = buffer.readInt();
		reference.setPayload(new byte[length]);
		buffer.readBytes(reference.getPayload());
	}

}

package beta.openfs.filestore.index.codec.encoder;

import beta.openfs.filestore.Constants;
import beta.openfs.filestore.codec.encoder.Encoder;
import beta.openfs.filestore.index.Index;
import io.netty.buffer.ByteBuf;

/**
 * @author Harrison, Alias: Hc747, Contact: harrisoncole05@gmail.com
 * @version 1.0
 * @since 14/2/17
 */
public final class PayloadEncoder implements Encoder<Index> {

	@Override
	public void encode(Index reference, ByteBuf buffer) {
		buffer.writeByte(Constants.PAYLOAD_OPCODE);
		buffer.writeInt(reference.size());
		buffer.writeBytes(reference.getPayload());
	}

}

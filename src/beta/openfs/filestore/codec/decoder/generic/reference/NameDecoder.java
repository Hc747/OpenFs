package beta.openfs.filestore.codec.decoder.generic.reference;

import beta.openfs.filestore.codec.decoder.Decoder;
import beta.openfs.filestore.reference.Reference;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

import java.nio.charset.StandardCharsets;

/**
 * @author Harrison, Alias: Hc747, Contact: harrisoncole05@gmail.com
 * @version 1.0
 * @since 14/2/17
 */
public final class NameDecoder<T extends Reference<T>> implements Decoder<T> {

	@Override
	public void decode(T reference, ByteBuf buffer) {
		final int length = buffer.readInt();
		final String name = new String(ByteBufUtil.getBytes(buffer, buffer.readerIndex(), length), StandardCharsets.UTF_8);
		reference.setName(name);
		buffer.readerIndex(buffer.readerIndex() + length);
	}

}

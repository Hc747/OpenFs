package org.openfs.filestore.store.codec.decoders;

import org.openfs.filestore.codec.decoder.ReferenceDecoder;
import org.openfs.filestore.store.FileStore;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

import java.nio.charset.StandardCharsets;

/**
 * @author Harrison, Alias: Hc747, Contact: harrisoncole05@gmail.com
 * @version 1.0
 * @since 7/2/17
 */
public final class NameDecoder implements ReferenceDecoder<FileStore> {

	@Override
	public void decode(FileStore reference, ByteBuf buffer) throws Exception {
		final int length = buffer.readInt();
		final String name = new String(ByteBufUtil.getBytes(buffer, buffer.readerIndex(), length), StandardCharsets.UTF_8);
		reference.setName(name);
		buffer.readerIndex(buffer.readerIndex() + length);
	}

}

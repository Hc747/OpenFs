package org.openfs.filestore.store.codec.decoders;

import org.openfs.filestore.codec.decoder.ReferenceDecoder;
import org.openfs.filestore.store.FileStore;
import io.netty.buffer.ByteBuf;

/**
 * @author Harrison, Alias: Hc747, Contact: harrisoncole05@gmail.com
 * @version 1.0
 * @since 7/2/17
 */
public final class IdentifierDecoder implements ReferenceDecoder<FileStore> {

	@Override
	public void decode(FileStore reference, ByteBuf buffer) throws Exception {
		reference.setIdentifier(buffer.readByte());
	}

}

package org.openfs.filestore.store.codec.decoders;

import org.openfs.filestore.codec.decoder.ReferenceDecoder;
import org.openfs.filestore.file.IndexedFile;
import org.openfs.filestore.store.FileStore;
import io.netty.buffer.ByteBuf;

/**
 * @author Harrison, Alias: Hc747, Contact: harrisoncole05@gmail.com
 * @version 1.0
 * @since 7/2/17
 */
public final class PayloadDecoder implements ReferenceDecoder<FileStore> {

	@Override
	public void decode(FileStore reference, ByteBuf buffer) throws Exception {
		final int length = buffer.readInt();
		for (int i = 0; i < length; i++)
			reference.add(IndexedFile.create(buffer));
	}

}

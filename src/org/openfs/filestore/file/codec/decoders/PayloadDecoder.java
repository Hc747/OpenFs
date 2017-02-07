package org.openfs.filestore.file.codec.decoders;

import org.openfs.filestore.codec.decoder.ReferenceDecoder;
import org.openfs.filestore.file.IndexedFile;
import io.netty.buffer.ByteBuf;

/**
 * @author Harrison, Alias: Hc747, Contact: harrisoncole05@gmail.com
 * @version 1.0
 * @since 7/2/17
 */
public final class PayloadDecoder implements ReferenceDecoder<IndexedFile> {

	@Override
	public void decode(IndexedFile reference, ByteBuf buffer) throws Exception {
		final int length = buffer.readInt();
		reference.setPayload(new byte[length]);
		buffer.readBytes(reference.getPayload());
	}

}

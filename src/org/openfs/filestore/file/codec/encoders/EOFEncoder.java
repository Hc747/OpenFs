package org.openfs.filestore.file.codec.encoders;

import org.openfs.filestore.Reference;
import org.openfs.filestore.codec.encoder.ReferenceEncoder;
import org.openfs.filestore.file.IndexedFile;
import io.netty.buffer.ByteBuf;

/**
 * @author Harrison, Alias: Hc747, Contact: harrisoncole05@gmail.com
 * @version 1.0
 * @since 7/2/17
 */
public final class EOFEncoder implements ReferenceEncoder<IndexedFile> {

	@Override
	public void encode(IndexedFile reference, ByteBuf buffer) {
		buffer.writeByte(Reference.EOF);
	}

}

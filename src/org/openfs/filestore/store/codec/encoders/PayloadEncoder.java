package org.openfs.filestore.store.codec.encoders;

import org.openfs.filestore.Reference;
import org.openfs.filestore.codec.encoder.ReferenceEncoder;
import org.openfs.filestore.store.FileStore;
import io.netty.buffer.ByteBuf;

/**
 * @author Harrison, Alias: Hc747, Contact: harrisoncole05@gmail.com
 * @version 1.0
 * @since 7/2/17
 */
public final class PayloadEncoder implements ReferenceEncoder<FileStore> {

	@Override
	public void encode(FileStore reference, ByteBuf buffer) throws Exception {
		buffer.writeByte(Reference.REFERENCE_PAYLOAD_OPCODE);
		buffer.writeInt(reference.size());
		reference.getFiles().forEach(it -> {
			try {
				buffer.writeBytes(it.encode());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

}

package org.openfs.filestore.store.codec.encoders;

import org.openfs.filestore.Reference;
import org.openfs.filestore.codec.encoder.ReferenceEncoder;
import org.openfs.filestore.store.FileStore;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

/**
 * @author Harrison, Alias: Hc747, Contact: harrisoncole05@gmail.com
 * @version 1.0
 * @since 7/2/17
 */
public final class NameEncoder implements ReferenceEncoder<FileStore> {

	@Override
	public void encode(FileStore reference, ByteBuf buffer) throws Exception {
		buffer.writeByte(Reference.REFERENCE_NAME_OPCODE);
		final byte[] name = reference.getName().getBytes(StandardCharsets.UTF_8);
		buffer.writeInt(name.length);
		buffer.writeBytes(name);
	}

}

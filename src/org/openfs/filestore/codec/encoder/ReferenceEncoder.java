package org.openfs.filestore.codec.encoder;

import org.openfs.filestore.Reference;
import io.netty.buffer.ByteBuf;

/**
 * @author Harrison, Alias: Hc747, Contact: harrisoncole05@gmail.com
 * @version 1.0
 * @since 7/2/17
 */
public interface ReferenceEncoder<T extends Reference> {

	void encode(T reference, ByteBuf buffer) throws Exception;

}

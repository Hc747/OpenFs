package beta.openfs.filestore.codec.decoder;

import io.netty.buffer.ByteBuf;

/**
 * @author Harrison, Alias: Hc747, Contact: harrisoncole05@gmail.com
 * @version 1.0
 * @since 14/2/17
 */
public interface Decoder<T> {

	void decode(T reference, ByteBuf buffer);

}

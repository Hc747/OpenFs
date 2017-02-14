package beta.openfs.filestore.codec;

import beta.openfs.filestore.Constants;
import beta.openfs.filestore.codec.decoder.Decoder;
import beta.openfs.filestore.codec.encoder.Encoder;
import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Map;

/**
 * @author Harrison, Alias: Hc747, Contact: harrisoncole05@gmail.com
 * @version 1.0
 * @since 14/2/17
 */
public interface Serializer<T> {

	default ByteBuf encode() {
		final ByteBuf buffer = Unpooled.buffer();
		encoders().forEach(it -> it.encode((T) this, buffer));
		return buffer;
	}

	Collection<Encoder<T>> encoders();

	default void decode(ByteBuf buffer) {
		for (;;) {
			final int opcode = buffer.readByte();
			if (opcode == Constants.TERMINATOR_OPCODE)
				break;
			final Decoder<T> decoder = decoders().get(opcode);
			Preconditions.checkNotNull(decoder, new UnsupportedEncodingException(String.format("Unhandled opcode received: %d", opcode)));
			decoder.decode((T) this, buffer);
		}
	}

	Map<Integer, Decoder<T>> decoders();

}

package org.openfs.filestore.file;

import com.google.common.base.Preconditions;
import com.google.gson.GsonBuilder;
import org.openfs.filestore.Reference;
import org.openfs.filestore.codec.decoder.ReferenceDecoder;
import org.openfs.filestore.codec.encoder.ReferenceEncoder;
import com.openfs.filestore.file.codec.decoders.*;
import com.openfs.filestore.file.codec.encoders.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Getter;
import lombok.Setter;
import org.openfs.filestore.file.codec.decoders.IdentifierDecoder;
import org.openfs.filestore.file.codec.decoders.NameDecoder;
import org.openfs.filestore.file.codec.decoders.PayloadDecoder;
import org.openfs.filestore.file.codec.encoders.EOFEncoder;
import org.openfs.filestore.file.codec.encoders.IdentifierEncoder;
import org.openfs.filestore.file.codec.encoders.NameEncoder;
import org.openfs.filestore.file.codec.encoders.PayloadEncoder;

import java.util.*;

/**
 * @author Harrison, Alias: Hc747, Contact: harrisoncole05@gmail.com
 * @version 1.0
 * @since 6/2/17
 */
@Getter
@Setter
public final class IndexedFile extends Reference {

	private static final Map<Integer, Class<? extends ReferenceDecoder<IndexedFile>>> decoders = new HashMap<>();
	private static final Deque<Class<? extends ReferenceEncoder<IndexedFile>>> encoders = new ArrayDeque<>();
	private static final String DEFAULT_NAME = "Empty";

	private byte[] payload;

	private IndexedFile() {}

	private IndexedFile(int identifier, String name, byte[] payload) {
		super(identifier, name);
		Preconditions.checkNotNull(payload, "\'payload\' not permitted to be null.");
		this.payload = payload;
	}

	public static IndexedFile create(ByteBuf buffer) throws Exception {
		final IndexedFile idx = new IndexedFile();
		idx.decode(buffer);
		return idx;
	}

	public static IndexedFile create(IndexedFile file) {
		return create(file.getIdentifier(), file.getName(), Arrays.copyOf(file.getPayload(), file.size()));
	}

	public static IndexedFile create(int identifier) {
		return create(identifier, DEFAULT_NAME + identifier);
	}

	public static IndexedFile create(int identifier, String name) {
		return create(identifier, name, new byte[0]);
	}

	public static IndexedFile create(int identifier, String name, byte[] payload) {
		return new IndexedFile(identifier, name, payload);
	}

	@Override
	public boolean isEmpty() {
		return payload.length == 0;
	}

	@Override
	public int size() {
		return payload.length;
	}

	@Override
	public ByteBuf encode() throws Exception {
		final ByteBuf buffer = Unpooled.buffer();

		encoders.forEach(it -> {
			try {
				it.newInstance().encode(this, buffer);
				System.out.println("\t\tIndexedFile encoded: "+it.getSimpleName());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		return buffer;
	}

	@Override
	public void decode(ByteBuf buffer) throws Exception {
		for (;;) {
			final int opcode = buffer.readByte();
			if (opcode == EOF)
				break;
			final Class<? extends ReferenceDecoder<IndexedFile>> decoder = decoders.get(opcode);
			if (decoder == null)
				throw new RuntimeException(String.format("Unhandled IndexedFile opcode: %d", opcode));
			decoder.newInstance().decode(this, buffer);
			System.out.println("\t\tIndexedFile decoded: "+decoder.getSimpleName());
		}
	}

	@Override
	public String toString() {
		return new GsonBuilder().create().toJson(this);
	}

	private static void registerDecoder(int opcode, Class<? extends ReferenceDecoder<IndexedFile>> clazz) {
		final Class<? extends ReferenceDecoder<IndexedFile>> decoder = decoders.get(opcode);
		if (decoder != null)
			throw new RuntimeException(String.format("ReferenceDecoder opcode already handled: %d, %s", opcode, decoder.getSimpleName()));
		decoders.put(opcode, clazz);
	}

	private static void registerEncoder(Class<? extends ReferenceEncoder<IndexedFile>> encoder) {
		if (encoders.contains(encoder))
			throw new RuntimeException(String.format("ReferenceEncoder already handled: %s", encoder.getSimpleName()));
		encoders.addLast(encoder);
	}

	static {
		registerDecoder(REFERENCE_IDENTIFIER_OPCODE, IdentifierDecoder.class);
		registerDecoder(REFERENCE_NAME_OPCODE, NameDecoder.class);
		registerDecoder(REFERENCE_PAYLOAD_OPCODE, PayloadDecoder.class);

		registerEncoder(IdentifierEncoder.class);
		registerEncoder(NameEncoder.class);
		registerEncoder(PayloadEncoder.class);
		registerEncoder(EOFEncoder.class);
	}

}

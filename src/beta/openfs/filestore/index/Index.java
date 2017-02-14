package beta.openfs.filestore.index;

import beta.openfs.filestore.Constants;
import beta.openfs.filestore.archive.Archive;
import beta.openfs.filestore.codec.decoder.Decoder;
import beta.openfs.filestore.codec.decoder.generic.reference.IdentifierDecoder;
import beta.openfs.filestore.codec.decoder.generic.reference.NameDecoder;
import beta.openfs.filestore.codec.encoder.Encoder;
import beta.openfs.filestore.codec.encoder.generic.TerminatorEncoder;
import beta.openfs.filestore.codec.encoder.generic.reference.IdentifierEncoder;
import beta.openfs.filestore.codec.encoder.generic.reference.NameEncoder;
import beta.openfs.filestore.index.codec.decoder.PayloadDecoder;
import beta.openfs.filestore.index.codec.encoder.PayloadEncoder;
import beta.openfs.filestore.reference.Reference;
import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;

import java.util.*;

/**
 * @author Harrison, Alias: Hc747, Contact: harrisoncole05@gmail.com
 * @version 1.0
 * @since 14/2/17
 */
public final class Index implements Reference<Index> {

	private String name;
	private int identifier;
	private byte[] payload;

	private Index() {}

	private Index(String name, int identifier, byte[] payload) {
		setName(name);
		setIdentifier(identifier);
		setPayload(payload);
	}

	public static Index create(ByteBuf buffer) {
		final Index index = new Index();
		index.decode(buffer);
		return index;
	}

	public static Index create(Archive archive) {
		return create(archive, new byte[0]);
	}

	public static Index create(Archive archive, byte[] payload) {
		return create(Constants.DEFAULT_NAME, archive, payload);
	}

	public static Index create(String name, Archive archive) {
		return create(name, archive, new byte[0]);
	}

	public static Index create(String name, Archive archive, byte[] payload) {
		return new Index(name, archive.size(), payload);
	}

	@Override
	public int size() {
		return payload.length;
	}

	@Override
	public boolean isEmpty() {
		return payload.length == 0;
	}

	@Override
	public int getIdentifier() {
		return identifier;
	}

	@Override
	public String getName() {
		return name;
	}

	public byte[] getPayload() {
		return payload;
	}

	@Override
	public Index setIdentifier(int identifier) {
		Preconditions.checkArgument(identifier >= 0, new IllegalArgumentException("\'identifier\' not permitted to be negative."));
		this.identifier = identifier;
		return this;
	}

	@Override
	public Index setName(String name) {
		Preconditions.checkNotNull(name, "\'name\' not permitted to be null.");
		Preconditions.checkArgument(!name.isEmpty(), new IllegalArgumentException("\'name\' not permitted to be empty."));
		this.name = name;
		return this;
	}

	public Index setPayload(byte[] payload) {
		Preconditions.checkNotNull(payload, "\'payload\' not permitted to be null.");
		this.payload = payload;
		return this;
	}

	@Override
	public Collection<Encoder<Index>> encoders() {
		return Collections.unmodifiableList(Arrays.asList(
				new IdentifierEncoder<Index>(),
				new NameEncoder<Index>(),
				new PayloadEncoder(),
				new TerminatorEncoder<Index>()
		));
	}

	@Override
	public Map<Integer, Decoder<Index>> decoders() {
		final HashMap<Integer, Decoder<Index>> decoders = new HashMap<>();
		decoders.put(Constants.NAME_OPCODE, new NameDecoder<>());
		decoders.put(Constants.IDENTIFIER_OPCODE, new IdentifierDecoder<>());
		decoders.put(Constants.PAYLOAD_OPCODE, new PayloadDecoder());
		return decoders;
	}

}

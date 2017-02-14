package beta.openfs.filestore.archive;

import beta.openfs.filestore.Constants;
import beta.openfs.filestore.codec.decoder.Decoder;
import beta.openfs.filestore.codec.decoder.generic.container.PayloadDecoder;
import beta.openfs.filestore.codec.decoder.generic.reference.IdentifierDecoder;
import beta.openfs.filestore.codec.decoder.generic.reference.NameDecoder;
import beta.openfs.filestore.codec.encoder.Encoder;
import beta.openfs.filestore.codec.encoder.generic.TerminatorEncoder;
import beta.openfs.filestore.codec.encoder.generic.container.PayloadEncoder;
import beta.openfs.filestore.codec.encoder.generic.reference.IdentifierEncoder;
import beta.openfs.filestore.codec.encoder.generic.reference.NameEncoder;
import beta.openfs.filestore.container.Container;
import beta.openfs.filestore.index.Index;
import beta.openfs.filestore.reference.Reference;
import beta.openfs.filestore.store.Store;
import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;

import java.util.*;
import java.util.function.Predicate;

/**
 * @author Harrison, Alias: Hc747, Contact: harrisoncole05@gmail.com
 * @version 1.0
 * @since 14/2/17
 */
public final class Archive implements Container<Index, Archive>, Reference<Archive> {

	private String name;
	private int identifier;
	private final List<Index> indexes = new ArrayList<>();

	private Archive() {}

	private Archive(String name, int identifier) {
		setName(name);
		setIdentifier(identifier);
	}

	public static Archive create(ByteBuf buffer) {
		final Archive archive = new Archive();
		archive.decode(buffer);
		return archive;
	}

	public static Archive create(Store store) {
		return create(Constants.DEFAULT_NAME, store);
	}

	public static Archive create(String name, Store store) {
		return new Archive(name, store.size());
	}

	@Override
	public Collection<Index> elements() {
		return Collections.unmodifiableCollection(indexes);
	}

	@Override
	public Archive add(Index reference) {
		Preconditions.checkNotNull(reference, "\'reference\' not permitted to be null.");
		Preconditions.checkArgument(!contains(reference::equals), new IllegalArgumentException("must invoke \'!contains\' prior to invoking \'add\'."));
		reference.setIdentifier(size());
		indexes.add(reference);
		return this;
	}

	@Override
	public Archive insert(Index reference, int index) {
		Preconditions.checkNotNull(reference, "\'reference\' not permitted to be null.");
		Preconditions.checkArgument(index >= 0 && index <= size(), new IndexOutOfBoundsException(String.format("0 <= \'index\' (%d) <= \'size\' (%d)", index, size())));
		final List<Index> references = new ArrayList<>();
		references.add(reference);
		while (index < size()) {
			references.add(indexes.remove(index));
		}
		references.forEach(this::add);
		return this;
	}

	@Override
	public Archive replace(Predicate<Index> lookup, Index reference) {
		Preconditions.checkNotNull(lookup, "\'lookup\' not permitted to be null.");
		Preconditions.checkNotNull(reference, "\'reference\' not permitted to be null.");
		final int index = indexes.indexOf(get(lookup));
		Preconditions.checkArgument(index >= 0, "\'index\' not permitted to be negative.");
		indexes.set(reference.setIdentifier(index).getIdentifier(), reference);
		return this;
	}

	@Override
	public Archive remove(Predicate<Index> lookup) {
		Preconditions.checkNotNull(lookup, "\'lookup\' not permitted to be null.");
		final Index reference = get(lookup);
		indexes.remove(reference);
		for (int i = reference.getIdentifier(); i < size(); i++) {
			indexes.get(i).setIdentifier(i);
		}
		return this;
	}

	@Override
	public Index get(Predicate<Index> lookup) {
		Preconditions.checkNotNull(lookup, "\'lookup\' not permitted to be null.");
		final Optional<Index> reference = indexes.stream().filter(lookup).findAny();
		Preconditions.checkArgument(reference.isPresent(), new IllegalArgumentException("must invoke \'contains\' prior to invoking \'get\'."));
		return reference.get();
	}

	@Override
	public Index createFromBuffer(ByteBuf buffer) {
		return Index.create(buffer);
	}

	@Override
	public boolean contains(Predicate<Index> lookup) {
		return indexes.stream().anyMatch(lookup);
	}

	@Override
	public int size() {
		return indexes.size();
	}

	@Override
	public boolean isEmpty() {
		return indexes.isEmpty();
	}

	@Override
	public int getIdentifier() {
		return identifier;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Archive setIdentifier(int identifier) {
		Preconditions.checkArgument(identifier >= 0, new IllegalArgumentException("\'identifier\' not permitted to be negative."));
		this.identifier = identifier;
		return this;
	}

	@Override
	public Archive setName(String name) {
		Preconditions.checkNotNull(name, "\'name\' not permitted to be null.");
		Preconditions.checkArgument(!name.isEmpty(), new IllegalArgumentException("\'name\' not permitted to be empty."));
		this.name = name;
		return this;
	}

	@Override
	public Collection<Encoder<Archive>> encoders() {
		return Collections.unmodifiableList(Arrays.asList(
				new IdentifierEncoder<Archive>(),
				new NameEncoder<Archive>(),
				new PayloadEncoder<Index, Archive>(),
				new TerminatorEncoder<Archive>()
		));
	}

	@Override
	public Map<Integer, Decoder<Archive>> decoders() {
		final HashMap<Integer, Decoder<Archive>> decoders = new HashMap<>();
		decoders.put(Constants.NAME_OPCODE, new NameDecoder<>());
		decoders.put(Constants.IDENTIFIER_OPCODE, new IdentifierDecoder<>());
		decoders.put(Constants.PAYLOAD_OPCODE, new PayloadDecoder<>());
		return decoders;
	}

}

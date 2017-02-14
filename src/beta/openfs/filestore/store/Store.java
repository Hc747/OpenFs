package beta.openfs.filestore.store;

import beta.openfs.filestore.Constants;
import beta.openfs.filestore.archive.Archive;
import beta.openfs.filestore.codec.decoder.Decoder;
import beta.openfs.filestore.codec.decoder.generic.container.PayloadDecoder;
import beta.openfs.filestore.codec.encoder.Encoder;
import beta.openfs.filestore.codec.encoder.generic.TerminatorEncoder;
import beta.openfs.filestore.codec.encoder.generic.container.PayloadEncoder;
import beta.openfs.filestore.container.Container;
import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;

import java.util.*;
import java.util.function.Predicate;

/**
 * @author Harrison, Alias: Hc747, Contact: harrisoncole05@gmail.com
 * @version 1.0
 * @since 14/2/17
 */
public final class Store implements Container<Archive, Store> {

	private final List<Archive> archives = new ArrayList<>();

	private Store() {}

	public static Store create(ByteBuf buffer) {
		final Store store = new Store();
		store.decode(buffer);
		return store;
	}

	public static Store create() {
		return new Store();
	}

	@Override
	public Collection<Archive> elements() {
		return Collections.unmodifiableCollection(archives);
	}

	@Override
	public Store add(Archive reference) {
		Preconditions.checkNotNull(reference, "\'reference\' not permitted to be null.");
		Preconditions.checkArgument(!contains(reference::equals), new IllegalArgumentException("must invoke \'!contains\' prior to invoking \'add\'."));
		reference.setIdentifier(size());
		archives.add(reference);
		return this;
	}

	@Override
	public Store insert(Archive reference, int index) {
		Preconditions.checkNotNull(reference, "\'reference\' not permitted to be null.");
		Preconditions.checkArgument(index >= 0 && index <= size(), new IndexOutOfBoundsException(String.format("0 <= \'index\' (%d) <= \'size\' (%d)", index, size())));
		final List<Archive> references = new ArrayList<>();
		references.add(reference);
		while (index < size()) {
			references.add(archives.remove(index));
		}
		references.forEach(this::add);
		return this;
	}

	@Override
	public Store replace(Predicate<Archive> lookup, Archive reference) {
		Preconditions.checkNotNull(lookup, "\'lookup\' not permitted to be null.");
		Preconditions.checkNotNull(reference, "\'reference\' not permitted to be null.");
		final int index = archives.indexOf(get(lookup));
		Preconditions.checkArgument(index >= 0, "\'index\' not permitted to be negative.");
		archives.set(reference.setIdentifier(index).getIdentifier(), reference);
		return this;
	}

	@Override
	public Store remove(Predicate<Archive> lookup) {
		Preconditions.checkNotNull(lookup, "\'lookup\' not permitted to be null.");
		final Archive reference = get(lookup);
		archives.remove(reference);
		for (int i = reference.getIdentifier(); i < size(); i++) {
			archives.get(i).setIdentifier(i);
		}
		return this;
	}

	@Override
	public Archive get(Predicate<Archive> lookup) {
		Preconditions.checkNotNull(lookup, "\'lookup\' not permitted to be null.");
		final Optional<Archive> reference = archives.stream().filter(lookup).findAny();
		Preconditions.checkArgument(reference.isPresent(), new IllegalArgumentException("must invoke \'contains\' prior to invoking \'get\'."));
		return reference.get();
	}

	@Override
	public Archive createFromBuffer(ByteBuf buffer) {
		return Archive.create(buffer);
	}

	@Override
	public boolean contains(Predicate<Archive> lookup) {
		return archives.stream().anyMatch(lookup);
	}

	@Override
	public int size() {
		return archives.size();
	}

	@Override
	public boolean isEmpty() {
		return archives.isEmpty();
	}

	@Override
	public Collection<Encoder<Store>> encoders() {
		return Collections.unmodifiableList(Arrays.asList(
				new PayloadEncoder<Archive, Store>(),
				new TerminatorEncoder<Store>()
		));
	}

	@Override
	public Map<Integer, Decoder<Store>> decoders() {
		final HashMap<Integer, Decoder<Store>> decoders = new HashMap<>();
		decoders.put(Constants.PAYLOAD_OPCODE, new PayloadDecoder<>());
		return decoders;
	}

}

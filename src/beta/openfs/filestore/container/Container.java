package beta.openfs.filestore.container;

import beta.openfs.filestore.codec.Serializer;
import beta.openfs.filestore.reference.Reference;
import io.netty.buffer.ByteBuf;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * @author Harrison, Alias: Hc747, Contact: harrisoncole05@gmail.com
 * @version 1.0
 * @since 14/2/17
 */
public interface Container<T extends Reference, Self> extends Serializer {

	Collection<T> elements();

	Self add(T reference);

	Self insert(T reference, int index);

	Self replace(Predicate<T> lookup, T reference);

	Self remove(Predicate<T> lookup);

	T get(Predicate<T> lookup);

	T createFromBuffer(ByteBuf buffer);

	boolean contains(Predicate<T> lookup);

	int size();

	boolean isEmpty();

}

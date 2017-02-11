package org.openfs.filestore;

import java.util.function.Predicate;

/**
 * @author Harrison, Alias: Hc747, Contact: harrisoncole05@gmail.com
 * @version 1.0
 * @since 6/2/17
 */
public interface Container<T extends Reference, P> {

	P add(T reference);

	P remove(Predicate<T> lookup);

	P replace(Predicate<T> lookup, T replacement);

	P insert(T reference, int index);

	boolean contains(Predicate<T> lookup);

	T get(Predicate<T> lookup);

}

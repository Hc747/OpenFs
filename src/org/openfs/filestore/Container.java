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

	//TODO consider making target a predicate
	P replace(T target, T replacement);

	//TODO consider making target a predicate
	P insert(T target, int index);

	boolean contains(Predicate<T> lookup);

	T get(Predicate<T> lookup);

}

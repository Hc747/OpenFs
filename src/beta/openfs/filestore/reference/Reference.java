package beta.openfs.filestore.reference;

import beta.openfs.filestore.codec.Serializer;

/**
 * @author Harrison, Alias: Hc747, Contact: harrisoncole05@gmail.com
 * @version 1.0
 * @since 14/2/17
 */
public interface Reference<Self extends Reference> extends Serializer {

	int size();

	boolean isEmpty();

	int getIdentifier();

	String getName();

	Self setIdentifier(int identifier);

	Self setName(String name);

}

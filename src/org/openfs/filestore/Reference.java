package org.openfs.filestore;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Harrison, Alias: Hc747, Contact: harrisoncole05@gmail.com
 * @version 1.0
 * @since 6/2/17
 */
@Getter
@Setter
public abstract class Reference {

	//TODO utilise observer pattern so that IndexedFileSystem can be notified of any mutations
	//TODO implement crc / integrity check
	//TODO equals implementation

	public static final int EOF = 0;

	public static final int REFERENCE_IDENTIFIER_OPCODE = 1;
	public static final int REFERENCE_NAME_OPCODE = 2;
	public static final int REFERENCE_PAYLOAD_OPCODE = 3;

	protected int identifier;
	protected String name;

	protected Reference() {}

	protected Reference(int identifier, String name) {
		Preconditions.checkArgument(identifier >= 0, "\'identifier\' not permitted to be negative.");
		Preconditions.checkNotNull(name, "\'name\' not permitted to be null.");
		Preconditions.checkArgument(!name.isEmpty(), "\'name\' not permitted to be empty.");
		this.identifier = identifier;
		this.name = name;
	}

	public abstract boolean isEmpty();

	public abstract int size();

	public abstract ByteBuf encode() throws Exception;

	public abstract void decode(ByteBuf buffer) throws Exception;

	@Override
	public boolean equals(Object other) {
		if (other instanceof Reference) {
			final Reference reference = (Reference) other;
			return identifier == reference.identifier && name.equals(reference.name);
		}
		return false;
	}

}

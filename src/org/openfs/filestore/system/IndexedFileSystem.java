package org.openfs.filestore.system;

import org.openfs.filestore.store.FileStore;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Harrison, Alias: Hc747, Contact: harrisoncole05@gmail.com
 * @version 1.0
 * @since 6/2/17
 */
@Getter
@Setter
public final class IndexedFileSystem implements AutoCloseable {

	//TODO immutable objects to ensure thread safety

	private final List<FileStore> stores = new ArrayList<>();

	private IndexedFileSystem() {}

	public static IndexedFileSystem create() {
		return new IndexedFileSystem();
	}

	@Override
	public void close() throws Exception {
		stores.clear();
	}
}

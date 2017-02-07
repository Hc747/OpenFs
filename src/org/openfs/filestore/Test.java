package org.openfs.filestore;

import org.openfs.filestore.file.IndexedFile;
import org.openfs.filestore.store.FileStore;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

/**
 * @author Harrison, Alias: Hc747, Contact: harrisoncole05@gmail.com
 * @version 1.0
 * @since 7/2/17
 */
public final class Test {

	private Test() {}

	public static void main(String[] a) throws Exception {
		FileStore store = FileStore.create(0, "Index Repository")
				.add(IndexedFile.create(0, "First Index", "payload".getBytes(StandardCharsets.UTF_8)))
				.add(IndexedFile.create(1, "Second Index"))
				.insert(IndexedFile.create(2, "Inserted Node", "Hello World".getBytes(StandardCharsets.UTF_8)), 1);

		System.out.println("Encoded:");
		System.out.println(store.toString());

		ByteBuf encoded = store.encode().asReadOnly();

		store = FileStore.create(encoded);

		System.out.println("");
		System.out.println("Decoded:");
		System.out.println(store.toString());

		System.out.println("");
		System.out.println("Retrievals:");
		System.out.println(store.get(2).toString());
		System.out.println(store.get("First Index").toString());

		System.out.println("");
		System.out.println("Removals:");
		System.out.println(store.remove(1).toString());
	}

}

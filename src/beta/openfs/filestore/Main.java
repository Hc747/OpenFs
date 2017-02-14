package beta.openfs.filestore;

import beta.openfs.filestore.archive.Archive;
import beta.openfs.filestore.index.Index;
import beta.openfs.filestore.store.Store;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.netty.buffer.ByteBuf;

/**
 * @author Harrison, Alias: Hc747, Contact: harrisoncole05@gmail.com
 * @version 1.0
 * @since 14/2/17
 */
public final class Main {

	private Main() {}

	public static void main(String[] a) {
		final Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
		Store store = Store.create();

		{
			final Archive archive = Archive.create("First Archive", store);
			store.add(archive);

			final Index first = Index.create("First Index", archive, "Hello world".getBytes());
			final Index second = Index.create("Second Index", archive);
			final Index inserted = Index.create("This index will be inserted at index 1", archive);

			archive.add(first).add(second).insert(inserted, 1);
		}

		{
			final Archive archive = Archive.create(store);
			store.add(archive);
		}

		{
			final Archive archive = Archive.create("This archive will be inserted at index 0", store);
			store.insert(archive, 0);
		}

		final String first_output = gson.toJson(store);
		System.out.println(first_output);

		System.out.println();
		System.out.println("Testing serialisation...");

		final ByteBuf buffer = store.encode();

		store = Store.create(buffer);

		final String second_output = gson.toJson(store);
		System.out.println(second_output);

		System.out.println();
		System.out.println("Verifying equality: "+(first_output.equals(second_output)));

	}

}

package org.openfs.fs;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.crypto.SecretKey;

import com.softgate.crypto.AESUtils;

/**
 * Represents file system based on a binary file that stores both its actual data and its data's indexes.
 *
 * @author Chad Adams
 */
public class IndexedFileSystem implements Closeable {

	private List<FileStore> stores = new ArrayList<>();

	private IndexedFileSystem() {

	}

	public static IndexedFileSystem create() {
		return new IndexedFileSystem();
	}

	public static IndexedFileSystem decode(String path) throws IOException {
		IndexedFileSystem fs = IndexedFileSystem.create();

		try(DataInputStream dis = new DataInputStream(new ByteArrayInputStream(Files.readAllBytes(Paths.get(path))))) {

			int stores = dis.readInt();

			for (int store = 0; store < stores; store++) {

				int id = dis.readByte();

				String name = dis.readUTF();

				int files = dis.readInt();

				fs.add(FileStore.create(id, name));

				for (int file = 0; file < files; file++) {

					int fileId = dis.readInt();

					String fileName = dis.readUTF();

					int decompressedLength = dis.readInt();

					int compressedLength = dis.readInt();

					byte[] compressed = new byte[compressedLength];

					dis.readFully(compressed);

					ByteArrayInputStream bis = new ByteArrayInputStream(compressed);

					byte[] decompressed = new byte[decompressedLength];

					try(GZIPInputStream gzip = new GZIPInputStream(bis)) {
						gzip.read(decompressed, 0, decompressed.length);
					}

					FileStore fileStore = fs.getStore(store);

					fileStore.add(fileId, fileName, decompressed);

				}

			}

		}

		return fs;
	}

	public byte[] encode() throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try(DataOutputStream out = new DataOutputStream(baos)) {

			out.writeInt(stores.size());

			for(FileStore store : stores) {

				int files = store.getFiles().size();

				out.writeByte(store.getId());
				out.writeUTF(store.getName());
				out.writeInt(files);

				for (int file = 0; file < files; file++) {

					IndexedFile indexedFile = store.getFiles().get(file);

					int fileId = indexedFile.getId();
					String name = indexedFile.getName();

					out.writeInt(fileId);
					out.writeUTF(name);

					byte[] decompressed = indexedFile.getPayload();

					out.writeInt(decompressed.length);

					ByteArrayOutputStream ba = new ByteArrayOutputStream();

					try(GZIPOutputStream gzip = new GZIPOutputStream(ba)) {
						gzip.write(decompressed);
					}

					byte[] compressed = ba.toByteArray();

					out.writeInt(compressed.length);

					out.write(compressed);

				}

			}

		}

		return baos.toByteArray();
	}

	public void write(String root, String name) throws IOException {

		File dir = new File(root);

		if (!dir.exists()) {
			throw new IOException();
		}

		byte[] data = encode();

		if (data == null || data.length <= 0) {
			return;
		}

		try(FileOutputStream fos = new FileOutputStream(new File(dir, name))) {
			fos.write(data);
		}

	}

	public FileStore add(FileStore index) {

		if (stores.isEmpty() || stores.size() == index.getId()) {
			stores.add(index);
			return index;
		}

		if (index.getId() > stores.size()) {
			for (int i = stores.size(); i < index.getId(); i++) {
				stores.add(FileStore.create(i, "empty"));
			}
		}

		FileStore idx = stores.get(index.getId());

		if (idx.isEmpty()) {

			stores.remove(idx.getId());

			stores.add(index.getId(), index);

		} else {
			stores.add(index.getId(), index);

			changeIndexId(index.getId() + 1);
		}

		return index;
	}

	public void remove(int id) {

		if (id < 0) {
			throw new IllegalArgumentException(String.format("identifier=%d cannot be negative.", id));
		}

		if (id > stores.size()) {
			throw new IllegalArgumentException(String.format("identifier=%d is greater than size=%d", id, stores.size()));
		}

		if (id < stores.size() - 1) {
			stores.remove(id);

			stores.add(id, FileStore.create(id, "empty"));
		} else {
			stores.remove(id);
		}

	}

	private void changeIndexId(int start) {
		for (int i = start; i < stores.size(); i++) {

			FileStore idx = stores.get(i);

			if (idx == null) {
				continue;
			}

			idx.setId(i);

		}
	}

	public byte[] read(int id, String fileName) {

		if (id < 0) {
			throw new IllegalArgumentException(String.format("identifier=%d cannot be negative.", id));
		}

		if (id > stores.size()) {
			throw new IllegalArgumentException(String.format("identifier=%d is out of range: %d", id, stores.size()));
		}

		for (IndexedFile file : stores.get(id).getFiles()) {
			if (file.getName().equalsIgnoreCase(fileName)) {
				return file.getPayload();
			}
		}

		return null;
	}

	public byte[] read(int id, int file) {

		if (id > stores.size() || id < 0) {
			throw new IllegalArgumentException(String.format("index=[%d] out of range.", id));
		}

		return stores.get(id).getFiles().get(file).getPayload();
	}

	public void rename(int id, String name) {
		if (id > stores.size() || id < 0) {
			throw new IllegalArgumentException(String.format("index=[%d] out of range.", id));
		}

		FileStore idx = stores.get(id);

		idx.setName(name);
	}

	public FileStore getStore(int id) {
		return stores.get(id);
	}

	public List<FileStore> getStores() {
		return stores;
	}

	@Override
	public void close() throws IOException {
		stores.clear();
	}

}
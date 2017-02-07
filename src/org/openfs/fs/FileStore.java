package org.openfs.fs;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Represents a binary directory that can have multiple non-directory files embedded inside it.
 *
 * @author Chad Adams
 */
public class FileStore {

	private final List<IndexedFile> files = new ArrayList<>();

	private int id;

	private String name;

	private FileStore() {
	}

	public FileStore copy() {
		FileStore copy = FileStore.create(id, name);
		
		files.forEach(it -> copy.getFiles().add(it));
		
		return copy;
	}

	public static FileStore create(int id) {
		FileStore idx = new FileStore();
		idx.id = id;
		idx.name = "index" + id;
		return idx;
	}

	public static FileStore create(int id, String name) {
		FileStore idx = new FileStore();
		idx.id = id;
		idx.name = name;
		return idx;
	}

	public static FileStore create(int id, IndexedFile file) {
		FileStore idx = new FileStore();
		idx.files.add(file);
		idx.id = id;
		idx.name = "index" + idx.id;
		return idx;
	}

	public static FileStore create(int id, String name, IndexedFile file) {
		FileStore idx = new FileStore();
		idx.files.add(file);
		idx.id = id;
		idx.name = name;
		return idx;
	}

	public static FileStore create(int id, IndexedFile... files) {
		FileStore idx = new FileStore();
		idx.files.addAll(Arrays.asList(files));
		idx.id = id;
		idx.name = "index" + idx.id;
		return idx;
	}

	public static FileStore create(int id, String name, IndexedFile... files) {
		FileStore idx = new FileStore();
		idx.files.addAll(Arrays.asList(files));
		idx.id = id;
		idx.name = name;
		return idx;
	}

	public FileStore add(File file) throws IOException {
		return add(file.getName(), Files.readAllBytes(file.toPath()));
	}

	public FileStore add(String name) {
		return add(name, new byte[1]);
	}

	public FileStore add(int id, String name) {
		return add(id, name, new byte[1]);
	}

	public FileStore add(String name, byte[] data) {
		return add(files.size(), name, data);
	}

	public FileStore add(int id, String name, byte[] data) {
		
		if (files.isEmpty()) {
			files.add(new IndexedFile(id, name, data));
			return this;
		}
		
		files.add(id, new IndexedFile(id, name, data));
		return this;
	}

	public void remove(int id) {	
		
		if (id < 0) {
			throw new IllegalArgumentException(String.format("identifier=%d cannot be negative.", id));
		}
		
		if (id < files.size() - 1) {
			
			IndexedFile indexedFile = files.get(id);
			
			IndexedFile copy = indexedFile.copy();
			
			files.remove(id);
			
			copy.setName("empty");
			copy.setPayload(new byte[0]);
			
			files.add(id, copy);
			
		} else {
			files.remove(id);
		}
		
		
	}

	public void remove(String name) {
		files.stream().filter(it -> it.getName().equalsIgnoreCase(name)).findFirst().ifPresent(it -> remove(it.getId()));
	}

	public void replace(int id, File file) throws IOException {		
		Optional<IndexedFile> optional = files.stream().filter(it -> it.getId() == id).findFirst();
		
		if (optional.isPresent()) {
			
			IndexedFile index = optional.get();

			index.setName(file.getName());
			index.setPayload(Files.readAllBytes(file.toPath()));
		}
	}

	public void replace(String name, File file) throws IOException {
		Optional<IndexedFile> optional = files.stream().filter(it -> it.getName().equalsIgnoreCase(name)).findFirst();
		
		if (optional.isPresent()) {
			
			IndexedFile index = optional.get();

			index.setName(file.getName());
			index.setPayload(Files.readAllBytes(file.toPath()));			
			
		}
	}

	public void replace(String name, byte[] data) {
		files.stream().filter(it -> it.getName().equals(name)).findFirst().ifPresent(it -> it.setPayload(data));
	}

	public void replace(int id, byte[] data) {
		files.stream().filter(it -> it.getId() == id).findFirst().ifPresent(it -> it.setPayload(data));
	}

	public IndexedFile getFile(int id) {
		for (IndexedFile file : files) {

			if (file == null) {
				continue;
			}

			if (file.getId() == id) {
				return file;
			}

		}
		return null;
	}

	public IndexedFile getFile(String name) {
		for (IndexedFile file : files) {

			if (file == null) {
				continue;
			}

			if (file.getName().equalsIgnoreCase(name)) {
				return file;
			}

		}
		return null;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}	

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isEmpty() {
		return files.isEmpty();
	}

	public List<IndexedFile> getFiles() {
		return files;
	}

}

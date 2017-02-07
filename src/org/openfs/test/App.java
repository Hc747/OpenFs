package org.openfs.test;

import com.softgate.fs.FileStore;
import com.softgate.fs.IndexedFile;
import com.softgate.fs.IndexedFileSystem;

public class App {

	public static void main(String[] args) throws Exception {
		create();
		read();
	}

	private static void read() throws Exception {
		long start = System.currentTimeMillis();

		try(IndexedFileSystem fs = IndexedFileSystem.decode("./cache.dat")) {
			for (FileStore index : fs.getStores()) {
				System.out.println(index.getId() + " " + index.getName());

				for (IndexedFile indexedFile : index.getFiles()) {
					System.out.println("\t\t" + indexedFile.getId() + " " + indexedFile.getName());
				}
			}

		}

		long end = System.currentTimeMillis();

		System.out.println("took: " + (end - start) + " ms to load");
	}

	private static void create() throws Exception {
		try(IndexedFileSystem fs = IndexedFileSystem.create()) {

			fs.add(FileStore.create(0, "settings"))
					.add("item.dat", "data inside item.dat".getBytes())
					.add("npc.dat", "data inside npc.dat".getBytes())
					.add("obj.dat", "data inside obj.dat".getBytes());

			fs.add(FileStore.create(1, "model"))
					.add(0, "0.dat")
					.add(1, "1.dat")
					.add(2, "2.dat");

			fs.add(FileStore.create(2, "animation"))
					.add(0, "0.dat")
					.add(1, "1.dat")
					.add(2, "2.dat");

			fs.add(FileStore.create(3, "music"))
					.add("soundtrack.midi")
					.add("login_music.midi");

			fs.add(FileStore.create(4, "map"))
					.add(0, "0.dat")
					.add(1, "1.dat")
					.add(2, "2.dat");

			fs.add(FileStore.create(5, "sound"))
					.add("low_alch.wav")
					.add("high_alch.wav")
					.add("telekenitic_grab.wav")
					.add("entangle.wav");

			fs.add(FileStore.create(6, "sprites"))
					.add("sprites.dat");

			fs.write("./", "cache.dat");

		}
	}


}

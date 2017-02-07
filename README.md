#Open FileStore (OPENFs)
Is an in-memory file system that allows you to pack multiple files into a single, compressed binary file. It is based largely off of Indexed Binary File System (IBFS) by Chad Adams.

### Features
* Super easy to use
* Can store anything as long as you can get the bytes of it
* All your data is kept in 1 file

### Coming soon
* Data compression
* Intuitive GUI
* Optional data encryption

### Dependencies
* Netty
* Lombok

### How it works
The file system has stores called a **FileStore**, which describe files that may be contained in them. A FileStore is similar to a directory in that it will store files, but a FileStore cannot store other directories.
The files in a FileStore are called an **IndexedFile**. An IndexedFile is a binary file that contains the actual files data.

* "archive" (FileStore)
   * "settings.dat" (IndexedFile)
   * "item.dat"
   * "npc.dat"
   * "obj.dat"
   * "idk.dat"
* "model"
   * "1.dat"
   * "2.dat"
* "animation"
   * "1.dat"
   * "2.dat"
* "music"
   * "soundtrack.midi"
   * "themesong.midi"
   * "arena.midi"
   * "shortclip.midi"
* "sound"
   * "high_alch.wav"
   * "low_alch.wav"
* "textures"
   * "0.dat"
   * "1.dat"
* "sprites"
   * "sprites.dat"
   
### Code examples
### Creating an IndexedFileSystem
```java

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
```


### Reading from an encoded IndexedFileSystem
```java

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

		System.out.println("took: " + (end - start) / 1000 + " seconds to load");
```

### Download
### Cache
(cache.dat)[http://www.mediafire.com/file/as74a3c37mt43fe/cache.dat]
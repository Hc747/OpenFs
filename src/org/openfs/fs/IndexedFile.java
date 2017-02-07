package org.openfs.fs;

/**
 * A wrapper class that will contain a files actual data.
 * 
 * @author Chad Adams
 */
public class IndexedFile {

	private int id;
	private String name;

	private byte[] payload;

	public IndexedFile(int id) {
		this(id, "empty", new byte[0]);
	}
	
	public IndexedFile(int id, String name) {
		this(id, name, new byte[0]);
	}

	public IndexedFile(int id, String name, byte[] payload) {
		this.id = id;
		this.name = name;
		this.payload = payload;
	}
	
	public IndexedFile copy() {		
		return new IndexedFile(id, name, payload);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getPayload() {
		return payload;
	}

	public void setPayload(byte[] payload) {
		this.payload = payload;
	}

}

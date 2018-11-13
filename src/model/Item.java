package model;

public abstract class Item {
	private String id;
	private String name;

	public Item(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public abstract String getTexture();

	public abstract Item clone();
}
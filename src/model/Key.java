package model;

public class Key extends Item {

	public Key(String id, String name) {
		super(id, name);
	}

	@Override
	public Item clone() {
		return new Key(super.getId(), super.getName());
	}

	@Override
	public String getTexture() {
		return "key";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((super.getId() == null) ? 0 : super.getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Key other = (Key) obj;
		if (super.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!super.getId().equals(other.getId()))
			return false;
		return true;
	}
}

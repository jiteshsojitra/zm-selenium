package com.synacor.qa.selenium.framework.ui;

public class Button {

	public static final Button B_NEWS = new Button("B_NEWS");

	public static final Button O_COMICS = new Button("O_COMICS");

	// Button properties
	private final String ID;

	protected Button(String id) {
		this.ID = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
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

		Button other = (Button) obj;
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ID;
	}
}

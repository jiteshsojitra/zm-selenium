package com.synacor.qa.selenium.framework.ui;

public class Shortcut {

	// General shortcuts
	public static final Shortcut S_RIGHT_CLICK = new Shortcut("S_RIGHT_CLICK", ",");
	public static final Shortcut S_GO_TO_NEWS = new Shortcut("S_GO_TO_NEWS", "gm");

	// Shortcut properties
	private final String ID;
	private final String Keys;
	
	protected Shortcut(String id, String keys) {
		this.ID = id;
		this.Keys = keys;
	}

	public String getKeys() {
		return (Keys);
	}
	
	@Override
	public String toString() {
		return ID;
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
		Shortcut other = (Shortcut) obj;
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		return true;
	}
}
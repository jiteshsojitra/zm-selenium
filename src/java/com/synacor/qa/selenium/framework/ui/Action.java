package com.synacor.qa.selenium.framework.ui;

public class Action {

	// General actions
	public static final Action A_LEFTCLICK = new Action("A_LEFTCLICK");
	public static final Action A_SHIFTSELECT = new Action("A_SHIFTSELECT");
	public static final Action A_CTRLSELECT = new Action("A_CTRLSELECT");
	public static final Action A_RIGHTCLICK = new Action("A_RIGHTCLICK");
	public static final Action A_DOUBLECLICK = new Action("A_DOUBLECLICK");
	public static final Action A_HOVEROVER = new Action("A_HOVEROVER");
	public static final Action A_CHECKBOX = new Action("A_CHECKBOX");
	public static final Action A_UNCHECKBOX = new Action("A_UNCHECKBOX");

	private final String ID;
	
	protected Action(String id) {
		this.ID = id;
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
		Action other = (Action) obj;
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		return true;
	}
}
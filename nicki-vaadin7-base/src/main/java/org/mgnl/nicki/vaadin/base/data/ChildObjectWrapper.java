package org.mgnl.nicki.vaadin.base.data;

import lombok.Getter;

public class ChildObjectWrapper extends ObjectWrapper {
	private @Getter String name;
	public ChildObjectWrapper(String name, Object child) {
		super(child);
		this.name = name;
	}
	
	public String getValue() {
		return getObject().toString();
	}
}

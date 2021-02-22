package org.mgnl.nicki.vaadin.base.data;

public class ObjectWrapper {
	private Object object;

	public ObjectWrapper(Object object) {
		this.object = object;
	}

	public Object getObject() {
		return object;
	}
	
	public String getName() {
		return "Object";
	}
	
	public String getType() {
		return getObject().getClass().getSimpleName();
	}
	
	public String getValue() {
		return "";
	}
}


package org.mgnl.nicki.vaadin.db.converter;

public abstract class AbstractConverter<X, T> {

	public abstract T convert(X value);

}

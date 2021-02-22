package org.mgnl.nicki.vaadin.base.data;

import org.mgnl.nicki.core.objects.DynamicObject;

import com.vaadin.data.ValueProvider;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class DynamicObjectGridColumn<T extends DynamicObject> implements ValueProvider<T, String> {
	public DynamicObjectGridColumn(String attributeName, String caption) {
		this.attributeName = attributeName;
		this.caption = caption;
	}
	private String attributeName;
	private String caption;
	
	@Override
	public String apply(T source) {
		return source.getAttribute(attributeName);
	}
}

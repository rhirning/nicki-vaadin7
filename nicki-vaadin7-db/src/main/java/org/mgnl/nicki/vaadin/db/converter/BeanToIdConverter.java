package org.mgnl.nicki.vaadin.db.converter;

import org.mgnl.nicki.db.helper.BeanHelper;

public class BeanToIdConverter extends AbstractConverter<Object, Long> {

	@Override
	public Long convert(Object bean) {
		String keyAttribute = BeanHelper.getKeyAttribute(bean);
		Long key = (Long) BeanHelper.getValue(bean, keyAttribute);

		return key;
	}

}

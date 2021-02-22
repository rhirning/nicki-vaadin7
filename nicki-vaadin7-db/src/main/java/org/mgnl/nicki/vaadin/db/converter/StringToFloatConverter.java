package org.mgnl.nicki.vaadin.db.converter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringToFloatConverter extends AbstractConverter<String, Float> {

	@Override
	public Float convert(String value) {
		try {
			return Float.parseFloat(value);
		} catch (NumberFormatException e) {
			log.error("Error parsing " + value);
			return null;
		}
	}

}

package org.mgnl.nicki.vaadin.db.converter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringToLongConverter extends AbstractConverter<String, Long> {

	@Override
	public Long convert(String value) {
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException e) {
			log.error("Error parsing " + value);
			return null;
		}
	}

}

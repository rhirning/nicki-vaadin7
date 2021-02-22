package org.mgnl.nicki.vaadin.db.converter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringToIntegerConverter extends AbstractConverter<String, Integer> {

	@Override
	public Integer convert(String value) {
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			log.error("Error parsing " + value);
			return null;
		}
	}

}

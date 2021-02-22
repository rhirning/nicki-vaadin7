package org.mgnl.nicki.vaadin.db.converter;

import java.time.LocalDate;
import java.util.Date;

import org.mgnl.nicki.core.helper.DataHelper;

public class LocalDateToDateConverter extends AbstractConverter<LocalDate, Date> {

	@Override
	public Date convert(LocalDate value) {
		return DataHelper.getDate(value);
	}

}

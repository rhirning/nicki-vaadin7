
package org.mgnl.nicki.vaadin.base.validation;

/*-
 * #%L
 * nicki-vaadin-base
 * %%
 * Copyright (C) 2017 Ralf Hirning
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.mgnl.nicki.core.objects.DynamicObject;
import org.mgnl.nicki.vaadin.base.editor.ValidationException;

import com.vaadin.ui.Field;

public class Validation {
	public static void notNull(Field<?> component, DynamicObject dynamicObject, String error) throws ValidationException {
		if (dynamicObject == null) {
			if (component != null) {
				component.focus();
			}
			throw new ValidationException(error);
		}
	}
	public static void notNull(DynamicObject dynamicObject, String error) throws ValidationException {
		if (dynamicObject == null) {
			throw new ValidationException(error);
		}
	}

	public static void notNull(Field<?> component, Date date, String error) throws ValidationException {
		if (date == null) {
			if (component != null) {
				component.focus();
			}
			throw new ValidationException(error);
		}
	}

	public static void notNull(Object value, String error) throws ValidationException {
		if (value == null) {
			throw new ValidationException(error);
		}
	}

	public static void dateInFuture(Field<?> component, Date date, String error) throws ValidationException {
		if ((date == null) || (date.compareTo(new Date()) < 0)) {
			if (component != null) {
				component.focus();
			}
			throw new ValidationException(error);
		}
	}

	public static void dateInPast(Field<?> component, Date date, String error) throws ValidationException {
		if ((date == null) || (date.compareTo(new Date()) > 0)) {
			if (component != null) {
				component.focus();
			}
			throw new ValidationException(error);
		}
	}

	public static void notEmpty(Field<?> component, String error) throws ValidationException {
		if (!StringUtils.isNotBlank((String) component.getValue())) {
			if (component != null) {
				component.focus();
			}
			throw new ValidationException(error);
		}
	}

	public static void isTrue(Field<?> component, boolean check, String error) throws ValidationException {
		if (!check) {
			if (component != null) {
				component.focus();
			}
			throw new ValidationException(error);
		}
	}

	public static void isNumeric(Field<?> component, String error) throws ValidationException {
		if (!StringUtils.isNumeric((String) component.getValue())) {
			if (component != null) {
				component.focus();
			}
			throw new ValidationException(error);
		}
	}


}

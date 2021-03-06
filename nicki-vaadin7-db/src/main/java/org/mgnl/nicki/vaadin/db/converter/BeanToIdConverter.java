package org.mgnl.nicki.vaadin.db.converter;

/*-
 * #%L
 * nicki-vaadin7-db
 * %%
 * Copyright (C) 2020 - 2021 Ralf Hirning
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

import org.mgnl.nicki.db.helper.BeanHelper;

public class BeanToIdConverter extends AbstractConverter<Object, Long> {

	@Override
	public Long convert(Object bean) {
		String keyAttribute = BeanHelper.getKeyAttribute(bean);
		Long key = (Long) BeanHelper.getValue(bean, keyAttribute);

		return key;
	}

}

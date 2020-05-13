
package org.mgnl.nicki.vaadin.base.helper;

import java.io.Serializable;

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


public class ValuePair implements Serializable {
	private static final long serialVersionUID = -4811398011208084823L;
	private String name;
	private String value;
	
	
	public ValuePair(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public String getValue() {
		return value;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name).append("=").append(value);
		return sb.toString();
	}

}

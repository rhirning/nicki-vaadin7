
package org.mgnl.nicki.vaadin.base.editor;

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


import java.io.Serializable;

import org.mgnl.nicki.core.data.TreeData;


import com.vaadin.ui.CustomComponent;

	@SuppressWarnings("serial")
	public abstract class BaseTreeAction extends CustomComponent implements TreeAction, Serializable {

		private Class<? extends TreeData> targetClass;
		private String name;
		
		public BaseTreeAction(Class<? extends TreeData> classDefinition, String name) {
			this.targetClass = classDefinition;
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public Class<? extends TreeData> getTargetClass() {
			return targetClass;
		}
		
		@Override
		public void close() {
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(getName()).append(": ").append(getTargetClass().getSimpleName());
			return sb.toString();
		}

	}

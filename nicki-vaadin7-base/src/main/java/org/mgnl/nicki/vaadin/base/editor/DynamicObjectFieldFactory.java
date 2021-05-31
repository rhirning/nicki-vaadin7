
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

import org.mgnl.nicki.core.objects.DataModel;
import org.mgnl.nicki.core.objects.DynamicAttribute;
import org.mgnl.nicki.core.objects.DynamicObject;
import org.mgnl.nicki.dynamic.objects.types.TextArea;
import org.mgnl.nicki.vaadin.base.fields.AttributeTextAreaReadonlyField;
import org.mgnl.nicki.vaadin.base.fields.AttributeTextField;
import org.mgnl.nicki.vaadin.base.fields.DynamicAttributeField;
import org.mgnl.nicki.vaadin.base.fields.TableListReadonlyAttributeField;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;

@SuppressWarnings("serial")
public class DynamicObjectFieldFactory implements Serializable {
	

	public Component createField(HasComponents parent, DynamicObject dynamicObject, String attributeName) {
		DynamicAttribute dynAttribute = dynamicObject.getDynamicAttribute(attributeName);
		DynamicAttributeField<String> field = null;

		if (field == null) {
			if (dynAttribute.isMultiple()) {
				field = new TableListReadonlyAttributeField();
			} else if (dynAttribute.isForeignKey()) {
				field = new AttributeTextAreaReadonlyField();
			} else if (dynAttribute.getAttributeClass() == TextArea.class) {
				field = new AttributeTextAreaReadonlyField();
			} else {
				field = new AttributeTextField();
			}
			field.init(attributeName, dynamicObject, null);
		}
		return field.getComponent(true);
	}
	
	
	public void addFields(HasComponents layout, DynamicObject dynamicObject) {
		DataModel model = dynamicObject.getModel();
		for (DynamicAttribute dynAttribute : model.getAttributes().values()) {
			if (!dynAttribute.isNaming()) {
				layout.add(createField(layout, dynamicObject, dynAttribute.getName()));
			}
		}
	}
	
}

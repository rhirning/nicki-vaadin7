
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

import org.apache.commons.lang.StringUtils;
import org.mgnl.nicki.core.objects.DataModel;
import org.mgnl.nicki.core.objects.DynamicAttribute;
import org.mgnl.nicki.core.objects.DynamicObject;
import org.mgnl.nicki.core.util.Classes;
import org.mgnl.nicki.dynamic.objects.types.TextArea;
import org.mgnl.nicki.vaadin.base.fields.AttributeSelectObjectField;
import org.mgnl.nicki.vaadin.base.fields.AttributeTextAreaField;
import org.mgnl.nicki.vaadin.base.fields.AttributeTextField;
import org.mgnl.nicki.vaadin.base.fields.DynamicAttributeField;
import org.mgnl.nicki.vaadin.base.fields.TableListAttributeField;

import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("serial")
public class DynamicObjectFieldFactory implements Serializable {
	private DynamicObjectValueChangeListener objectListener;
	
	public DynamicObjectFieldFactory(DynamicObjectValueChangeListener objectListener) {
		this.objectListener = objectListener;
	}

	@SuppressWarnings("unchecked")
	public Component createField(Component parent, DynamicObject dynamicObject, String attributeName, boolean create) {
		DynamicAttribute dynAttribute = dynamicObject.getDynamicAttribute(attributeName);
		DynamicAttributeField field = null;
		if (StringUtils.isNotEmpty(dynAttribute.getEditorClass())) {
			try {
				field = (DynamicAttributeField) Classes.newInstance(dynAttribute.getEditorClass());
				field.init(attributeName, dynamicObject, objectListener);
			} catch (Exception e) {
				field = null;
				log.error("Error", e);
			}
		}
		if (field == null) {
			if (dynAttribute.isMultiple()) {
				field = new TableListAttributeField();
			} else if (dynAttribute.isForeignKey()) {
				field = new AttributeSelectObjectField();
			} else if (dynAttribute.getAttributeClass() == TextArea.class) {
				field = new AttributeTextAreaField();
			} else {
				field = new AttributeTextField();
			}
			field.init(attributeName, dynamicObject, objectListener);
		}
		boolean readOnly = dynAttribute.isReadonly();
		if (!create && dynAttribute.isNaming()) {
			readOnly = true;
		}
		return field.getComponent(readOnly);
	}
	
	
	public void addFields(AbstractOrderedLayout layout, DynamicObject dynamicObject, boolean create) {
		DataModel model = dynamicObject.getModel();
		for (DynamicAttribute dynAttribute : model.getAttributes().values()) {
			if (!dynAttribute.isNaming()
					&& (objectListener == null || objectListener.acceptAttribute(dynAttribute.getName()))) {
				layout.addComponent(createField(layout, dynamicObject, dynAttribute.getName(), create));
			}
		}
	}
	
}

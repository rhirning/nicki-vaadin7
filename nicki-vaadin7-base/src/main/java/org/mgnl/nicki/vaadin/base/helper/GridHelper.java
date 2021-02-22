package org.mgnl.nicki.vaadin.base.helper;

import java.util.ArrayList;
import java.util.List;

import org.mgnl.nicki.core.context.NickiContext;
import org.mgnl.nicki.core.i18n.I18n;
import org.mgnl.nicki.core.objects.DataModel;
import org.mgnl.nicki.core.objects.DynamicAttribute;
import org.mgnl.nicki.core.objects.DynamicObject;
import org.mgnl.nicki.vaadin.base.data.DynamicObjectGridColumn;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GridHelper {

	public static <T extends DynamicObject> List<DynamicAttribute> getColumnAttributes(NickiContext context, Class<T> clazz) {
		List<DynamicAttribute> list = new ArrayList<>();
		try {
			DataModel model = context.getDataModel(clazz);
			for (DynamicAttribute dynAttribute : model.getAttributes().values()) {
				if (dynAttribute.isSearchable()) {
					list.add(dynAttribute);
				}
			}
		} catch (Exception e) {
			log.error("Error reading datamodel", e);
		}
		return list;
	}

	
	public static <T extends DynamicObject> List<DynamicObjectGridColumn<T>> getColumns(NickiContext context, Class<T> clazz) {
		List<DynamicObjectGridColumn<T>> list = new ArrayList<DynamicObjectGridColumn<T>>();
		
		for (DynamicAttribute dynAttribute : getColumnAttributes(context, clazz)) {
			if (dynAttribute.isSearchable()) {
				list.add(new DynamicObjectGridColumn<T>(dynAttribute.getName(), I18n.getText(dynAttribute.getCaption(), dynAttribute.getName())));
			}
		}
		return list;
	}
}

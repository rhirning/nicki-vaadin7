package org.mgnl.nicki.vaadin.base.views;

/*-
 * #%L
 * nicki-vaadin-base
 * %%
 * Copyright (C) 2017 - 2018 Ralf Hirning
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

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.mgnl.nicki.core.auth.InvalidPrincipalException;
import org.mgnl.nicki.core.config.Config;
import org.mgnl.nicki.core.context.AppContext;
import org.mgnl.nicki.core.context.NickiContext;
import org.mgnl.nicki.core.data.InstantiateDynamicObjectException;
import org.mgnl.nicki.core.objects.DynamicObject;
import org.mgnl.nicki.core.objects.DynamicObjectException;

/**
 * 
 * @author Ralf Hirning
 * 
 * configuration:
 * 
 * targetName (optional)		Name of Target where to store the script
 * configPath					Config key for Script path
 * 
 *
 */
@SuppressWarnings("serial")
public abstract class DynamicObjectInfo<T extends DynamicObject> implements InfoStore {

	private Map<String, String> configuration;
	private T dynamicObject;
	private Class<? extends T> clazz;
	
	public DynamicObjectInfo(Class<? extends T> clazz) {
		this.clazz = clazz;
	}
	
	public void setData(String value) throws InfoStoreException {
		load();
		dynamicObject.put(getAttributeName(), value);
	}

	@Override
	public void setConfiguration(Map<String, String> configuration) {
		this.configuration = configuration;
	}

	private void load() throws InfoStoreException {
		if (dynamicObject == null) {
			try {
				dynamicObject = getContext().loadObject(clazz, getPath());
			} catch (InvalidPrincipalException e) {
//				throw new InfoStoreException(e);
			}
			
			if (dynamicObject == null) {
	
				String dn = getPath();
				String parentPath = getParentPath(dn);
				String namingvalue = getNamingValue(dn);
				try {
					dynamicObject = getContext().createDynamicObject(clazz, parentPath, namingvalue);
					dynamicObject.create();
				} catch (InstantiateDynamicObjectException | DynamicObjectException | InvalidPrincipalException e) {
					throw new InfoStoreException(e);
				}
			}
		}
	}

	private NickiContext getContext() throws InvalidPrincipalException {
		return AppContext.getSystemContext(getTargetName());
	}

	private String getPath() {
		return Config.getString(configuration.get("configPath"));
	}

	private String getTargetName() throws InvalidPrincipalException {
		if (configuration.containsKey("targetName")) {
			return configuration.get("targetName");
		} else {
			return AppContext.getSystemContext().getTarget().getName();
		}
	}

	@Override
	public String getData() throws InfoStoreException {
		load();
		return (String) dynamicObject.get(getAttributeName());
	}

	@Override
	public void save() throws InfoStoreException {
		load();
		try {
			dynamicObject.update(getAttributeName());
		} catch (DynamicObjectException e) {
			throw new InfoStoreException(e);
		}
	}

	protected abstract String getAttributeName();

	public static String getParentPath(String path) {
		return StringUtils.strip(StringUtils.substringAfter(path, ","));
	}

	public static String getNamingValue(String path) {
		return StringUtils.strip(StringUtils.substringAfter(StringUtils.substringBefore(path, ","), "="));
	}

}

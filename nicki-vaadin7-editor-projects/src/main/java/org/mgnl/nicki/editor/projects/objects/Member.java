
package org.mgnl.nicki.editor.projects.objects;

/*-
 * #%L
 * nicki-editor-projects
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
import java.util.ArrayList;
import java.util.List;

import org.mgnl.nicki.core.annotation.DynamicAttribute;
import org.mgnl.nicki.core.annotation.DynamicObject;
import org.mgnl.nicki.core.annotation.ObjectClass;
import org.mgnl.nicki.core.objects.BaseDynamicObject;
import org.mgnl.nicki.core.objects.DynamicObjectException;
import org.mgnl.nicki.dynamic.objects.objects.Person;

@DynamicObject
@ObjectClass({ "nickiProjectMember" })
@SuppressWarnings("serial")
public class Member extends BaseDynamicObject implements Serializable {

	public enum RIGHT {

		NONE, READ, WRITE
	};

	public static final String ATTRIBUTE_DIRECTORY_READ = "directoryRead";
	public static final String ATTRIBUTE_DIRECTORY_WRITE = "directoryWrite";
	public static final String ATTRIBUTE_MEMBER = "member";
	

	@DynamicAttribute(externalName = "cn", naming = true)
	private String name;
	@DynamicAttribute(externalName = "nickiProjectPerson")
	private String member;
	@DynamicAttribute(externalName = "nickiProjectDirectoryRead")
	private String directoryRead[];
	@DynamicAttribute(externalName = "nickiProjectDirectoryWrite")
	private String directoryWrite[];
	
	@Override
	public String getDisplayName() {
		return getForeignKeyObject(Person.class, ATTRIBUTE_MEMBER).getDisplayName();
	}

	public Person getPerson() {
		return getForeignKeyObject(Person.class, ATTRIBUTE_MEMBER);
	}

	@SuppressWarnings("unchecked")
	public List<String> getReadRights() {
		return get(ATTRIBUTE_DIRECTORY_READ) != null ? (List<String>) get(ATTRIBUTE_DIRECTORY_READ) : new ArrayList<String>();
	}

	public void setReadRights(List<String> readRights) {
		put(ATTRIBUTE_DIRECTORY_READ, readRights);
	}

	@SuppressWarnings("unchecked")
	public List<String> getWriteRights() {
		return get(ATTRIBUTE_DIRECTORY_WRITE) != null ? (List<String>) get(ATTRIBUTE_DIRECTORY_WRITE) : new ArrayList<String>();
	}

	public void setWriteRights(List<String> writeRights) {
		put("directoryWrite", writeRights);
	}

	public void setRight(Directory directory, RIGHT right) {
		if (right == RIGHT.READ) {
			addReadRight(directory);
		} else if (right == RIGHT.WRITE) {
			addWriteRight(directory);
		} else {
			removeRights(directory);
		}
	}

	public void setReadRight(Directory directory) {
		addReadRight(directory);
		removeWriteRight(directory);
	}

	public void removeRights(Directory directory) {
		removeReadRight(directory);
		removeWriteRight(directory);
	}

	public void setWriteRight(Directory directory) {
		addWriteRight(directory);
		removeReadRight(directory);
	}

	public boolean hasReadRight(Directory directory) {
		return getReadRights().contains(directory.getPath());
	}

	public boolean hasWriteRight(Directory directory) {
		return getWriteRights().contains(directory.getPath());
	}

	public void addReadRight(Directory directory) {
		List<String> list = getReadRights();
		if (!list.contains(directory.getPath())) {
			list.add(directory.getPath());
			setReadRights(list);
		}
	}

	public void removeReadRight(Directory directory) {
		List<String> list = getReadRights();
		if (list.contains(directory.getPath())) {
			list.remove(directory.getPath());
			setReadRights(list);
		}
	}

	public void addWriteRight(Directory directory) {
		List<String> list = getWriteRights();
		if (!list.contains(directory.getPath())) {
			list.add(directory.getPath());
			setWriteRights(list);
		}
	}

	public void removeWriteRight(Directory directory) {
		List<String> list = getWriteRights();
		if (list.contains(directory.getPath())) {
			list.remove(directory.getPath());
			setWriteRights(list);
		}
	}

	@Override
	public void delete() throws DynamicObjectException {
		setReadRights(null);
		setWriteRights(null);

		update();

		super.delete();
	}
}

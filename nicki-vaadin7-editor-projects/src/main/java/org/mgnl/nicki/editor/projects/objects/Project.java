
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
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.mgnl.nicki.core.annotation.Child;
import org.mgnl.nicki.core.annotation.DynamicAttribute;
import org.mgnl.nicki.core.annotation.DynamicObject;
import org.mgnl.nicki.core.annotation.DynamicReferenceAttribute;
import org.mgnl.nicki.core.annotation.ObjectClass;
import org.mgnl.nicki.core.data.InstantiateDynamicObjectException;
import org.mgnl.nicki.core.objects.BaseDynamicObject;
import org.mgnl.nicki.dynamic.objects.objects.Person;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("serial")
@DynamicObject
@ObjectClass({ "nickiProject" })
@Child(name="child", objectFilter={Directory.class, Member.class})
public class Project extends BaseDynamicObject implements Serializable {
	public static final String ATTRIBUTE_PROJECT_DIRECTORY = "projectDirectory";
	public static final String ATTRIBUTE_DESCRIPTION = "description";
	public static final String ATTRIBUTE_OWNER = "owner";
	public static final String ATTRIBUTE_DEPUTY = "deputy";
	public static final String ATTRIBUTE_DIRECTORY = "directory";
	public static final String ATTRIBUTE_MEMBER = "member";


	@DynamicAttribute(externalName = "cn", naming = true)
	private String name;
	@DynamicAttribute(externalName = "nickiProjectDirectory")
	private String projectDirectory;

	@DynamicAttribute(externalName = "nickiDescription")
	private String description;

	@DynamicReferenceAttribute(externalName="nickiOwner", foreignKey=Person.class,
			reference=Person.class,
			baseProperty="nicki.users.basedn")
	private String owner;

	@DynamicReferenceAttribute(externalName="nickiDeputy", foreignKey=Person.class,
	reference=Person.class,
	baseProperty="nicki.users.basedn")
	private String deputy;
	

	public String getDescription() {
		String description = getAttribute(ATTRIBUTE_DESCRIPTION);
		return description!=null?description:"";
	}
	
	public String getProjectDirectory() {
		String directory = getAttribute(ATTRIBUTE_PROJECT_DIRECTORY);
		return directory!=null?directory:"";
	}

	public void setDescription(String description) {
		put(ATTRIBUTE_DESCRIPTION, description);
	}
	
	public void setProjectDirectory(String directory) {
		put(ATTRIBUTE_PROJECT_DIRECTORY, directory);
	}
	
	public void setDirectory(String directory) {
		put(ATTRIBUTE_DIRECTORY, directory);
	}

	public Person getProjectLeader() {
		return getForeignKeyObject(Person.class, ATTRIBUTE_OWNER);
	}

	public void setProjectLeader(Person projectLeader) {
		this.put(ATTRIBUTE_OWNER, projectLeader.getPath());
	}

	public boolean isProjectLeader(org.mgnl.nicki.core.objects.DynamicObject user) {
		try {
			return StringUtils.equalsIgnoreCase(user.getPath(), getProjectLeader().getPath());
		} catch (Exception e) {
			log.debug("Error", e);
		}
		return false;

	}

	public Member getDeputyProjectLeader() {
		return getForeignKeyObject(Member.class, ATTRIBUTE_MEMBER);
	}
	
	public boolean isProjectDeputyLeader(org.mgnl.nicki.core.objects.DynamicObject user) {
		try {
			return StringUtils.equalsIgnoreCase(user.getPath(),
					getDeputyProjectLeader().getForeignKeyObject(Person.class, ATTRIBUTE_MEMBER).getPath());
		} catch (Exception e) {
			log.debug("Error", e);
		}
		return false;

	}

	public void setDeputyProjectLeader(Member deputyProjectLeader) {
		this.put(ATTRIBUTE_DEPUTY, deputyProjectLeader.getPath());
	}

	public Collection<Member> getMembers() {
		return getChildren(Member.class);
	}

	public Collection<Directory> getDirectories() {
		return getChildren(Directory.class);
	}

	public void removeMember(Member target) {
		this.getMembers().remove(target);
	}

	public void addDirectory(String name) throws InstantiateDynamicObjectException {
		Directory direcory = getContext().getObjectFactory().getNewDynamicObject(Directory.class,
				getPath(), name);
		this.getDirectories().add(direcory);
	}
}

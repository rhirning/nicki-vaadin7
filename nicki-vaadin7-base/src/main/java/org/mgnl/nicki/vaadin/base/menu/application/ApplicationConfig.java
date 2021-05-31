package org.mgnl.nicki.vaadin.base.menu.application;

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

import java.util.List;
import java.util.Map;

import org.mgnl.nicki.core.util.Classes;
import org.mgnl.nicki.dynamic.objects.objects.Person;
import org.mgnl.nicki.vaadin.base.application.AccessGroupEvaluator;
import org.mgnl.nicki.vaadin.base.application.AccessRoleEvaluator;
import org.mgnl.nicki.vaadin.base.application.DefaultGroupEvaluator;
import org.mgnl.nicki.vaadin.base.application.DefaultRoleEvaluator;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApplicationConfig {
	private AccessGroupEvaluator accessGroupEvaluator = new DefaultGroupEvaluator();
	private AccessRoleEvaluator accessRoleEvaluator = new DefaultRoleEvaluator();
	private String accessGroupEvaluatorClass;
	private String accessRoleEvaluatorClass;
	private @Getter @Setter ApplicationView start;
	private @Getter @Setter List<ApplicationChapter> chapters;
	private @Getter @Setter Map<String, String> config;


	public String getAccessGroupEvaluatorClass() {
		return accessGroupEvaluatorClass;
	}

	public void setAccessGroupEvaluatorClass(String accessGroupEvaluatorClass) {
		this.accessGroupEvaluatorClass = accessGroupEvaluatorClass;
		try {
			this.accessGroupEvaluator = Classes.newInstance(accessGroupEvaluatorClass);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			log.error("Invalid AccessGroupEvaluatorClass", e);
		}
	}

	public String getAccessRoleEvaluatorClass() {
		return accessRoleEvaluatorClass;
	}

	public void setAccessRoleEvaluatorClass(String accessRoleEvaluatorClass) {
		this.accessRoleEvaluatorClass = accessRoleEvaluatorClass;
		try {
			this.accessRoleEvaluator = Classes.newInstance(accessRoleEvaluatorClass);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			log.error("Invalid AccessRoleEvaluatorClass", e);
		}
	}

	public boolean isAllowed(Person person, List<String> groups, List<String> roles) {
		if (isEmpty(groups) && isEmpty(roles)) {
			return true;
		}
		if (!isEmpty(groups)) {
			if (accessGroupEvaluator.isMemberOf(person, groups.toArray(new String[0]))) {
				return true;
			}
		}
		if (!isEmpty(roles)) {
			if (accessRoleEvaluator.hasRole(person, groups.toArray(new String[0]))) {
				return true;
			}
		}
		return false;
	}

	private boolean isEmpty(List<String> groups) {
		return groups == null || groups.size() == 0;
	}

}

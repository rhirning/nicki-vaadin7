
package org.mgnl.nicki.editor.projects.core;

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

import org.mgnl.nicki.core.objects.DynamicObject;
import org.mgnl.nicki.core.data.EntryFilter;
import org.mgnl.nicki.core.data.TreeData;
import org.mgnl.nicki.editor.projects.objects.Project;

@SuppressWarnings("serial")
public class ProjectFilter implements EntryFilter, Serializable {
	private DynamicObject user;

	public ProjectFilter(DynamicObject user) {
		super();
		this.user = user;
	}

	public boolean accepts(TreeData object) {
		if (object instanceof Project) {
			Project project = (Project) object;
			if (!project.isProjectLeader(user) && !project.isProjectDeputyLeader(user)) {
				return false;
			}
		}
		return true;
	}

}

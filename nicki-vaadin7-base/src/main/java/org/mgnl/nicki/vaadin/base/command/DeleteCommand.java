
package org.mgnl.nicki.vaadin.base.command;

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


import org.mgnl.nicki.core.data.InvalidActionException;
import org.mgnl.nicki.core.data.TreeData;
import org.mgnl.nicki.core.i18n.I18n;
import org.mgnl.nicki.core.objects.DynamicObjectException;
import org.mgnl.nicki.vaadin.base.editor.NickiTreeEditor;
import org.mgnl.nicki.vaadin.base.notification.Notification;

public class DeleteCommand implements Command {
	private TreeData target;
	private NickiTreeEditor nickiEditor;

	public DeleteCommand(NickiTreeEditor nickiEditor, TreeData target) {
		this.nickiEditor = nickiEditor;
		this.target = target;
	}

	public void execute() throws CommandException {
		TreeData parent = this.nickiEditor.getParent(this.target);
		try {
			this.target.delete();
		} catch (DynamicObjectException | InvalidActionException e) {
			throw new CommandException(e);
		}
		nickiEditor.removeItem(target);
		Notification.show(I18n.getText("nicki.editor.delete.info"));
		if (parent != null) {
			this.nickiEditor.refresh(parent);
		}
	}

	public String getTitle() {
		return I18n.getText("nicki.editor.delete.title");
	}

	public String getHeadline() {
		return I18n.getText("nicki.editor.delete.headline", target.getDisplayName());
	}

	public String getCancelCaption() {
		return I18n.getText("nicki.editor.delete.cancel");
	}

	public String getConfirmCaption() {
		return I18n.getText("nicki.editor.delete.confirm");
	}

	public String getErrorText() {
		return I18n.getText("nicki.editor.delete.error");
	}
}

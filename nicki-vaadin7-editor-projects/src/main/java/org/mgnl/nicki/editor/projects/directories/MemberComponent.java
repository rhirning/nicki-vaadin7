
package org.mgnl.nicki.editor.projects.directories;

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


import org.mgnl.nicki.core.i18n.I18n;
import org.mgnl.nicki.editor.projects.objects.Directory;
import org.mgnl.nicki.editor.projects.objects.Member;
import org.mgnl.nicki.editor.projects.objects.Member.RIGHT;


import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class MemberComponent extends CustomComponent {

	@AutoGenerated
	private VerticalLayout mainLayout;
	@AutoGenerated
	private HorizontalLayout horizontalLayout_1;

	private OptionGroup rights;
	@AutoGenerated
	private Label memberLabel;
	private Directory directory;
	private Member member;
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public MemberComponent(Directory target, Member person) {
		this.member = person;
		this.directory = target;
		buildMainLayout();
		setCompositionRoot(mainLayout);

		memberLabel.setValue(this.member.getDisplayName());

		rights.addItem(Member.RIGHT.NONE);
		rights.setItemCaption(Member.RIGHT.NONE, I18n.getText("nicki.editor.projects.right.none"));
		rights.addItem(Member.RIGHT.READ);
		rights.setItemCaption(Member.RIGHT.READ, I18n.getText("nicki.editor.projects.right.read"));
		rights.addItem(Member.RIGHT.WRITE);
		rights.setItemCaption(Member.RIGHT.WRITE, I18n.getText("nicki.editor.projects.right.write"));
		if (member.hasReadRight(directory)) {
			rights.select(Member.RIGHT.READ);
		} else if (member.hasWriteRight(directory)) {
				rights.select(Member.RIGHT.WRITE);
		} else {
			rights.select(Member.RIGHT.NONE);
		}
		rights.setStyleName("horizontal");
	}

	public void save() {
		member.setRight(directory, (RIGHT) rights.getValue());
	}

	@AutoGenerated
	private VerticalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new VerticalLayout();
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("-1");
		
		// horizontalLayout_1
		horizontalLayout_1 = buildHorizontalLayout_1();
		mainLayout.addComponent(horizontalLayout_1);
		
		return mainLayout;
	}

	@AutoGenerated
	private HorizontalLayout buildHorizontalLayout_1() {
		// common part: create layout
		horizontalLayout_1 = new HorizontalLayout();
		horizontalLayout_1.setWidth("100.0%");
		horizontalLayout_1.setHeight("-1px");
		horizontalLayout_1.setMargin(false);
		
		// memberLabel
		memberLabel = new Label();
		memberLabel.setWidth("200px");
		memberLabel.setHeight("-1px");
		memberLabel.setValue("Mitglied");
		horizontalLayout_1.addComponent(memberLabel);
		
		// rights
		rights = new OptionGroup();
		rights.setWidth("-1px");
		rights.setHeight("-1px");
		horizontalLayout_1.addComponent(rights);

		return horizontalLayout_1;
	}

	public Member getMember() {
		return member;
	}

}

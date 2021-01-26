
package org.mgnl.nicki.shop.core;

/*-
 * #%L
 * nicki-shop
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


import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.mgnl.nicki.core.helper.DataHelper;
import org.mgnl.nicki.core.i18n.I18n;
import org.mgnl.nicki.shop.base.inventory.Inventory;
import org.mgnl.nicki.shop.base.inventory.InventoryArticle;
import org.mgnl.nicki.shop.base.objects.CatalogArticle;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Item;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class InventoryViewer extends CustomComponent {

	@AutoGenerated
	private VerticalLayout mainLayout;
	@AutoGenerated
	private Table inventoryEntries;
	@AutoGenerated
	private Label recipient;

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */
	
	private static final long serialVersionUID = 8441664095916322794L;
	private Inventory inventory;

	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public InventoryViewer(Inventory inventory) {
		this.inventory = inventory;
		buildMainLayout();
		setCompositionRoot(mainLayout);

		this.recipient.setValue(this.inventory.getPerson().getDisplayName());
		fillInventoryEntries();
	}

	@SuppressWarnings("unchecked")
	private void fillInventoryEntries() {

		inventoryEntries.addContainerProperty("status", String.class, "");
		inventoryEntries.setColumnWidth("status", 100);
		inventoryEntries.setColumnHeader("status", "Status");
		inventoryEntries.addContainerProperty("start", String.class, "");
		inventoryEntries.setColumnWidth("start", 70);
		inventoryEntries.setColumnHeader("start", I18n.getText(CatalogArticle.CAPTION_START));
		inventoryEntries.addContainerProperty("article", String.class, "");
		inventoryEntries.setColumnWidth("article", -1);
		inventoryEntries.setColumnHeader("article", "Artikel");
		for (String key : inventory.getMultiArticles().keySet()) {
			Map<String, InventoryArticle> list = inventory.getMultiArticles().get(key);
			for (String specifier : list.keySet()) {
				InventoryArticle iArticle = list.get(specifier);
				Item item = inventoryEntries.addItem(iArticle);
				String status = iArticle.getStatus().toString();
				if (iArticle.isReadOnly()) {
					status += ";R/O";
				}
				item.getItemProperty("status").setValue(status);
				item.getItemProperty("article").setValue(iArticle.getArticle().getDisplayName()
						+ ": " + iArticle.getSpecifier());
				if (iArticle.getStart() != null) {
					item.getItemProperty("start").setValue(DataHelper.getDisplayDay(iArticle.getStart()));
				}
			}
		}
		for (String key : inventory.getArticles().keySet()) {
			InventoryArticle iArticle = inventory.getArticles().get(key);
			Item item = inventoryEntries.addItem(iArticle);
			String status = iArticle.getStatus().toString();
			if (iArticle.isReadOnly()) {
				status += ";R/O";
			}
			item.getItemProperty("status").setValue(status);
			String articleEntry = iArticle.getArticle().getDisplayName();
			if (StringUtils.isNotBlank(iArticle.getSpecifier())) {
				articleEntry += "(" + iArticle.getSpecifier() + ")";
			}
			item.getItemProperty("article").setValue(articleEntry);
			if (iArticle.getStart() != null) {
				item.getItemProperty("start").setValue(DataHelper.getDisplayDay(iArticle.getStart()));
			}
		}
	}

	@AutoGenerated
	private VerticalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");
		
		// recipient
		recipient = new Label();
		recipient.setCaption("Empf�nger");
		recipient.setWidth("-1px");
		recipient.setHeight("-1px");
		recipient.setValue("recipient");
		mainLayout.addComponent(recipient);
		
		// inventoryEntries
		inventoryEntries = new Table();
		inventoryEntries.setWidth("100.0%");
		inventoryEntries.setHeight("100.0%");
		mainLayout.addComponent(inventoryEntries);
		mainLayout.setExpandRatio(inventoryEntries, 1.0f);
		
		return mainLayout;
	}

}

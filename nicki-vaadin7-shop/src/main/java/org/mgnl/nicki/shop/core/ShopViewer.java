
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



import java.io.Serializable;
import java.util.List;

import org.mgnl.nicki.core.auth.InvalidPrincipalException;
import org.mgnl.nicki.core.data.InstantiateDynamicObjectException;
import org.mgnl.nicki.core.i18n.I18n;
import org.mgnl.nicki.core.util.Classes;
import org.mgnl.nicki.dynamic.objects.objects.Person;
import org.mgnl.nicki.shop.base.objects.Cart;
import org.mgnl.nicki.shop.base.objects.CatalogArticle;
import org.mgnl.nicki.shop.base.inventory.Inventory;
import org.mgnl.nicki.shop.renderer.ShopRenderer;
import org.mgnl.nicki.shop.renderer.TabRenderer;

import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("serial")
public class ShopViewer extends CustomComponent implements ShopViewerComponent, CloseListener, Serializable {

	private Person shopper;
	private Person recipient;
	private Inventory inventory;
	private Shop shop;
	private Button saveButton;
	private Button rememberButton;
	private Button showInventoryButton;
	private Button showCartButton;
	private ShopRenderer renderer;
	private ShopParent parent;
	private Cart cart;
	private ShopViewer showViewer;;

	public ShopViewer(Person user, Shop shop, Person recipient, ShopParent parent, Cart cart) throws InvalidPrincipalException, InstantiateDynamicObjectException {
		this.showViewer = this;
		this.shopper = user;
		this.shop = shop;
		this.recipient = recipient;
		this.parent = parent;
		this.cart = cart;
		if (cart != null) {
			this.inventory = Inventory.fromCart(user, recipient, cart);
		} else {
			this.inventory = new Inventory(user, recipient);
		}
		init();
	}
	
	private void init() {
		if (shop.getRenderer() != null) {
			try {
				this.renderer = (ShopRenderer) Classes.newInstance(shop.getRenderer());
			} catch (Exception e) {
				log.error("Error", e);
				this.renderer = null;
			}
		}
		if (this.renderer == null) {
			this.renderer = new TabRenderer();
		}
		setCompositionRoot(getShop());
	}
	
	private VerticalLayout getShop() {
		VerticalLayout shopLayout = new VerticalLayout();
		shopLayout.setHeight("100%");
		AbsoluteLayout layout = new AbsoluteLayout();
		layout.setHeight(24, Unit.PIXELS);
		layout.setWidth("100%");

		saveButton = new Button(I18n.getText("nicki.editor.generic.button.save"));
		saveButton.addClickListener(new Button.ClickListener() {
			
			public void buttonClick(ClickEvent event) {
				if (getInventory() != null) {
					// log.debug(getInventory().toString());
					try {
						if (!getInventory().hasChanged()) {
							Notification.show(I18n.getText(parent.getI18nBase() + ".save.empty"),
									Notification.Type.HUMANIZED_MESSAGE);
						} else {
							setCart(getInventory().save("shop", getCart()));
							Notification.show(I18n.getText(parent.getI18nBase() + ".save.success"),
									Notification.Type.HUMANIZED_MESSAGE);
							parent.closeShop();
						}
					} catch (Exception e) {
						Notification.show(I18n.getText(parent.getI18nBase() + ".save.error"),
								e.getMessage(),
								Notification.Type.ERROR_MESSAGE);
						log.error("Error", e);
					}
				}
			}
		});
		

		rememberButton = new Button(I18n.getText("nicki.editor.generic.button.remember"));
		rememberButton.addClickListener(new Button.ClickListener() {
			
			public void buttonClick(ClickEvent event) {
				if (getInventory() != null) {
					// log.debug(getInventory().toString());
					try {
						if (!getInventory().hasChanged()) {
							Notification.show(I18n.getText(parent.getI18nBase() + ".remember.empty"),
									Notification.Type.HUMANIZED_MESSAGE);
						} else {
							setCart(getInventory().remember("shop", getCart()));
							Notification.show(I18n.getText(parent.getI18nBase() + ".remember.success"),
									Notification.Type.HUMANIZED_MESSAGE);
							//parent.closeShop();
							parent.render(cart);
						}
					} catch (Exception e) {
						Notification.show(I18n.getText(parent.getI18nBase() + ".remember.error"),
								e.getMessage(),
								Notification.Type.ERROR_MESSAGE);
						log.error("Error", e);
					}
				}
			}
		});
		

		showInventoryButton = new Button(I18n.getText("nicki.editor.generic.button.showInventory"));
		showInventoryButton.addClickListener(new Button.ClickListener() {
			
			public void buttonClick(ClickEvent event) {
				if (getInventory() != null) {
					InventoryViewer inventoryViewer = new InventoryViewer(getInventory());
					Window newWindow = new Window(I18n.getText(parent.getI18nBase() + ".inventory.window.title"), inventoryViewer);
					newWindow.setWidth(1000, Unit.PIXELS);
					newWindow.setHeight(600, Unit.PIXELS);
					newWindow.setModal(true);
					UI.getCurrent().addWindow(newWindow);
				} else {
					Notification.show(I18n.getText(parent.getI18nBase() + ".showInventory.empty"),
							Notification.Type.HUMANIZED_MESSAGE);
				}
			}
		});

		showCartButton = new Button(I18n.getText("nicki.editor.generic.button.showCart"));
		showCartButton.addClickListener(new Button.ClickListener() {
			
			public void buttonClick(ClickEvent event) {
				if (getInventory() != null) {
					if (!getInventory().hasChanged()) {
						Notification.show(I18n.getText(parent.getI18nBase() + ".showCart.empty"),
								Notification.Type.HUMANIZED_MESSAGE);
					} else {
						CartViewer cartViewer = new CartViewer(getInventory());
						Window newWindow = new Window(I18n.getText(parent.getI18nBase() + ".cart.window.title"), cartViewer);
						newWindow.addCloseListener(showViewer);
						newWindow.setWidth(1000, Unit.PIXELS);
						newWindow.setHeight(600, Unit.PIXELS);
						newWindow.setModal(true);
						UI.getCurrent().addWindow(newWindow);
						/*
						getWindow().showNotification(I18n.getText(parent.getI18nBase() + ".showCart.success"), getInventory().toString(),
								Notification.TYPE_HUMANIZED_MESSAGE);
						*/
					}
				} else {
					Notification.show(I18n.getText(parent.getI18nBase() + ".showCart.empty"),
							Notification.Type.HUMANIZED_MESSAGE);
				}
			}
		});

		layout.addComponent(saveButton, "top:0.0px;left:20.0px;");
		layout.addComponent(rememberButton, "top:0.0px;left:220.0px;");
		layout.addComponent(showInventoryButton, "top:0.0px;right:200.0px;");
		layout.addComponent(showCartButton, "top:0.0px;right:20.0px;");
		shopLayout.addComponent(layout);
		Component shopComponent = renderer.render(getShopViewerComponent(), getInventory());
		shopLayout.addComponent(shopComponent);
		shopComponent.setSizeFull();
		shopLayout.setExpandRatio(shopComponent, 1);
		return shopLayout;
	}
	@Override
	public void windowClose(CloseEvent e) {
		setCompositionRoot(getShop());
	}


	public ShopViewerComponent getShopViewerComponent() {
		return this;
	}


	public List<ShopPage> getPageList() {
		return this.shop.getPageList();
	}


	public List<CatalogArticle> getArticles() {
		return shop.getArticles();
	}

	public List<CatalogArticle> getAllArticles() {
		return shop.getAllArticles();
	}


	public Inventory getInventory() {
		return inventory;
	}


	public Cart getCart() {
		return cart;
	}


	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Person getRecipient() {
		return recipient;
	}

	public void setRecipient(Person recipient) {
		this.recipient = recipient;
	}

	public Person getShopper() {
		return shopper;
	}

	public void setShopper(Person shopper) {
		this.shopper = shopper;
	}


}

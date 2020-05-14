
package org.mgnl.nicki.shop.rules;

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


import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.mgnl.nicki.core.config.Config;
import org.mgnl.nicki.core.context.AppContext;
import org.mgnl.nicki.core.data.InstantiateDynamicObjectException;
import org.mgnl.nicki.core.objects.DynamicObjectException;
import org.mgnl.nicki.dynamic.objects.objects.Person;
import org.mgnl.nicki.ldap.helper.LdapHelper;
import org.mgnl.nicki.ldap.helper.LdapHelper.LOGIC;
import org.mgnl.nicki.shop.base.objects.Cart;
import org.mgnl.nicki.shop.base.objects.CartEntry;
import org.mgnl.nicki.shop.base.objects.Catalog;
import org.mgnl.nicki.shop.base.objects.CatalogArticle;
import org.mgnl.nicki.shop.base.objects.Selector;
import org.mgnl.nicki.shop.base.objects.ValueProvider;
import org.mgnl.nicki.shop.base.inventory.Inventory;

import lombok.extern.slf4j.Slf4j;

/* 
 * Attribute nickiDirectory: Pfad getrennt mit "/"
 */
@Slf4j
public class RuleManager {
	private static String activeFilter;
	
	static {
		try {
			activeFilter = AppContext.getSystemContext().getObjectFactory().getDynamicObject(Person.class).getActiveFilter();
		} catch (Exception e) {
			activeFilter = null;
			log.error("Error", e);
		}

	}
	
	public static List<CatalogArticle> getArticles(Person person) {
		String query = getArticleQuery(person);
		return person.getContext().loadObjects(CatalogArticle.class,
				Config.getString("nicki.catalogs.basedn"), query);
	}
	
	public static String getArticleQuery(Person person) {
		StringBuilder sb = new StringBuilder();
		LdapHelper.addQuery(sb, "nickiRule=*", LOGIC.AND);
		List<Selector> selectors = person.getContext().loadChildObjects(Selector.class, 
				Config.getString("nicki.selectors.basedn"), ""); 
		for (Selector selector : selectors) {
			Object value = person.get(selector.getName());
			if (selector.hasValueProvider()) {
				ValueProvider valueProvider = selector.getValueProvider();
				LdapHelper.addQuery(sb, valueProvider.getArticleQuery(person, value), LOGIC.AND);
			} else {
				StringBuilder sb2 = new StringBuilder();
				LdapHelper.addQuery(sb2, "nickiRule=" + selector.getName() + "=*", LOGIC.OR);
				LdapHelper.negateQuery(sb2);
				if (value != null && value instanceof String) {
					String stringValue = (String) value;
					LdapHelper.addQuery(sb2, "nickiRule=" + selector.getName() + "=" + stringValue, LOGIC.OR);
				} else if (value != null && value instanceof List) {
					@SuppressWarnings("unchecked")
					List<String> values = (List<String>) value;
					for (String stringValue : values) {
						LdapHelper.addQuery(sb2, "nickiRule=" + selector.getName() + "=" + stringValue, LOGIC.OR);
					}
				}
				LdapHelper.addQuery(sb, sb2.toString(), LOGIC.AND);
			}
		}
		return sb.toString();
	}

	public static List<Person> getUsers(CatalogArticle article, String additionalQuery) {
		if (!article.hasRules()) {
			if (StringUtils.isEmpty(additionalQuery)) {
				return new ArrayList<Person>();
			}
		}
		
		RuleQuery query = getRuleQuery(article);
		if (!query.isNeedQuery()) {
			return new ArrayList<Person>();
		}
		StringBuilder filter = new StringBuilder();
		if (StringUtils.isNotEmpty(query.getQuery())) {
			LdapHelper.addQuery(filter, query.getQuery(), LOGIC.AND);
		}
		if (StringUtils.isNotEmpty(additionalQuery)) {
			LdapHelper.addQuery(filter, additionalQuery, LOGIC.AND);
		}
		return article.getContext().loadObjects(Person.class, query.getBaseDn(), filter.toString());
	}

	public static String getAssignedRuleArticlesQuery(Person person) {
		StringBuilder sb = new StringBuilder();
		@SuppressWarnings("unchecked")
		List<String> articles = (List<String>) person.get(Person.ATTRIBUTE_ASSIGNEDARTICLE);
		if (articles != null && articles.size() > 0) {
			for (String article : articles) {
				LdapHelper.addQuery(sb, "entryDn=" + article, LOGIC.OR);
			}
		}
		return sb.toString();
	}
	
	public static String getAssignedRulePersonsQuery(CatalogArticle article) {
		return "(nickiCatalogArticle=rule#" + article.getPath() + "#*)";
	}
	
	// missing = plan - assigned 	= (plannedArticles) && !(assignedArticles)
	// surplus = assigned - planned = (assignedArticles) && !(plannedArticles)
	public static ChangeSet getChangeSet(Person person) {
		ChangeSet changeSet = new ChangeSet();
		String planned = getArticleQuery(person);
		String assigned = getAssignedRuleArticlesQuery(person);
		
		// missing
		StringBuilder sb = new StringBuilder();
		LdapHelper.addQuery(sb, assigned, LOGIC.AND);
		LdapHelper.negateQuery(sb);
		LdapHelper.addQuery(sb, planned, LOGIC.AND);
		List<CatalogArticle> missingArticles = person.getContext().loadObjects(CatalogArticle.class,
				Config.getString("nicki.catalogs.basedn"), sb.toString());
		if (missingArticles != null && missingArticles.size() > 0) {
			for (CatalogArticle article : missingArticles) {
				changeSet.addToMissing(person, article);
			}
		}
		
		// surplus
		sb.setLength(0);
		LdapHelper.addQuery(sb, planned, LOGIC.AND);
		LdapHelper.negateQuery(sb);
		LdapHelper.addQuery(sb, assigned, LOGIC.AND);
		List<CatalogArticle> surplusArticles = person.getContext().loadObjects(CatalogArticle.class,
				Config.getString("nicki.catalogs.basedn"), sb.toString());
		if (surplusArticles != null && surplusArticles.size() > 0) {
			for (CatalogArticle article : surplusArticles) {
				changeSet.addToSurplus(person, article);
			}
		}
		return changeSet;
	}
	
	// missing = plan - assigned 	= (plannedArticles) && !(assignedArticles)
	// surplus = assigned - planned = (assignedArticles) && !(plannedArticles)
	public static ChangeSet getChangeSet(CatalogArticle article) {
		ChangeSet changeSet = new ChangeSet();
		StringBuilder sb = new StringBuilder();
		// missing
		if (article.hasRules()) {
			LdapHelper.addQuery(sb, getAssignedRulePersonsQuery(article), LOGIC.AND);
			LdapHelper.negateQuery(sb);
			Collection<Person> missing = getUsers(article, sb.toString());
			for (Person person : missing) {
				changeSet.addToMissing(person, article);
			}
		}
		
		return changeSet;
	}
	
	

	public static RuleQuery getRuleQuery(CatalogArticle article) {
		RuleQuery ruleQuery = new RuleQuery();
		if (!article.hasRules()) {
			return ruleQuery;
		}
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		for (String entry : article.getRules()) {
			add(map,entry);
		}
		StringBuilder sb = new StringBuilder();
		for (String selectorName : map.keySet()) {
			StringBuilder sb2 = new StringBuilder();
			for (String value : map.get(selectorName)) {
				Selector selector = getSelector(article, selectorName);
				if (selector.hasValueProvider()) {
					ValueProvider valueProvider = selector.getValueProvider();
					String query = valueProvider.getPersonQuery(article, value);
					LdapHelper.addQuery(sb2, query, LOGIC.OR);
				} else {
					String query = getPersonQuery(article, selectorName, value);
					LdapHelper.addQuery(sb2, query, LOGIC.OR);
				}
			}
			if (sb2.length() > 0) {
				LdapHelper.addQuery(sb, sb2.toString(), LOGIC.AND);
			}
		}
		if (activeFilter != null) {
			LdapHelper.addQuery(sb, activeFilter, LOGIC.AND);
		}

		ruleQuery.setBaseDn(Config.getString("nicki.users.basedn"));
		ruleQuery.setQuery(sb.toString());
		return ruleQuery;
	}

	private static Selector getSelector(CatalogArticle article, String selectorName) {
		return article.getContext().loadObject(Selector.class, "cn=" + selectorName + "," + Config.getString("nicki.selectors.basedn"));
	}

	public static String getPersonQuery(CatalogArticle article, String selectorName, String value) {
		Selector selector = getSelector(article, selectorName);
		if (selector.hasValueProvider()) {
			return selector.getValueProvider().getPersonQuery(article, value);
		} else {
			return BasicValueProvider.getLdapName(article, selectorName) + "=" + value;
		}
	}

	private static void add(Map<String, List<String>> map, String entry) {
		String parts[] = StringUtils.split(entry, "=");
		if (parts.length == 2) {
			List<String> list = map.get(parts[0]);
			if (list == null) {
				list = new ArrayList<String>();
				map.put(parts[0], list);
			}
			list.add(parts[1]);
		}
	}
	
	public Cart saveChangeCart(Person person, Person user) throws InstantiateDynamicObjectException, DynamicObjectException {
		ChangeSet changeSet = getChangeSet(person);
		if (changeSet != null && changeSet.isNotEmpty()) {
			Cart cart = user.getContext().getObjectFactory().getDynamicObject(Cart.class);

			cart.getContext().getAdapter().initNew(cart, Config.getString("nicki.carts.basedn"),
					Long.toString(new Date().getTime()));
			cart.setInitiator(user);
			cart.setRecipient(person);
			cart.setRequestDate(new Date());
			cart.setCartStatus(Cart.CART_STATUS.NEW);
			cart.setSource(Inventory.SOURCE.RULE.getValue());
			cart.setCatalog(Catalog.getCatalog());
			for (ArticleChange article : changeSet.getChanges()) {
				CartEntry entry = new CartEntry(null, article.getArticle().getCatalogPath(), article.getCartEntryType());
				cart.addCartEntry(entry);
			}
			cart.create();
			return cart;
		}
		return null;
	}

	public void saveChangeCarts(CatalogArticle catalogArticle, Person user) throws InstantiateDynamicObjectException, DynamicObjectException {
		ChangeSet changeSet = getChangeSet(catalogArticle);
		if (changeSet != null && changeSet.isNotEmpty()) {
			for (ArticleChange article : changeSet.getChanges()) {
				Cart cart = user.getContext().getObjectFactory().getDynamicObject(Cart.class);
				cart.getContext().getAdapter().initNew(cart, Config.getString("nicki.carts.basedn"),
						Long.toString(new Date().getTime()));
				cart.setInitiator(user);
				cart.setRecipient(article.getPerson());
				cart.setRequestDate(new Date());
				cart.setCartStatus(Cart.CART_STATUS.NEW);
				cart.setSource(Inventory.SOURCE.RULE.getValue());
				cart.setCatalog(Catalog.getCatalog());
	
				CartEntry entry = new CartEntry(null, article.getArticle().getCatalogPath(), article.getCartEntryType());
				cart.addCartEntry(entry);

				cart.create();
			}
		}
	}


}

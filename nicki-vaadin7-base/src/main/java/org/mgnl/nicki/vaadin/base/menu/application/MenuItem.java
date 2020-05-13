package org.mgnl.nicki.vaadin.base.menu.application;

import java.util.List;

public interface MenuItem {
	List<String> getGroups();
	List<String> getRoles();
	String getRule();

}

package org.mgnl.nicki.vaadin.base.components;

import com.vaadin.ui.TreeGrid;

@SuppressWarnings("serial")
public class NoHeaderTreeGrid<T> extends TreeGrid<T> {
	public NoHeaderTreeGrid () {
		getHeader().setVisible(false);
	}

}

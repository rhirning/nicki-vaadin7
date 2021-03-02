package org.mgnl.nicki.vaadin.base.components;

import com.vaadin.ui.Grid;

@SuppressWarnings("serial")
public class NoHeaderGrid<T> extends Grid<T> {
	public NoHeaderGrid () {
		getHeader().setVisible(false);
	}

}

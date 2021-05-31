package org.mgnl.nicki.vaadin.base.notification;

/*-
 * #%L
 * nicki-vaadin7-base
 * %%
 * Copyright (C) 2020 - 2021 Ralf Hirning
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


import org.apache.commons.lang.StringUtils;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification.Position;

public class Notification {
	public enum Type {
		ERROR_MESSAGE, HUMANIZED_MESSAGE, TRAY_NOTIFICATION, WARNING_MESSAGE

	}

	public static void show(String header, String message, Type type) {
		int duration = 3000;
		Position position = Position.MIDDLE;
		StringBuilder text = new StringBuilder();
		if (StringUtils.isNotBlank(header)) {
			text.append(header).append("\n\n");
		}
		if (StringUtils.isNotBlank(message)) {
			text.append(message);
		}
		
		if (type == Type.ERROR_MESSAGE) {

		} else if (type == Type.HUMANIZED_MESSAGE) {
			duration = 1000;
		} else if (type == Type.WARNING_MESSAGE) {
			duration = 1000;
		} else if (type == Type.TRAY_NOTIFICATION) {
			position = Position.BOTTOM_CENTER;
		}
		showNotification(duration, position, new Span(text.toString()));
	}

	public static void show(String message, Type type) {
		show(null, message, type);
	}

	public static void show(String message) {
		show(message, Type.HUMANIZED_MESSAGE);
	}
	
	
	private static void showNotification(int duration, Position position, Component ...components) {
		com.vaadin.flow.component.notification.Notification notification = new com.vaadin.flow.component.notification.Notification(components);
		notification.setDuration(duration);
		notification.setPosition(Position.MIDDLE);
		notification.open();
	}

}

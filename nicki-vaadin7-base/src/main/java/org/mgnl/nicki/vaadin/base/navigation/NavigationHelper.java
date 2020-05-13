
package org.mgnl.nicki.vaadin.base.navigation;

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


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.mgnl.nicki.core.helper.DataHelper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NavigationHelper {
	private static String COMMAND_SEPARATOR = "|";

	public static void executeCommands(Object object, NavigationCommand command) {
		List<NavigationCommandEntry> toBeRemoved = new ArrayList<NavigationCommandEntry>();
		for (NavigationCommandEntry commandEntry : command.getEntries()) {
			if (canExecute(object, commandEntry)) {
				try {
					execute(object, commandEntry);
					toBeRemoved.add(commandEntry);
				} catch (Exception e) {
					log.error("Error", e);
				}
				continue;
			}
		}
		
		for (NavigationCommandEntry navigationCommandEntry : toBeRemoved) {
			command.remove(navigationCommandEntry);
		}
	}

	private static boolean canExecute(Object object,
			NavigationCommandEntry commandEntry) {
		for (Method method : object.getClass().getMethods()) {
			Command command = method.getAnnotation(Command.class);
			if (command != null && StringUtils.equals(command.name(), commandEntry.getCommandName())) {
				return true;
			}
		}
		return false;
	}

	private static void execute(Object object,
			NavigationCommandEntry commandEntry) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		for (Method method : object.getClass().getMethods()) {
			Command command = method.getAnnotation(Command.class);
			if (command != null && StringUtils.equals(command.name(), commandEntry.getCommandName())) {
				method.invoke(object, commandEntry.getData());
			}
		}
		
	}
	
	public static String encode(NavigationCommand command) {
		return Base64.encodeBase64String(toString(command).getBytes());
	}
	
	private static String toString(NavigationCommand command) {
		StringBuilder sb = new StringBuilder();
		for (NavigationCommandEntry commandEntry : command.getEntries()) {
			if (sb.length() > 0) {
				sb.append(COMMAND_SEPARATOR );
			}
			sb.append(toString(commandEntry));
		}
		return sb.toString();
	}

	private static StringBuilder toString(NavigationCommandEntry commandEntry) {
		StringBuilder sb = new StringBuilder();
		sb.append(commandEntry.getCommandName());
		if (commandEntry.getData() != null && commandEntry.getData().length > 0) {
			sb.append("(");
			boolean first = true;
			for (Object data : commandEntry.getData()) {
				if (!first) {
					sb.append(",");
				}
				sb.append(data.toString());
				first = false;
			}
			sb.append(")");
		}
		return sb;
	}
	
	public static NavigationCommand decode(String string) {
		return commandFromString(new String(Base64.decodeBase64(string)));
	}

	private static NavigationCommand commandFromString(String string) {
		NavigationCommand command = new NavigationCommand();
		for (String entry : DataHelper.getList(string, COMMAND_SEPARATOR)) {
			NavigationCommandEntry commandEntry = commandEntryFromString(entry);
			if (commandEntry != null) {
				command.add(commandEntry);
			}
		}
		return command;
	}

	private static NavigationCommandEntry commandEntryFromString(String entry) {
		String commandName = StringUtils.substringBefore(entry, "(");
		String rest = StringUtils.substringAfter(entry, "(");
		String params = StringUtils.substringBeforeLast(rest, ")");
		NavigationCommandEntry commandEntry = new NavigationCommandEntry(commandName, DataHelper.toStringArray(params, ","));
		return commandEntry;
	}


}

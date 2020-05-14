
package org.mgnl.nicki.editor.jcr;

/*-
 * #%L
 * nicki-editor-jcr
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


import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PropertyWrapper {

	private Property property;

	public PropertyWrapper(Property property) {
		super();
		this.property = property;
	}

	public String getName() {
		try {
			return property.getName();
		} catch (RepositoryException e) {
			log.error("Error", e);
		}
		return null;
	}

	public PROPERTY_TYPE getType() {
		try {
			return PROPERTY_TYPE.of(property.getType());
		} catch (RepositoryException e) {
			log.error("Error", e);
		}
		return null;
	}

	public String getValue() {
		try {
			return property.getValue().getString();
		} catch (ValueFormatException e) {
			log.error("Error", e);
		} catch (IllegalStateException e) {
			log.error("Error", e);
		} catch (RepositoryException e) {
			log.error("Error", e);
		}
		return null;
	}

	public enum PROPERTY_TYPE {
		STRING(PropertyType.STRING) {
			@Override
			void setProperty(Node node, String name, String value)
					throws ValueFormatException, VersionException,
					LockException, ConstraintViolationException,
					RepositoryException {
				node.setProperty(name, value);
			}
		},
		BINARY(PropertyType.BINARY) {
			@Override
			void setProperty(Node node, String name, String value)
					throws ValueFormatException, VersionException,
					LockException, ConstraintViolationException,
					RepositoryException {
				node.setProperty(name, value);
			}
		},
		DATE(PropertyType.DATE) {
			@Override
			void setProperty(Node node, String name, String value)
					throws ValueFormatException, VersionException,
					LockException, ConstraintViolationException,
					RepositoryException {
				node.setProperty(name, value);
			}
		},
		DOUBLE(PropertyType.DOUBLE) {
			@Override
			void setProperty(Node node, String name, String value)
					throws ValueFormatException, VersionException,
					LockException, ConstraintViolationException,
					RepositoryException {
				node.setProperty(name, value);
			}
		},
		LONG(PropertyType.LONG) {
			@Override
			void setProperty(Node node, String name, String value)
					throws ValueFormatException, VersionException,
					LockException, ConstraintViolationException,
					RepositoryException {
				node.setProperty(name, value);
			}
		},
		BOOLEAN(PropertyType.BOOLEAN) {
			@Override
			void setProperty(Node node, String name, String value)
					throws ValueFormatException, VersionException,
					LockException, ConstraintViolationException,
					RepositoryException {
				node.setProperty(name, value);
			}
		},
		NAME(PropertyType.NAME) {
			@Override
			void setProperty(Node node, String name, String value)
					throws ValueFormatException, VersionException,
					LockException, ConstraintViolationException,
					RepositoryException {
				node.setProperty(name, value);
			}
		},
		PATH(PropertyType.PATH) {
			@Override
			void setProperty(Node node, String name, String value)
					throws ValueFormatException, VersionException,
					LockException, ConstraintViolationException,
					RepositoryException {
				node.setProperty(name, value);
			}
		},
		REFERENCE(PropertyType.REFERENCE) {
			@Override
			void setProperty(Node node, String name, String value)
					throws ValueFormatException, VersionException,
					LockException, ConstraintViolationException,
					RepositoryException {
				node.setProperty(name, value);
			}
		},
		WEAKREFERENCE(PropertyType.WEAKREFERENCE) {
			@Override
			void setProperty(Node node, String name, String value)
					throws ValueFormatException, VersionException,
					LockException, ConstraintViolationException,
					RepositoryException {
				node.setProperty(name, value);
			}
		},
		URI(PropertyType.URI) {
			@Override
			void setProperty(Node node, String name, String value)
					throws ValueFormatException, VersionException,
					LockException, ConstraintViolationException,
					RepositoryException {
				node.setProperty(name, value);
			}
		};

		int value;

		public int getValue() {
			return value;
		}

		public static PROPERTY_TYPE of(int value) {
			for (PROPERTY_TYPE p : PROPERTY_TYPE.values()) {
				if (p.value == value) {
					return p;
				}
			}
			return null;
		}

		PROPERTY_TYPE(int value) {
			this.value = value;
		}

		public static String toString(int value) {
			return of(value).toString();
		}

		abstract void setProperty(Node node, String name, String value)
				throws ValueFormatException, VersionException, LockException,
				ConstraintViolationException, RepositoryException;
	}

}

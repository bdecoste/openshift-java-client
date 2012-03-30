/******************************************************************************* 
 * Copyright (c) 2012 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package com.openshift.express.internal.client.response.unmarshalling.dto;

import java.util.List;


/**
 * @author Andre Dietisheim
 */
public class LinkParameter {

	protected final String name;
	protected final String type;
	protected final String description;
	protected final String defaultValue;
	protected final List<String> validOptions;

	public LinkParameter(final String name, final String type, final String defaultValue, final String description,
			final List<String> validOptions) {
		this.name = name;
		this.type = type;
		this.description = description;
		this.defaultValue = defaultValue;
		this.validOptions = validOptions;
	}

	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * @return the type
	 */
	public final String getType() {
		return type;
	}

	/**
	 * @return the description
	 */
	public final String getDescription() {
		return description;
	}

	/**
	 * @return the defaultValue, or null. Only applicable to optional parameters.
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	public List<String> getValidOptions() {
		return validOptions;
	}
}
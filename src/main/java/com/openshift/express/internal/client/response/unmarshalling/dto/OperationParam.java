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
 * @author Xavier Coulon
 */
public class OperationParam {

	private final String name;
	private final String type;
	private final String description;
	private final List<String> validOptions;

	public OperationParam(final String name, final String type, final String description,
			final List<String> validOptions) {
		this.name = name;
		this.type = type;
		this.description = description;
		this.validOptions = validOptions;
	}

	/**
	 * @return the name
	 */
	protected final String getName() {
		return name;
	}

	/**
	 * @return the type
	 */
	protected final String getType() {
		return type;
	}

	/**
	 * @return the description
	 */
	protected final String getDescription() {
		return description;
	}

	/**
	 * @return the validOptions
	 */
	protected final List<String> getValidOptions() {
		return validOptions;
	}
}

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
 * @author Andre Dietisheim
 */
public class OptionalLinkParameter extends RequiredLinkParameter {

	private final String defaultValue;
	protected final List<String> validOptions;

	public OptionalLinkParameter(final String name, final String type, final String defaultValue, final String description,
			final List<String> validOptions) {
		super(name, type, description);
		this.defaultValue = defaultValue;
		this.validOptions = validOptions;
	}

	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	public List<String> getValidOptions() {
		return validOptions;
	}

}

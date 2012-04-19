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
package com.openshift.internal.client.response;

import java.util.Map;

/**
 * @author Xavier Coulon
 *
 */
public class CartridgeResourceDTO extends BaseResourceDTO {

	private final String name;
	
	private final String type;
	
	/**
	 * @param links
	 */
	public CartridgeResourceDTO(final String name, final String type, final Map<String, Link> links) {
		super(links);
		this.name = name;
		this.type = type;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CartridgeResourceDTO [name=" + name + ", type=" + type + "]";
	}

}

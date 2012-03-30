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

import java.util.Map;

public class UserResourceDTO extends BaseResourceDTO {

	/** the user's login on rhcloud. */
	private final String rhLogin;
	
	public UserResourceDTO(final String rhLogin, final Map<String, Link> links) {
		super(links);
		this.rhLogin = rhLogin;
	}

	/**
	 * @return the rhLogin
	 */
	public String getRhLogin() {
		return rhLogin;
	}
	
	

}

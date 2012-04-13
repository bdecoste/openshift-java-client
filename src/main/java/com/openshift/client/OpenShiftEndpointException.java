/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package com.openshift.client;

import com.openshift.internal.client.response.unmarshalling.dto.ResourceDTOFactory;
import com.openshift.internal.client.response.unmarshalling.dto.RestResponse;

/**
 * @author André Dietisheim
 */
public class OpenShiftEndpointException extends OpenShiftException {

	private static final long serialVersionUID = 1L;

	private final String url;
	private final String response;
	
	public OpenShiftEndpointException(final String url, final Throwable cause, final String response, final String message, final Object... arguments) {
		super(cause, message, arguments);
		this.response = response;
		this.url = url;
	}
		
	public RestResponse getRestResponse() throws OpenShiftException {
		if (response == null) {
			return null;
		}
		return ResourceDTOFactory.get(response);
	}

	protected String getUrl() {
		return url;
	}
}

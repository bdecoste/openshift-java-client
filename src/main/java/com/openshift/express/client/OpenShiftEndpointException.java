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
package com.openshift.express.client;

import com.openshift.express.internal.client.response.unmarshalling.dto.ResourceDTOFactory;
import com.openshift.express.internal.client.response.unmarshalling.dto.RestResponse;

/**
 * @author André Dietisheim
 */
public class OpenShiftEndpointException extends OpenShiftException {

	private static final long serialVersionUID = 1L;

	private String url;
	private RestResponse restResponse;
	
	public OpenShiftEndpointException(String url, Throwable cause, String message, Object... arguments) throws OpenShiftException {
		this(url, cause, ResourceDTOFactory.get(cause.getMessage()), message, arguments);
	}

	public OpenShiftEndpointException(String url, Throwable cause, RestResponse restResponse,
			String message, Object... arguments) {
		super(cause, message, arguments);
		this.restResponse = restResponse;
		this.url = url;
	}
		
	public RestResponse getRestResponse() {
		return restResponse;
	}

	protected String getUrl() {
		return url;
	}
}

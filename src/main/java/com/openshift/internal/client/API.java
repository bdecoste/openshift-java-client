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
package com.openshift.internal.client;

import java.net.SocketTimeoutException;
import java.util.Map;

import com.openshift.client.HttpMethod;
import com.openshift.client.OpenShiftException;
import com.openshift.internal.client.response.unmarshalling.dto.Link;

/**
 * @author Andre Dietisheim
 */
public class API extends AbstractOpenShiftResource {

	public API(IRestService service) {
		super(service);
	}

	@Override
	protected Map<String, Link> getLinks() throws SocketTimeoutException, OpenShiftException {
		if (!areLinksLoaded()) {
			Map<String, Link> links = new GetAPIRequest().execute();
			setLinks(links);
		}
		return super.getLinks();
	}


	private class GetAPIRequest extends ServiceRequest {

		public GetAPIRequest() {
			super(new Link("Get API", "/api", HttpMethod.GET), API.this);
		}

		public Map<String, Link> execute() throws SocketTimeoutException, OpenShiftException {
			return super.execute();
		}
	}
}

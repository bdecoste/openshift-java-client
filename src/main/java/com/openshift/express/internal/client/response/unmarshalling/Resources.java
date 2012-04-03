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
package com.openshift.express.internal.client.response.unmarshalling;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Map;

import com.openshift.express.client.HttpMethod;
import com.openshift.express.client.OpenShiftException;
import com.openshift.express.internal.client.IRestService;
import com.openshift.express.internal.client.response.unmarshalling.dto.Link;

/**
 * @author Andre Dietisheim
 */
public class Resources {

	private Map<String, Link> links;
	private IRestService service;
	private Link resourcesLink;

	public Resources(IRestService service) {
		this.service = service;
		this.resourcesLink = new Link("Get API", "/api", HttpMethod.GET, null, null);
	}

	public Map<String, Link> getLinks() throws MalformedURLException, UnsupportedEncodingException, OpenShiftException {
		if (links == null) {
			service.execute(resourcesLink);
		}
		return links;
	}

	public Link getLinkByName(String name) {
		return links.get(name);
	}
}

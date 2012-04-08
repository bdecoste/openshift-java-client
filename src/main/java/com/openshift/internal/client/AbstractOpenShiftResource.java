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

import com.openshift.client.OpenShiftException;
import com.openshift.internal.client.response.unmarshalling.dto.Link;

/**
 * @author Xavier Coulon
 * @author Andre Dietisheim
 * 
 */
public class AbstractOpenShiftResource {

	private Map<String, Link> links;
	final private IRestService service;

	public AbstractOpenShiftResource(final IRestService service) {
		this(service, null);
	}

	public AbstractOpenShiftResource(final IRestService service, final Map<String, Link> links) {
		this.service = service;
		this.links = links;
	}

	protected Map<String, Link> getLinks() throws SocketTimeoutException, OpenShiftException {
		return links;
	}
	
	protected Link getLink(String linkName) throws SocketTimeoutException, OpenShiftException {
		if (links == null) {
			return null;
		}
		return getLinks().get(linkName);
	}
	
	protected void setLinks(Map<String, Link> links) {
		this.links = links;
	}
	
	protected IRestService getService() {
		return service;
	}
}

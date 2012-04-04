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
package com.openshift.express.internal.client;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import com.openshift.express.client.OpenShiftException;
import com.openshift.express.internal.client.response.unmarshalling.dto.Link;
import com.openshift.express.internal.client.response.unmarshalling.dto.RestResponse;

/**
 * @author Xavier Coulon
 * 
 */
public class AbstractOpenShiftResource {

	final Map<String, Link> links = new HashMap<String, Link>();

	final IRestService service;

	public AbstractOpenShiftResource(final IRestService service) {
		this(service, null);
	}

	public AbstractOpenShiftResource(final IRestService service, final Map<String, Link> links) {
		this.service = service;
		if (links != null) {
			this.links.putAll(links);
		}
	}

	// made protectedfor testing purpose, but not part of the public interface, though
	protected Link getLink(String linkName) throws OpenShiftException {
		return links.get(linkName);
	}

	<T> T execute(Link link, ServiceParameter... parameters) throws OpenShiftException {
		assert link != null;
		// avoid concurrency issues, to prevent reading the links map while it is still being retrieved
		try {
			Map<String, Object> params = null;
			if (parameters != null) {
				params = new HashMap<String, Object>();
				for (ServiceParameter parameter : parameters) {
					params.put(parameter.getKey(), parameter.getValue());
				}
			}
			RestResponse response = service.execute(link, params);
			return response.getData();
		} catch (MalformedURLException e) {
			throw new OpenShiftException(e, "Failed to execute {0} {1}", link.getHttpMethod().name(), link.getHref());
		} catch (UnsupportedEncodingException e) {
			throw new OpenShiftException(e, "Failed to execute {0} {1}", link.getHttpMethod().name(), link.getHref());
		}

	}

}

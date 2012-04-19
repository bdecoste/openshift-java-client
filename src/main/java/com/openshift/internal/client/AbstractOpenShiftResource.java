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
import com.openshift.internal.client.response.Link;
import com.openshift.internal.client.response.RestResponse;

/**
 * The Class AbstractOpenShiftResource.
 * 
 * @author Xavier Coulon
 * @author Andre Dietisheim
 */
public abstract class AbstractOpenShiftResource {

	/** The links. Null means collection is not loaded yet. */
	private Map<String, Link> links;

	/** The service. */
	private final IRestService service;

	/**
	 * Instantiates a new abstract open shift resource.
	 * 
	 * @param service
	 *            the service
	 */
	public AbstractOpenShiftResource(final IRestService service) {
		this(service, null);
	}

	/**
	 * Instantiates a new abstract open shift resource.
	 * 
	 * @param service
	 *            the service
	 * @param links
	 *            the links
	 */
	public AbstractOpenShiftResource(final IRestService service, final Map<String, Link> links) {
		this.service = service;
		this.links = links;
	}

	/**
	 * Gets the links.
	 * 
	 * @return the links
	 * @throws OpenShiftException
	 * @throws SocketTimeoutException
	 */
	Map<String, Link> getLinks() throws SocketTimeoutException, OpenShiftException {
		return links;
	}

	void setLinks(final Map<String, Link> links) {
		this.links = links;
	}

	/**
	 * Gets the service.
	 * 
	 * @return the service
	 */
	protected final IRestService getService() {
		return service;
	}

	// made protected for testing purpose, but not part of the public interface, though
	/**
	 * Gets the link.
	 * 
	 * @param linkName
	 *            the link name
	 * @return the link
	 * @throws OpenShiftException
	 *             the open shift exception
	 * @throws SocketTimeoutException
	 *             the socket timeout exception
	 */
	protected Link getLink(String linkName) throws SocketTimeoutException, OpenShiftException {
		if (getLinks() == null) {
			return null;
		}
		return getLinks().get(linkName);
	}

	/**
	 * Execute.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param link
	 *            the link
	 * @param parameters
	 *            the parameters
	 * @return the t
	 * @throws OpenShiftException
	 *             the open shift exception
	 * @throws SocketTimeoutException
	 *             the socket timeout exception <T> T execute(Link link, ServiceParameter... parameters) throws
	 *             OpenShiftException, SocketTimeoutException { assert link != null; // avoid concurrency issues, to
	 *             prevent reading the links map while it is still being retrieved try { RestResponse response =
	 *             service.execute(link, parameters); return response.getData(); } catch (MalformedURLException e) {
	 *             throw new OpenShiftException(e, "Failed to execute {0} {1}", link.getHttpMethod().name(),
	 *             link.getHref()); } catch (UnsupportedEncodingException e) { throw new OpenShiftException(e,
	 *             "Failed to execute {0} {1}", link.getHttpMethod().name(), link.getHref()); } }
	 */

	protected boolean areLinksLoaded() {
		return links != null;
	}

	protected class ServiceRequest {

		private String linkName;

		protected ServiceRequest(String linkName) {
			this.linkName = linkName;
		}

		protected <DTO> DTO execute(ServiceParameter... parameters) throws OpenShiftException, SocketTimeoutException {
			Link link = getLink(linkName);
			RestResponse response = getService().execute(link, parameters);
			// in some cases, there is not response body, just a return code to indicate that the operation was successful (e.g.: delete domain)
			if (response == null) {
				return null;
			}
			return response.getData();
		}

	}

}

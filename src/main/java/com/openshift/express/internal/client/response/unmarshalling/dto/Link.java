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
 */
public class Link {

	private final String rel;
	private final String href;
	private final String httpMethod;
	private final List<LinkParam> requiredParams;
	private final List<LinkParam> optionalParams;
	
	public Link(final String rel, final String href, final String httpMethod,
			final List<LinkParam> requiredParams,
			final List<LinkParam> optionalParams) {
		this.rel = rel;
		this.href = href;
		this.httpMethod = httpMethod;
		this.requiredParams = requiredParams;
		this.optionalParams = optionalParams;
	}

	/**
	 * @return the rel
	 */
	public final String getRel() {
		return rel;
	}

	/**
	 * @return the href
	 */
	public final String getHref() {
		return href;
	}

	/**
	 * @return the httpMethod
	 */
	public final String getHttpMethod() {
		return httpMethod;
	}

	/**
	 * @return the requiredParams
	 */
	public final List<LinkParam> getRequiredParams() {
		return requiredParams;
	}

	/**
	 * @return the optionalParams
	 */
	public final List<LinkParam> getOptionalParams() {
		return optionalParams;
	}

}

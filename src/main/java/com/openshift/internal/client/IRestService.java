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

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.Map;

import com.openshift.client.OpenShiftException;
import com.openshift.internal.client.response.unmarshalling.dto.Link;
import com.openshift.internal.client.response.unmarshalling.dto.RestResponse;

/**
 * @author Andre Dietisheim
 */
public interface IRestService {

	public abstract RestResponse execute(Link link)
			throws OpenShiftException, MalformedURLException, UnsupportedEncodingException, SocketTimeoutException;

	public RestResponse execute(Link link, ServiceParameter... serviceParameters)
			throws SocketTimeoutException, MalformedURLException, UnsupportedEncodingException, OpenShiftException;

	public abstract RestResponse execute(Link link, Map<String, Object> parameters)
			throws OpenShiftException, MalformedURLException, UnsupportedEncodingException, SocketTimeoutException;

	public abstract void setProxySet(boolean proxySet);

	public abstract void setProxyHost(String proxyHost);

	public abstract void setProxyPort(String proxyPort);

	public abstract String getServiceUrl();

	public abstract String getPlatformUrl();

}
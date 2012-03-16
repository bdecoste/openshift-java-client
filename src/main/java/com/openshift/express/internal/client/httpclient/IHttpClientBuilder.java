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
package com.openshift.express.internal.client.httpclient;

import java.net.MalformedURLException;
import java.net.URL;

import com.openshift.express.client.IHttpClient;

/**
 * @author Andre Dietisheim
 */
public interface IHttpClientBuilder {

	public IHttpClientBuilder setUserAgent(String userAgent);

	public IHttpClientBuilder setSSLChecks(boolean check);

	public IHttpClientBuilder setCredentials(String username, String password);

	public IHttpClient setUrl(URL url);

	public IHttpClient setUrl(String url) throws MalformedURLException;
}
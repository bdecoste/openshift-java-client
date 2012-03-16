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
package com.openshift.express.internal.client.test.fakes;

import java.net.SocketTimeoutException;
import java.net.URL;

import com.openshift.express.client.IHttpClient;
import com.openshift.express.internal.client.httpclient.HttpClientException;

/**
 * @author Andre Dietisheim
 */
public class NoopHttpClientFake implements IHttpClient {
	public String post(String data, URL url) throws HttpClientException {
		throw new UnsupportedOperationException();
	}

	public String get(URL url) throws HttpClientException {
		throw new UnsupportedOperationException();
	}

	public String put(String data, URL url) throws HttpClientException, SocketTimeoutException {
		throw new UnsupportedOperationException();
	}

	public String delete(URL url) throws HttpClientException, SocketTimeoutException {
		throw new UnsupportedOperationException();
	}

}

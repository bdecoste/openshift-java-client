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
package com.openshift.express.client;

import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import com.openshift.express.internal.client.httpclient.HttpClientException;

/**
 * @author Andre Dietisheim
 */
public interface IHttpClientRequest {

	public static class GetRequest extends HttpClientRequest {

		public GetRequest(URL url) {
			super(null, url);
		}

		@Override
		public String execute(IHttpClient client) throws SocketTimeoutException, HttpClientException, MalformedURLException {
			return client.get(url);
		}
	}

	public static class PostRequest extends HttpClientRequest {

		public PostRequest(String data, URL url) {
			super(data, url);
		}

		@Override
		public String execute(IHttpClient client) throws SocketTimeoutException, HttpClientException, MalformedURLException {
			return client.post(data, url);
		}
	}

	public static class PutRequest extends HttpClientRequest {

		public PutRequest(String data, URL url) {
			super(data, url);
		}

		@Override
		public String execute(IHttpClient client) throws SocketTimeoutException, HttpClientException, MalformedURLException {
			return client.put(data, url);
		}
	}

	public static class DeleteRequest extends HttpClientRequest {

		public DeleteRequest(URL url) {
			super(null, url);
		}

		@Override
		public String execute(IHttpClient client) throws SocketTimeoutException, HttpClientException, MalformedURLException {
			return client.post(data, url);
		}
	}

	static abstract class HttpClientRequest implements IHttpClientRequest {
		protected String data;
		protected URL url;

		public HttpClientRequest(String data, URL url) {
			this.data = data;
			this.url = url;
		}

		public abstract String execute(IHttpClient client) throws SocketTimeoutException, HttpClientException, MalformedURLException;
		
		public URL getUrl() {
			return url;
		}
	}

	public String execute(IHttpClient client) throws SocketTimeoutException, HttpClientException, MalformedURLException;

	public URL getUrl();
}

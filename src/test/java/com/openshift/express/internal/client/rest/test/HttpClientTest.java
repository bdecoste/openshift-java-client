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
package com.openshift.express.internal.client.rest.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.openshift.express.client.IHttpClient;
import com.openshift.express.internal.client.httpclient.HttpClientException;
import com.openshift.express.internal.client.httpclient.UrlConnectionHttpClient;
import com.openshift.express.internal.client.test.fakes.ServerFake;

public class HttpClientTest {

	private ServerFake serverFake;
	private IHttpClient httpClient;

	@Before
	public void setUp() throws MalformedURLException {
		this.serverFake = new ServerFake();
		serverFake.start();
		this.httpClient = new UrlConnectionHttpClient("com.openshift.express.client.test", new URL(serverFake.getUrl()));
	}

	@After
	public void tearDown() {
		serverFake.stop();
	}

	@Test
	public void canGet() throws MalformedURLException, SocketTimeoutException, HttpClientException {
		String response = httpClient.get();
		assertNotNull(response);
		assertTrue(response.startsWith("GET"));
	}

	@Test
	public void canPost() throws MalformedURLException, SocketTimeoutException, HttpClientException {
		String response = httpClient.post("dummy");
		assertNotNull(response);
		assertTrue(response.startsWith("POST"));
	}

	@Test
	public void canPut() throws MalformedURLException, SocketTimeoutException, HttpClientException {
		String response = httpClient.put("dummy");
		assertNotNull(response);
		assertTrue(response.startsWith("PUT"));
	}

	@Test
	public void canDelete() throws MalformedURLException, SocketTimeoutException, HttpClientException {
		String response = httpClient.delete();
		assertNotNull(response);
		assertTrue(response.startsWith("DELETE"));
	}
}

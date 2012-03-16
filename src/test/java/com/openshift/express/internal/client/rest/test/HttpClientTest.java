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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.openshift.express.client.IHttpClient;
import com.openshift.express.client.utils.Base64Coder;
import com.openshift.express.internal.client.httpclient.HttpClientException;
import com.openshift.express.internal.client.httpclient.UrlConnectionHttpClientBuilder;
import com.openshift.express.internal.client.test.fakes.ServerFake;

public class HttpClientTest {

	private static final String ACCEPT_APPLICATION_JSON = "Accept: application/json";

	private static final Pattern AUTHORIZATION_PATTERN = Pattern.compile("Authorization: Basic ([^\n]*)");

	private ServerFake serverFake;
	private IHttpClient httpClient;

	@Before
	public void setUp() throws MalformedURLException {
		this.serverFake = new ServerFake();
		serverFake.start();
		this.httpClient = new UrlConnectionHttpClientBuilder()
				.setUserAgent("com.openshift.express.client.test")
				.setUrl(new URL(serverFake.getUrl()));
	}

	@After
	public void tearDown() {
		serverFake.stop();
	}

	@Test
	public void canGet() throws SocketTimeoutException, HttpClientException {
		String response = httpClient.get();
		assertNotNull(response);
		assertTrue(response.startsWith("GET"));
	}

	@Test
	public void canPost() throws SocketTimeoutException, HttpClientException {
		String response = httpClient.post("dummy");
		assertNotNull(response);
		assertTrue(response.startsWith("POST"));
	}

	@Test
	public void canPut() throws SocketTimeoutException, HttpClientException {
		String response = httpClient.put("dummy");
		assertNotNull(response);
		assertTrue(response.startsWith("PUT"));
	}

	@Test
	public void canDelete() throws SocketTimeoutException, HttpClientException {
		String response = httpClient.delete();
		assertNotNull(response);
		assertTrue(response.startsWith("DELETE"));
	}

	@Test
	public void canAddAuthorization() throws SocketTimeoutException, HttpClientException, MalformedURLException {
		String username = "andre.dietisheim@redhat.com";
		String password = "dummyPassword";
		IHttpClient httpClient = new UrlConnectionHttpClientBuilder()
				.setUserAgent("com.openshift.express.client.test")
				.setCredentials(username, password)
				.setUrl(new URL(serverFake.getUrl()));

		String response = httpClient.get();
		assertNotNull(response);
		Matcher matcher = AUTHORIZATION_PATTERN.matcher(response);
		assertTrue(matcher.find());
		assertEquals(1, matcher.groupCount());
		String credentials = matcher.group(1);
		String cleartextCredentials = new String(Base64Coder.decode(credentials));
		assertEquals(username + ":" + password, cleartextCredentials);
	}

	@Test
	public void canAcceptJson() throws SocketTimeoutException, HttpClientException, MalformedURLException {
		IHttpClient httpClient = new UrlConnectionHttpClientBuilder()
				.setUserAgent("com.openshift.express.client.test")
				.setUrl(new URL(serverFake.getUrl()));

		String response = httpClient.get();
		assertNotNull(response);
		assertTrue(response.indexOf(ACCEPT_APPLICATION_JSON) > 0);
	}

}

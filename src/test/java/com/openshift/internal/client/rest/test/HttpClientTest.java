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
package com.openshift.internal.client.rest.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.openshift.client.IHttpClient;
import com.openshift.client.utils.Base64Coder;
import com.openshift.internal.client.httpclient.HttpClientException;
import com.openshift.internal.client.httpclient.UrlConnectionHttpClientBuilder;
import com.openshift.internal.client.test.fakes.ServerFake;

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
				.setUserAgent("com.openshift.client.test")
				.client();
	}

	@After
	public void tearDown() {
		serverFake.stop();
	}

	@Test
	public void canGet() throws SocketTimeoutException, HttpClientException, MalformedURLException {
		String response = httpClient.get(new URL(serverFake.getUrl()));
		assertNotNull(response);
		assertTrue(response.startsWith("GET"));
	}

	@Test
	public void canPost() throws SocketTimeoutException, HttpClientException, MalformedURLException, UnsupportedEncodingException {
		String response = httpClient.post(new HashMap<String, Object>(), new URL(serverFake.getUrl()));
		assertNotNull(response);
		assertTrue(response.startsWith("POST"));
	}

	@Test
	public void canPut() throws SocketTimeoutException, HttpClientException, MalformedURLException, UnsupportedEncodingException {
		String response = httpClient.put(new HashMap<String, Object>(), new URL(serverFake.getUrl()));
		assertNotNull(response);
		assertTrue(response.startsWith("PUT"));
	}

	@Test
	public void canDelete() throws SocketTimeoutException, HttpClientException, MalformedURLException {
		String response = httpClient.delete(new URL(serverFake.getUrl()));
		assertNotNull(response);
		assertTrue(response.startsWith("DELETE"));
	}

	@Test
	public void canAddAuthorization() throws SocketTimeoutException, HttpClientException, MalformedURLException {
		String username = "andre.dietisheim@redhat.com";
		String password = "dummyPassword";
		IHttpClient httpClient = new UrlConnectionHttpClientBuilder()
				.setUserAgent("com.openshift.client.test")
				.setCredentials(username, password)
				.client();

		String response = httpClient.get(new URL(serverFake.getUrl()));
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
				.setUserAgent("com.openshift.client.test")
				.client();

		String response = httpClient.get(new URL(serverFake.getUrl()));
		assertNotNull(response);
		assertTrue(response.indexOf(ACCEPT_APPLICATION_JSON) > 0);
	}

}

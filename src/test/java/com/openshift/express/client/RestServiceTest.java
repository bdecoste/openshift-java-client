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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.openshift.express.internal.client.HttpParameters;
import com.openshift.express.internal.client.RestService;
import com.openshift.express.internal.client.httpclient.HttpClientException;
import com.openshift.express.internal.client.response.unmarshalling.dto.Link;
import com.openshift.express.internal.client.response.unmarshalling.dto.LinkParameter;
import com.openshift.express.internal.client.response.unmarshalling.dto.LinkParameterType;

/**
 * @author Andre Dietisheim
 */
public class RestServiceTest {

	private RestService service;
	private IHttpClient clientMock;

	@Before
	public void setUp() throws FileNotFoundException, IOException, OpenShiftException {
		this.clientMock = mock(IHttpClient.class);
		this.service = new RestService(clientMock);
	}
	
	@Test(expected=OpenShiftException.class)
	public void throwsIfRequiredParameterMissing() throws MalformedURLException, UnsupportedEncodingException, OpenShiftException {
		// operation
		LinkParameter parameter = new LinkParameter("required string parameter", LinkParameterType.STRING, null, null, null);
		Link link = new Link("1 required parameter", "/dummy", HttpMethod.GET, Arrays.asList(parameter), null);
		service.execute(link, new HttpParameters());
	}
	
	@Test
	public void doesNotThrowIfNoReqiredParameter() throws MalformedURLException, UnsupportedEncodingException, OpenShiftException {
		// operation
		Link link = new Link("0 require parameter", "/dummy", HttpMethod.GET, null, null);
		service.execute(link, new HttpParameters());
	}

	@Test
	public void doesGetIfGetHttpMethod() throws MalformedURLException, UnsupportedEncodingException, OpenShiftException, SocketTimeoutException, HttpClientException {
		// operation
		service.execute(new Link("0 require parameter", "http://www.redhat.com", HttpMethod.GET, null, null));
		// verifications
		verify(clientMock, times(1)).get(any(URL.class));
	}

	@Test
	public void doesPostIfPostHttpMethod() throws MalformedURLException, UnsupportedEncodingException, OpenShiftException, SocketTimeoutException, HttpClientException {
		// operation
		service.execute(new Link("0 require parameter", "http://www.redhat.com", HttpMethod.POST, null, null));
		// verifications
		verify(clientMock, times(1)).post(any(String.class), any(URL.class));
	}

	@Test
	public void doesPutIfPutHttpMethod() throws MalformedURLException, UnsupportedEncodingException, OpenShiftException, SocketTimeoutException, HttpClientException {
		// operation
		service.execute(new Link("0 require parameter", "http://www.redhat.com", HttpMethod.PUT, null, null));
		// verifications
		verify(clientMock, times(1)).put(any(String.class), any(URL.class));
	}

	@Test
	public void doesDeleteIfDeleteHttpMethod() throws MalformedURLException, UnsupportedEncodingException, OpenShiftException, SocketTimeoutException, HttpClientException {
		// operation
		service.execute(new Link("0 require parameter", "http://www.redhat.com", HttpMethod.DELETE, null, null));
		// verifications
		verify(clientMock, times(1)).delete(any(URL.class));
	}
}

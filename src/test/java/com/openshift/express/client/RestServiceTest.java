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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.openshift.express.internal.client.HttpParameters;
import com.openshift.express.internal.client.RestService;
import com.openshift.express.internal.client.response.unmarshalling.dto.Link;
import com.openshift.express.internal.client.response.unmarshalling.dto.LinkParameter;
import com.openshift.express.internal.client.response.unmarshalling.dto.LinkParameterType;

/**
 * @author Andre Dietisheim
 */
public class RestServiceTest {

	private RestService service;

	@Before
	public void setUp() throws FileNotFoundException, IOException, OpenShiftException {
		IHttpClient clientMock = mock(IHttpClient.class);
		this.service = new RestService(clientMock);
	}
	
	@Test(expected=OpenShiftException.class)
	public void throwsIfRequiredParameterMissing() throws MalformedURLException, UnsupportedEncodingException, OpenShiftException {
		LinkParameter parameter = new LinkParameter("required string parameter", LinkParameterType.STRING, null, null, null);
		Link link = new Link("1 require parameter", "/dummy", HttpMethod.GET, Arrays.asList(parameter), null);
		service.execute(link, new HttpParameters());
	}
	
	public void doesNotThrowIfNoReqiredParameter() throws MalformedURLException, UnsupportedEncodingException, OpenShiftException {
		Link link = new Link("1 require parameter", "/dummy", HttpMethod.GET, null, null);
		service.execute(link, new HttpParameters());
	}
}

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

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import com.openshift.express.internal.client.RestService;
import com.openshift.express.internal.client.httpclient.HttpClientException;

/**
 * @author Xavier Coulon
 * @author Andre Dietisheim
 */
public class UserTest {

	private static final String CLIENT_ID = "openshift-java-client-rest-test";

	private IUser user;
	private IHttpClient clientMock;

	@Before
	public void setup() throws FileNotFoundException, IOException, OpenShiftException {
		this.clientMock = mock(IHttpClient.class);
		this.user = new UserBuilder()
				.service(new RestService(clientMock))
				.clientId(CLIENT_ID)
				.build();
	}

	@Test
	public void shouldLoadSingleUserDomain() throws IOException, HttpClientException, OpenShiftException {
		// pre-conditions
		String response = getContentAsString("get-domains-1existing.json");
		when(clientMock.get(any(URL.class))).thenReturn(response);
		// operation
		final List<IDomain> domains = user.getDomains();
		// verifications
		assertThat(domains).hasSize(1);
		// verify(mockClient.get(any(URL.class))).times(1);

	}

	private String getContentAsString(String fileName) throws IOException {
		final InputStream contentStream = getClass()
				.getResourceAsStream("/samples/" + fileName);
		return IOUtils.toString(contentStream);
	}

}
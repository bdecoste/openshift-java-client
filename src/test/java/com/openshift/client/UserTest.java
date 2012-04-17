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
package com.openshift.client;

import static com.openshift.client.utils.CustomArgumentMatchers.urlEndsWith;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.openshift.client.utils.Samples;
import com.openshift.internal.client.RestService;

/**
 * @author Xavier Coulon
 * @author Andre Dietisheim
 */
public class UserTest {

	private IUser user;
	private IHttpClient mockClient;

	@Before
	public void setup() throws Throwable {
		mockClient = mock(IHttpClient.class);
		when(mockClient.get(urlEndsWith("/broker/rest/api")))
		.thenReturn(Samples.GET_REST_API_JSON.getContentAsString());
		when(mockClient.get(urlEndsWith("/user"))).thenReturn(
				Samples.GET_USER.getContentAsString());
		final IOpenShiftConnection connection = new OpenShiftConnectionManager().getConnection(new RestService("http://mock",
				"clientId", mockClient), "foo@redhat.com", "bar");
		this.user = connection.getUser();
	}

	@Test
	public void shouldLoadUser() throws Throwable {
		// verifications
		assertThat(user.getRhlogin()).isEqualTo("foo@redhat.com");
		assertThat(user.getPassword()).isEqualTo("bar");
	}

	
	
}

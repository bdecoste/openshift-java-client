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
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URL;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import com.openshift.express.client.utils.Samples;
import com.openshift.express.internal.client.RestService;

/**
 * @author Xavier Coulon
 * @author Andre Dietisheim
 */
public class UserTest {

	private IUser user;
	private IHttpClient mockClient;

	/**
	 * Custom Mockito Matcher that verifies that the given URL ends with a given suffix.
	 * 
	 * @author Xavier Coulon
	 * 
	 */
	class UrlEndsWithMatcher extends ArgumentMatcher<URL> {

		private final String urlSuffix;

		public UrlEndsWithMatcher(final String urlSuffix) {
			this.urlSuffix = urlSuffix;
		}

		@Override
		public boolean matches(Object argument) {
			if(argument == null) {
				return false;
			}
			URL url = (URL) argument;
			return url.toExternalForm().endsWith(urlSuffix);
		}
	}

	/** More friendly way to call the {@link UrlEndsWithMatcher}. */
	private URL urlEndsWith(String suffix) {
		return argThat(new UrlEndsWithMatcher(suffix));
	}

	@Before
	public void setup() throws Throwable {
		mockClient = mock(IHttpClient.class);
		when(mockClient.get(urlEndsWith("/broker/rest/api"))).thenReturn(Samples.GET_REST_API_JSON.getContentAsString());
		this.user = new UserBuilder().configure(new RestService(IRestServiceTestConstants.CLIENT_ID, mockClient)).build();
	}

	@Test
	public void shouldLoadEmptyListOfDomains() throws Throwable {
		// pre-conditions
		when(mockClient.get(urlEndsWith("/domains"))).thenReturn(
				Samples.GET_DOMAINS_NOEXISTING_JSON.getContentAsString());
		// operation
		final List<IDomain> domains = user.getDomains();
		// verifications
		assertThat(domains).hasSize(0);
		verify(mockClient, times(2)).get(any(URL.class));
	}

	@Test
	public void shouldLoadSingleUserDomain() throws Throwable {
		// pre-conditions
		when(mockClient.get(urlEndsWith("/domains"))).thenReturn(
				Samples.GET_DOMAINS_1EXISTING_JSON.getContentAsString());
		// operation
		final List<IDomain> domains = user.getDomains();
		// verifications
		assertThat(domains).hasSize(1);
		verify(mockClient, times(2)).get(any(URL.class));
	}

	@Test
	public void shouldLoadEmptyListOfApplications() throws Throwable {
		// pre-conditions
		when(mockClient.get(urlEndsWith("/domains"))).thenReturn(
				Samples.GET_DOMAINS_1EXISTING_JSON.getContentAsString());
		when(mockClient.get(urlEndsWith("/domains/foobar/applications"))).thenReturn(
				Samples.GET_APPLICATIONS_WITH2APPS_JSON.getContentAsString());
		// operation
		final List<IApplication> applications = user.getDomains().get(0).getApplications();
		// verifications
		assertThat(applications).hasSize(2);
		verify(mockClient, times(2)).get(any(URL.class));
	}
}

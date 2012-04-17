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
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.openshift.client.utils.OpenShiftTestConfiguration;
import com.openshift.client.utils.Samples;
import com.openshift.internal.client.LinkRetriever;
import com.openshift.internal.client.RestService;
import com.openshift.internal.client.httpclient.HttpClientException;
import com.openshift.internal.client.httpclient.UnauthorizedException;

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
		OpenShiftTestConfiguration configuration = new OpenShiftTestConfiguration();
		this.user = new UserBuilder().configure(
				new RestService(
						configuration.getStagingServer(),
						configuration.getClientId(),
						mockClient)).build();
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

	@Test(expected = InvalidCredentialsOpenShiftException.class)
	public void shouldNotLoadDomainsWithInvalidCredentials() throws OpenShiftException, SocketTimeoutException,
			HttpClientException {
		// pre-conditions
		when(mockClient.get(urlEndsWith("/api")))
				.thenThrow(new UnauthorizedException("invalid mock credentials", null));
		// operation
		user.getDomains();
		// verifications
		// expect an exception
	}

	@Test
	public void shouldCreateNewDomain() throws Throwable {
		// pre-conditions
		when(mockClient.get(urlEndsWith("/domains"))).thenReturn(
				Samples.GET_DOMAINS_NOEXISTING_JSON.getContentAsString());
		when(mockClient.post(anyMapOf(String.class, Object.class), urlEndsWith("/domains"))).thenReturn(
				Samples.ADD_DOMAIN_JSON.getContentAsString());
		// operation
		final IDomain domain = user.createDomain("foobar2");
		// verifications
		assertThat(domain.getId()).isEqualTo("foobar2");
	}

	@Test(expected = OpenShiftException.class)
	public void shouldNotRecreateExistingDomain() throws Throwable {
		// pre-conditions
		when(mockClient.get(urlEndsWith("/domains"))).thenReturn(
				Samples.GET_DOMAINS_1EXISTING_JSON.getContentAsString());
		when(mockClient.post(anyMapOf(String.class, Object.class), urlEndsWith("/domains"))).thenReturn(
				Samples.ADD_DOMAIN_JSON.getContentAsString());
		// operation
		user.createDomain("foobar");
		// verifications
		// expect an exception
	}

	@Test
	public void shouldUpdateDomainNamespace() throws Throwable {
		// pre-conditions
		when(mockClient.get(urlEndsWith("/domains"))).thenReturn(
				Samples.GET_DOMAINS_1EXISTING_JSON.getContentAsString());
		when(mockClient.put(anyMapOf(String.class, Object.class), urlEndsWith("/domains/foobar"))).thenReturn(
				Samples.UPDATE_DOMAIN_NAMESPACE.getContentAsString());
		final IDomain domain = user.getDomain("foobar");
		// operation
		domain.setId("foobarbaz");
		// verifications
		final IDomain updatedDomain = user.getDomain("foobarbaz");
		assertThat(updatedDomain.getId()).isEqualTo("foobarbaz");
		assertThat(LinkRetriever.retrieveLink(updatedDomain, "UPDATE").getHref()).contains("/foobarbaz");
		verify(mockClient, times(1)).put(anyMapOf(String.class, Object.class), any(URL.class));
	}

	@Ignore
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

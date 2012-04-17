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
import static com.openshift.client.utils.Samples.ADD_DOMAIN_JSON;
import static com.openshift.client.utils.Samples.DELETE_DOMAIN_JSON;
import static com.openshift.client.utils.Samples.GET_DOMAINS_1EXISTING_JSON;
import static com.openshift.client.utils.Samples.GET_DOMAINS_NOEXISTING_JSON;
import static com.openshift.client.utils.Samples.UPDATE_DOMAIN_ID;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
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
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.openshift.client.utils.Samples;
import com.openshift.internal.client.LinkRetriever;
import com.openshift.internal.client.RestService;
import com.openshift.internal.client.httpclient.BadRequestException;
import com.openshift.internal.client.httpclient.HttpClientException;
import com.openshift.internal.client.httpclient.UnauthorizedException;

/**
 * @author Xavier Coulon
 * @author Andre Dietisheim
 */
public class DomainResourceTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(DomainResourceTest.class);

	private IUser user;
	private IHttpClient mockClient;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Rule
	public ErrorCollector errorCollector = new ErrorCollector();

	@Before
	public void setup() throws Throwable {
		mockClient = mock(IHttpClient.class);
		when(mockClient.get(urlEndsWith("/broker/rest/api")))
		.thenReturn(Samples.GET_REST_API_JSON.getContentAsString());
		when(mockClient.get(urlEndsWith("/user"))).thenReturn(
				Samples.GET_USER.getContentAsString());
		when(mockClient.get(urlEndsWith("/domains"))).thenReturn(GET_DOMAINS_1EXISTING_JSON.getContentAsString());
		final IOpenShiftConnection connection = new OpenShiftConnectionManager().getConnection(new RestService("http://mock",
				"clientId", mockClient), "foo@redhat.com", "bar");
		this.user = connection.getUser();
	}

	@Test
	public void shouldLoadEmptyListOfDomains() throws Throwable {
		// pre-conditions
		when(mockClient.get(urlEndsWith("/domains"))).thenReturn(GET_DOMAINS_NOEXISTING_JSON.getContentAsString());
		// operation
		final List<IDomain> domains = user.getDomains();
		// verifications
		assertThat(domains).hasSize(0);
		// 3 calls: /API + /API/user + /API/domains
		verify(mockClient, times(3)).get(any(URL.class));
	}

	@Test
	public void shouldLoadSingleUserDomain() throws Throwable {
		// pre-conditions
		when(mockClient.get(urlEndsWith("/domains"))).thenReturn(GET_DOMAINS_1EXISTING_JSON.getContentAsString());
		// operation
		final List<IDomain> domains = user.getDomains();
		// verifications
		assertThat(domains).hasSize(1);
		// 3 calls: /API + /API/user + /API/domains
		verify(mockClient, times(3)).get(any(URL.class));
	}

	@Test
	@Ignore("Can't happen: user is already loaded with his credentials")
	public void shouldNotLoadDomainsWithInvalidCredentials() throws OpenShiftException, SocketTimeoutException,
			HttpClientException {
		// pre-conditions
		when(mockClient.get(urlEndsWith("/api")))
				.thenThrow(new UnauthorizedException("invalid mock credentials", null));
		expectedException.expect(InvalidCredentialsOpenShiftException.class);
		// operation
		user.getDomains();
		// verifications
		// expect an exception
	}

	@Test
	public void shouldCreateNewDomain() throws Throwable {
		// pre-conditions
		when(mockClient.get(urlEndsWith("/domains"))).thenReturn(GET_DOMAINS_NOEXISTING_JSON.getContentAsString());
		when(mockClient.post(anyMapOf(String.class, Object.class), urlEndsWith("/domains"))).thenReturn(
				ADD_DOMAIN_JSON.getContentAsString());
		// operation
		final IDomain domain = user.createDomain("foobar2");
		// verifications
		assertThat(domain.getId()).isEqualTo("foobar2");
		assertThat(domain.getSuffix()).isEqualTo("stg.rhcloud.com");
	}

	@Test(expected = OpenShiftException.class)
	public void shouldNotRecreateExistingDomain() throws Throwable {
		// pre-conditions
		when(mockClient.get(urlEndsWith("/domains"))).thenReturn(GET_DOMAINS_1EXISTING_JSON.getContentAsString());
		when(mockClient.post(anyMapOf(String.class, Object.class), urlEndsWith("/domains"))).thenReturn(
				ADD_DOMAIN_JSON.getContentAsString());
		// operation
		user.createDomain("foobar");
		// verifications
		// expect an exception
	}

	@Test
	public void shouldDestroyDomain() throws Throwable {
		// pre-conditions
		when(mockClient.get(urlEndsWith("/domains"))).thenReturn(GET_DOMAINS_1EXISTING_JSON.getContentAsString());
		when(mockClient.delete(urlEndsWith("/domains/foobar"))).thenReturn(DELETE_DOMAIN_JSON.getContentAsString());
		// operation
		final IDomain domain = user.getDomain("foobar");
		domain.destroy();
		// verifications
		assertThat(user.getDomain("foobar")).isNull();
		assertThat(user.getDomains()).isEmpty();
	}

	@Test
	public void shouldNotDestroyDomainWithApp() throws Throwable {
		// pre-conditions
		when(mockClient.get(urlEndsWith("/domains"))).thenReturn(GET_DOMAINS_1EXISTING_JSON.getContentAsString());
		final BadRequestException badRequestException = new BadRequestException(
				"Domain contains applications. Delete applications first or set force to true.", null);
		when(mockClient.delete(urlEndsWith("/domains/foobar"))).thenThrow(badRequestException);
		// operation
		final IDomain domain = user.getDomain("foobar");
		try {
			domain.destroy();
			fail("Expected an exception here..");
		} catch(OpenShiftEndpointException e) {
			assertThat(e.getCause()).isInstanceOf(BadRequestException.class);
		}
		// verifications
		assertThat(domain).isNotNull();
		assertThat(user.getDomains()).isNotEmpty().contains(domain);
	}

	@Test
	public void shouldUpdateDomainId() throws Throwable {
		// pre-conditions
		when(mockClient.get(urlEndsWith("/domains"))).thenReturn(GET_DOMAINS_1EXISTING_JSON.getContentAsString());
		when(mockClient.put(anyMapOf(String.class, Object.class), urlEndsWith("/domains/foobar"))).thenReturn(
				UPDATE_DOMAIN_ID.getContentAsString());
		final IDomain domain = user.getDomain("foobar");
		// operation
		domain.setId("foobarbaz");
		// verifications
		final IDomain updatedDomain = user.getDomain("foobarbaz");
		assertThat(updatedDomain.getId()).isEqualTo("foobarbaz");
		assertThat(LinkRetriever.retrieveLink(updatedDomain, "UPDATE").getHref()).contains("/foobarbaz");
		verify(mockClient, times(1)).put(anyMapOf(String.class, Object.class), any(URL.class));
	}

	@Test
	@Ignore
	public void shouldRefreshDomain() throws Throwable {
		fail("not implemented yet");
	}

	@Test
	@Ignore
	public void shouldNotReloadDomainTwice() throws Throwable {
		fail("not implemented yet");
	}

	@Test
	@Ignore
	public void shouldNotifyAfterDomainCreated() throws Throwable {
		fail("not implemented yet");
	}

	@Test
	@Ignore
	public void shouldNotifyAfterDomainUpdated() throws Throwable {
		fail("not implemented yet");
	}

	@Test
	@Ignore
	public void shouldNotifyAfterDomainDestroyed() throws Throwable {
		fail("not implemented yet");
	}
}

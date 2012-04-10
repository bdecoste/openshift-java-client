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

import static com.openshift.client.IRestServiceTestConstants.CLIENT_ID;
import static com.openshift.client.utils.CustomArgumentMatchers.urlEndsWith;
import static com.openshift.client.utils.Samples.*;
import static com.openshift.client.utils.Samples.GET_APPLICATIONS_WITHNOAPP_JSON;
import static com.openshift.client.utils.Samples.GET_DOMAINS_1EXISTING_JSON;
import static com.openshift.client.utils.Samples.GET_DOMAINS_NOEXISTING_JSON;
import static com.openshift.client.utils.Samples.GET_REST_API_JSON;
import static com.openshift.client.utils.Samples.UPDATE_DOMAIN_NAMESPACE;
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
import org.junit.Test;

import com.openshift.internal.client.EmbeddableCartridge;
import com.openshift.internal.client.LinkRetriever;
import com.openshift.internal.client.RestService;
import com.openshift.internal.client.httpclient.HttpClientException;
import com.openshift.internal.client.httpclient.UnauthorizedException;

/**
 * @author Xavier Coulon
 * @author Andre Dietisheim
 */
public class ApplicationResourceTest {

	private IDomain domain;
	private IHttpClient mockClient;

	@Before
	public void setup() throws Throwable {
		mockClient = mock(IHttpClient.class);
		when(mockClient.get(urlEndsWith("/broker/rest/api"))).thenReturn(GET_REST_API_JSON.getContentAsString());
		when(mockClient.get(urlEndsWith("/domains"))).thenReturn(
				GET_DOMAINS_1EXISTING_JSON.getContentAsString());
		IUser user = new UserBuilder().configure(new RestService(CLIENT_ID, mockClient)).build();
		this.domain = user.getDomain("foobar"); 
	}

	@Test
	public void shouldLoadListOfApplicationsWithNoElement() throws Throwable {
		// pre-conditions
		when(mockClient.get(urlEndsWith("/domains/foobar/applications"))).thenReturn(
				GET_APPLICATIONS_WITHNOAPP_JSON.getContentAsString());
		// operation
		final List<IApplication> apps = domain.getApplications();
		// verifications
		assertThat(apps).isEmpty();
		verify(mockClient, times(3)).get(any(URL.class));
	}

	@Test
	public void shouldLoadListOfApplicationsWith1Element() throws Throwable {
		// pre-conditions
		when(mockClient.get(urlEndsWith("/domains/foobar/applications"))).thenReturn(
						GET_APPLICATIONS_WITH1APP_JSON.getContentAsString());
		// operation
		final List<IApplication> apps = domain.getApplications();
		// verifications
		assertThat(apps).hasSize(1);
		verify(mockClient, times(3)).get(any(URL.class));
	}

	@Test
	public void shouldLoadListOfApplicationsWith2Elements() throws Throwable {
		// pre-conditions
		when(mockClient.get(urlEndsWith("/domains/foobar/applications"))).thenReturn(
						GET_APPLICATIONS_WITH2APPS_JSON.getContentAsString());
		// operation
		final List<IApplication> apps = domain.getApplications();
		// verifications
		assertThat(apps).hasSize(2);
		verify(mockClient, times(3)).get(any(URL.class));
	}
	
	@Test(expected=InvalidCredentialsOpenShiftException.class)
	public void shouldNotLoadListOfApplicationsWithInvalidCredentials() throws OpenShiftException, SocketTimeoutException, HttpClientException {
		// pre-conditions
		when(mockClient.get(urlEndsWith("/domains/foobar/applications"))).thenThrow(new UnauthorizedException("invalid credentials (mock)", null));
		// operation
		domain.getApplications();
		// verifications
		// expect an exception
	}
	
	@Test
	public void shouldCreateApplication() throws Throwable {
		// pre-conditions
		when(mockClient.get(urlEndsWith("/domains/foobar/applications"))).thenReturn(
				GET_APPLICATIONS_WITH2APPS_JSON.getContentAsString());
		when(mockClient.post(anyMapOf(String.class, Object.class), urlEndsWith("/domains"))).thenReturn(
				ADD_DOMAIN_JSON.getContentAsString());
		// operation
		final ICartridge cartridge = new EmbeddableCartridge("jbossas-7");
		final IApplication app = domain.createApplication("foo", cartridge);
		// verifications
		assertThat(app.getName()).isEqualTo("foo");
		assertThat(app.getApplicationUrl()).isNotNull().endsWith("/domains/foobar/applications/foo");
		assertThat(app.getCreationTime()).isNotNull();
		assertThat(app.getGitUri()).isNotNull();
		assertThat(app.getCartridge()).isEqualTo(cartridge);
		assertThat(app.getUUID()).isNotNull();
		assertThat(app.getDomain()).isEqualTo(domain);
		assertThat(LinkRetriever.retrieveLinks(app)).hasSize(7);
		assertThat(domain.getApplications()).hasSize(2).contains(app);
	}
	
	@Test(expected=OpenShiftException.class)
	public void shouldNotCreateApplicationWithMissingName() throws Throwable {
		// pre-conditions
		when(mockClient.get(urlEndsWith("/domains/foobar/applications"))).thenReturn(
				GET_APPLICATIONS_WITH2APPS_JSON.getContentAsString());
		when(mockClient.post(anyMapOf(String.class, Object.class), urlEndsWith("/domains"))).thenReturn(
				ADD_DOMAIN_JSON.getContentAsString());
		// operation
		domain.createApplication(null, new EmbeddableCartridge("jbossas-7"));
		// verifications
		// expected exception
	}

	@Test(expected=OpenShiftException.class)
	public void shouldNotCreateApplicationWithMissingCartridge() throws Throwable {
		// pre-conditions
		when(mockClient.get(urlEndsWith("/domains/foobar/applications"))).thenReturn(
				GET_APPLICATIONS_WITH2APPS_JSON.getContentAsString());
		when(mockClient.post(anyMapOf(String.class, Object.class), urlEndsWith("/domains"))).thenReturn(
				ADD_DOMAIN_JSON.getContentAsString());
		// operation
		domain.createApplication("foo", null);
		// verifications
		// expected exception
	}

	@Test(expected=OpenShiftException.class)
	public void shouldNotRecreateExistingApplication() throws Throwable {
		// pre-conditions
		when(mockClient.get(urlEndsWith("/domains/foobar/applications"))).thenReturn(
				GET_APPLICATIONS_WITH2APPS_JSON.getContentAsString());
		// operation
		final ICartridge cartridge = new EmbeddableCartridge("jbossas-7");
		final IApplication app = domain.createApplication("sample", cartridge);
		// verifications
		// expect an exception
		fail("should *also* verify that domain.applications size still equals 1");
	}

	@Test
	public void shouldDestroyApplication() throws Throwable {
		// pre-conditions
		when(mockClient.get(urlEndsWith("/domains/foobar/applications"))).thenReturn(
				GET_APPLICATIONS_WITH2APPS_JSON.getContentAsString());
		final IApplication app = domain.getApplicationByName("sample");
		// operation
		app.destroy();
		// verifications 
		assertThat(domain.getApplications()).hasSize(1).excludes(app);
	}

	@Test
	@Ignore
	public void shouldUpdateApplication() throws Throwable {
		//TODO: See if gears/gearsize can be changed after creation
	}
	
	@Test
	@Ignore
	public void shouldRefreshApplication() throws Throwable {
		fail("not implemented yet");
	}

	@Test
	@Ignore
	public void shouldNotLoadApplicationTwice() throws Throwable {
		fail("not implemented yet");
	}

	@Test
	@Ignore
	public void shouldNotifyAfterApplicationCreated() throws Throwable {
		fail("not implemented yet");
	}
	
	@Test
	@Ignore
	public void shouldNotifyAfterApplicationUpdated() throws Throwable {
		fail("not implemented yet");
	}

	@Test
	@Ignore
	public void shouldNotifyAfterApplicationDestroyed() throws Throwable {
		fail("not implemented yet");
	}

	
}

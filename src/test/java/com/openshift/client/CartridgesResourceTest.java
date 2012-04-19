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

import static com.openshift.client.utils.MockUtils.anyForm;
import static com.openshift.client.utils.Samples.ADD_APPLICATION_ALIAS;
import static com.openshift.client.utils.Samples.ADD_APPLICATION_CARTRIDGE;
import static com.openshift.client.utils.Samples.ADD_APPLICATION_JSON;
import static com.openshift.client.utils.Samples.ADD_DOMAIN_JSON;
import static com.openshift.client.utils.Samples.DELETE_APPLICATION_CARTRIDGE;
import static com.openshift.client.utils.Samples.GET_APPLICATIONS_WITH1APP_JSON;
import static com.openshift.client.utils.Samples.GET_APPLICATIONS_WITH2APPS_JSON;
import static com.openshift.client.utils.Samples.GET_APPLICATIONS_WITHNOAPP_JSON;
import static com.openshift.client.utils.Samples.GET_APPLICATION_CARTRIDGES_WITH1ELEMENT;
import static com.openshift.client.utils.Samples.GET_APPLICATION_CARTRIDGES_WITH2ELEMENTS;
import static com.openshift.client.utils.Samples.GET_APPLICATION_GEARS_WITH1ELEMENT;
import static com.openshift.client.utils.Samples.GET_APPLICATION_GEARS_WITH2ELEMENTS;
import static com.openshift.client.utils.Samples.GET_APPLICATION_WITH1CARTRIDGE1ALIAS;
import static com.openshift.client.utils.Samples.GET_APPLICATION_WITH2CARTRIDGES2ALIASES;
import static com.openshift.client.utils.Samples.GET_DOMAINS_1EXISTING_JSON;
import static com.openshift.client.utils.Samples.REMOVE_APPLICATION_ALIAS;
import static com.openshift.client.utils.Samples.START_APPLICATION;
import static com.openshift.client.utils.Samples.STOP_APPLICATION;
import static com.openshift.client.utils.Samples.STOP_FORCE_APPLICATION;
import static com.openshift.client.utils.UrlEndsWithMatcher.urlEndsWith;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;

import org.fest.assertions.Condition;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.openshift.client.utils.Samples;
import com.openshift.internal.client.Cartridge;
import com.openshift.internal.client.EmbeddableCartridgeResource;
import com.openshift.internal.client.LinkRetriever;
import com.openshift.internal.client.RestService;
import com.openshift.internal.client.httpclient.HttpClientException;
import com.openshift.internal.client.httpclient.InternalServerErrorException;
import com.openshift.internal.client.httpclient.UnauthorizedException;

/**
 * @author Xavier Coulon
 * @author Andre Dietisheim
 */
public class CartridgesResourceTest {

	private IHttpClient mockClient;
	
	private IOpenShiftConnection connection;

	@Before
	public void setup() throws Throwable {
		mockClient = mock(IHttpClient.class);
		when(mockClient.get(urlEndsWith("/broker/rest/api")))
		.thenReturn(Samples.GET_REST_API.getContentAsString());
		when(mockClient.get(urlEndsWith("/cartridges"))).thenReturn(
				Samples.GET_CARTRIDGES.getContentAsString());
		connection = new OpenShiftConnectionFactory().getConnection(new RestService("http://mock",
				"clientId", mockClient), "foo@redhat.com", "bar");
	}
		

	/**
	 * Syntactic sugar.
	 * 
	 * @return
	 */
	@Test
	public void shouldLoadListOfStandaloneCartridges() throws Throwable {
		// pre-conditions
		// operation
		final List<String> cartridges = connection.getStandaloneCartridgeNames();
		// verifications
		assertThat(cartridges).hasSize(8).contains("nodejs-0.6", "jbossas-7").excludes("mongodb-2.0", "mysql-5.1");
	}

	@Test
	public void shouldLoadListOfEmbeddedCartridges() throws Throwable {
		// pre-conditions
		// operation
		final List<String> cartridges = connection.getEmbeddedCartridgeNames();
		// verifications
		assertThat(cartridges).hasSize(10).contains("mongodb-2.0", "mysql-5.1").excludes("nodejs-0.6", "jbossas-7");
	}
}

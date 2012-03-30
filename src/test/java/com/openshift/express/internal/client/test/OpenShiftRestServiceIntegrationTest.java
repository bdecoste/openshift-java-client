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
package com.openshift.express.internal.client.test;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Before;
import org.junit.Test;

import com.openshift.express.client.OpenShiftException;
import com.openshift.express.client.OpenShiftService;
import com.openshift.express.client.configuration.OpenShiftConfiguration;
import com.openshift.express.internal.client.response.unmarshalling.dto.RestResponse;
import com.openshift.express.internal.client.test.fakes.TestUser;

public class OpenShiftRestServiceIntegrationTest {

	private static final String PATH_DOMAINS = "domains";
	
	private TestUser user;
	private OpenShiftService service;

	@Before
	public void setUp() throws OpenShiftException, IOException {
		this.service = new OpenShiftService("com.openshift.express.test", new OpenShiftConfiguration().getLibraServer());
		this.user = new TestUser(service);
	}

	@Test
	public void canGetDomains() throws OpenShiftException, MalformedURLException {
		RestResponse domainsResponse = service.getDomains(PATH_DOMAINS, user);
		assertNotNull(domainsResponse);
	}
	
}

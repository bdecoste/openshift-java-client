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

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.openshift.internal.client.RestServiceProperties;

/**
 * @author Andre Dietisheim
 */
public class RestServicePropertiesIntegrationTest {

	private RestServiceProperties restServiceProperties;

	@Before
	public void setUp() {
		this.restServiceProperties = new RestServiceProperties();
	}
		
	@Test
	public void userAgentShouldContainOpenShiftAndClientId() {
		String clientId = "com.openshift.client.test";
		assertThat(restServiceProperties.getUseragent(clientId)).contains("OpenShift").contains(clientId);
	}
	
}

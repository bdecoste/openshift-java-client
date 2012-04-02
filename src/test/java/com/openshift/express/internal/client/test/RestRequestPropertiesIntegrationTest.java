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

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

import com.openshift.express.internal.client.RestRequestProperties;

/**
 * @author Andre Dietisheim
 */
public class RestRequestPropertiesIntegrationTest {

	private static final Pattern NUMBER_PATTERN = Pattern.compile("[0-9]+(\\.[0-9]+)");
	
	private RestRequestProperties restRequestProperties;

	@Before
	public void setUp() {
		this.restRequestProperties = new RestRequestProperties();
	}
	
	@Test
	public void versionShouldBeNumeric() {
		String version = restRequestProperties.getVersion();
		assertTrue(NUMBER_PATTERN.matcher(version).matches());
	}
	
	@Test
	public void userAgentShouldContainOpenShiftAndMessageFormat() {
		assertThat(restRequestProperties.getUseragent()).contains("OpenShift").contains("{0}");
	}
	
}

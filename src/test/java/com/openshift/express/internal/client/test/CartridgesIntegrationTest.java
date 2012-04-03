/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package com.openshift.express.internal.client.test;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.openshift.express.client.OpenShiftException;
import com.openshift.express.client.OpenShiftService;

/**
 * @author André Dietisheim
 */
public class CartridgesIntegrationTest {

//	private OpenShiftService openShiftService;

//	private User user;
	
	@Before
	public void setUp() throws OpenShiftException, IOException {
//		UserConfiguration userConfiguration = new UserConfiguration(new SystemConfiguration(new DefaultConfiguration()));
//		this.openShiftService = new OpenShiftService(TestUser.ID, userConfiguration.getLibraServer());
//		openShiftService.setEnableSSLCertChecks(Boolean.parseBoolean(System.getProperty("enableSSLCertChecks")));
//		this.user = new TestUser(openShiftService);
	}

	@Test
	public void canListCartridges() throws Exception {
//		List<ICartridge> cartridges = openShiftService.getCartridges(user);
//		assertNotNull(cartridges);
//		assertTrue(cartridges.size() > 0);
	}

	@Test
	public void canListEmbeddableCartridges() throws Exception {
//		List<IEmbeddableCartridge> cartridges = openShiftService.getEmbeddableCartridges(user);
//		assertNotNull(cartridges);
//		assertTrue(cartridges.size() > 0);
	}
}

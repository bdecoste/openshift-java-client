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
package com.openshift.client.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.SocketTimeoutException;
import java.util.regex.Pattern;

import org.fest.assertions.AssertExtension;

import com.openshift.client.IApplication;
import com.openshift.client.OpenShiftException;

/**
 * @author André Dietisheim
 */
public class ApplicationAssert implements AssertExtension {

	
	public static final Pattern APPLICATION_URL_REGEXP = Pattern.compile("https*://[^\\.]+\\..{2,3}");

	private IApplication application;

	public ApplicationAssert(IApplication application) {
		this.application = application;
	}

	public ApplicationAssert hasName(String name) {
		assertEquals(name, application.getName());
		return this;
	}
	
	public ApplicationAssert hasUUID(String uuid) {
		assertEquals(uuid, application.getUUID());
		return this;
	}

	public ApplicationAssert hasUUID() {
		assertNotNull(application.getUUID());
		return this;
	}

	public ApplicationAssert hasCartridge(String cartridgeName) {
		assertEquals(cartridgeName, application.getCartridge());
		return this;
	}

	public ApplicationAssert hasCreationTime(String creationTime) {
		assertEquals(creationTime, application.getCreationTime());
		return this;
	}
	
	public ApplicationAssert hasGitUrl(String gitUrl) {
		assertEquals(gitUrl, application.getGitUrl());
		return this;
	}
	
	public ApplicationAssert hasApplicationUrl(String applicationUrl) {
		assertEquals(applicationUrl, application.getApplicationUrl());
		return this;
	}

	public ApplicationAssert hasValidApplicationUrl() {
		assertTrue(APPLICATION_URL_REGEXP.matcher(application.getApplicationUrl()).find());
		return this;
	}

	public ApplicationAssert hasEmbeddableCartridges(String...embeddableCartridgeNames) throws SocketTimeoutException, OpenShiftException {
		if (embeddableCartridgeNames.length == 0) {
			assertEquals(0, application.getEmbeddedCartridges().size());
		}
		
		for (String cartridgeName : embeddableCartridgeNames) {
			application.hasEmbeddedCartridge(cartridgeName);
		}
		return this;
	}
}

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
package com.openshift.internal.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Collections;

import org.junit.Test;
import org.mockito.Mockito;

import com.openshift.client.IEmbeddableCartridge;
import com.openshift.internal.client.response.Link;
import com.openshift.internal.client.response.Message;

/**
 * @author Andre Dietisheim
 */
public class EmbeddableCartridgeTest {

	@Test
	public void shouldEqualsOtherCartridgeWithSameName() {
		assertEquals(new EmbeddableCartridge("redhat"), new EmbeddableCartridge("redhat"));
		assertEquals(IEmbeddableCartridge.JENKINS_14,
				new EmbeddableCartridge(IEmbeddableCartridge.JENKINS_14.getName()));
		assertFalse(new EmbeddableCartridge("redhat").equals(new EmbeddableCartridge("jboss")));
	}

	@Test
	public void embeddedCartridgeShouldEqualsEmbeddableCartridge() {
		ApplicationResource applicationResourceMock = Mockito.mock(ApplicationResource.class);
		
		assertEquals(
				new EmbeddableCartridge("redhat"),
				new EmbeddedCartridgeResource(
						"redhat",
						CartridgeType.EMBEDDED,
						Collections.<String, Link> emptyMap(),
						Collections.<Message> emptyList(),
						applicationResourceMock));
		assertEquals(IEmbeddableCartridge.JENKINS_14,
				new EmbeddableCartridge(IEmbeddableCartridge.JENKINS_14.getName()));
		assertFalse(new EmbeddableCartridge("redhat").equals(new EmbeddableCartridge("jboss")));
	}
}

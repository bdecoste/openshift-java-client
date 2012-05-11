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
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashSet;

import org.junit.Test;
import org.mockito.Mockito;

import com.openshift.client.IEmbeddedCartridge;
import com.openshift.internal.client.response.Link;
import com.openshift.internal.client.response.Message;

/**
 * @author Andre Dietisheim
 */
public class EmbeddableCartridgeTest {

	@Test
	public void shouldEqualsOtherCartridge() {
		// pre-coniditions
		// operation
		// verification
		assertEquals(new EmbeddableCartridge("redhat"), new EmbeddableCartridge("redhat"));
		assertFalse(new EmbeddableCartridge("redhat").equals(new EmbeddableCartridge("jboss")));
	}

	@Test
	public void shouldEqualsEmbeddableCartridge() {
		// pre-coniditions
		ApplicationResource applicationResourceMock = Mockito.mock(ApplicationResource.class);

		// operation
		// verification
		assertEquals(
				new EmbeddableCartridge("redhat"),
				new EmbeddedCartridgeResource(
						"redhat",
						CartridgeType.EMBEDDED,
						Collections.<String, Link> emptyMap(),
						Collections.<Message> emptyList(),
						applicationResourceMock));
		assertFalse(new EmbeddableCartridge("redhat").equals(new EmbeddableCartridge("jboss")));
	}

	@Test
	public void shouldHaveSameHashCode() {
		// pre-coniditions
		ApplicationResource applicationResourceMock = Mockito.mock(ApplicationResource.class);
		// operation
		// verification
		EmbeddedCartridgeResource embeddedCartridge =
				new EmbeddedCartridgeResource(
						"redhat",
						CartridgeType.EMBEDDED,
						Collections.<String, Link> emptyMap(),
						Collections.<Message> emptyList(),
						applicationResourceMock);
		assertEquals(embeddedCartridge.hashCode(), new EmbeddableCartridge("redhat").hashCode());
	}
	
	@Test
	public void shouldRemoveEmbeddedCartridgeInASetByEmbeddableCartridge() {
		// pre-coniditions
		ApplicationResource applicationResourceMock = Mockito.mock(ApplicationResource.class);
		EmbeddedCartridgeResource embeddedCartridge =
				new EmbeddedCartridgeResource(
						"redhat",
						CartridgeType.EMBEDDED,
						Collections.<String, Link> emptyMap(),
						Collections.<Message> emptyList(),
						applicationResourceMock);
		HashSet<IEmbeddedCartridge> cartridges = new HashSet<IEmbeddedCartridge>();
		cartridges.add(embeddedCartridge);
		assertEquals(cartridges.size(), 1);
		// operation
		boolean removed = cartridges.remove(new EmbeddableCartridge("redhat"));
		
		// verification
		assertTrue(removed);
		assertEquals(0, cartridges.size());
	}
}

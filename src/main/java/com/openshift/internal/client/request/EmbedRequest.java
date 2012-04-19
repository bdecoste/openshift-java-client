/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package com.openshift.internal.client.request;

import com.openshift.client.IEmbeddedCartridge;

/**
 * @author André Dietisheim
 */
public class EmbedRequest extends AbstractOpenShiftRequest {

	private String name;
	private IEmbeddedCartridge cartridge ;
	private EmbedAction action;

	public EmbedRequest(String name, IEmbeddedCartridge cartridge, EmbedAction action, String username) {
		this(name, cartridge, action, username, false);
	}

	public EmbedRequest(String name, IEmbeddedCartridge cartridge, EmbedAction action, String username, boolean debug) {
		super(username, debug);
		this.name = name;
		this.cartridge = cartridge;
		this.action = action;
	}

	public EmbedAction getAction() {
		return action;
	}

	public String getName() {
		return name;
	}

	public IEmbeddedCartridge getEmbeddableCartridge() {
		return cartridge;
	}

	public String getResourcePath() {
		return "embed_cartridge";
	}
}

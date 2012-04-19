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
package com.openshift.internal.client;

import com.openshift.client.IEmbeddableCartridge;

/**
 * An interface that designate a cartridge that can be embedded into an application.
 * @author Xavier Coulon
 *
 * @see IEmbeddableCartridge for cartridges that have already been added and configured to an application.
 */
public class EmbeddableCartridge implements IEmbeddableCartridge {

	/** the embeddable cartridge name.*/
	private final String name;
	
	public EmbeddableCartridge(final String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

}

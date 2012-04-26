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
package com.openshift.client;

import java.net.SocketTimeoutException;


/**
 * Interface to designate a cartridge that has been added and configured
 * @author André Dietisheim
 */
public interface IEmbeddedCartridge extends IEmbeddableCartridge {

	public String getUrl() throws OpenShiftException;
	
	public String getCreationLog();
	
	public void setCreationLog(String creationLog);
	
	/**
	 * Destroys this cartridge (and removes it from the list of existing cartridges)
	 * 
	 * @throws OpenShiftException
	 * @throws SocketTimeoutException
	 */
	public void destroy() throws OpenShiftException, SocketTimeoutException;


}
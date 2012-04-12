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

import java.net.SocketTimeoutException;

/**
 * @author Andre Dietisheim
 */
public interface IOpenShiftSSHKey extends ISSHPublicKey {

	/**
	 * Returns the name that is used to store this key on OpenShift. 
	 * 
	 * @return
	 */
	public String getName();
	
	public void setPublicKey(String publicKey) throws SocketTimeoutException, OpenShiftException;
	
	public void setKeyType(SSHKeyType type) throws SocketTimeoutException, OpenShiftException;

}
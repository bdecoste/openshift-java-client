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
import java.util.List;

/**
 * @author Xavier Coulon
 *
 */
public interface IOpenShiftConnection {

	/**
	 * Returns the user associated with the current OpenShift connection.
	 * @return the user
	 * @throws OpenShiftException
	 * @throws SocketTimeoutException
	 */
	public IUser getUser() throws OpenShiftException, SocketTimeoutException;
	
	/**
	 * Returns the domains associated with the current OpenShift connection.
	 * @return the domains
	 * @throws OpenShiftException
	 * @throws SocketTimeoutException
	 */
	public List<IDomain> getDomains() throws OpenShiftException, SocketTimeoutException;
	
	/**
	 * Returns the available standalone cartridge names associated with the current OpenShift connection.
	 * @return the available standalone cartridge names
	 * @throws OpenShiftException
	 * @throws SocketTimeoutException
	 */
	public List<String> getStandaloneCartridgeNames() throws OpenShiftException, SocketTimeoutException;
	
	/**
	 * Returns the available embedded cartridge names associated with the current OpenShift connection.
	 * @return the available embedded cartridge names
	 * @throws OpenShiftException
	 * @throws SocketTimeoutException
	 */
	public List<String> getEmbeddedCartridgeNames() throws OpenShiftException, SocketTimeoutException;

}

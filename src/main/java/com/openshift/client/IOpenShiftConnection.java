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
	 */
	public IUser getUser() throws OpenShiftException;
	
	/**
	 * Returns the domains associated with the current OpenShift connection.
	 * @return the domains
	 * @throws OpenShiftException
	 */
	public List<IDomain> getDomains() throws OpenShiftException;
	
	/**
	 * Returns the available standalone cartridges associated with the current OpenShift connection.
	 * @return the available standalone cartridges
	 * @throws OpenShiftException
	 */
	public List<ICartridge> getStandaloneCartridges() throws OpenShiftException;
	
	/**
	 * Returns the available embeddable cartridges associated with the current OpenShift connection.
	 * @return the available embeddable cartridges 
	 * @throws OpenShiftException
	 */
	public List<IEmbeddableCartridge> getEmbeddableCartridges() throws OpenShiftException;

}

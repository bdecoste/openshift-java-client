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
package com.openshift.express.internal.client;

import java.util.List;

import com.openshift.express.client.ICartridge;
import com.openshift.express.client.IDomain;
import com.openshift.express.client.IEmbeddableCartridge;
import com.openshift.express.client.ISSHPublicKey;
import com.openshift.express.client.IUser;
import com.openshift.express.client.OpenShiftException;

/**
 * @author Xavier Coulon
 *
 */
public class User implements IUser {

	private String rhLogin = null;
	
	private String password = null;
	
	private List<IDomain> domains = null;
	
	/**
	 * {@inheritDoc}
	 */
	public String getRhlogin() {
		return rhLogin;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getAuthKey() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getAuthIV() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isValid() throws OpenShiftException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getUUID() throws OpenShiftException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public IDomain createDomain(String name, ISSHPublicKey key) throws OpenShiftException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<IDomain> getDomains() throws OpenShiftException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasDomain() throws OpenShiftException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<ISSHPublicKey> getSshKeys() throws OpenShiftException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<ICartridge> getCartridges() throws OpenShiftException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<IEmbeddableCartridge> getEmbeddableCartridges() throws OpenShiftException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public ICartridge getCartridgeByName(String name) throws OpenShiftException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void refresh() throws OpenShiftException {
		// TODO Auto-generated method stub

	}

}

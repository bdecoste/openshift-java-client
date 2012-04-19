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
import java.util.List;


/**
 * @author André Dietisheim
 */
public interface IUser {

	public static final String ID = "com.openshift.client";

	public String getRhlogin();

	public String getPassword();
	
	public String getAuthKey();
	
	public String getAuthIV();
	
	public IOpenShiftConnection getConnection();

	public IDomain createDomain(String name) throws OpenShiftException, SocketTimeoutException;

	public List<IDomain> getDomains() throws OpenShiftException, SocketTimeoutException;
	
	public IDomain getDefaultDomain() throws OpenShiftException, SocketTimeoutException;
	
	public IDomain getDomain(String namespace) throws OpenShiftException, SocketTimeoutException;
	
	public boolean hasDomain() throws OpenShiftException, SocketTimeoutException;

	public List<IOpenShiftSSHKey> getSSHKeys() throws OpenShiftException, SocketTimeoutException;

	public IOpenShiftSSHKey putSSHKey(String name, ISSHPublicKey key) throws OpenShiftException, SocketTimeoutException;

	public IOpenShiftSSHKey getSSHKeyByName(String name) throws SocketTimeoutException, OpenShiftUnknonwSSHKeyTypeException, OpenShiftException;
	
	public IOpenShiftSSHKey getSSHKeyByPublicKey(String publicKey) throws SocketTimeoutException, OpenShiftUnknonwSSHKeyTypeException, OpenShiftException;

	public boolean hasSSHKeyName(String name) throws SocketTimeoutException, OpenShiftUnknonwSSHKeyTypeException, OpenShiftException;
	
	public boolean hasSSHPublicKey(String publicKey) throws SocketTimeoutException, OpenShiftUnknonwSSHKeyTypeException, OpenShiftException;

	public void refresh() throws OpenShiftException;

}
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
package com.openshift.internal.client;

import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;

import com.openshift.client.HttpMethod;
import com.openshift.client.ICartridge;
import com.openshift.client.IDomain;
import com.openshift.client.IEmbeddableCartridge;
import com.openshift.client.ISSHPublicKey;
import com.openshift.client.IUser;
import com.openshift.client.OpenShiftException;
import com.openshift.internal.client.response.unmarshalling.dto.Link;

/**
 * @author André Dietisheim
 */
public class User implements IUser {

	private String rhlogin;
	private String password;
	private String authKey;
	private String authIV;
	private List<ICartridge> cartridges;
	private List<IEmbeddableCartridge> embeddableCartridges;
	private API api;
	private IRestService service;

	public User(IRestService service) {
		this.service = service;
	}

	public boolean isValid() throws OpenShiftException {
		throw new UnsupportedOperationException();
		// try {
		// return service.isValid(this);
		// } catch (InvalidCredentialsOpenShiftException e) {
		// return false;
		// }
	}

	public IDomain createDomain(String name) throws OpenShiftException, SocketTimeoutException {
		return getAPI().createDomain(name);
	}

	public IDomain createDomain(String name, ISSHPublicKey key) throws OpenShiftException {
		throw new UnsupportedOperationException();
		// setSshKey(key);
		// this.domain = getService().createDomain(name, key, this);
		// return domain;
	}

	// protected void destroyDomain() throws OpenShiftException {
	// if (getApplications().size() > 0) {
	// throw new OpenShiftException(
	// "There are still applications, you can only delete the domain only if you delete all apps first!");
	// }
	//
	// service.destroyDomain(domain.getNamespace(), this);
	// getUserInfo().clearNameSpace();
	// this.domain = null;
	// }

	public List<IDomain> getDomains() throws OpenShiftException, SocketTimeoutException {
		return getAPI().getDomains();
	}

	public IDomain getDomain(String namespace) throws OpenShiftException, SocketTimeoutException {
		return getAPI().getDomain(namespace);
	}

	public boolean hasDomain() throws OpenShiftException, SocketTimeoutException {
		return getDomains() != null;
	}

	// private void setSshKey(ISSHPublicKey key) {
	// this.sshKey = key;
	// }

	// public ISSHPublicKey getSshKey() throws OpenShiftException {
	// if (sshKey == null) {
	// this.sshKey = getUserInfo().getSshPublicKey();
	// }
	// return sshKey;
	// }

	public List<ISSHPublicKey> getSshKeys() throws OpenShiftException, SocketTimeoutException {
		return getAPI().getUser().getSSHKeys();
	}

	public void addSshKey(ISSHPublicKey key) {

	}

	public String getRhlogin() {
		return rhlogin;
	}

	public String getPassword() {
		return password;
	}

	public String getAuthKey() {
		return authKey;
	}

	public String getAuthIV() {
		return authIV;
	}

	public String getUUID() throws OpenShiftException {
		throw new UnsupportedOperationException();
		// return getUserInfo().getUuid();
	}

	public List<ICartridge> getCartridges() throws OpenShiftException {
		throw new UnsupportedOperationException();
		// if (cartridges == null) {
		// this.cartridges = service.getCartridges(this);
		// }
		// return Collections.unmodifiableList(cartridges);
	}

	public List<IEmbeddableCartridge> getEmbeddableCartridges() throws OpenShiftException {
		throw new UnsupportedOperationException();
		// if (embeddableCartridges == null) {
		// this.embeddableCartridges = service.getEmbeddableCartridges(this);
		// }
		// return embeddableCartridges;
	}

	public ICartridge getCartridgeByName(String name) throws OpenShiftException {
		ICartridge matchingCartridge = null;
		for (ICartridge cartridge : getCartridges()) {
			if (name.equals(cartridge.getName())) {
				matchingCartridge = cartridge;
				break;
			}
		}
		return matchingCartridge;
	}

	public void refresh() throws OpenShiftException {
		throw new UnsupportedOperationException();
	}

	private API getAPI() throws SocketTimeoutException, OpenShiftException {
		if (api == null) {
			@SuppressWarnings("unchecked")
			Map<String, Link> apiLinks =
					(Map<String, Link>) service.execute(new Link("Get API", "/api", HttpMethod.GET));
			this.api = new API(service, apiLinks);
		}
		return api;
	}
}
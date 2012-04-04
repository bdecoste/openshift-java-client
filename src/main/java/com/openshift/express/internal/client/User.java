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
package com.openshift.express.internal.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.openshift.express.client.HttpMethod;
import com.openshift.express.client.IApplication;
import com.openshift.express.client.ICartridge;
import com.openshift.express.client.IDomain;
import com.openshift.express.client.IEmbeddableCartridge;
import com.openshift.express.client.ISSHPublicKey;
import com.openshift.express.client.IUser;
import com.openshift.express.client.OpenShiftException;
import com.openshift.express.internal.client.response.unmarshalling.dto.DomainResourceDTO;
import com.openshift.express.internal.client.response.unmarshalling.dto.Link;

/**
 * @author André Dietisheim
 */
public class User extends AbstractOpenShiftResource implements IUser {

	private String rhlogin;
	private String password;
	private String authKey;
	private String authIV;
	private ISSHPublicKey sshKey;
	private List<IDomain> domains;
	private UserInfo userInfo;
	private List<ICartridge> cartridges;
	private List<IEmbeddableCartridge> embeddableCartridges;
	private List<IApplication> applications = new ArrayList<IApplication>();

	public User(IRestService service) throws FileNotFoundException, IOException, OpenShiftException {
		super(service);
		postInitializeLinks();
	}

	/**
	 * Cause the underlying REST Service to call the root API in an asynchronous manner to avoid UI blockings.
	 * 
	 * @throws OpenShiftException
	 */
	private void postInitializeLinks() throws OpenShiftException {
		synchronized (links) {
			Map<String, Link> links = execute(new Link("Get API", "/api", HttpMethod.GET, null, null));
			this.links.putAll(links);
		}
	}

	public boolean isValid() throws OpenShiftException {
		throw new UnsupportedOperationException();
		// try {
		// return service.isValid(this);
		// } catch (InvalidCredentialsOpenShiftException e) {
		// return false;
		// }
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

	public List<IDomain> getDomains() throws OpenShiftException {
		if (this.domains == null) {
			this.domains = new ArrayList<IDomain>();
			List<DomainResourceDTO> domainDTOs = execute(getLink("LIST_DOMAINS"));
			for (DomainResourceDTO domainDTO : domainDTOs) {
				IDomain domain = new Domain(domainDTO.getNamespace(), domainDTO.getLinks(), this);
				this.domains.add(domain);
			}
		}
		return domains;
	}

	// public IDomain getDomain() throws OpenShiftException {
	// if (domain == null
	// && getUserInfo().hasDomain()) {
	// try {
	// this.domain = new Domain(
	// getUserInfo().getNamespace()
	// , getUserInfo().getRhcDomain()
	// , this
	// , service);
	// } catch (NotFoundOpenShiftException e) {
	// return null;
	// }
	// }
	// return domain;
	// }

	public boolean hasDomain() throws OpenShiftException {
		throw new UnsupportedOperationException();
		// try {
		// return getDomain() != null;
		// } catch(NotFoundOpenShiftException e) {
		// // domain not found
		// return false;
		// }
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

	public List<ISSHPublicKey> getSshKeys() throws OpenShiftException {
		throw new UnsupportedOperationException();
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

	public void setSshPublicKey(ISSHPublicKey key) {
		this.sshKey = key;
	}

	protected UserInfo refreshUserInfo() throws OpenShiftException {
		this.userInfo = null;
		return getUserInfo();
	}

	protected UserInfo getUserInfo() throws OpenShiftException {
		throw new UnsupportedOperationException();
		// if (userInfo == null) {
		// this.userInfo = service.getUserInfo(this);
		// }
		// return userInfo;
	}

	public void refresh() throws OpenShiftException {
		this.domains = null;
		this.sshKey = null;
		this.userInfo = null;
		getUserInfo();
	}

}
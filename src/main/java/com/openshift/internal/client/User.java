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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.openshift.client.HttpMethod;
import com.openshift.client.IApplication;
import com.openshift.client.ICartridge;
import com.openshift.client.IDomain;
import com.openshift.client.IEmbeddableCartridge;
import com.openshift.client.ISSHPublicKey;
import com.openshift.client.IUser;
import com.openshift.client.OpenShiftException;
import com.openshift.internal.client.response.unmarshalling.dto.DomainResourceDTO;
import com.openshift.internal.client.response.unmarshalling.dto.Link;

/**
 * @author André Dietisheim
 */
public class User extends AbstractOpenShiftResource implements IUser {

	private String rhlogin;
	private String password;
	private String authKey;
	private String authIV;
	private List<ISSHPublicKey> sshKeys;
	private List<IDomain> domains;
	private UserInfo userInfo;
	private List<ICartridge> cartridges;
	private List<IEmbeddableCartridge> embeddableCartridges;
	private List<IApplication> applications;

	public User(IRestService service) throws FileNotFoundException, IOException, OpenShiftException {
		super(service);
	}

	/**
	 * Cause the underlying REST Service to call the root API in an asynchronous
	 * manner to avoid UI blockings.
	 * @throws OpenShiftException 
	 * @throws SocketTimeoutException 
	 */
	@Override
	protected Map<String, Link> getLinks() throws SocketTimeoutException, OpenShiftException {
		if (super.getLinks() == null) {
			Map<String, Link> apiLinks = new GetAPIRequest().execute();
			setLinks(apiLinks);
		}
		return super.getLinks();
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
		if (hasDomain(name)) {
			throw new OpenShiftException("Domain {0} already exists", name);
		}

		final DomainResourceDTO domainDTO = new AddDomainRequest().execute(name);
		IDomain domain = new Domain(domainDTO.getNamespace(), domainDTO.getSuffix(), domainDTO.getLinks(), getService());
		this.domains.add(domain);
		return domain;
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
		if (domains == null) {
			this.domains = new ArrayList<IDomain>();
			List<DomainResourceDTO> domainDTOs = new ListDomainsRequest().execute();
			for (DomainResourceDTO domainDTO : domainDTOs) {
				IDomain domain = new Domain(domainDTO.getNamespace(), domainDTO.getSuffix(), domainDTO.getLinks(), getService());
				this.domains.add(domain);
			}
		}
		return domains;
	}

	public IDomain getDomain(String namespace) throws OpenShiftException, SocketTimeoutException {
		for (IDomain domain : getDomains()) {
			if (domain.getNamespace().equals(namespace)) {
				return domain;
			}
		}
		return null;
	}

	boolean hasDomain(String name) throws OpenShiftException, SocketTimeoutException {
		return getDomain(name) != null;
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
		if (sshK)
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
		this.sshKeys = null;
		this.userInfo = null;
		getUserInfo();
	}

	private class GetAPIRequest extends ServiceRequest {

		public GetAPIRequest() {
			super(new Link("Get API", "/api", HttpMethod.GET), User.this);
		}

		public Map<String, Link> execute() throws SocketTimeoutException, OpenShiftException {
			return super.execute();
		}
	}

	private class AddDomainRequest extends ServiceRequest {

		public AddDomainRequest() throws SocketTimeoutException, OpenShiftException {
			super("ADD_DOMAIN", User.this);
		}

		public DomainResourceDTO execute(String namespace) throws SocketTimeoutException, OpenShiftException {
			return execute(new ServiceParameter("namespace", namespace));
		}
	}
	
	private class ListDomainsRequest extends ServiceRequest {

		public ListDomainsRequest() throws SocketTimeoutException, OpenShiftException {
			super("LIST_DOMAINS", User.this);
		}

		public List<DomainResourceDTO> execute() throws SocketTimeoutException, OpenShiftException {
			return super.execute();
		}
	}

	private class GetSShKeysRequest extends ServiceRequest {

		public GetSShKeysRequest() throws SocketTimeoutException, OpenShiftException {
			super("GET", User.this);
		}

		public List<DomainResourceDTO> execute() throws SocketTimeoutException, OpenShiftException {
			return super.execute();
		}
	}
	
	
}
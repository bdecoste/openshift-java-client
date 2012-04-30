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

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.openshift.client.ICartridge;
import com.openshift.client.IDomain;
import com.openshift.client.IEmbeddableCartridge;
import com.openshift.client.IOpenShiftConnection;
import com.openshift.client.IUser;
import com.openshift.client.OpenShiftException;
import com.openshift.internal.client.response.CartridgeResourceDTO;
import com.openshift.internal.client.response.DomainResourceDTO;
import com.openshift.internal.client.response.Link;
import com.openshift.internal.client.response.UserResourceDTO;
import com.openshift.internal.client.utils.CollectionUtils;
import com.openshift.internal.client.utils.IOpenShiftJsonConstants;

/**
 * @author Andre Dietisheim
 * @author Xavier Coulon
 */
public class APIResource extends AbstractOpenShiftResource implements IOpenShiftConnection {

	private final String login;
	private final String password;
	private List<IDomain> domains;
	private UserResource user;
	private final List<ICartridge> standaloneCartridgeNames = new ArrayList<ICartridge>();
	private final List<IEmbeddableCartridge> embeddedCartridgeNames = new ArrayList<IEmbeddableCartridge>();
	
	
	protected APIResource(final String login, final String password, final IRestService service, final Map<String, Link> links) {
		super(service, links, null);
		this.login = login;
		this.password = password;
	}

	/**
	 * @return the login
	 */
	protected final String getLogin() {
		return login;
	}

	/**
	 * @return the password
	 */
	protected final String getPassword() {
		return password;
	}

	public IUser getUser() throws SocketTimeoutException, OpenShiftException {
		if (user == null) {
			this.user = new UserResource(this, new GetUserRequest().execute(), this.password);
		}
		return this.user;
	}
	
	public List<IDomain> getDomains() throws OpenShiftException, SocketTimeoutException {
		if (this.domains == null) {
			this.domains = loadDomains();
		}
		return CollectionUtils.toUnmodifiableCopy(this.domains);
	}

	private List<IDomain> loadDomains() throws SocketTimeoutException, OpenShiftException {
		List<IDomain> domains = new ArrayList<IDomain>();
		for (DomainResourceDTO domainDTO : new ListDomainsRequest().execute()) {
			domains.add(new DomainResource(domainDTO, this));
		}
		return domains;
	}
	
	public IDomain getDomain(String id) throws OpenShiftException, SocketTimeoutException {
		for (IDomain domain : getDomains()) {
			if (domain.getId().equals(id)) {
				return domain;
			}
		}
		return null;
	}

	public IDomain createDomain(String id) throws OpenShiftException, SocketTimeoutException {
		if (hasDomain(id)) {
			throw new OpenShiftException("Domain {0} already exists", id);
		}

		final DomainResourceDTO domainDTO = new AddDomainRequest().execute(id);
		final IDomain domain = new DomainResource(domainDTO, this);
		this.domains.add(domain);
		return domain;
	}
	
	public List<ICartridge> getStandaloneCartridges() throws OpenShiftException, SocketTimeoutException {
		if(standaloneCartridgeNames.isEmpty()) {
			retrieveCartridges();
		}
		return standaloneCartridgeNames;
	}

	public List<IEmbeddableCartridge> getEmbeddableCartridges() throws OpenShiftException, SocketTimeoutException {
		if(embeddedCartridgeNames.isEmpty()) {
			retrieveCartridges();
		}
		return CollectionUtils.toUnmodifiableCopy(embeddedCartridgeNames);
	}

	private void retrieveCartridges() throws SocketTimeoutException, OpenShiftException {
		final List<CartridgeResourceDTO> cartridgeDTOs = new GetCartridgesRequest().execute();
		for(CartridgeResourceDTO cartridgeDTO : cartridgeDTOs) {
			// TODO replace by enum (standalone, embedded)
			if("standalone".equals(cartridgeDTO.getType())) {
				this.standaloneCartridgeNames.add(new Cartridge(cartridgeDTO.getName()));
			} else if("embedded".equals(cartridgeDTO.getType())) {
				this.embeddedCartridgeNames.add(new EmbeddableCartridge(cartridgeDTO.getName()));
			}
		}
	}
	
	
	/**
	 * Called after a domain has been destroyed
	 * @param domain the domain to remove from the API's domains list.
	 */
	protected void removeDomain(final IDomain domain) {
		this.domains.remove(domain);
	}

	protected boolean hasDomain(String name) throws OpenShiftException, SocketTimeoutException {
		return getDomain(name) != null;
	}

	private class AddDomainRequest extends ServiceRequest {

		public AddDomainRequest() throws SocketTimeoutException, OpenShiftException {
			super("ADD_DOMAIN");
		}

		public DomainResourceDTO execute(String namespace) throws SocketTimeoutException, OpenShiftException {
			return execute(new ServiceParameter(IOpenShiftJsonConstants.PROPERTY_ID, namespace));
		}
	}
	
	private class ListDomainsRequest extends ServiceRequest {

		public ListDomainsRequest() throws SocketTimeoutException, OpenShiftException {
			super("LIST_DOMAINS");
		}

		public List<DomainResourceDTO> execute() throws SocketTimeoutException, OpenShiftException {
			return super.execute();
		}
	}

	private class GetUserRequest extends ServiceRequest {

		public GetUserRequest() throws SocketTimeoutException, OpenShiftException {
			super("GET_USER");
		}

		public UserResourceDTO execute() throws SocketTimeoutException, OpenShiftException {
			return super.execute();
		}
	}

	private class GetCartridgesRequest extends ServiceRequest {

		public GetCartridgesRequest() throws SocketTimeoutException, OpenShiftException {
			super("LIST_CARTRIDGES");
		}

		public List<CartridgeResourceDTO> execute() throws SocketTimeoutException, OpenShiftException {
			return super.execute();
		}
	}
}

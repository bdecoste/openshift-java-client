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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.openshift.client.IApplication;
import com.openshift.client.ICartridge;
import com.openshift.client.IDomain;
import com.openshift.client.OpenShiftException;
import com.openshift.internal.client.response.unmarshalling.dto.ApplicationResourceDTO;
import com.openshift.internal.client.response.unmarshalling.dto.DomainResourceDTO;
import com.openshift.internal.client.response.unmarshalling.dto.Link;
import com.openshift.internal.client.response.unmarshalling.dto.LinkParameter;


/**
 * @author André Dietisheim
 */
public class Domain extends AbstractOpenShiftResource implements IDomain {

	private static final String LINK_LIST_APPLICATIONS = "LIST_APPLICATIONS";
	private static final String LINK_ADD_APPLICATION = "ADD_APPLICATION";
	private static final String LINK_UPDATE = "UPDATE";
	private static final String LINK_DELETE = "DELETE";
	private String namespace;
	private String rhcDomain;
	/** root node in the business domain. */
	private final API api; 
	/** Applications for the domain. */
	//TODO: replace by a map indexed by application names ?
	private List<IApplication> applications = null;

	public Domain(final String namespace, final String suffix, final Map<String, Link> links, final API api) {
		super(api.getService(), links);
		this.namespace = namespace;
		this.rhcDomain = suffix;
		this.api = api;
	}
	
	protected Domain(DomainResourceDTO domainDTO, final API api) {
		this(domainDTO.getNamespace(), domainDTO.getSuffix(), domainDTO.getLinks(), api);
	}
	
	public String getNamespace() {
		return namespace;
	}
	
	public String getRhcDomain() throws OpenShiftException {
		return rhcDomain;
	}

	public void setNamespace(String namespace) throws OpenShiftException, SocketTimeoutException {
    	DomainResourceDTO domainDTO = new UpdateDomainRequest().execute(namespace);
    	this.namespace = domainDTO.getNamespace();
    	this.rhcDomain = domainDTO.getSuffix();
    	this.getLinks().clear();
    	this.getLinks().putAll(domainDTO.getLinks());
	}

	public boolean waitForAccessible(long timeout) throws OpenShiftException {
    	throw new UnsupportedOperationException();
//		boolean accessible = true;
//		for (IApplication application : getInternalUser().getApplications()) {
//			accessible |= service.waitForHostResolves(application.getApplicationUrl(), timeout);
//		}
//		return accessible;
	}
	
	public IApplication createApplication(String name, String cartridge, Boolean scale, String nodeProfile) throws OpenShiftException, SocketTimeoutException {
		// check that an application with the same does not already exists, and btw, loads the list of applications if needed (lazy)
		if(name == null) {
			throw new OpenShiftException("Application name is mandatory but none was given.");
		}
		if(cartridge == null) {
			throw new OpenShiftException("Application type is mandatory but none was given.");
		}
		if(hasApplication(name)) {
			throw new OpenShiftException("Application with name '{0}' already exists.", name);
		}
		ApplicationResourceDTO applicationDTO = new CreateApplicationRequest().execute(name, cartridge, scale, nodeProfile);
		Application application = new Application(applicationDTO.getName(), applicationDTO.getUuid(), applicationDTO.getCreationTime(), applicationDTO.getApplicationUrl(), applicationDTO.getGitUrl(), cartridge, applicationDTO.getAliases(), applicationDTO.getLinks(), this);
		this.applications.add(application);
		return application;
	}

	public IApplication getApplicationByName(String name) throws OpenShiftException, SocketTimeoutException {
		IApplication matchingApplication = null;
		for (IApplication application : getApplications()) {
			if (application.getName().equals(name)) {
				matchingApplication = application;
				break;
			}
		}
		return matchingApplication;
	}

	public boolean hasApplication(String name) throws OpenShiftException, SocketTimeoutException {
		return getApplicationByName(name) != null;
	}

	public List<IApplication> getApplicationsByCartridge(ICartridge cartridge) throws OpenShiftException {
		List<IApplication> matchingApplications = new ArrayList<IApplication>();
		for (IApplication application : this.applications) {
			if (cartridge.equals(application.getCartridge())) {
				matchingApplications.add(application);
			}
		}
		return matchingApplications;
	}
	
	public boolean hasApplication(ICartridge cartridge) throws OpenShiftException {
		return getApplicationsByCartridge(cartridge).size() > 0;
	}

	protected void add(IApplication application) {
    	throw new UnsupportedOperationException();
//		applications.add(application);
	}
	
	protected void remove(IApplication application) {
    	throw new UnsupportedOperationException();
//		applications.remove(application);
//		this.userInfo.removeApplicationInfo(application.getName());
	}

	public void destroy() throws OpenShiftException, SocketTimeoutException {
		destroy(false);
    }

	public void destroy(boolean force) throws OpenShiftException, SocketTimeoutException {
		new DeleteDomainRequest().execute();
		api.removeDomain(this);
    }
	
	public List<IApplication> getApplications() throws OpenShiftException, SocketTimeoutException {
		if (this.applications == null) {
			this.applications = new ArrayList<IApplication>();
			List<ApplicationResourceDTO> applicationDTOs = new ListApplicationsRequest().execute();
			for (ApplicationResourceDTO applicationDTO : applicationDTOs) {
				final Application application = new Application(applicationDTO.getName(), applicationDTO.getUuid(),
						applicationDTO.getCreationTime(), applicationDTO.getApplicationUrl(),
						applicationDTO.getGitUrl(), applicationDTO.getFramework(), applicationDTO.getAliases(),
						applicationDTO.getLinks(), this);
				this.applications.add(application);
			}
		}
		return Collections.unmodifiableList(applications);
	}
	
	protected void removeApplication(Application application) {
		//TODO: can this collection be a null ? 
		this.applications.remove(application);
	}

	public List<String> getAvailableCartridges() throws OpenShiftException, SocketTimeoutException {
		for(LinkParameter param : getLink(LINK_ADD_APPLICATION).getRequiredParams()) {
			if(param.getName().equals("cartridge")) {
				return param.getValidOptions();
			}
		}
		return Collections.emptyList();
	}
	
	@Override
	public String toString() {
		return "Domain [" + namespace + "-" + rhcDomain + "]";
	}
    
	private void update(List<ApplicationInfo> applicationInfos) {
    	throw new UnsupportedOperationException();
//		for (ApplicationInfo applicationInfo : applicationInfos) {
//			IApplication application = getApplicationByName(applicationInfo.getName(), applications);
//			if (application == null) {
//				applications.add(createApplication(applicationInfo));
//			}
//		}
	}

	private class ListApplicationsRequest extends ServiceRequest {

		public ListApplicationsRequest() throws SocketTimeoutException, OpenShiftException {
			super(LINK_LIST_APPLICATIONS);
		}
		
	}
	
	private class CreateApplicationRequest extends ServiceRequest {

		public CreateApplicationRequest() throws SocketTimeoutException, OpenShiftException {
			super(LINK_ADD_APPLICATION);
		}
		
		public ApplicationResourceDTO execute(final String name, final String cartridge, final Boolean scale, final String nodeProfile) throws SocketTimeoutException, OpenShiftException {
			return super.execute(new ServiceParameter("name", name), new ServiceParameter("cartridge",
					cartridge), new ServiceParameter("scale", scale), new ServiceParameter("nodeProfile",
							nodeProfile));
		}

	}
	
	private class UpdateDomainRequest extends ServiceRequest {

		public UpdateDomainRequest() throws SocketTimeoutException, OpenShiftException {
			super(LINK_UPDATE);
		}
		
		public DomainResourceDTO execute(String namespace) throws SocketTimeoutException, OpenShiftException {
			return super.execute(new ServiceParameter("namespace", namespace));
		}
	}
	
	private class DeleteDomainRequest extends ServiceRequest {
		public DeleteDomainRequest() throws SocketTimeoutException, OpenShiftException {
			super(LINK_DELETE);
		}

	}
	
}

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
import java.util.Map.Entry;

import com.openshift.client.Cartridge;
import com.openshift.client.IApplication;
import com.openshift.client.ICartridge;
import com.openshift.client.IDomain;
import com.openshift.client.IEmbeddableCartridge;
import com.openshift.client.OpenShiftException;
import com.openshift.internal.client.response.unmarshalling.dto.ApplicationResourceDTO;
import com.openshift.internal.client.response.unmarshalling.dto.DomainResourceDTO;
import com.openshift.internal.client.response.unmarshalling.dto.Link;


/**
 * @author André Dietisheim
 */
public class Domain extends AbstractOpenShiftResource implements IDomain {

	public static final String LINK_UPDATE = "UPDATE";
	private static final String LINK_LIST_APPLICATIONS = "LIST_APPLICATIONS";
	private static final String LINK_DELETE = "LIST_DELETE";
	private String namespace;
	private String rhcDomain;
	/** Applications for the domain. */
	private List<IApplication> applications = null;
	private final User user;

	public Domain(final String namespace, final String suffix, final Map<String, Link> links, final User user) {
		super(user.getService(), links);
		this.namespace = namespace;
		this.rhcDomain = suffix;
		this.user = user;
	}

	public String getNamespace() {
		return namespace;
	}
	
	public String getRhcDomain() throws OpenShiftException {
		return rhcDomain;
	}

	/**
	 * @return the user
	 */
	protected final User getUser() {
		return user;
	}

	public void setNamespace(String namespace) throws OpenShiftException, SocketTimeoutException {
    	DomainResourceDTO domainDTO = execute(getLink(LINK_UPDATE), new ServiceParameter("namespace", namespace));
    	this.namespace = domainDTO.getNamespace();
    	this.rhcDomain = domainDTO.getSuffix();
    	this.getLinks().clear();
    	this.getLinks().putAll(domainDTO.getLinks());
	}

//	private void update(IDomain domain) throws OpenShiftException {
//		this.namespace = domain.getNamespace();
//		this.rhcDomain = domain.getRhcDomain();
//	}

	public boolean waitForAccessible(long timeout) throws OpenShiftException {
    	throw new UnsupportedOperationException();
//		boolean accessible = true;
//		for (IApplication application : getInternalUser().getApplications()) {
//			accessible |= service.waitForHostResolves(application.getApplicationUrl(), timeout);
//		}
//		return accessible;
	}
	
	public IApplication createApplication(String name, ICartridge cartridge) throws OpenShiftException {
    	throw new UnsupportedOperationException();
//		IApplication application = service.createApplication(name, cartridge, this);
//		add(application);
//		return application;
	}

	public IApplication getApplicationByName(String name) throws OpenShiftException {
		IApplication matchingApplication = null;
		for (IApplication application : applications) {
			if (name.equals(application.getName())) {
				matchingApplication = application;
				break;
			}
		}
		return matchingApplication;
	}

	public boolean hasApplication(String name) throws OpenShiftException {
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
		execute(getLink(LINK_DELETE));
    }
	
	public List<IApplication> getApplications() throws OpenShiftException, SocketTimeoutException {
		if (this.applications == null) {
			this.applications = new ArrayList<IApplication>();
			List<ApplicationResourceDTO> applicationDTOs = execute(getLink(LINK_LIST_APPLICATIONS));
			for (ApplicationResourceDTO applicationDTO : applicationDTOs) {
				ICartridge cartridge = new Cartridge(applicationDTO.getFramework());
				Application application = new Application(applicationDTO.getName(), applicationDTO.getUuid(), applicationDTO.getCreationTime(), cartridge, applicationDTO.getLinks(), this);
				for(Entry<String, String> entry : applicationDTO.getEmbeddedCartridges().entrySet()) {
					IEmbeddableCartridge embeddableCartridge = new EmbeddableCartridge(entry.getKey(), entry.getValue(), application);
					application.addEmbbedCartridge(embeddableCartridge);
				}
				this.applications.add(application);
			}
		}
		return Collections.unmodifiableList(applications);
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

}

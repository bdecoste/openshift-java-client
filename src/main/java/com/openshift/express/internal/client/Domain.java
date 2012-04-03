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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.openshift.express.client.IApplication;
import com.openshift.express.client.ICartridge;
import com.openshift.express.client.IDomain;
import com.openshift.express.client.OpenShiftException;
import com.openshift.express.client.User;


/**
 * @author André Dietisheim
 */
public class Domain extends UserInfoAware implements IDomain {

	private String namespace;
	private String rhcDomain;

	public Domain(String namespace, User user) {
		this(namespace, null, user);
	}

	public Domain(String namespace, String rhcDomain, User user) {
		super(user);
		this.namespace = namespace;
		this.rhcDomain = rhcDomain;
	}

	public String getNamespace() {
		return namespace;
	}
	
	public String getRhcDomain() throws OpenShiftException {
		if (rhcDomain == null) {
			this.rhcDomain = getUserInfo().getRhcDomain();
		}
		return rhcDomain;
	}

	public void setNamespace(String namespace) throws OpenShiftException {
    	throw new UnsupportedOperationException();
//		InternalUser user = getInternalUser();
//		IDomain domain = service.changeDomain(namespace, user.getSshKey(), user);
//		update(domain);
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
		return getApplicationByName(name, getApplications());
	}

	public boolean hasApplication(String name) throws OpenShiftException {
		return getApplicationByName(name) != null;
	}

	private IApplication getApplicationByName(String name, Collection<IApplication> applications) {
		IApplication matchingApplication = null;
		for (IApplication application : applications) {
			if (name.equals(application.getName())) {
				matchingApplication = application;
				break;
			}
		}
		return matchingApplication;
	}

	public List<IApplication> getApplicationsByCartridge(ICartridge cartridge) throws OpenShiftException {
		List<IApplication> matchingApplications = new ArrayList<IApplication>();
		for (IApplication application : getApplications()) {
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

	public void destroy() throws OpenShiftException {
    	throw new UnsupportedOperationException();
    	//    	getInternalUser().destroyDomain();
    }

	public List<IApplication> getApplications() throws OpenShiftException {
    	throw new UnsupportedOperationException();
//		if (getUserInfo().getApplicationInfos().size() > applications.size()) {
//			update(getUserInfo().getApplicationInfos());
//		}
//		return Collections.unmodifiableList(applications);
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

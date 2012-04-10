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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.openshift.client.ApplicationLogReader;
import com.openshift.client.IApplication;
import com.openshift.client.ICartridge;
import com.openshift.client.IDomain;
import com.openshift.client.IEmbeddableCartridge;
import com.openshift.client.OpenShiftException;
import com.openshift.internal.client.response.unmarshalling.dto.Link;

/**
 * @author André Dietisheim
 */
public class Application extends AbstractOpenShiftResource implements IApplication {

	private static final String GIT_URI_PATTERN = "ssh://{0}@{1}-{2}.{3}/~/git/{1}.git/";
	private static final String APPLICATION_URL_PATTERN = "https://{0}-{1}.{2}/";
	private static final String DEFAULT_LOGREADER = "defaultLogReader";

	private final String uuid;
	private final String name;
	private final String creationTime;
	private final ICartridge cartridge;
	private final List<IEmbeddableCartridge> embeddedCartridges = new ArrayList<IEmbeddableCartridge>();
	private HashMap<String, ApplicationLogReader> logReaders = new HashMap<String, ApplicationLogReader>();
	//TODO : replace when pubsub/notification is available ?
	private final String creationLog;
	private final Domain domain;

	public Application(final String name, final String uuid, final String creationTime, final ICartridge cartridge,  
			final Map<String, Link> links, final Domain domain) {
		this(name, uuid, creationTime, null, cartridge, links, domain);
	}

	public Application(final String name, final String uuid, final String creationTime, final String creationLog, final ICartridge cartridge,
			final Map<String, Link> links, final Domain domain) {
		super(domain.getService(), links);
		this.name = name;
		this.uuid = uuid;
		this.creationTime = creationTime;
		this.creationLog = creationLog;
		this.cartridge = cartridge;
		this.domain = domain;
	}

	public String getName() {
		return name;
	}

	public String getUUID() throws OpenShiftException {
		return uuid;
	}

	public ICartridge getCartridge() {
		return cartridge;
	}

	public String getCreationTime() throws OpenShiftException {
		return creationTime;
	}

	public String getCreationLog() {
		return creationLog;
	}
	
	public IDomain getDomain() {
		return this.domain;
	}

	public void destroy() throws OpenShiftException {
		throw new UnsupportedOperationException();
//		getInternalUser().destroy(this);
	}

	public void start() throws OpenShiftException {
		throw new UnsupportedOperationException();
//		service.startApplication(name, cartridge, getInternalUser());
	}

	public void restart() throws OpenShiftException {
		throw new UnsupportedOperationException();
//		service.restartApplication(name, cartridge, getInternalUser());
	}

	public void stop() throws OpenShiftException {
		throw new UnsupportedOperationException();
//		service.stopApplication(name, cartridge, getInternalUser());
	}

	public ApplicationLogReader getLogReader() throws OpenShiftException {
		throw new UnsupportedOperationException();
//		ApplicationLogReader logReader = null;
//		if (logReaders.get(DEFAULT_LOGREADER) == null) {
//			logReader = new ApplicationLogReader(this, getInternalUser(), service);
//			logReaders.put(DEFAULT_LOGREADER, logReader);
//		}
//		return logReader;
	}

	public ApplicationLogReader getLogReader(String logFile) throws OpenShiftException {
		throw new UnsupportedOperationException();
//		ApplicationLogReader logReader = null;
//		if (logReaders.get(logFile) == null) {
//			logReader = new ApplicationLogReader(this, getInternalUser(), service, logFile);
//			logReaders.put(logFile, logReader);
//		}
//		return logReader;
	}

	public String getGitUri() throws OpenShiftException {
		throw new UnsupportedOperationException();
//		IDomain domain = getInternalUser().getDomain();
//		if (domain == null) {
//			return null;
//		}
//		return MessageFormat
//				.format(GIT_URI_PATTERN, getUUID(), getName(), domain.getNamespace(), domain.getRhcDomain());
	}

	public String getApplicationUrl() throws OpenShiftException {
		throw new UnsupportedOperationException();
//		IDomain domain = getInternalUser().getDomain();
//		if (domain == null) {
//			return null;
//		}
//		return MessageFormat.format(APPLICATION_URL_PATTERN, name, domain.getNamespace(), domain.getRhcDomain());
	}

	public String getHealthCheckUrl() throws OpenShiftException {
		throw new OpenShiftException("NOT SUPPORTED FOR GENERIC APPLICATION");
	}
	
	public String getHealthCheckResponse() throws OpenShiftException {
		throw new OpenShiftException("NOT SUPPORTED FOR GENERIC APPLICATION");
	}

	public void addEmbbedCartridge(IEmbeddableCartridge embeddedCartridge) throws OpenShiftException {
		throw new UnsupportedOperationException();
//		service.addEmbeddedCartridge(getName(), embeddedCartridge, getInternalUser());
//		Assert.isTrue(embeddedCartridge instanceof EmbeddableCartridge);
//		((EmbeddableCartridge) embeddedCartridge).setApplication(this);
//		this.embeddedCartridges.add(embeddedCartridge);
	}

	public void addEmbbedCartridges(List<IEmbeddableCartridge> embeddedCartridges) throws OpenShiftException {
		for (IEmbeddableCartridge cartridge : embeddedCartridges) {
			// TODO: catch exceptions when removing cartridges, contine removing
			// and report the exceptions that occurred<
			addEmbbedCartridge(cartridge);
		}
	}

	public void removeEmbbedCartridge(IEmbeddableCartridge embeddedCartridge) throws OpenShiftException {
		throw new UnsupportedOperationException();
//		if (!hasEmbeddedCartridge(embeddedCartridge.getName())) {
//			throw new OpenShiftException("There's no cartridge \"{0}\" embedded to the application \"{1}\"",
//					cartridge.getName(), getName());
//		}
//		service.removeEmbeddedCartridge(getName(), embeddedCartridge, getInternalUser());
//		embeddedCartridges.remove(embeddedCartridge);
	}

	public void removeEmbbedCartridges(List<IEmbeddableCartridge> embeddedCartridges) throws OpenShiftException {
		for (IEmbeddableCartridge cartridge : embeddedCartridges) {
			// TODO: catch exceptions when removing cartridges, contine removing
			// and report the exceptions that occurred<
			removeEmbbedCartridge(cartridge);
		}
	}

	public List<IEmbeddableCartridge> getEmbeddedCartridges() throws OpenShiftException {
		return embeddedCartridges;
	}

	public boolean hasEmbeddedCartridge(String cartridgeName) throws OpenShiftException {
		return getEmbeddedCartridge(cartridgeName) != null;
	}

	public IEmbeddableCartridge getEmbeddedCartridge(String cartridgeName) throws OpenShiftException {
		IEmbeddableCartridge embeddedCartridge = null;
		for (IEmbeddableCartridge cartridge : getEmbeddedCartridges()) {
			if (cartridgeName.equals(cartridge.getName())) {
				embeddedCartridge = cartridge;
				break;
			}
		}
		return embeddedCartridge;
	}

	public boolean waitForAccessible(long timeout) throws OpenShiftException {
		throw new UnsupportedOperationException();
//		return service.waitForApplication(getHealthCheckUrl(), timeout, getHealthCheckResponse());
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null)
			return false;
		if (getClass() != object.getClass())
			return false;
		Application other = (Application) object;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public String toString() {
		return name;
	}

}

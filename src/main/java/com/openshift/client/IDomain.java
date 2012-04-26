/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package com.openshift.client;

import java.net.SocketTimeoutException;
import java.util.List;

/**
 * @author André Dietisheim
 */
public interface IDomain {
	
	/**
	 * @return the domain id (formerly known as 'namespace'). A unique litteral identifier on OpenShift.
	 */
	public String getId();

	/**
	 * The domain suffix is the host part eg: 'rhcloud.com')
	 * @return
	 */
	public String getSuffix();
	
	
	/**
	 * Rename the current domain with the given id....
	 * @param id
	 * @throws OpenShiftException
	 * @throws SocketTimeoutException
	 */
	public void rename(String id) throws OpenShiftException, SocketTimeoutException;
	
	/**
	 * Returns the currently connected user that manages this domain.
	 * 
	 * @return
	 * @throws OpenShiftException 
	 * @throws SocketTimeoutException 
	 */
	public IUser getUser() throws SocketTimeoutException, OpenShiftException;

	
	/**
	 * Destroys the current domain. This method works only if it has not application.
	 * @throws OpenShiftException
	 * @throws SocketTimeoutException
	 */
	public void destroy() throws OpenShiftException, SocketTimeoutException;

	/**
	 * Destroys the current domain, using the 'force' parameter to also destroy the domain applications. The domain cannot
	 * be destroyed without setting 'force-true' if it still contains applications.
	 * 
	 * @param force
	 * @throws OpenShiftException
	 * @throws SocketTimeoutException
	 */
	public void destroy(final boolean force) throws OpenShiftException, SocketTimeoutException;

	/**
	 * Waits for the domain to become accessible. A domain is considered as accessible as soon as at least 1 application
	 * url in it resolves to a valid ip address.
	 * 
	 * @return boolean true if at least 1 application within this domain resolves
	 * @throws OpenShiftException
	 */
	public boolean waitForAccessible(long timeout) throws OpenShiftException;

	/**
	 * Creates a new application with the given name and the given cartridge/framework. Optionally, adds scalability and
	 * uses a specific nodeProfile, otherwise (ie, null values), uses default
	 * 
	 * @param name
	 * @param cartridge
	 * @param scale
	 *            or null (will use default on openshift, ie, false)
	 * @param nodeProfile
	 *            ("small", "micro", "medium", "large", "exlarge", "jumbo") or null (will use default on openshift, ie,
	 *            'small')
	 * @return
	 * @throws OpenShiftException
	 * @throws SocketTimeoutException 
	 */
	public IApplication createApplication(final String name, final ICartridge cartridge, final EnumApplicationScale scale, final String nodeProfile) throws OpenShiftException, SocketTimeoutException;

	public List<IApplication> getApplications() throws OpenShiftException, SocketTimeoutException;
	
	/**
	 * Returns the list of cartridges that can be used to create a new application.
	 * @return the list of cartridges that can be used to create a new application.
	 * @throws OpenShiftException
	 * @throws SocketTimeoutException
	 */
	public List<String> getAvailableCartridgeNames() throws OpenShiftException, SocketTimeoutException;

	/**
	 * Returns the list of node profiles that can be used to create a new application.
	 * @return the list of node profiles that can be used to create a new application.
	 * @throws OpenShiftException
	 * @throws SocketTimeoutException
	 */
	public List<String> getAvailableNodeProfiles() throws OpenShiftException, SocketTimeoutException;
	
	
	public IApplication getApplicationByName(String name) throws OpenShiftException, SocketTimeoutException;

	public boolean hasApplicationByName(String name) throws OpenShiftException, SocketTimeoutException;

	public List<IApplication> getApplicationsByCartridge(String cartridge) throws OpenShiftException;

	public boolean hasApplicationByCartridge(ICartridge cartridge) throws OpenShiftException;

}
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

	public void setNamespace(String namespace) throws OpenShiftException, SocketTimeoutException;

	// TODO : rename as 'name' to match the json messages ?
	public String getNamespace();

	// TODO: rename as 'suffix' to match the json messages ?
	public String getRhcDomain() throws OpenShiftException;

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

	public IApplication createApplication(String name, ICartridge cartridge) throws OpenShiftException;

	public List<IApplication> getApplications() throws OpenShiftException, SocketTimeoutException;

	public IApplication getApplicationByName(String name) throws OpenShiftException;

	public boolean hasApplication(String name) throws OpenShiftException;

	public List<IApplication> getApplicationsByCartridge(ICartridge cartridge) throws OpenShiftException;

	public boolean hasApplication(ICartridge cartridge) throws OpenShiftException;

}
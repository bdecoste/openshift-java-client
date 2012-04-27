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
package com.openshift.client;

import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.List;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * @author André Dietisheim
 */
public interface IApplication extends IOpenShiftResource {

	/**
	 * Returns the name of this application.
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * Returns the uuid of this application.
	 * 
	 * @return the uuid of this application.
	 */
	public String getUUID();

	/**
	 * Returns the uri at which the git repository of this application may be reached at.
	 * 
	 * @return the uri of the git repo of this application.
	 */
	public String getGitUrl();

	/**
	 * Returns the url at which this application may be reached at.
	 * 
	 * @return the url of this application.
	 */
	public String getApplicationUrl();

	/**
	 * Returns the url at which this application may be checked for its health state.
	 * 
	 * @return the url at which the health state may be queried.
	 */
	public String getHealthCheckUrl();

	/**
	 * Returns the cartridge (application type) that this app is running on.
	 * 
	 * @return the cartridge of this application
	 * 
	 */
	public ICartridge getCartridge();

	/**
	 * Adds the given embeddable cartridge to this application.
	 * 
	 * @param cartridge
	 * @throws OpenShiftException
	 * @throws SocketTimeoutException 
	 */
	public IEmbeddedCartridge addEmbeddableCartridge(IEmbeddableCartridge cartridge) throws OpenShiftException, SocketTimeoutException;

	/**
	 * Adds all given embedded cartridges from this app, given their names.
	 * 
	 * @param embeddedCartridges
	 * @throws OpenShiftException
	 * @throws SocketTimeoutException 
	 * 
	 * @see #addEmbeddableCartridge(IEmbeddedCartridge)
	 * @see #removeEmbeddedCartridge(IEmbeddedCartridge)
	 */
	public List<IEmbeddedCartridge> addEmbeddableCartridges(List<IEmbeddableCartridge> cartridge) throws OpenShiftException, SocketTimeoutException;

		/**
	 * Returns all embedded cartridges.
	 * 
	 * @return all embedded cartridges.
	 * @throws OpenShiftException
	 * @throws SocketTimeoutException 
	 * 
	 * @see IEmbeddedCartridge
	 * @see #addEmbeddableCartridge(IEmbeddedCartridge)
	 * @see #removeEmbeddedCartridge(IEmbeddedCartridge)
	 */
	public List<IEmbeddedCartridge> getEmbeddedCartridges() throws OpenShiftException, SocketTimeoutException;

	/**
	 * Returns <code>true</code> if this application has an embedded cartridge. Returns <code>false</code>
	 * otherwise.
	 * 
	 * @param the
	 *            name of the cartridge to look for
	 * @return true if there's an embedded cartridge with the given name
	 * @throws OpenShiftException
	 * @throws SocketTimeoutException 
	 * 
	 * @see IEmbeddedCartridge
	 * @see #addEmbeddableCartridge(IEmbeddedCartridge)
	 * @see #removeEmbeddedCartridge(IEmbeddedCartridge)
	 */
	public boolean hasEmbeddedCartridge(IEmbeddableCartridge cartridge) throws OpenShiftException, SocketTimeoutException;

	/**
	 * Returns <code>true</code> if this application has an embedded cartridge. Returns <code>false</code>
	 * otherwise.
	 * 
	 * @param the
	 *            name of the cartridge to look for
	 * @return true if there's an embedded cartridge with the given name
	 * @throws OpenShiftException
	 * @throws SocketTimeoutException 
	 * 
	 * @see IEmbeddedCartridge
	 * @see #addEmbeddableCartridge(IEmbeddedCartridge)
	 * @see #removeEmbeddedCartridge(IEmbeddedCartridge)
	 */
	public boolean hasEmbeddedCartridge(String cartridgeName) throws OpenShiftException, SocketTimeoutException;

	/**
	 * Returns the embedded cartridge given its name. Returns <code>null</code> if none was found.
	 * 
	 * @param cartridgeName
	 * @return the embedded cartridge with the given name
	 * @throws OpenShiftException
	 * @throws SocketTimeoutException 
	 */
	public IEmbeddedCartridge getEmbeddedCartridge(String cartridgeName) throws OpenShiftException, SocketTimeoutException;

	/**
	 * Returns the embedded cartridge. Returns <code>null</code> if none was found.
	 * 
	 * @param cartridge
	 * @return the embedded cartridge
	 * @throws OpenShiftException
	 * @throws SocketTimeoutException 
	 */
	public IEmbeddedCartridge getEmbeddedCartridge(IEmbeddableCartridge cartridge) throws OpenShiftException, SocketTimeoutException;

	/**
	 * Returns all gears.
	 * 
	 * @return all gears.
	 * @throws OpenShiftException
	 * @throws SocketTimeoutException 
	 * 
	 * @see IApplicationGear
	 */
	public List<IApplicationGear> getGears() throws OpenShiftException, SocketTimeoutException;

	/**
	 * Returns the timestamp at which this app was created.
	 * 
	 * @return the creation time
	 * 
	 * @throws OpenShiftException
	 */
	public Date getCreationTime();

	/**
	 * Destroys this application (and removes it from the list of available applications)
	 * 
	 * @throws OpenShiftException
	 * @throws SocketTimeoutException
	 * 
	 * @see IUser#getApplications()
	 */
	public void destroy() throws OpenShiftException, SocketTimeoutException;

	/**
	 * Starts this application. Has no effect if this app is already running.
	 * 
	 * @throws OpenShiftException
	 * @throws SocketTimeoutException
	 */
	public void start() throws OpenShiftException, SocketTimeoutException;

	/**
	 * Restarts this application.
	 * 
	 * @throws OpenShiftException
	 * @throws SocketTimeoutException
	 */
	public void restart() throws OpenShiftException, SocketTimeoutException;

	/**
	 * Stops this application.
	 * 
	 * @throws OpenShiftException
	 * @throws SocketTimeoutException
	 */
	public void stop() throws OpenShiftException, SocketTimeoutException;

	/**
	 * Stops this application
	 * 
	 * @param force
	 *            : true to force stop, false otherwise
	 * 
	 * @throws OpenShiftException
	 * @throws SocketTimeoutException
	 */
	public void stop(boolean force) throws OpenShiftException, SocketTimeoutException;

	/**
	 * Waits for this application to become accessible on its public url.
	 * 
	 * @param timeout
	 * @return
	 * @throws OpenShiftException
	 * 
	 * @see IApplication#getApplicationUrl()
	 */
	public boolean waitForAccessible(long timeout) throws OpenShiftException;

	/**
	 * Get the domain of the application.
	 * @return the domain
	 */
	public IDomain getDomain();

	/**
	 * Expose application port
	 * @throws OpenShiftException 
	 * @throws SocketTimeoutException 
	 */
	public void exposePort() throws SocketTimeoutException, OpenShiftException;

	/**
	 * Conceal application port
	 * @throws OpenShiftException 
	 * @throws SocketTimeoutException 
	 */
	public void concealPort() throws SocketTimeoutException, OpenShiftException;

	/**
	 * Show application port
	 * @throws OpenShiftException 
	 * @throws SocketTimeoutException 
	 */
	public void showPort() throws SocketTimeoutException, OpenShiftException;

	/**
	 * Scale down application
	 * @throws OpenShiftException 
	 * @throws SocketTimeoutException 
	 */
	public void scaleDown() throws SocketTimeoutException, OpenShiftException;

	/**
	 * Scale up application
	 * @throws OpenShiftException 
	 * @throws SocketTimeoutException 
	 */
	public void scaleUp() throws SocketTimeoutException, OpenShiftException;

	/**
	 * Add application alias
	 * @throws OpenShiftException 
	 * @throws SocketTimeoutException 
	 */
	public void addAlias(String string) throws SocketTimeoutException, OpenShiftException;

	/**
	 * Retrieve all application aliases
	 * @return application aliases in an unmodifiable collection
	 */
	public List<String> getAliases();

	public boolean hasAlias(String name);
	
	/**
	 * Remove application alias
	 * @throws OpenShiftException 
	 * @throws SocketTimeoutException 
	 */
	public void removeAlias(String alias) throws SocketTimeoutException, OpenShiftException;
	
	
	/**
	 * Refresh the application but reloading its content from OpenShift. 
	 * At the same time, this operation automatically set the embedded cartridges back to an 'unloaded' state.
	 *  
	 * @throws SocketTimeoutException
	 * @throws OpenShiftException
	 */
	public void refresh() throws SocketTimeoutException, OpenShiftException;
	
	/**
	 * Sets the SSH session that this application will use to connect to OpenShift to perform some operations. This SSH
	 * session must be initialized out of the library, since the user's SSH settings may depend on the runtime
	 * environment (Eclipse, etc.).
	 * 
	 * @param session the SSH session
	 */
	public void setSSHSession(Session session);
	
	/**
	 * Returns the SSH session that this application uses to connect to OpenShift.
	 * @return the SSH session that this application uses to connect to OpenShift.
	 */
	public Session getSSHSession();
	
	/**
	 * Returns true if the application was already provided with an SSH session, and this session is still valid (connected).
	 * @return true if the application was already provided with an SSH session, and this session is still valid (connected).
	 */
	public boolean hasSSHSession();
	
	/**
	 * Returns true if the port-forwarding has been started, false otherwise.
	 * @return true if the port-forwarding has been started, false otherwise.
	 * @throws OpenShiftSSHOperationException 
	 */
	public boolean isPortFowardingStarted() throws OpenShiftSSHOperationException;
	
	/**
	 * Returns the list of forwardable ports on OpenShift for this application.
	 * @return the list of forwardable ports on OpenShift for this application.
	 * @throws OpenShiftSSHOperationException 
	 */
	public List<IApplicationPortForwarding> getForwardablePorts() throws OpenShiftSSHOperationException;
	
	/**@
	 * Starts the port-forwarding for all ports.
	 * @return the list of forwardable ports on OpenShift for this application.
	 * @throws JSchException 
	 */
	public List<IApplicationPortForwarding> startPortForwarding() throws OpenShiftSSHOperationException;
	
	/**
	 * Stop the port-forwarding for all ports.
	 * @return the list of forwardable ports on OpenShift for this application.
	 * @throws OpenShiftSSHOperationException 
	 */
	public List<IApplicationPortForwarding> stopPortForwarding() throws OpenShiftSSHOperationException;

	/**
	 * Refreshes the list of port-forwarding. Started ones are kept as-is.
	 * @return the list of forwardable ports on OpenShift for this application.
	 * @throws OpenShiftSSHOperationException 
	 */
	public List<IApplicationPortForwarding> refreshForwardablePorts() throws OpenShiftSSHOperationException;
	
	

}
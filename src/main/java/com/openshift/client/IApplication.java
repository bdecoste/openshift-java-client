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
import java.util.List;

/**
 * @author André Dietisheim
 */
public interface IApplication {

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
	public String getGitUri();

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

	public String getHealthCheckResponse() throws OpenShiftException;

	/**
	 * Returns the cartridge (application type) that this app is running on.
	 * 
	 * @return the cartridge of this application
	 * 
	 */
	public String getCartridge();

	/**
	 * Adds the given embeddable cartridge to this app, given its name.
	 * 
	 * @param embeddedCartridge
	 * @throws OpenShiftException
	 * @throws SocketTimeoutException 
	 */
	public void addEmbeddableCartridge(String embeddedCartridgeName) throws OpenShiftException, SocketTimeoutException;

	/**
	 * Adds all given embeddable cartridges from this app, given their names.
	 * 
	 * @param embeddedCartridges
	 * @throws OpenShiftException
	 * @throws SocketTimeoutException 
	 * 
	 * @see #addEmbeddableCartridge(IEmbeddableCartridge)
	 * @see #removeEmbeddedCartridge(IEmbeddableCartridge)
	 */
	public void addEmbeddableCartridges(List<String> embeddedCartridgeNames) throws OpenShiftException, SocketTimeoutException;

	/**
	 * Removes the given cartridge from this app.
	 * 
	 * @param embeddedCartridge
	 * @throws OpenShiftException
	 * 
	 * @see IEmbeddableCartridge
	 * @see #addEmbeddableCartridge(IEmbeddableCartridge)
	 * @see #removeEmbeddedCartridge(IEmbeddableCartridge)
	 */
	public void removeEmbeddedCartridge(IEmbeddableCartridge embeddedCartridge) throws OpenShiftException;

	/**
	 * Removes all given cartridges from this app.
	 * 
	 * @param embeddedCartridges
	 *            all cartridges that shall be removed.
	 * @throws OpenShiftException
	 * 
	 * @see IEmbeddableCartridge
	 * @see #addEmbeddableCartridge(IEmbeddableCartridge)
	 * @see #removeEmbeddedCartridge(IEmbeddableCartridge)
	 */
	public void removeEmbeddedCartridges(List<IEmbeddableCartridge> embeddedCartridges) throws OpenShiftException;;

	/**
	 * Returns all embedded cartridges.
	 * 
	 * @return all embedded cartridges.
	 * @throws OpenShiftException
	 * @throws SocketTimeoutException 
	 * 
	 * @see IEmbeddableCartridge
	 * @see #addEmbeddableCartridge(IEmbeddableCartridge)
	 * @see #removeEmbeddedCartridge(IEmbeddableCartridge)
	 */
	public List<IEmbeddableCartridge> getEmbeddedCartridges() throws OpenShiftException, SocketTimeoutException;

	/**
	 * Returns <code>true</code> if this app has an embedded cartridge with the given name. Returns <code>false</code>
	 * otherwise.
	 * 
	 * @param the
	 *            name of the cartridge to look for
	 * @return true if there's an embedded cartridge with the given name
	 * @throws OpenShiftException
	 * @throws SocketTimeoutException 
	 * 
	 * @see IEmbeddableCartridge
	 * @see #addEmbeddableCartridge(IEmbeddableCartridge)
	 * @see #removeEmbeddedCartridge(IEmbeddableCartridge)
	 */
	public boolean hasEmbeddedCartridge(String cartridgeName) throws OpenShiftException, SocketTimeoutException;

	/**
	 * Returns the embedded cartridge with the given name. Returns <code>null</code> if none was found.
	 * 
	 * @param cartridgeName
	 * @return the embedded cartridge with the given name
	 * @throws OpenShiftException
	 * @throws SocketTimeoutException 
	 */
	public IEmbeddableCartridge getEmbeddedCartridge(String cartridgeName) throws OpenShiftException, SocketTimeoutException;

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
	public String getCreationTime() throws OpenShiftException;

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
	 * Gets the log that was created when the application was created.
	 * 
	 * @return the log which reported the creation of this app
	 */
	public String getCreationLog();

	/**
	 * Returns a reader that will allow you to read from the application log.
	 * 
	 * @return a reader that you can read the log from
	 * @throws OpenShiftException
	 * 
	 * @see ApplicationLogReader
	 */
	public ApplicationLogReader getLogReader() throws OpenShiftException;

	/**
	 * Returns a reader that will allow you to read from the application log.
	 * 
	 * @param logFile
	 *            the log file
	 * @return a reader that you can read the log from
	 * @throws OpenShiftException
	 * 
	 * @see ApplicationLogReader
	 */
	public ApplicationLogReader getLogReader(String logFile) throws OpenShiftException;

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

}
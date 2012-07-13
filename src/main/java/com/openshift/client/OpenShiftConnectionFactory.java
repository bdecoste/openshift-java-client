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
package com.openshift.client;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.openshift.client.configuration.IOpenShiftConfiguration;
import com.openshift.client.configuration.OpenShiftConfiguration;
import com.openshift.internal.client.AbstractOpenShiftConnectionFactory;
import com.openshift.internal.client.IRestService;
import com.openshift.internal.client.RestService;
import com.openshift.internal.client.httpclient.UrlConnectionHttpClientBuilder;

/**
 * Connection Factory, used to establish a connection and retrieve a user.
 * 
 * @author Xavier Coulon
 * @author Andre Dietisheim
 * 
 */
public class OpenShiftConnectionFactory extends AbstractOpenShiftConnectionFactory {

	/**
	 * Establish a connection with the clientId along with user's password. User's login and Server URL are retrieved
	 * from the local configuration file (in see $USER_HOME/.openshift/express.conf)
	 * 
	 * @param clientId
	 *            : http client id
	 * @param password
	 *            : user's password
	 * @return a valid connection
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws OpenShiftException
	 */
	public IOpenShiftConnection getConnection(final String clientId, final String password) throws OpenShiftException {
		IOpenShiftConfiguration configuration = null;
		try {
			configuration = new OpenShiftConfiguration();
		} catch (FileNotFoundException e) {
			throw new OpenShiftException(e, "Failed to load OpenShift configuration file.");
		} catch (IOException e) {
			throw new OpenShiftException(e, "Failed to load OpenShift configuration file.");
		}
		return getConnection(clientId, configuration.getRhlogin(), password, configuration.getLibraServer());
	}

	/**
	 * Establish a connection with the clientId along with user's login and password. Server URL is retrieved from the
	 * local configuration file (in see $USER_HOME/.openshift/express.conf)
	 * 
	 * @param clientId
	 *            : http client id
	 * @param login
	 *            : user's login
	 * @param password
	 *            : user's password
	 * @return a valid connection
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws OpenShiftException
	 */
	public IOpenShiftConnection getConnection(final String clientId, final String login, final String password)
			throws OpenShiftException {
		IOpenShiftConfiguration configuration;
		try {
			configuration = new OpenShiftConfiguration();
		} catch (FileNotFoundException e) {
			throw new OpenShiftException(e, "Failed to load OpenShift configuration file.");
		} catch (IOException e) {
			throw new OpenShiftException(e, "Failed to load OpenShift configuration file.");
		}
		return getConnection(clientId, login, password, configuration.getLibraServer());
	}

	/**
	 * Establish a connection with the clientId along with user's login and password.
	 * 
	 * @param clientId
	 *            : http client id
	 * @param login
	 *            : user's login.
	 * @param password
	 *            : user's password.
	 * @param serverUrl
	 *            : the server url.
	 * @return a valid connection
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws OpenShiftException
	 */
	public IOpenShiftConnection getConnection(final String clientId, final String login, final String password,
			final String serverUrl) throws OpenShiftException {
		try {
			final IHttpClient httpClient = new UrlConnectionHttpClientBuilder().setCredentials(login, password)
					.client();
			final IRestService service = new RestService(serverUrl, clientId, httpClient);
			return getConnection(service, login, password);
		} catch (FileNotFoundException e) {
			throw new OpenShiftException(e, "Failed to establish connection for user ''{0}}''", login);
		} catch (IOException e) {
			throw new OpenShiftException(e, "Failed to establish connection for user ''{0}}''", login);
		}
	}
}

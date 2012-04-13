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
import com.openshift.internal.client.IRestService;
import com.openshift.internal.client.RestService;
import com.openshift.internal.client.User;
import com.openshift.internal.client.httpclient.UrlConnectionHttpClientBuilder;

/**
 * User Builder, used to establish a connection and retrieve a user.
 * 
 * @author Xavier Coulon
 * @author Andre Dietisheim
 * 
 */
public class UserBuilder {

	protected IRestService service;

	public UserBuilder configure(final String clientId, final String password) throws FileNotFoundException, IOException, OpenShiftException {
		IOpenShiftConfiguration configuration = new OpenShiftConfiguration();
		return configure(clientId, configuration.getRhlogin(), password, configuration.getLibraServer());
	}

	public UserBuilder configure(final String clientId, final String login, final String password, final String serverUrl) throws FileNotFoundException, IOException, OpenShiftException {
		IHttpClient client = new UrlConnectionHttpClientBuilder().setCredentials(login, password).client();
		this.service = new RestService(serverUrl, clientId, client);
		return this;
	}

	
	public UserBuilder configure(IRestService service) {
		this.service = service;
		return this;
	}

	public IUser build() throws FileNotFoundException, IOException, OpenShiftException {
		return new User(service);
	}
}

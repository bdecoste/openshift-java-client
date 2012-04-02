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
package com.openshift.express.client;

import com.openshift.express.internal.client.InternalUser;
import com.openshift.express.internal.client.httpclient.UrlConnectionHttpClientBuilder;

/**
 * User Builder, used to establish a connection and retrieve a user.
 * 
 * @author Xavier Coulon
 *
 */
public class UserBuilder {
	
	/** the client that sends http request to openshift. */
	private IHttpClient client;
	
	/**
	 * Default constructor.
	 */
	public UserBuilder() {
		this(new UrlConnectionHttpClientBuilder().client());
	}
	
	/**
	 * Internal constructor.
	 * @param client
	 */
	protected UserBuilder(IHttpClient client) {
		this.client = client;
	}
	
	/**
	 * Builds the user from the given credentials.
	 *
	 * @param login the login
	 * @param password the password
	 * @return the i user
	 */
	public IUser build(String login, String password) {
		//FIXME: change service initialization, using RestService instead.
		IOpenShiftService service = null;
		return new InternalUser(login, password, service);
	}

}

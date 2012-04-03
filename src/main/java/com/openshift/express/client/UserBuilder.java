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

import java.io.FileNotFoundException;
import java.io.IOException;

import com.openshift.express.internal.client.IRestService;

/**
 * User Builder, used to establish a connection and retrieve a user.
 * 
 * @author Xavier Coulon
 * @author Andre Dietisheim
 * 
 */
public class UserBuilder {

	private String clientId;
	private IRestService service;
	private String login;
	private String password;

	public UserBuilder clientId(String clientId) {
		this.clientId = clientId;
		return this;
	}

	public ServiceUnawareUserBuilder credentials(String login, String password) {
		this.login = login;
		this.password = password;
		return new ServiceUnawareUserBuilder();
	}

	public ServiceAwareUserBuilder service(IRestService service) {
		this.service = service;
		return new ServiceAwareUserBuilder();
	}

	public class ServiceAwareUserBuilder extends AbstractUserBuilder {

		public IUser build() {
			return new User(service);
		}

		public ServiceAwareUserBuilder clientId(String clientId) {
			super.clientId(clientId);
			return this;
		}
	}

	public class ServiceUnawareUserBuilder extends AbstractUserBuilder {
		public IUser build() throws FileNotFoundException, IOException, OpenShiftException {
			return new User(login, password, clientId);
		}

		public ServiceUnawareUserBuilder clientId(String clientId) {
			super.clientId(clientId);
			return this;
		}
	}

	public abstract class AbstractUserBuilder {

		protected AbstractUserBuilder() {
			// inhibit instantiation
		}

		public AbstractUserBuilder clientId(String clientId) {
			UserBuilder.this.clientId = clientId;
			return this;
		}
	}

}

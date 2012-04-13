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
package com.openshift.client.utils;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.openshift.client.IHttpClient;
import com.openshift.client.IRestServiceTestConstants;
import com.openshift.client.OpenShiftException;
import com.openshift.client.UserBuilder;
import com.openshift.internal.client.RestService;
import com.openshift.internal.client.httpclient.UrlConnectionHttpClientBuilder;

/**
 * User Builder, used to establish a connection and retrieve a user.
 * 
 * @author Andre Dietisheim
 * 
 */
public class TestUserBuilder extends UserBuilder {

	private static String SYSPROP_PASSWORD = "password";
	
	public TestUserBuilder configure() throws FileNotFoundException, IOException, OpenShiftException {
		return (TestUserBuilder) configure(
				IRestServiceTestConstants.CLIENT_ID
				, System.getProperty(SYSPROP_PASSWORD));
	}
}

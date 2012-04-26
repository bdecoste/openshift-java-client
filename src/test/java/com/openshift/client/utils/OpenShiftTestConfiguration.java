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
package com.openshift.client.utils;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.openshift.client.OpenShiftException;
import com.openshift.client.configuration.OpenShiftConfiguration;

/**
 * @author André Dietisheim
 */
public class OpenShiftTestConfiguration extends OpenShiftConfiguration {

	public static final String CLIENT_ID = "openshift-java-client-rest-test";
	public static final String LIBRA_SERVER_STG = "http://stg.openshift.redhat.com";
	public static final String LIBRA_SERVER_PROD = "http://openshift.redhat.com";

	private static final String KEY_PASSWORD = "rhpassword";

	public OpenShiftTestConfiguration() throws FileNotFoundException, IOException, OpenShiftException {
		super();
	}
	
	public String getPassword() {
		return (String) System.getProperty(KEY_PASSWORD);
	}
	
	public String getClientId() {
		return CLIENT_ID;
	}
	
	public String getStagingServer() {
		return LIBRA_SERVER_STG;
	}

	public String getProductionServer() {
		return LIBRA_SERVER_PROD;
	}
}

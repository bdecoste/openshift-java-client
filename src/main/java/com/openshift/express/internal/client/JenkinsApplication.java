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
package com.openshift.express.internal.client;

import com.openshift.express.client.ICartridge;
import com.openshift.express.client.IJenkinsApplication;
import com.openshift.express.client.OpenShiftException;
import com.openshift.express.client.User;

/**
 * @author William DeCoste
 * @author Andre Dietisheim
 */
public class JenkinsApplication extends Application implements IJenkinsApplication {

	public JenkinsApplication(String name, String uuid, String creationLog, String healthCheckPath, ICartridge cartridge,
			User user, IRestService service) {
		super(name, uuid, creationLog, healthCheckPath, cartridge, user, service);
	}

	public JenkinsApplication(String name, String uuid, ICartridge cartridge, ApplicationInfo applicationInfo, User user,
			IRestService service) {
		super(name, uuid, cartridge, applicationInfo, user, service);
	}
	
	public String getHealthCheckUrl() throws OpenShiftException {
		return getApplicationUrl() + "login?from=%2F";
	}
	
	public String getHealthCheckResponse() throws OpenShiftException {
		return "<html>";
	}
}

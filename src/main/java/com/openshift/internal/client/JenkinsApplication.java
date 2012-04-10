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
package com.openshift.internal.client;

import java.util.Map;

import com.openshift.client.ICartridge;
import com.openshift.client.IJenkinsApplication;
import com.openshift.client.OpenShiftException;
import com.openshift.internal.client.response.unmarshalling.dto.Link;

/**
 * @author William DeCoste
 * @author Andre Dietisheim
 */
public class JenkinsApplication extends Application implements IJenkinsApplication {

	public JenkinsApplication(String name, String uuid, String creationTime, ICartridge cartridge,
			Map<String, Link> links, Domain domain) {
		super(name, uuid, creationTime, cartridge, links, domain);
		// TODO Auto-generated constructor stub
	}

	public JenkinsApplication(String name, String uuid, String creationTime, String creationLog, ICartridge cartridge,
			Map<String, Link> links, Domain domain) {
		super(name, uuid, creationTime, creationLog, cartridge, links, domain);
		// TODO Auto-generated constructor stub
	}

	public String getHealthCheckUrl() throws OpenShiftException {
		return getApplicationUrl() + "login?from=%2F";
	}
	
	public String getHealthCheckResponse() throws OpenShiftException {
		return "<html>";
	}
}

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
package com.openshift.internal.client.response.unmarshalling;

import org.jboss.dmr.ModelNode;

import com.openshift.client.HAProxyCartridge;
import com.openshift.client.IApplication;
import com.openshift.client.ICartridge;
import com.openshift.client.IUser;
import com.openshift.client.JBossCartridge;
import com.openshift.client.JenkinsCartridge;
import com.openshift.client.NodeJSCartridge;
import com.openshift.client.PHPCartridge;
import com.openshift.client.PerlCartridge;
import com.openshift.client.PythonCartridge;
import com.openshift.client.RawCartridge;
import com.openshift.client.RubyCartridge;
import com.openshift.internal.client.Application;
import com.openshift.internal.client.HAProxyApplication;
import com.openshift.internal.client.IRestService;
import com.openshift.internal.client.JBossASApplication;
import com.openshift.internal.client.JenkinsApplication;
import com.openshift.internal.client.NodeJSApplication;
import com.openshift.internal.client.PHPApplication;
import com.openshift.internal.client.PerlApplication;
import com.openshift.internal.client.PythonApplication;
import com.openshift.internal.client.RawApplication;
import com.openshift.internal.client.RubyApplication;
import com.openshift.internal.client.User;
import com.openshift.internal.client.utils.IOpenShiftJsonConstants;

/**
 * @author André Dietisheim
 */
public class ApplicationResponseUnmarshaller extends AbstractOpenShiftJsonResponseUnmarshaller<IApplication> {

	protected final User user;
	protected final String applicationName;
	protected final ICartridge cartridge;
	protected final IRestService service;

	public ApplicationResponseUnmarshaller(final String applicationName, final ICartridge cartridge, final IUser user,
			final IRestService service) {
		this.applicationName = applicationName;
		this.cartridge = cartridge;
		this.user = (User) user;
		this.service = service;
	}

	protected IApplication createOpenShiftObject(ModelNode node) {
		String creationLog = getString(IOpenShiftJsonConstants.PROPERTY_RESULT, node);
		String healthCheckPath = getDataNodeProperty(IOpenShiftJsonConstants.PROPERTY_HEALTH_CHECK_PATH, node);
		String uuid = getDataNodeProperty(IOpenShiftJsonConstants.PROPERTY_UUID, node);
		
		if (cartridge instanceof JBossCartridge) {
			return new JBossASApplication(applicationName, uuid, creationLog, healthCheckPath, cartridge, user, service);
		} else if (cartridge instanceof RubyCartridge) {
			return new RubyApplication(applicationName, uuid, creationLog, healthCheckPath, cartridge, user, service);
		} else {
			return new Application(applicationName, uuid, creationLog, healthCheckPath, cartridge, user, service);
		}
	}
}

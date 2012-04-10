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

import com.openshift.client.IApplication;
import com.openshift.client.ICartridge;
import com.openshift.client.IUser;
import com.openshift.client.JBossCartridge;
import com.openshift.client.RubyCartridge;
import com.openshift.internal.client.IRestService;
import com.openshift.internal.client.JBossASApplication;
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
			return null;//new JBossASApplication(applicationName, uuid, creationLog, healthCheckPath, cartridge, user, service);
		} else if (cartridge instanceof RubyCartridge) {
			return null;//new RubyApplication(applicationName, uuid, creationLog, healthCheckPath, cartridge, user, service);
		} else {
			throw new UnsupportedOperationException();
//			return new Application(applicationName, uuid, creationLog, healthCheckPath, cartridge, user, service);
		}
	}
}

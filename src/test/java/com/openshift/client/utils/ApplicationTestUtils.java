/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package com.openshift.client.utils;

import java.net.SocketTimeoutException;
import java.util.Iterator;

import com.openshift.client.IApplication;
import com.openshift.client.ICartridge;
import com.openshift.client.IDomain;
import com.openshift.client.IOpenShiftService;
import com.openshift.client.OpenShiftException;
import com.openshift.internal.client.UserResource;

/**
 * @author André Dietisheim
 */
public class ApplicationTestUtils {

	public static String createRandomApplicationName() {
		return String.valueOf(System.currentTimeMillis());
	}

	@Deprecated
	public static void silentlyDestroyAS7Application(String name, UserResource user, IOpenShiftService service) {
		silentlyDestroyApplication(name, ICartridge.JBOSSAS_7, user, service);
	}
	
	@Deprecated
	public static void silentlyDestroyRubyApplication(String name, UserResource user, IOpenShiftService service) {
		silentlyDestroyApplication(name, ICartridge.RUBY_18, user, service);
	}

	@Deprecated
	public static void silentlyDestroyJenkinsApplication(String name, UserResource user, IOpenShiftService service) {
		silentlyDestroyApplication(name, ICartridge.JENKINS_14, user, service);
	}

	public static void silentlyDestroyApplication(String name, ICartridge cartridge, UserResource user,
			IOpenShiftService service) {
		try {
			if (name == null) {
				return;
			}
			service.destroyApplication(name, cartridge, user);
		} catch (OpenShiftException e) {
			e.printStackTrace();
		}
	}

	public static void silentlyDestroyAnyJenkinsApplication(UserResource user) {
		throw new UnsupportedOperationException();
//		try {
//			for (IApplication application : user.getApplicationsByCartridge(ICartridge.JENKINS_14)) {
//				application.destroy();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	public static void silentlyDestroyAllApplications(UserResource user) {
		throw new UnsupportedOperationException();
//		try {
//			for (IApplication application : user.getApplications()) {
//				application.destroy();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	
	public static IApplication getOrCreateApplication(IDomain domain) throws SocketTimeoutException, OpenShiftException {
		Iterator<IApplication> applicationIterator = domain.getApplications().iterator();
		if (applicationIterator.hasNext()) {
			return applicationIterator.next();
		}
		
		return domain.createApplication(StringUtils.createRandomString(), "jbossas-7", null, null);
	}
}

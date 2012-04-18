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

import java.net.SocketTimeoutException;
import java.util.Iterator;

import com.openshift.client.IApplication;
import com.openshift.client.IDomain;
import com.openshift.client.OpenShiftException;
import com.openshift.internal.client.Cartridge;

/**
 * @author André Dietisheim
 */
public class ApplicationTestUtils {

	public static String createRandomApplicationName() {
		return String.valueOf(System.currentTimeMillis());
	}

	public static void silentlyDestroyApplication(IApplication application) {
		try {
			if (application == null) {
				return;
			}
			application.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void silentlyDestroyAllApplications(IDomain domain) {
		try {
			for (IApplication application : domain.getApplications()) {
				application.destroy();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static IApplication getOrCreateApplication(IDomain domain) throws SocketTimeoutException, OpenShiftException {
		Iterator<IApplication> applicationIterator = domain.getApplications().iterator();
		if (applicationIterator.hasNext()) {
			return applicationIterator.next();
		}
		
		return domain.createApplication(StringUtils.createRandomString(), new Cartridge("jbossas-7"), null, null);
	}
}

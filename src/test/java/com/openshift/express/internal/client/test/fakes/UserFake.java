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
package com.openshift.express.internal.client.test.fakes;

import java.io.IOException;

import com.openshift.express.client.IApplication;
import com.openshift.express.client.OpenShiftException;
import com.openshift.express.client.User;
import com.openshift.express.internal.client.IRestService;

/**
 * @author André Dietisheim
 */
public class UserFake extends User {

	public UserFake(IRestService service) throws OpenShiftException, IOException {
		super(service);
	}

	public void add(IApplication application) {
		throw new UnsupportedOperationException();
//		super.add(application);
	}
}

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

import com.openshift.client.IDomain;
import com.openshift.client.IUser;
import com.openshift.internal.client.DomainResource;
import com.openshift.internal.client.UserResource;

/**
 * @author André Dietisheim
 */
public class DomainResponseUnmarshaller extends AbstractOpenShiftJsonResponseUnmarshaller<IDomain> {

	private final String domainName;
	private final UserResource user;
	
	public DomainResponseUnmarshaller(final String domainName, final IUser user) {
		this.domainName = domainName;
		this.user = (UserResource) user;
	}

	protected IDomain createOpenShiftObject(final ModelNode node) {
		return null; //new Domain(domainName, user);
	}
}

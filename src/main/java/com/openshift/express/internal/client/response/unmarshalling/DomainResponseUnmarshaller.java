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
package com.openshift.express.internal.client.response.unmarshalling;

import org.jboss.dmr.ModelNode;

import com.openshift.express.client.IDomain;
import com.openshift.express.client.IUser;
import com.openshift.express.internal.client.Domain;
import com.openshift.express.internal.client.User;

/**
 * @author André Dietisheim
 */
public class DomainResponseUnmarshaller extends AbstractOpenShiftJsonResponseUnmarshaller<IDomain> {

	private final String domainName;
	private final User user;
	
	public DomainResponseUnmarshaller(final String domainName, final IUser user) {
		this.domainName = domainName;
		this.user = (User) user;
	}

	protected IDomain createOpenShiftObject(final ModelNode node) {
		return new Domain(domainName, user);
	}
}

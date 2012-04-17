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
package com.openshift.client;

import java.net.SocketTimeoutException;

/**
 * @author Xavier Coulon
 *
 */
public interface IOpenShiftConnection {

	/**
	 * Returns the user associated with the current OpenShift connection.
	 * @return the user
	 * @throws OpenShiftException
	 * @throws SocketTimeoutException
	 */
	public IUser getUser() throws OpenShiftException, SocketTimeoutException;
}

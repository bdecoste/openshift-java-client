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
package com.openshift.express.internal.client.response.unmarshalling.dto;

import com.openshift.express.client.OpenShiftRequestParameterException;


/**
 * @author Andre Dietisheim
 */
public enum LinkParameterType {
	STRING, BOOLEAN;
	
	public static LinkParameterType valueOfIgnoreCase(String name) throws OpenShiftRequestParameterException {
		if (name == null) {
			throw new OpenShiftRequestParameterException("Unknow request parameter type 'null");
		}
		try {
			return			valueOf(name.toUpperCase());
		} catch(IllegalArgumentException e) {
			throw new OpenShiftRequestParameterException("Unknow request parameter type {0}", name);
		}
	}
}

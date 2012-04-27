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

/**
 * @author Andre Dietisheim
 */
public enum GearProfile {

	JUMBO, EXLARGE, LARGE, MEDIUM, MICRO, SMALL;
	
	public static GearProfile safeValueOf(String gearProfile) {
		try {
			if (gearProfile == null) {
				return null;
			}
			return valueOf(gearProfile.toUpperCase());
		} catch(IllegalArgumentException e) {
			return null;
		}
	}
	
	public String getValue() {
		return name().toLowerCase();
	}
}

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
package com.openshift.client;

import java.net.SocketTimeoutException;


/**
 * @author André Dietisheim
 */
public interface IEmbeddedCartridge {

	@Deprecated
	public static final IEmbeddedCartridge PHPMYADMIN_34 = null;//new EmbeddableCartridge("phpmyadmin-3.4");
	@Deprecated
	public static final IEmbeddedCartridge MYSQL_51 = null;//new EmbeddableCartridge("mysql-5.1");
	@Deprecated
	public static final IEmbeddedCartridge JENKINS_14 = null;//new EmbeddableCartridge("jenkins-client-1.4");
	@Deprecated
	public static final IEmbeddedCartridge METRICS_01 = null;//new EmbeddableCartridge("metrics-0.1");

	public String getName();

	public String getUrl() throws OpenShiftException;
	
	public String getCreationLog();
	
	public void setCreationLog(String creationLog);
	
	/**
	 * Destroys this cartridge (and removes it from the list of existing cartridges)
	 * 
	 * @throws OpenShiftException
	 * @throws SocketTimeoutException
	 */
	public void destroy() throws OpenShiftException, SocketTimeoutException;


}
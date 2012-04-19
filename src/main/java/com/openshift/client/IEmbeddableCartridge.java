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

import com.openshift.internal.client.EmbeddableCartridge;

/**
 * @author Xavier Coulon
 *
 */
public interface IEmbeddableCartridge {
	
	public static final EmbeddableCartridge PHPMYADMIN_34 = new EmbeddableCartridge("phpmyadmin-3.4");

	public static final EmbeddableCartridge MYSQL_51 = new EmbeddableCartridge("mysql-5.1");

	public static final EmbeddableCartridge JENKINS_14 = new EmbeddableCartridge("jenkins-client-1.4");

	public static final EmbeddableCartridge METRICS_01 = new EmbeddableCartridge("metrics-0.1");

	public String getName();

	

}

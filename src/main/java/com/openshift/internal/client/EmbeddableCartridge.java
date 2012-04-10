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
package com.openshift.internal.client;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

import com.openshift.client.Cartridge;
import com.openshift.client.ICartridge;
import com.openshift.client.IEmbeddableCartridge;
import com.openshift.client.IOpenShiftService;
import com.openshift.client.IUser;
import com.openshift.client.OpenShiftException;

/**
 * A cartridge that may be embedded into an application. This class is no enum
 * since we dont know all available types and they may change at any time.
 * 
 * @author André Dietisheim
 */
public class EmbeddableCartridge extends Cartridge implements IEmbeddableCartridge {
	
	protected static final String JENKINS_CLIENT = "jenkins-client";
	protected static final String MYSQL = "mysql";
	protected static final String PHPMYADMIN = "phpmyadmin";
	protected static final String METRICS = "metrics";
	protected static final String POSTGRES = "postgresql";
	protected static final String MONGO = "mongodb";
	protected static final String ROCKMONGO = "rockmongo";
	protected static final String CRON = "cron";
	protected static final String GEN_MMS_AGENT = "10gen-mms-agent";

	private String creationLog;
	private String url;
	private Application application;
	
	public EmbeddableCartridge(IOpenShiftService service, IUser user) {
		super(service, user);
	}
	
	public EmbeddableCartridge(IOpenShiftService service, IUser user, Application application) {
		super(service, user);
		this.application = application;
	}
	
	public EmbeddableCartridge(IOpenShiftService service, IUser user, String url) {
		super(service, user);
		this.url = url;
	}
	
	public EmbeddableCartridge(IOpenShiftService service, IUser user, String url, Application application) {
		super(service, user);
		this.url = url;
		this.application = application;
	}


	public EmbeddableCartridge(String name) {
		this(name, (Application) null);
	}

	public EmbeddableCartridge(String name, Application application) {
		super(name);
		this.application = application;
	}

	public EmbeddableCartridge(final String name, final String info, final Application application) {
		this(name, application);
		this.url = info;
	}

	public String getUrl() throws OpenShiftException {
		return url;
	}

	protected void update(EmbeddableCartridgeInfo cartridgeInfo) throws OpenShiftException {
		setName(cartridgeInfo.getName());
		this.url = cartridgeInfo.getUrl();
	}
	
	public void setCreationLog(String creationLog) {
		this.creationLog = creationLog;
	}

	public String getCreationLog() {
		return creationLog;
	}
	
	
	
	

}
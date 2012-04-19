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

import java.net.SocketTimeoutException;
import java.util.Map;

import com.openshift.client.IEmbeddedCartridge;
import com.openshift.client.OpenShiftException;
import com.openshift.internal.client.response.Link;

/**
 * A cartridge that may be embedded into an application. This class is no enum
 * since we dont know all available types and they may change at any time.
 * 
 * @author André Dietisheim
 */
public class EmbeddedCartridgeResource extends AbstractOpenShiftResource implements IEmbeddedCartridge {
	
	public static final String EMBEDDED_TYPE = "embedded";
	
	protected static final String JENKINS_CLIENT = "jenkins-client";
	protected static final String MYSQL = "mysql";
	protected static final String PHPMYADMIN = "phpmyadmin";
	protected static final String METRICS = "metrics";
	protected static final String POSTGRES = "postgresql";
	protected static final String MONGO = "mongodb";
	protected static final String ROCKMONGO = "rockmongo";
	protected static final String CRON = "cron";
	protected static final String GEN_MMS_AGENT = "10gen-mms-agent";

	private static final String LINK_DELETE_CARTRIDGE = "DELETE";

	private final String name;
	private final String info; // not supported yet
	private final String type;
	private String creationLog;
	private String url;
	private final ApplicationResource application;
	
	public EmbeddedCartridgeResource(final String name, final String type, final Map<String, Link> links, final ApplicationResource application) {
		super(application.getService(), links);
		this.name = name;
		this.type = type;
		this.info = null; // FIXME: see bugzilla 
		this.application = application;
	}
	
	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * @return the type
	 */
	protected final String getType() {
		return type;
	}

	/**
	 * @return the info
	 */
	public final String getInfo() {
		return info;
	}

	/**
	 * @return the application
	 */
	public final ApplicationResource getApplication() {
		return application;
	}

	public String getUrl() throws OpenShiftException {
		return url;
	}

	public void setCreationLog(String creationLog) {
		this.creationLog = creationLog;
	}

	public String getCreationLog() {
		return creationLog;
	}
	
	/* (non-Javadoc)
	 * @see com.openshift.client.IApplication#destroy()
	 */
	public void destroy() throws OpenShiftException, SocketTimeoutException {
		new DeleteCartridgeRequest().execute();
		application.removeEmbeddedCartridge(this);
	}
	
	/**
	 * The Class DeleteApplicationRequest.
	 */
	private class DeleteCartridgeRequest extends ServiceRequest {

		/**
		 * Instantiates a new delete application request.
		 */
		protected DeleteCartridgeRequest() {
			super(LINK_DELETE_CARTRIDGE);
		}

	}


	
	

}
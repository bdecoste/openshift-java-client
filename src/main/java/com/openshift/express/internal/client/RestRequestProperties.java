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
package com.openshift.express.internal.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.openshift.express.internal.client.utils.StreamUtils;

/**
 * @author Andre Dietisheim
 */
public class RestRequestProperties {

	private static final String PROPERTIES_FILE = "restrequest.properties";

	private static final String KEY_USERAGENT = "useragent";
	private static final String KEY_VERSION = "version";
	private static final String KEY_CLIENTID = "clientid";
	
	private String version;
	private String userAgent;
	private String clientId;

	private Properties properties;

	public String getVersion() {
		if (version == null) {
			userAgent = getStringProperty(KEY_VERSION);
		}
		return version;
	}

	private String getStringProperty(String key) {
		try {
			return getProperties().getProperty(key);
		} catch (IOException e) {
			return "Unknown";
		}
	}

	public String getUseragent() {
		if (userAgent == null) {
			userAgent = getStringProperty(KEY_USERAGENT);
		}

		return userAgent;
	}
	
	public String getClientId() {
		if (clientId == null) {
			clientId = getStringProperty(KEY_CLIENTID);
		}

		return clientId;
	}
	
	private Properties getProperties() throws IOException {
		if (properties == null) {
			InputStream in = null;
			try {
				properties = new Properties();
				in = getClass().getResourceAsStream("/" + PROPERTIES_FILE);
				properties.load(in);
			} finally {
				StreamUtils.quietlyClose(in);
			}
		}
		return properties;
	}
}

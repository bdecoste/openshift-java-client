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
package com.openshift.internal.client.httpclient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.openshift.client.IHttpClient;

/**
 * @author Andre Dietisheim
 */
public class HttpParameters {

	private static final String UTF8 = "UTF-8";

	private Map<String, Object> parameters;

	public HttpParameters() {
		this.parameters = new HashMap<String, Object>();
	}

	protected HttpParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}


	protected HttpParameters put(String key, Object value) {
		parameters.put(key, value);
		return this;
	}
	
	public boolean containsKey(String key) {
		return parameters.containsKey(key);
	}
		
	public Object get(String key) {
		return parameters.get(key); 
	}

	public String toUrlEncoded() throws UnsupportedEncodingException {
		if (parameters == null) {
			return null;
		}
		StringBuilder builder = new StringBuilder();
		for (Entry<String, Object> entry : parameters.entrySet()) {
			append(entry.getKey(), URLEncoder.encode(String.valueOf(entry.getValue()), UTF8), builder);
		}
		return builder.toString();
	}

	private void append(String name, Object value, StringBuilder builder) {
		if (builder.length() > 0) {
			builder.append(IHttpClient.AMPERSAND);
		}
		builder.append(name)
				.append(IHttpClient.EQUALS)
				.append(value.toString());
	}
}

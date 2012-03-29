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
package com.openshift.express.internal.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Andre Dietisheim
 */
public class HttpParameters {

	private static final char AMPERSAND = '&';
	private static final char EQUALS = '=';
	private static final String UTF8 = "UTF-8";

	private Map<String, Object> parameters = new LinkedHashMap<String, Object>();

	public HttpParameters put(String key, Object value) {
		parameters.put(key, value);
		return this;
	}

	public String toUrlEncoded() throws UnsupportedEncodingException {
		StringBuilder builder = new StringBuilder();
		for (Entry<String, Object> entry : parameters.entrySet()) {
			append(entry.getKey(), entry.getValue(), builder);
		}
		return URLEncoder.encode(builder.toString(), UTF8);
	}

	private void append(String name, Object value, StringBuilder builder) {
		if (builder.length() > 0) {
			builder.append(AMPERSAND);
		}
		builder.append(name)
				.append(EQUALS)
				.append(value.toString());
	}
}

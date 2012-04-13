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
package com.openshift.client.utils;

import static org.mockito.Matchers.argThat;

import java.net.URL;

import org.mockito.ArgumentMatcher;

/**
 * Custom Mockito Matcher that verifies that the given URL ends with a given suffix.
 * 
 * @author Xavier Coulon
 * 
 */
public class UrlEndsWithMatcher extends ArgumentMatcher<URL> {

	private final String urlSuffix;

	public UrlEndsWithMatcher(final String urlSuffix) {
		this.urlSuffix = urlSuffix;
	}

	@Override
	public boolean matches(Object argument) {
		if(argument == null) {
			return false;
		}
		URL url = (URL) argument;
		return url.toExternalForm().endsWith(urlSuffix);
	}
	
	/** More friendly way to call the {@link UrlEndsWithMatcher}. */
	public static URL urlEndsWith(String suffix) {
		return argThat(new UrlEndsWithMatcher(suffix));
	}

}
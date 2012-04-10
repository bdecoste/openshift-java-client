package com.openshift.client.utils;

import static org.mockito.Matchers.argThat;

import java.net.URL;

import org.mockito.ArgumentMatcher;

public class CustomArgumentMatchers {

	/** More friendly way to call the {@link UrlEndsWithMatcher}. */
	public static URL urlEndsWith(String suffix) {
		return argThat(new UrlEndsWithMatcher(suffix));
	}

	/**
	 * Custom Mockito Matcher that verifies that the given URL ends with a given suffix.
	 * 
	 * @author Xavier Coulon
	 * 
	 */
	static class UrlEndsWithMatcher extends ArgumentMatcher<URL> {

		private final String urlSuffix;

		public UrlEndsWithMatcher(final String urlSuffix) {
			this.urlSuffix = urlSuffix;
		}

		@Override
		public boolean matches(Object argument) {
			if (argument == null) {
				return false;
			}
			URL url = (URL) argument;
			return url.toExternalForm().endsWith(urlSuffix);
		}
	}

}
package com.openshift.client.utils;

import static org.mockito.Matchers.argThat;

import java.net.URL;

import org.mockito.ArgumentMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.openshift.client.DomainResourceTest;

public class CustomArgumentMatchers {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomArgumentMatchers.class);
	
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
			final boolean match = url.toExternalForm().endsWith(urlSuffix);
			if(match) {
				LOGGER.info("Matching {}", urlSuffix);
			}
			return match;
		}
	}

}
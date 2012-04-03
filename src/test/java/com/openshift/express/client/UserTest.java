package com.openshift.express.client;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import com.openshift.express.internal.client.RestService;
import com.openshift.express.internal.client.httpclient.HttpClientException;

public class UserTest {

	private static final String CLIENT_ID = "openshift-java-client-rest-test";

	private IUser user;
	private IHttpClient clientMock;
	private IHttpClient mockClient;

	/**
	 * Custom Mockito Matcher that verifies that the given URL ends with a given suffix.
	 * 
	 * @author Xavier Coulon
	 * 
	 */
	class UrlEndsWithMatcher extends ArgumentMatcher<URL> {

		private final String urlSuffix;

		public UrlEndsWithMatcher(final String urlSuffix) {
			this.urlSuffix = urlSuffix;
		}

		@Override
		public boolean matches(Object argument) {
			URL url = (URL) argument;
			return url.toExternalForm().endsWith(urlSuffix);
		}
	}

	/** More friendly way to call the {@link UrlEndsWithMatcher}. */
	private URL urlEndsWith(String suffix) {
		return argThat(new UrlEndsWithMatcher(suffix));
	}

	/** Convenient method to retrieve the content of a file as a String. */
	private String getContentAsString(String fileName) {
		String content = null;
		try {
			final InputStream contentStream = getClass().getResourceAsStream("/samples/" + fileName);
			content = IOUtils.toString(contentStream);
		} catch (Throwable e) {
			e.printStackTrace();
			fail("Failed to obtain file content: " + e.getMessage());
		}
		return content;
	}

	@Before
	public void setup() throws HttpClientException, FileNotFoundException, IOException, OpenShiftException {
		mockClient = mock(IHttpClient.class);
		when(mockClient.get(urlEndsWith("/broker/rest/api"))).thenReturn(getContentAsString("get-rest-api.json"));
		this.user = new UserBuilder()
		.configure(new RestService(clientMock))
		.build();
	}
	
	@Test
	public void shouldLoadEmptyListOfDomains() throws OpenShiftException, SocketTimeoutException, HttpClientException {
		// pre-conditions
		when(mockClient.get(urlEndsWith("/broker/domains"))).thenReturn(getContentAsString("get-domains-noexisting.json"));
		// operation
		final List<IDomain> domains = user.getDomains();
		// verifications
		assertThat(domains).hasSize(0);
		verify(mockClient, times(2)).get(any(URL.class));
	}
	
	@Test
	public void shouldLoadSingleUserDomain() throws OpenShiftException, SocketTimeoutException, HttpClientException {
		// pre-conditions
		when(mockClient.get(urlEndsWith("/broker/domains"))).thenReturn(getContentAsString("get-domains-1existing.json"));
		// operation
		final List<IDomain> domains = user.getDomains();
		// verifications
		assertThat(domains).hasSize(1);
		verify(mockClient, times(2)).get(any(URL.class));
	}
	
	@Test
	public void shouldLoadEmptyListOfApplications() throws OpenShiftException, SocketTimeoutException, HttpClientException {
		// pre-conditions
		when(mockClient.get(urlEndsWith("/broker/domains"))).thenReturn(getContentAsString("get-domains-1existing.json"));
		when(mockClient.get(urlEndsWith("/broker/domains/foobar/applications"))).thenReturn(getContentAsString("get-applications-with2apps.json"));
		// operation
		final List<IApplication> applications = user.getDomains().get(0).getApplications();
		// verifications
		assertThat(applications).hasSize(2);
		verify(mockClient, times(2)).get(any(URL.class));
	}

}

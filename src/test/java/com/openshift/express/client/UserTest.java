package com.openshift.express.client;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import com.openshift.express.internal.client.httpclient.HttpClientException;

public class UserTest {
	
	private IUser user;
	
	private IHttpClient mockClient;
	
	@Before
	public void setup() {
		UserBuilder builder = new UserBuilder(mockClient);
		this.user = builder.build("login", "password");
	}
	
	private String getContentAsString(String fileName) throws IOException {
		final InputStream contentStream = getClass()
				.getResourceAsStream("/samples/" + fileName);
		return IOUtils.toString(contentStream);
	}
	
	@Test 
	public void shouldLoadSingleUserDomain() throws IOException, HttpClientException, OpenShiftException {
		// pre-conditions
		String response = getContentAsString("get-domains-1existing.json");
		when(mockClient.get(any(URL.class))).thenReturn(response);
		// operation
		final List<IDomain> domains = user.getDomains();
		// verifications
		assertThat(domains).hasSize(1);
		//verify(mockClient.get(any(URL.class))).times(1);
		
	}

}

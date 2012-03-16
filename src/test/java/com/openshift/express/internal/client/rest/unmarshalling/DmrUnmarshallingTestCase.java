package com.openshift.express.internal.client.rest.unmarshalling;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.openshift.express.client.OpenShiftException;
import com.openshift.express.internal.client.response.unmarshalling.dto.DomainsDTO;
import com.openshift.express.internal.client.response.unmarshalling.dto.ObjectFactory;

public class DmrUnmarshallingTestCase {

	private String getContentAsString(String fileName) throws IOException {
		final InputStream contentStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("samples/" + fileName);
		return IOUtils.toString(contentStream);
	}
	
	@Test
	public void shouldUnmarshallGetDomainsWith1ExistingResponseBody() throws OpenShiftException, IOException {
		//pre-conditions
		String content = getContentAsString("get-domains-1existing.json");
		assertNotNull(content);
		// operation
		DomainsDTO domains = ObjectFactory.get(content, DomainsDTO.class);
		// verifications
		assertNotNull(domains);
		assertThat(domains.getDomains()).isNotEmpty();
	}
}

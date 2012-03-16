package com.openshift.express.internal.client.rest.unmarshalling;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.openshift.express.client.OpenShiftException;
import com.openshift.express.internal.client.response.unmarshalling.dto.DTOFactory;
import com.openshift.express.internal.client.response.unmarshalling.dto.DomainDTO;
import com.openshift.express.internal.client.response.unmarshalling.dto.DomainsDTO;

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
		DomainsDTO domains = DTOFactory.get(content, DomainsDTO.class);
		// verifications
		assertNotNull(domains);
		assertThat(domains.getDomains()).isNotEmpty();
		assertThat(domains.getDomains()).hasSize(1);
		assertThat(domains.getDomains().get(0).getNamespace()).isEqualTo("xcoulon");
		assertThat(domains.getDomains().get(0).getLinks()).hasSize(7);
	}

	@Test
	public void shouldUnmarshallGetDomainsWithNoExistingResponseBody() throws OpenShiftException, IOException {
		//pre-conditions
		String content = getContentAsString("get-domains-noexisting.json");
		assertNotNull(content);
		// operation
		DomainsDTO domains = DTOFactory.get(content, DomainsDTO.class);
		// verifications
		assertNotNull(domains);
		assertThat(domains.getDomains()).isEmpty();
	}

	@Test
	public void shouldUnmarshallGetDomainWith2ApplicationsResponseBody() throws OpenShiftException, IOException {
		//pre-conditions
		String content = getContentAsString("get-domain-with2applications.json");
		assertNotNull(content);
		// operation
		DomainDTO domain = DTOFactory.get(content, DomainDTO.class);
		// verifications
		assertNotNull(domain);
		assertThat(domain.getNamespace()).isEqualTo("xcoulon");
		assertThat(domain.getLinks()).hasSize(7);
	}

	@Test
	public void shouldUnmarshallGetDomainsWithNoApplicationResponseBody() throws OpenShiftException, IOException {
		//pre-conditions
		String content = getContentAsString("get-domain-withoutapplication.json");
		assertNotNull(content);
		// operation
		DomainDTO domain = DTOFactory.get(content, DomainDTO.class);
		// verifications
		assertNotNull(domain);
		assertThat(domain.getNamespace()).isEqualTo("xcoulon");
		assertThat(domain.getLinks()).hasSize(7);
	}
}

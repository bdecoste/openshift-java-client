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
package com.openshift.express.internal.client.rest.unmarshalling;

import static com.openshift.express.internal.client.response.unmarshalling.dto.ILinkNames.ADD_APPLICATION;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.openshift.express.client.HttpMethod;
import com.openshift.express.client.OpenShiftException;
import com.openshift.express.internal.client.response.unmarshalling.dto.ApplicationDTO;
import com.openshift.express.internal.client.response.unmarshalling.dto.CartridgeDTO;
import com.openshift.express.internal.client.response.unmarshalling.dto.DTOFactory;
import com.openshift.express.internal.client.response.unmarshalling.dto.DomainDTO;
import com.openshift.express.internal.client.response.unmarshalling.dto.EnumDataType;
import com.openshift.express.internal.client.response.unmarshalling.dto.Link;
import com.openshift.express.internal.client.response.unmarshalling.dto.RequiredLinkParameter;
import com.openshift.express.internal.client.response.unmarshalling.dto.Response;

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
		Response response = DTOFactory.get(content);
		// verifications
		assertThat(response.getDataType()).isEqualTo(EnumDataType.domains);
		final List<DomainDTO> domainDTOs = response.getData();
		assertThat(domainDTOs).isNotEmpty();
		assertThat(domainDTOs).hasSize(1);
		final DomainDTO domainDTO = domainDTOs.get(0);
		assertThat(domainDTO.getNamespace()).isEqualTo("xcoulon");
		assertThat(domainDTO.getLinks()).hasSize(7);
		final Link link = domainDTO.getLink(ADD_APPLICATION);
		assertThat(link).isNotNull();
		assertThat(link.getHref()).isEqualTo("/domains/xcoulon/applications");
		assertThat(link.getRel()).isEqualTo("Create new application");
		assertThat(link.getHttpMethod()).isEqualTo(HttpMethod.POST);
		final List<RequiredLinkParameter> requiredParams = link.getRequiredParams();
		assertThat(requiredParams).hasSize(2);
	}

	@Test
	public void shouldUnmarshallGetDomainsWithNoExistingResponseBody() throws OpenShiftException, IOException {
		//pre-conditions
		String content = getContentAsString("get-domains-noexisting.json");
		assertNotNull(content);
		// operation
		Response response = DTOFactory.get(content);
		// verifications
		assertThat(response.getDataType()).isEqualTo(EnumDataType.domains);
		final List<DomainDTO> domains = response.getData();
		assertThat(domains).isEmpty();
	}

	@Test
	public void shouldUnmarshallGetDomainResponseBody() throws OpenShiftException, IOException {
		//pre-conditions
		String content = getContentAsString("get-domain.json");
		assertNotNull(content);
		// operation
		Response response = DTOFactory.get(content);
		// verifications
		assertThat(response.getDataType()).isEqualTo(EnumDataType.domain);
		final DomainDTO domain = response.getData();
		assertNotNull(domain);
		assertThat(domain.getNamespace()).isEqualTo("xcoulon");
		assertThat(domain.getLinks()).hasSize(7);
	}

	@Test
	public void shouldUnmarshallGetApplicationsWith4AppsResponseBody() throws OpenShiftException, IOException {
		//pre-conditions
		String content = getContentAsString("get-applications-with4apps.json");
		assertNotNull(content);
		// operation
		Response response = DTOFactory.get(content);
		// verifications
		assertThat(response.getDataType()).isEqualTo(EnumDataType.applications);
		final List<ApplicationDTO> applications = response.getData();
		assertThat(applications).hasSize(4);
	}

	/**
	 * Should unmarshall get application response body.
	 *
	 * @throws OpenShiftException the open shift exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void shouldUnmarshallGetApplicationResponseBody() throws OpenShiftException, IOException {
		//pre-conditions
		String content = getContentAsString("get-application-test3.json");
		assertNotNull(content);
		// operation
		Response response = DTOFactory.get(content);
		// verifications
		assertThat(response.getDataType()).isEqualTo(EnumDataType.application);
		final ApplicationDTO application = response.getData();
		assertThat(application.getCreationTime()).isEqualTo("2012-03-22T06:55:54-04:00");
		assertThat(application.getDomainId()).isEqualTo("xcoulon");
		assertThat(application.getFramework()).isEqualTo("jbossas-7");
		assertThat(application.getName()).isEqualTo("test3");
		assertThat(application.getLinks()).hasSize(15);
	}
	
	/**
	 * Should unmarshall get application response body.
	 *
	 * @throws OpenShiftException the open shift exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void shouldUnmarshallGetApplicationWithAliasesResponseBody() throws OpenShiftException, IOException {
		//pre-conditions
		String content = getContentAsString("get-application-sample-withoutCartridge.json");
		assertNotNull(content);
		// operation
		Response response = DTOFactory.get(content);
		// verifications
		assertThat(response.getDataType()).isEqualTo(EnumDataType.application);
		final ApplicationDTO application = response.getData();
		assertThat(application.getAliases()).hasSize(2);
	}
	
	/**
	 * Should unmarshall get application response body.
	 *
	 * @throws OpenShiftException the open shift exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void shouldUnmarshallGetApplicationWithEmbeddedCartridgesResponseBody() throws OpenShiftException, IOException {
		//pre-conditions
		String content = getContentAsString("get-application-sample-withCartridges.json");
		assertNotNull(content);
		// operation
		Response response = DTOFactory.get(content);
		// verifications
		assertThat(response.getDataType()).isEqualTo(EnumDataType.application);
		final ApplicationDTO application = response.getData();
		assertThat(application.getEmbeddedCartridges()).hasSize(2);
	}
	
	/**
	 * Should unmarshall get application response body.
	 *
	 * @throws OpenShiftException the open shift exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void shouldUnmarshallAddApplicationEmbeddedCartridgeResponseBody() throws OpenShiftException, IOException {
		//pre-conditions
		String content = getContentAsString("add-application-cartridge.json");
		assertNotNull(content);
		// operation
		Response response = DTOFactory.get(content);
		// verifications
		assertThat(response.getMessages()).hasSize(3);
		assertThat(response.getDataType()).isEqualTo(EnumDataType.cartridge);
		final CartridgeDTO cartridge = response.getData();
		assertThat(cartridge.getName()).isEqualTo("mongodb-2.0");
		assertThat(cartridge.getType()).isEqualTo("embedded");
		assertThat(cartridge.getLinks()).hasSize(6);
		
	}
	
	/**
	 * Should unmarshall get application response body.
	 *
	 * @throws OpenShiftException the open shift exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void shouldUnmarshallGetApplicationCartridgesResponseBody() throws OpenShiftException, IOException {
		//pre-conditions
		String content = getContentAsString("get-application-cartridges-with2elements.json");
		assertNotNull(content);
		// operation
		Response response = DTOFactory.get(content);
		// verifications
		assertThat(response.getMessages()).hasSize(0);
		assertThat(response.getDataType()).isEqualTo(EnumDataType.cartridges);
		final List<CartridgeDTO> cartridges = response.getData();
		assertThat(cartridges).hasSize(2);
		assertThat(cartridges).onProperty("name").contains("mongodb-2.0", "mysql-5.1");
	}
	
}

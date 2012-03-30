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
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.openshift.express.client.HttpMethod;
import com.openshift.express.client.OpenShiftException;
import com.openshift.express.internal.client.response.unmarshalling.dto.ApplicationResourceDTO;
import com.openshift.express.internal.client.response.unmarshalling.dto.CartridgeResourceDTO;
import com.openshift.express.internal.client.response.unmarshalling.dto.DomainResourceDTO;
import com.openshift.express.internal.client.response.unmarshalling.dto.EnumDataType;
import com.openshift.express.internal.client.response.unmarshalling.dto.KeyResourceDTO;
import com.openshift.express.internal.client.response.unmarshalling.dto.Link;
import com.openshift.express.internal.client.response.unmarshalling.dto.LinkParameter;
import com.openshift.express.internal.client.response.unmarshalling.dto.ResourceDTOFactory;
import com.openshift.express.internal.client.response.unmarshalling.dto.RestResponse;
import com.openshift.express.internal.client.response.unmarshalling.dto.UserResourceDTO;

public class DmrUnmarshallingTestCase {

	private String getContentAsString(String fileName) throws IOException {
		final InputStream contentStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("samples/" + fileName);
		return IOUtils.toString(contentStream);
	}
	
	@Test
	public void shouldUnmarshallGetUserResponseBody() throws OpenShiftException, IOException {
		//pre-conditions
		String content = getContentAsString("get-user.json");
		assertNotNull(content);
		// operation
		RestResponse response = ResourceDTOFactory.get(content);
		// verifications
		assertThat(response.getDataType()).isEqualTo(EnumDataType.user);
		UserResourceDTO userResourceDTO = response.getData();
		assertThat(userResourceDTO.getRhLogin()).isEqualTo("xcoulon+test@redhat.com");
		assertThat(userResourceDTO.getLinks()).hasSize(3);
	}
	
	@Test
	public void shouldUnmarshallGetUserNoKeyResponseBody() throws OpenShiftException, IOException {
		//pre-conditions
		String content = getContentAsString("get-user-keys-none.json");
		assertNotNull(content);
		// operation
		RestResponse response = ResourceDTOFactory.get(content);
		// verifications
		assertThat(response.getDataType()).isEqualTo(EnumDataType.keys);
		List<KeyResourceDTO> keys = response.getData();
		assertThat(keys).isEmpty();
	}

	@Test
	public void shouldUnmarshallGetUserSingleKeyResponseBody() throws OpenShiftException, IOException {
		//pre-conditions
		String content = getContentAsString("get-user-keys-single.json");
		assertNotNull(content);
		// operation
		RestResponse response = ResourceDTOFactory.get(content);
		// verifications
		assertThat(response.getDataType()).isEqualTo(EnumDataType.keys);
		List<KeyResourceDTO> keys = response.getData();
		assertThat(keys).hasSize(1);
		final KeyResourceDTO key = keys.get(0);
		assertThat(key.getLinks()).hasSize(3);
		assertThat(key.getName()).isEqualTo("default");
		assertThat(key.getType()).isEqualTo("ssh-rsa");
		assertThat(key.getContent()).isEqualTo("AAAA");
	}

	@Test
	public void shouldUnmarshallGetUserMultipleKeyResponseBody() throws OpenShiftException, IOException {
		//pre-conditions
		String content = getContentAsString("get-user-keys-multiple.json");
		assertNotNull(content);
		// operation
		RestResponse response = ResourceDTOFactory.get(content);
		// verifications
		assertThat(response.getDataType()).isEqualTo(EnumDataType.keys);
		List<KeyResourceDTO> keys = response.getData();
		final KeyResourceDTO key = keys.get(0);
		assertThat(key.getLinks()).hasSize(3);
		assertThat(key.getName()).isEqualTo("default");
		assertThat(key.getType()).isEqualTo("ssh-rsa");
		assertThat(key.getContent()).isEqualTo("AAAA");
	}
	
	
	@Test
	public void shouldUnmarshallGetRootAPIResponseBody() throws OpenShiftException, IOException {
		//pre-conditions
		String content = getContentAsString("get-rest-api.json");
		assertNotNull(content);
		// operation
		RestResponse response = ResourceDTOFactory.get(content);
		// verifications
		assertThat(response.getDataType()).isEqualTo(EnumDataType.links);
		final Map<String, Link> links = response.getData();
		assertThat(links).hasSize(7);
	}

	
	@Test
	public void shouldUnmarshallGetDomainsWith1ExistingResponseBody() throws OpenShiftException, IOException {
		//pre-conditions
		String content = getContentAsString("get-domains-1existing.json");
		assertNotNull(content);
		// operation
		RestResponse response = ResourceDTOFactory.get(content);
		// verifications
		assertThat(response.getDataType()).isEqualTo(EnumDataType.domains);
		final List<DomainResourceDTO> domainDTOs = response.getData();
		assertThat(domainDTOs).isNotEmpty();
		assertThat(domainDTOs).hasSize(1);
		final DomainResourceDTO domainDTO = domainDTOs.get(0);
		assertThat(domainDTO.getNamespace()).isEqualTo("xcoulon");
		assertThat(domainDTO.getLinks()).hasSize(7);
		final Link link = domainDTO.getLink(ADD_APPLICATION);
		assertThat(link).isNotNull();
		assertThat(link.getHref()).isEqualTo("/domains/xcoulon/applications");
		assertThat(link.getRel()).isEqualTo("Create new application");
		assertThat(link.getHttpMethod()).isEqualTo(HttpMethod.POST);
		final List<LinkParameter> requiredParams = link.getRequiredParams();
		assertThat(requiredParams).hasSize(2);
	}

	@Test
	public void shouldUnmarshallGetDomainsWithNoExistingResponseBody() throws OpenShiftException, IOException {
		//pre-conditions
		String content = getContentAsString("get-domains-noexisting.json");
		assertNotNull(content);
		// operation
		RestResponse response = ResourceDTOFactory.get(content);
		// verifications
		assertThat(response.getDataType()).isEqualTo(EnumDataType.domains);
		final List<DomainResourceDTO> domains = response.getData();
		assertThat(domains).isEmpty();
	}

	@Test
	public void shouldUnmarshallGetDomainResponseBody() throws OpenShiftException, IOException {
		//pre-conditions
		String content = getContentAsString("get-domain.json");
		assertNotNull(content);
		// operation
		RestResponse response = ResourceDTOFactory.get(content);
		// verifications
		assertThat(response.getDataType()).isEqualTo(EnumDataType.domain);
		final DomainResourceDTO domain = response.getData();
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
		RestResponse response = ResourceDTOFactory.get(content);
		// verifications
		assertThat(response.getDataType()).isEqualTo(EnumDataType.applications);
		final List<ApplicationResourceDTO> applications = response.getData();
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
		RestResponse response = ResourceDTOFactory.get(content);
		// verifications
		assertThat(response.getDataType()).isEqualTo(EnumDataType.application);
		final ApplicationResourceDTO application = response.getData();
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
		RestResponse response = ResourceDTOFactory.get(content);
		// verifications
		assertThat(response.getDataType()).isEqualTo(EnumDataType.application);
		final ApplicationResourceDTO application = response.getData();
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
		RestResponse response = ResourceDTOFactory.get(content);
		// verifications
		assertThat(response.getDataType()).isEqualTo(EnumDataType.application);
		final ApplicationResourceDTO application = response.getData();
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
		RestResponse response = ResourceDTOFactory.get(content);
		// verifications
		assertThat(response.getMessages()).hasSize(3);
		assertThat(response.getDataType()).isEqualTo(EnumDataType.cartridge);
		final CartridgeResourceDTO cartridge = response.getData();
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
		RestResponse response = ResourceDTOFactory.get(content);
		// verifications
		assertThat(response.getMessages()).hasSize(0);
		assertThat(response.getDataType()).isEqualTo(EnumDataType.cartridges);
		final List<CartridgeResourceDTO> cartridges = response.getData();
		assertThat(cartridges).hasSize(2);
		assertThat(cartridges).onProperty("name").contains("mongodb-2.0", "mysql-5.1");
	}
	
	@Test
	public void shouldUnmarshallSingleValidOptionInResponseBody() throws OpenShiftException, IOException {
		//pre-conditions
		String content = getContentAsString("add-application-cartridge.json");
		assertNotNull(content);
		// operation
		RestResponse response = ResourceDTOFactory.get(content);
		// verifications
		final CartridgeResourceDTO cartridge = response.getData();
		final Link link = cartridge.getLink("RESTART");
		assertThat(link.getOptionalParams()).hasSize(0);
		assertThat(link.getRequiredParams().get(0).getValidOptions()).containsExactly("restart");
	}
	
	@Test
	public void shouldUnmarshallMultipleValidOptionInResponseBody() throws OpenShiftException, IOException {
		//pre-conditions
		String content = getContentAsString("add-user-key-ok.json");
		assertNotNull(content);
		// operation
		RestResponse response = ResourceDTOFactory.get(content);
		// verifications
		final KeyResourceDTO key = response.getData();
		final Link link = key.getLink("UPDATE");
		assertThat(link.getOptionalParams()).hasSize(0);
		assertThat(link.getRequiredParams().get(0).getValidOptions()).containsExactly("ssh-rsa", "ssh-dss");
	}
	
}

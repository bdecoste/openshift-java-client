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
package com.openshift.internal.client.response;

import static com.openshift.internal.client.response.ILinkNames.ADD_APPLICATION;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.fest.assertions.Condition;
import org.junit.Test;

import com.openshift.client.HttpMethod;
import com.openshift.client.OpenShiftException;
import com.openshift.internal.client.response.ApplicationResourceDTO;
import com.openshift.internal.client.response.CartridgeResourceDTO;
import com.openshift.internal.client.response.DomainResourceDTO;
import com.openshift.internal.client.response.EnumDataType;
import com.openshift.internal.client.response.GearComponentDTO;
import com.openshift.internal.client.response.GearResourceDTO;
import com.openshift.internal.client.response.KeyResourceDTO;
import com.openshift.internal.client.response.Link;
import com.openshift.internal.client.response.LinkParameter;
import com.openshift.internal.client.response.ResourceDTOFactory;
import com.openshift.internal.client.response.RestResponse;
import com.openshift.internal.client.response.UserResourceDTO;

public class ResourceDTOFactoryTest {

	private static final class ValidLinkCondition extends Condition<Map<?, ?>> {
		@Override
		public boolean matches(Map<?, ?> links) {
			for (Entry<?, ?> entry : links.entrySet()) {
				Link link = (Link) entry.getValue();
				if (link.getHref() == null || link.getHttpMethod() == null || link.getRel() == null) {
					return false;
				}
			}
			return true;
		}
	}

	private String getContentAsString(String fileName) throws IOException {
		final InputStream contentStream = getClass()
				.getResourceAsStream("/samples/" + fileName);
		return IOUtils.toString(contentStream);
	}

	@Test
	public void shouldUnmarshallGetUserResponseBody() throws OpenShiftException, IOException {
		// pre-conditions
		String content = getContentAsString("get-user.json");
		assertNotNull(content);
		// operation
		RestResponse response = ResourceDTOFactory.get(content);
		// verifications
		assertThat(response.getDataType()).isEqualTo(EnumDataType.user);
		UserResourceDTO userResourceDTO = response.getData();
		assertThat(userResourceDTO.getRhLogin()).isEqualTo("foobar+test@redhat.com");
		assertThat(userResourceDTO.getLinks()).hasSize(3);
	}

	@Test
	public void shouldUnmarshallGetUserNoKeyResponseBody() throws OpenShiftException, IOException {
		// pre-conditions
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
		// pre-conditions
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
		// pre-conditions
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
		// pre-conditions
		String content = getContentAsString("get-rest-api.json");
		assertNotNull(content);
		// operation
		RestResponse response = ResourceDTOFactory.get(content);
		// verifications
		assertThat(response.getDataType()).isEqualTo(EnumDataType.links);
		final Map<String, Link> links = response.getData();
		assertThat(links).hasSize(7);
		assertThat(links).satisfies(new ValidLinkCondition());

	}

	@Test
	public void shouldUnmarshallGetDomainsWith1ExistingResponseBody() throws OpenShiftException, IOException {
		// pre-conditions
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
		assertThat(domainDTO.getNamespace()).isEqualTo("foobar");
		assertThat(domainDTO.getLinks()).hasSize(6);
		final Link link = domainDTO.getLink(ADD_APPLICATION);
		assertThat(link).isNotNull();
		assertThat(link.getHref()).isEqualTo("/domains/foobar/applications");
		assertThat(link.getRel()).isEqualTo("Create new application");
		assertThat(link.getHttpMethod()).isEqualTo(HttpMethod.POST);
		final List<LinkParameter> requiredParams = link.getRequiredParams();
		assertThat(requiredParams).hasSize(2);
	}

	@Test
	public void shouldUnmarshallGetDomainsWithNoExistingResponseBody() throws OpenShiftException, IOException {
		// pre-conditions
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
		// pre-conditions
		String content = getContentAsString("get-domain.json");
		assertNotNull(content);
		// operation
		RestResponse response = ResourceDTOFactory.get(content);
		// verifications
		assertThat(response.getDataType()).isEqualTo(EnumDataType.domain);
		final DomainResourceDTO domain = response.getData();
		assertNotNull(domain);
		assertThat(domain.getNamespace()).isEqualTo("foobar");
		assertThat(domain.getLinks()).hasSize(6);
	}

	@Test
	public void shouldUnmarshallDeleteDomainKoNotFoundResponseBody() throws OpenShiftException, IOException {
		// pre-conditions
		String content = getContentAsString("delete-domain-ko-notfound.json");
		assertNotNull(content);
		// operation
		RestResponse response = ResourceDTOFactory.get(content);
		// verifications
		assertThat(response.getDataType()).isNull();
		assertThat(response.getMessages()).hasSize(1);
	}

	@Test
	public void shouldUnmarshallGetApplicationsWith2AppsResponseBody() throws OpenShiftException, IOException {
		// pre-conditions
		String content = getContentAsString("get-applications-with2apps.json");
		assertNotNull(content);
		// operation
		RestResponse response = ResourceDTOFactory.get(content);
		// verifications
		assertThat(response.getDataType()).isEqualTo(EnumDataType.applications);
		final List<ApplicationResourceDTO> applications = response.getData();
		assertThat(applications).hasSize(2);
	}

	/**
	 * Should unmarshall get application response body.
	 * 
	 * @throws OpenShiftException
	 *             the open shift exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void shouldUnmarshallGetApplicationWithAliasesResponseBody() throws OpenShiftException, IOException {
		// pre-conditions
		String content = getContentAsString("get-application-2cartridges-2aliases.json");
		assertNotNull(content);
		// operation
		RestResponse response = ResourceDTOFactory.get(content);
		// verifications
		assertThat(response.getDataType()).isEqualTo(EnumDataType.application);
		final ApplicationResourceDTO application = response.getData();
		assertThat(application.getUuid()).hasSize(32);
		assertThat(application.getCreationTime()).startsWith("2012-");
		assertThat(application.getDomainId()).isEqualTo("foobar");
		assertThat(application.getFramework()).isEqualTo("jbossas-7");
		assertThat(application.getName()).isEqualTo("sample");
		assertThat(application.getLinks()).hasSize(17);
		assertThat(application.getAliases()).contains("an_alias", "another_alias");
	}

	/**
	 * Should unmarshall get application response body.
	 * 
	 * @throws OpenShiftException
	 *             the open shift exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void shouldUnmarshallAddApplicationEmbeddedCartridgeResponseBody() throws OpenShiftException, IOException {
		// pre-conditions
		String content = getContentAsString("add-application-cartridge.json");
		assertNotNull(content);
		// operation
		RestResponse response = ResourceDTOFactory.get(content);
		// verifications
		assertThat(response.getMessages()).hasSize(3);
		assertThat(response.getDataType()).isEqualTo(EnumDataType.cartridge);
		final CartridgeResourceDTO cartridge = response.getData();
		assertThat(cartridge.getName()).isEqualTo("mysql-5.1");
		assertThat(cartridge.getType()).isEqualTo("embedded");
		assertThat(cartridge.getLinks()).hasSize(6);

	}

	/**
	 * Should unmarshall get application response body.
	 * 
	 * @throws OpenShiftException
	 *             the open shift exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void shouldUnmarshallGetApplicationCartridgesWith1ElementResponseBody() throws OpenShiftException, IOException {
		// pre-conditions
		String content = getContentAsString("get-application-cartridges-with1element.json");
		assertNotNull(content);
		// operation
		RestResponse response = ResourceDTOFactory.get(content);
		// verifications
		assertThat(response.getMessages()).hasSize(0);
		assertThat(response.getDataType()).isEqualTo(EnumDataType.cartridges);
		final List<CartridgeResourceDTO> cartridges = response.getData();
		assertThat(cartridges).hasSize(1);
		assertThat(cartridges).onProperty("name").contains("mongodb-2.0");
	}

	/**
	 * Should unmarshall get application response body.
	 * 
	 * @throws OpenShiftException
	 *             the open shift exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void shouldUnmarshallGetApplicationCartridgesWith2ElementsResponseBody() throws OpenShiftException, IOException {
		// pre-conditions
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

	/**
	 * Should unmarshall get application response body.
	 * 
	 * @throws OpenShiftException
	 *             the open shift exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void shouldUnmarshallGetApplicationGearsResponseBody() throws OpenShiftException, IOException {
		// pre-conditions
		String content = getContentAsString("get-appgears-with3components.json");
		assertNotNull(content);
		// operation
		RestResponse response = ResourceDTOFactory.get(content);
		// verifications
		final List<GearResourceDTO> gears = response.getData();
		assertThat(gears).hasSize(1);
		final GearResourceDTO gear = gears.get(0); 
		assertThat(gear.getUuid()).isEqualTo("5229a2c0f1424aed90d4ca8f20c0f9a2");
		assertThat(gear.getGitUrl()).isEqualTo(
				"ssh://5229a2c0f1424aed90d4ca8f20c0f9a2@sample-foobar.stg.rhcloud.com/~/git/sample.git/");
		assertThat(gear.getComponents()).contains(new GearComponentDTO("jbossas-7", "8080", "proxy", "3128"),
				new GearComponentDTO("mongodb-2.0", null, null, null), new GearComponentDTO("mysql-5.1", null, null, null));
	}

	@Test
	public void shouldUnmarshallSingleValidOptionInResponseBody() throws OpenShiftException, IOException {
		// pre-conditions
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
		// pre-conditions
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

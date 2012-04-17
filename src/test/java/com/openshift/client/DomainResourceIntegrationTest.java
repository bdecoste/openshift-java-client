/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package com.openshift.client;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.openshift.client.utils.ApplicationTestUtils;
import com.openshift.client.utils.DomainTestUtils;
import com.openshift.client.utils.OpenShiftTestConfiguration;
import com.openshift.client.utils.StringUtils;
import com.openshift.client.utils.TestUserBuilder;

public class DomainResourceIntegrationTest {

	private IUser user;
	private IUser invalidUser;

	@Before
	public void setUp() throws OpenShiftException, IOException {
		final OpenShiftTestConfiguration configuration = new OpenShiftTestConfiguration();
		final IOpenShiftConnection connection = new OpenShiftConnectionManager().getConnection(
				configuration.getClientId(), configuration.getRhlogin(), configuration.getPassword(),
				configuration.getLibraServer());
		this.user = connection.getUser();
		this.invalidUser = new TestUserBuilder().getConnection(
				OpenShiftTestConfiguration.CLIENT_ID, "bogus-password").getUser();

		// ensureDomainExists(user);
		// ensureNoApplicationsExist(user);
	}

	@Test(expected = InvalidCredentialsOpenShiftException.class)
	public void shouldThrowIfGetDomainsWithInvalidCredentials() throws Exception {
		invalidUser.getDomains();
	}

	@Test(expected = InvalidCredentialsOpenShiftException.class)
	public void shouldThrowIfCreateDomainWithInvalidCredentials() throws Exception {
		invalidUser.createDomain(String.valueOf(System.currentTimeMillis()));
	}

	@Test
	public void shouldReturnDomains() throws SocketTimeoutException, OpenShiftException {
		// operation
		List<IDomain> domains = user.getDomains();
		assertThat(domains).isNotNull();
	}

	@Test
	public void shouldCreateDomain() throws SocketTimeoutException, OpenShiftException {
		IDomain domain = null;
		try {
			// pre-condition
			DomainTestUtils.silentlyDestroyAllDomains(user);

			// operation
			String id = StringUtils.createRandomString();
			domain = user.createDomain(id);

			// verification
			assertThat(domain.getId()).isEqualTo(id);
		} finally {
			DomainTestUtils.silentlyDestroy(domain);
		}
	}

	@Test
	public void shouldReturnDomainByName() throws SocketTimeoutException, OpenShiftException {
		IDomain domain = null;
		try {
			// pre-condition
			DomainTestUtils.silentlyDestroyAllDomains(user);

			// operation
			String id = StringUtils.createRandomString();
			domain = user.createDomain(id);

			// verification
			IDomain domainByNamespace = user.getDomain(id);
			assertThat(domainByNamespace.getId()).isEqualTo(id);
		} finally {
			DomainTestUtils.silentlyDestroy(domain);
		}
	}

	@Test
	public void shouldSetNamespace() throws Exception {
		IDomain domain = null;
		try {
			// pre-condition
			domain = DomainTestUtils.getFirstDomainOrCreate(user);

			// operation
			String namespace = StringUtils.createRandomString();
			domain.setId(namespace);

			// verification
			IDomain domainByNamespace = user.getDomain(namespace);
			assertThat(domainByNamespace.getId()).isEqualTo(namespace);
		} finally {
			DomainTestUtils.silentlyDestroy(domain);
		}
	}

	@Test
	public void canWaitForDomainToBecomeAccessible() throws OpenShiftException {
		// IDomain domain = user.getDomain();
		// assertNotNull(domain);
		// String newDomainName = createRandomString();
		// domain.setNamespace(newDomainName);
		// assertEquals(newDomainName, domain.getNamespace());
		// assertTrue(domain.waitForAccessible(10 * 1024));
	}

	@Test
	public void shouldDeleteDomainWithoutApplications() throws Exception {
		IDomain domain = null;
		try {
			// pre-condition
			DomainTestUtils.silentlyDestroyAllDomains(user);
			String id = StringUtils.createRandomString();
			domain = user.createDomain(id);
		
			// operation
			domain.destroy();
			
			// verification
			IDomain domainByNamespace = user.getDomain(id);
			assertThat(domainByNamespace).isNull();
		} finally {
			DomainTestUtils.silentlyDestroy(domain);
		}
	}

	@Test(expected = OpenShiftException.class)
	public void shouldNotDeleteDomainWithApplications() throws OpenShiftException, SocketTimeoutException {
		IDomain domain = null;
		try {
			// pre-condition
			domain = DomainTestUtils.getFirstDomainOrCreate(user);
			ApplicationTestUtils.getOrCreateApplication(domain);
			
			// operation
			domain.destroy();
		} finally {
			DomainTestUtils.silentlyDestroy(domain);
		}
	}

	private void ensureNoApplicationsExist(IUser user) throws OpenShiftException {
		// try {
		// List<IApplication> allApplications = new ArrayList<IApplication>();
		// allApplications.addAll(user.getApplications());
		// for (IApplication application : allApplications) {
		// application.destroy();
		// }
		// } catch (NotFoundOpenShiftException e) {
		// // no domain present, ignore
		// }
	}

	private void ensureDomainExists(IUser user) throws OpenShiftException, IOException {
		// try {
		// user.getDomain();
		// } catch (OpenShiftException e) {
		// // no domain present
		// SSHKeyPair sshKey = TestSSHKey.create();
		// service.createDomain(createRandomString(), sshKey, user);
		// }
	}
}

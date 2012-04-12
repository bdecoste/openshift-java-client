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
package com.openshift.client;

import static com.openshift.client.utils.FileUtils.createRandomTempFile;
import static com.openshift.client.utils.UrlEndsWithMatcher.urlEndsWith;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.fest.assertions.AssertExtension;
import org.junit.Before;
import org.junit.Test;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.KeyPair;
import com.openshift.client.utils.Samples;
import com.openshift.internal.client.RestService;
import com.openshift.internal.client.httpclient.HttpClientException;

public class SSHKeyTest {

	private static final String SSH_RSA = "ssh-rsa";
	private static final String SSH_DSA = "ssh-dss";
	private static final String PASSPHRASE = "12345";
	private IHttpClient mockClient;
	private IUser user;
	private RestService service;

	@Before
	public void setUp() throws SocketTimeoutException, HttpClientException, Throwable {
		mockClient = mock(IHttpClient.class);
		when(mockClient.get(urlEndsWith("/api")))
				.thenReturn(Samples.GET_REST_API_JSON.getContentAsString());
		when(mockClient.get(urlEndsWith("/user"))).thenReturn(Samples.GET_USER_JSON.getContentAsString());
		this.service = new RestService(IRestServiceTestConstants.CLIENT_ID, mockClient);
		this.user = new UserBuilder().configure(service).build();
	}

	@Test
	public void canCreatePublicKey() throws Exception {
		String publicKeyPath = createRandomTempFile().getAbsolutePath();
		String privateKeyPath = createRandomTempFile().getAbsolutePath();
		SSHKeyPair sshKey = SSHKeyPair.create(PASSPHRASE, privateKeyPath, publicKeyPath);
		String publicKey = sshKey.getPublicKey();
		assertNotNull(sshKey.getKeyType());
		String keyType = sshKey.getKeyType().getTypeId();

		assertNotNull(publicKey);
		assertTrue(!publicKey.contains(SSH_RSA)); // no identifier
		assertTrue(!publicKey.contains(" ")); // no comment
		assertEquals(SSHKeyType.SSH_RSA.getTypeId(), keyType);
	}

	@Test
	public void canLoadKeyPair() throws Exception {
		String publicKeyPath = createRandomTempFile().getAbsolutePath();
		String privateKeyPath = createRandomTempFile().getAbsolutePath();
		SSHKeyPair.create(PASSPHRASE, privateKeyPath, publicKeyPath);

		SSHKeyPair sshKey = SSHKeyPair.load(privateKeyPath, publicKeyPath);
		String publicKey = sshKey.getPublicKey();
		assertNotNull(sshKey.getKeyType());
		String keyType = sshKey.getKeyType().getTypeId();

		assertNotNull(publicKey);
		assertTrue(!publicKey.contains(SSH_RSA)); // no identifier
		assertTrue(!publicKey.contains(" ")); // no comment
		assertEquals(SSHKeyType.SSH_RSA.getTypeId(), keyType);
	}

	@Test
	public void canLoadPublicKey() throws Exception {
		String publicKeyPath = createRandomTempFile().getAbsolutePath();
		String privateKeyPath = createRandomTempFile().getAbsolutePath();
		SSHKeyPair.create(PASSPHRASE, privateKeyPath, publicKeyPath);

		ISSHPublicKey sshKey = new SSHPublicKey(new File(publicKeyPath));
		String publicKey = sshKey.getPublicKey();
		assertNotNull(sshKey.getKeyType());
		String keyType = sshKey.getKeyType().getTypeId();

		assertNotNull(publicKey);
		assertTrue(!publicKey.contains(SSH_RSA)); // no identifier
		assertTrue(!publicKey.contains(" ")); // no comment

		SSHKeyPair keyPair = SSHKeyPair.load(privateKeyPath, publicKeyPath);
		assertEquals(publicKey, keyPair.getPublicKey());
		assertEquals(SSHKeyType.SSH_RSA.getTypeId(), keyType);
	}

	@Test
	public void canLoadKeyPairDsa() throws Exception {
		String publicKeyPath = createRandomTempFile().getAbsolutePath();
		String privateKeyPath = createRandomTempFile().getAbsolutePath();
		createDsaKeyPair(publicKeyPath, privateKeyPath);

		SSHKeyPair sshKey = SSHKeyPair.load(privateKeyPath, publicKeyPath);
		String publicKey = sshKey.getPublicKey();
		assertNotNull(sshKey.getKeyType());
		String keyType = sshKey.getKeyType().getTypeId();

		assertNotNull(publicKey);
		assertTrue(!publicKey.contains(SSH_DSA)); // no identifier
		assertTrue(!publicKey.contains(" ")); // no comment
		assertEquals(SSHKeyType.SSH_DSA.getTypeId(), keyType);
	}

	@Test
	public void canLoadPublicKeyDsa() throws Exception {
		String publicKeyPath = createRandomTempFile().getAbsolutePath();
		String privateKeyPath = createRandomTempFile().getAbsolutePath();
		createDsaKeyPair(publicKeyPath, privateKeyPath);

		ISSHPublicKey sshKey = new SSHPublicKey(publicKeyPath);
		String publicKey = sshKey.getPublicKey();
		assertNotNull(sshKey.getKeyType());
		String keyType = sshKey.getKeyType().getTypeId();

		assertNotNull(publicKey);
		assertTrue(!publicKey.contains(SSH_DSA)); // no identifier
		assertTrue(!publicKey.contains(" ")); // no comment

		SSHKeyPair keyPair = SSHKeyPair.load(privateKeyPath, publicKeyPath);
		assertEquals(publicKey, keyPair.getPublicKey());
		assertEquals(SSHKeyType.SSH_DSA.getTypeId(), keyType);
	}

	@Test
	public void canGetKeyTypeByTypeId() throws OpenShiftUnknonwSSHKeyTypeException {
		assertTrue(SSHKeyType.SSH_DSA == SSHKeyType.getByTypeId(SSH_DSA));
		assertTrue(SSHKeyType.SSH_RSA == SSHKeyType.getByTypeId(SSH_RSA));
	}

	@Test(expected = OpenShiftUnknonwSSHKeyTypeException.class)
	public void getKeyTypeByTypeIdReturnsNullIfNoMatchingType() throws OpenShiftUnknonwSSHKeyTypeException {
		SSHKeyType.getByTypeId("dummy");
	}

	@Test
	public void shouldReturn2SSHKeys() throws HttpClientException, Throwable {
		// pre-conditions
		when(mockClient.get(urlEndsWith("/user/keys")))
				.thenReturn(Samples.GET_USER_KEYS_MULTIPLE_JSON.getContentAsString());
		// operation
		List<IOpenShiftSSHKey> sshKeys = user.getSshKeys();
		// verifications
		assertThat(sshKeys).hasSize(2);
		assertThat(new SSHPublicKeyAssertion(sshKeys.get(0)))
				.hasName("default").hasPublicKey("AAAA").isType("ssh-rsa");
		assertThat(new SSHPublicKeyAssertion(sshKeys.get(1)))
				.hasName("default2").hasPublicKey("AAAB").isType("ssh-dss");
	}

	@Test
	public void shouldAddAndUpdateKey() throws SocketTimeoutException, HttpClientException, Throwable {
		// pre-conditions
		when(mockClient.post(anyMapOf(String.class, Object.class), urlEndsWith("/user/keys")))
				.thenReturn(Samples.ADD_USER_KEY2_OK_JSON.getContentAsString());
		String publicKeyPath = createRandomTempFile().getAbsolutePath();
		String privateKeyPath = createRandomTempFile().getAbsolutePath();
		createDsaKeyPair(publicKeyPath, privateKeyPath);
		SSHPublicKey publicKey = new SSHPublicKey(publicKeyPath);
		assertThat(publicKey.getName()).isNull();

		// operation
		user.addSshKey("default2", publicKey);

		// verifications
		List<IOpenShiftSSHKey> keys = user.getSshKeys();
		assertThat(keys).hasSize(1);
		assertThat(new SSHPublicKeyAssertion(keys.get(0)))
				.hasName("default2").hasPublicKey("AAAAB3Nz").isType("ssh-rsa");
	}

	@Test
	public void shouldUpdateKeyType() throws SocketTimeoutException, HttpClientException, Throwable {
		// pre-conditions
		String keyName = "default";
		String keyUrl = service.getServiceUrl() + "user/keys/" + keyName;
		when(mockClient.get(urlEndsWith("/user/keys")))
				.thenReturn(Samples.GET_USER_KEYS_SINGLE_JSON.getContentAsString());
		when(mockClient.put(anyMapOf(String.class, Object.class), urlEndsWith(keyUrl)))
				.thenReturn(Samples.UPDATE_USER_KEY_JSON.getContentAsString());
		// operation
		List<IOpenShiftSSHKey> keys = user.getSshKeys();
		assertThat(keys).hasSize(1);
		IOpenShiftSSHKey key = keys.get(0);
		assertThat(key.getKeyType()).isEqualTo(SSHKeyType.SSH_RSA);
		key.setKeyType(SSHKeyType.SSH_DSA);
		// verification
		assertThat(key.getKeyType()).isEqualTo(SSHKeyType.SSH_DSA);
		HashMap<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("type", SSHKeyType.SSH_DSA.getTypeId());
		parameterMap.put("content", key.getPublicKey());
		verify(mockClient).put(parameterMap, new URL(keyUrl));
	}

	private void createDsaKeyPair(String publicKeyPath, String privateKeyPath) throws IOException, JSchException {
		KeyPair keyPair = KeyPair.genKeyPair(new JSch(), KeyPair.DSA, 1024);
		keyPair.setPassphrase(PASSPHRASE);
		keyPair.writePublicKey(publicKeyPath, "created by " + IOpenShiftService.ID);
		keyPair.writePrivateKey(privateKeyPath);
	}

	private class SSHPublicKeyAssertion implements AssertExtension {

		private IOpenShiftSSHKey sshKey;

		public SSHPublicKeyAssertion(IOpenShiftSSHKey key) {
			this.sshKey = key;
		}

		public SSHPublicKeyAssertion hasName(String name) {
			assertEquals(sshKey.getName(), name);
			return this;
		}

		public SSHPublicKeyAssertion hasPublicKey(String publicKey) {
			assertEquals(sshKey.getPublicKey(), publicKey);
			return this;
		}

		public SSHPublicKeyAssertion isType(String type) {
			assertEquals(sshKey.getKeyType().getTypeId(), type);
			return this;
		}
	}
}

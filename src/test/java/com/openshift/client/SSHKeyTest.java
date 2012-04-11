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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

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

	@Before
	public void setUp() throws SocketTimeoutException, HttpClientException, Throwable {
		mockClient = mock(IHttpClient.class);
		when(mockClient.get(urlEndsWith("/broker/rest/api")))
				.thenReturn(Samples.GET_REST_API_JSON.getContentAsString());
		when(mockClient.get(urlEndsWith("/broker/rest/user"))).thenReturn(Samples.GET_USER_JSON.getContentAsString());
		this.user = new UserBuilder().configure(new RestService(IRestServiceTestConstants.CLIENT_ID, mockClient))
				.build();

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

		ISSHPublicKey sshKey = SSHPublicKey.create(new File(publicKeyPath));
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

		ISSHPublicKey sshKey = SSHPublicKey.create(new File(publicKeyPath));
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
	public void shouldUnmarshallSSHKeysResponse() throws HttpClientException, Throwable {
		when(mockClient.get(urlEndsWith("/broker/rest/user/keys")))
				.thenReturn(Samples.GET_USER_KEYS_MULTIPLE_JSON.getContentAsString());
		List<ISSHPublicKey> sshKeys = user.getSshKeys();
		assertThat(sshKeys).hasSize(2);
		assertThat(new SSHPublicKey("ssh-rsa", "AAAA")).isIn(sshKeys);
		assertThat(new SSHPublicKey("ssh-rsa", "AAAB")).isIn(sshKeys);
	}

	private void createDsaKeyPair(String publicKeyPath, String privateKeyPath) throws IOException, JSchException {
		KeyPair keyPair = KeyPair.genKeyPair(new JSch(), KeyPair.DSA, 1024);
		keyPair.setPassphrase(PASSPHRASE);
		keyPair.writePublicKey(publicKeyPath, "created by " + IOpenShiftService.ID);
		keyPair.writePrivateKey(privateKeyPath);
	}

}

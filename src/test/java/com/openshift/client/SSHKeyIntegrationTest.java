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
import static org.fest.assertions.Assertions.assertThat;

import java.net.SocketTimeoutException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.openshift.client.utils.SSHKeyTestUtils;
import com.openshift.client.utils.TestUserBuilder;
import com.openshift.internal.client.httpclient.HttpClientException;

/**
 * @author Andre Dietisheim
 */
public class SSHKeyIntegrationTest {

	private IUser user;

	@Before
	public void setUp() throws SocketTimeoutException, HttpClientException, Throwable {
		this.user = new TestUserBuilder().configure().build();
	}

	@Test
	public void shouldReturnExistingKeys() throws HttpClientException, Throwable {
		// pre-conditions
		// operation
		List<IOpenShiftSSHKey> sshKeys = user.getSSHKeys();
		// verifications
		assertThat(sshKeys).isNotNull();
	}

	@Test
	public void shouldAddKey() throws SocketTimeoutException, HttpClientException, Throwable {
		// pre-conditions
		String keyName = String.valueOf(System.currentTimeMillis());
		String publicKeyPath = createRandomTempFile().getAbsolutePath();
		String privateKeyPath = createRandomTempFile().getAbsolutePath();
		SSHKeyTestUtils.createDsaKeyPair(publicKeyPath, privateKeyPath);
		ISSHPublicKey publicKey = new SSHPublicKey(publicKeyPath);
		int numOfKeys = user.getSSHKeys().size();

		// operation
		IOpenShiftSSHKey key = user.putSSHKey(keyName, publicKey);

		// verifications
		assertThat(
				new SSHKeyTestUtils.SSHPublicKeyAssertion(key))
				.hasName(keyName)
				.hasPublicKey(publicKey.getPublicKey())
				.isType(publicKey.getKeyType());
		List<IOpenShiftSSHKey> keys = user.getSSHKeys();
		assertThat(keys.size()).isEqualTo(numOfKeys + 1);
		IOpenShiftSSHKey keyInList = SSHKeyTestUtils.getKey(keyName, keys);
		assertThat(key).isEqualTo(keyInList);
	}

	@Test
	public void shouldUpdatePublicKey() throws SocketTimeoutException, HttpClientException, Throwable {
		// pre-conditions
		String keyName = String.valueOf(System.currentTimeMillis());
		String publicKeyPath = createRandomTempFile().getAbsolutePath();
		String privateKeyPath = createRandomTempFile().getAbsolutePath();
		SSHKeyPair keyPair = SSHKeyPair.create(
				SSHKeyType.SSH_RSA,
				SSHKeyTestUtils.DEFAULT_PASSPHRASE,
				privateKeyPath,
				publicKeyPath);
		IOpenShiftSSHKey key = user.putSSHKey(keyName, keyPair);

		// operation
		String publicKey = SSHKeyPair.create(
				SSHKeyType.SSH_RSA,
				SSHKeyTestUtils.DEFAULT_PASSPHRASE,
				privateKeyPath,
				publicKeyPath).getPublicKey();
		key.setPublicKey(publicKey);

		// verification
		assertThat(key.getPublicKey()).isEqualTo(publicKey);
		IOpenShiftSSHKey openshiftKey = user.getSSHKeyByName(keyName);
		assertThat(
				new SSHKeyTestUtils.SSHPublicKeyAssertion(openshiftKey))
				.hasName(keyName)
				.hasPublicKey(publicKey)
				.isType(openshiftKey.getKeyType());
	}

	@Test
	public void shouldReturnKeyForName() throws SocketTimeoutException, HttpClientException, Throwable {
		// pre-conditions
		String keyName = String.valueOf(System.currentTimeMillis());
		String publicKeyPath = createRandomTempFile().getAbsolutePath();
		String privateKeyPath = createRandomTempFile().getAbsolutePath();
		SSHKeyTestUtils.createDsaKeyPair(publicKeyPath, privateKeyPath);
		ISSHPublicKey publicKey = new SSHPublicKey(publicKeyPath);

		// operation
		IOpenShiftSSHKey key = user.putSSHKey(keyName, publicKey);
		IOpenShiftSSHKey keyByName = user.getSSHKeyByName(keyName);

		// verifications
		assertThat(key).isEqualTo(keyByName);
	}

	@Test
	public void shouldUpdateKeyTypeAndPublicKey() throws SocketTimeoutException, HttpClientException, Throwable {
		// pre-conditions
		String keyName = String.valueOf(System.currentTimeMillis());
		String publicKeyPath = createRandomTempFile().getAbsolutePath();
		String privateKeyPath = createRandomTempFile().getAbsolutePath();
		SSHKeyTestUtils.createDsaKeyPair(publicKeyPath, privateKeyPath);
		ISSHPublicKey publicKey = new SSHPublicKey(publicKeyPath);
		assertThat(publicKey.getKeyType()).isEqualTo(SSHKeyType.SSH_DSA);
		IOpenShiftSSHKey key = user.putSSHKey(keyName, publicKey);
		SSHKeyPair keyPair = SSHKeyPair.create(
				SSHKeyType.SSH_RSA, SSHKeyTestUtils.DEFAULT_PASSPHRASE, privateKeyPath, publicKeyPath);

		// operation
		key.setKeyType(SSHKeyType.SSH_RSA, keyPair.getPublicKey());

		// verification
		assertThat(key.getKeyType()).isEqualTo(SSHKeyType.SSH_RSA);
		assertThat(key.getPublicKey()).isEqualTo(keyPair.getPublicKey());
	}
}

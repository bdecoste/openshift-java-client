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
		List<IOpenShiftSSHKey> sshKeys = user.getSshKeys();
		// verifications
		assertThat(sshKeys).isNotNull();
	}

	@Test
	public void shouldAddAndUpdateKey() throws SocketTimeoutException, HttpClientException, Throwable {
		// pre-conditions
		String keyName = String.valueOf(System.currentTimeMillis());
		String publicKeyPath = createRandomTempFile().getAbsolutePath();
		String privateKeyPath = createRandomTempFile().getAbsolutePath();
		SSHKeyTestUtils.createDsaKeyPair(publicKeyPath, privateKeyPath);
		SSHPublicKey publicKey = new SSHPublicKey(publicKeyPath);
		int numOfKeys = user.getSshKeys().size();

		// operation
		user.addSshKey(keyName, publicKey);

		// verifications
		List<IOpenShiftSSHKey> keys = user.getSshKeys();
		assertThat(keys.size()).isEqualTo(numOfKeys + 1);
		IOpenShiftSSHKey key = SSHKeyTestUtils.getKey(keyName, keys);
		assertThat(new SSHKeyTestUtils.SSHPublicKeyAssertion(key))
				.hasName(keyName).hasPublicKey(publicKey.getPublicKey()).isType(publicKey.getKeyType());
	}

	@Test
	public void shouldUpdateKeyTypeAndPublicKey() throws SocketTimeoutException, HttpClientException, Throwable {
//		// pre-conditions
//		String keyName = "default";
//		String keyUrl = service.getServiceUrl() + "user/keys/" + keyName;
//		String newPublicKey = "AAAAB3Nza...";
//
//		when(mockClient.get(urlEndsWith("/user/keys")))
//				.thenReturn(Samples.GET_USER_KEYS_SINGLE_JSON.getContentAsString());
//		when(mockClient.put(anyMapOf(String.class, Object.class), urlEndsWith(keyUrl)))
//				.thenReturn(Samples.UPDATE_USER_KEY_JSON.getContentAsString());
//
//		// operation
//		List<IOpenShiftSSHKey> keys = user.getSshKeys();
//		assertThat(keys).hasSize(1);
//		IOpenShiftSSHKey key = keys.get(0);
//		assertThat(key.getKeyType()).isEqualTo(SSHKeyType.SSH_RSA);
//		key.setKeyType(SSHKeyType.SSH_DSA, newPublicKey);
//
//		// verification
//		assertThat(key.getKeyType()).isEqualTo(SSHKeyType.SSH_DSA);
//		assertThat(key.getPublicKey()).isEqualTo(newPublicKey);
//		HashMap<String, Object> parameterMap = new HashMap<String, Object>();
//		parameterMap.put("type", SSH_DSA);
//		parameterMap.put("content", key.getPublicKey());
//		verify(mockClient).put(parameterMap, new URL(keyUrl));
	}

	@Test
	public void shouldUpdatePublicKey() throws SocketTimeoutException, HttpClientException, Throwable {
//		// pre-conditions
//		String keyName = "default";
//		String keyUrl = service.getServiceUrl() + "user/keys/" + keyName;
//		String newPublicKey = "AAAAB3Nza...";
//		when(mockClient.get(urlEndsWith("/user/keys")))
//				.thenReturn(Samples.GET_USER_KEYS_SINGLE_JSON.getContentAsString());
//		when(mockClient.put(anyMapOf(String.class, Object.class), urlEndsWith(keyUrl)))
//				.thenReturn(Samples.UPDATE_USER_KEY_RSA_JSON.getContentAsString());
//
//		// operation
//		List<IOpenShiftSSHKey> keys = user.getSshKeys();
//		assertThat(keys).hasSize(1);
//		IOpenShiftSSHKey key = keys.get(0);
//		assertThat(key.getKeyType()).isEqualTo(SSHKeyType.SSH_RSA);
//		assertThat(key.getPublicKey()).isNotEqualTo(newPublicKey);
//		key.setPublicKey(newPublicKey);
//
//		// verification
//		assertThat(key.getKeyType()).isEqualTo(SSHKeyType.SSH_RSA);
//		assertThat(key.getPublicKey()).isEqualTo(newPublicKey);
//		HashMap<String, Object> parameterMap = new HashMap<String, Object>();
//		parameterMap.put("type", SSHKeyTestUtils.SSH_RSA);
//		parameterMap.put("content", newPublicKey);
//		verify(mockClient).put(parameterMap, new URL(keyUrl));
	}


}

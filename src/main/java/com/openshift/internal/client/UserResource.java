/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package com.openshift.internal.client;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import com.openshift.client.IDomain;
import com.openshift.client.IOpenShiftConnection;
import com.openshift.client.IOpenShiftSSHKey;
import com.openshift.client.ISSHPublicKey;
import com.openshift.client.IUser;
import com.openshift.client.OpenShiftException;
import com.openshift.client.OpenShiftSSHKeyException;
import com.openshift.client.OpenShiftUnknonwSSHKeyTypeException;
import com.openshift.client.SSHKeyType;
import com.openshift.internal.client.response.KeyResourceDTO;
import com.openshift.internal.client.response.UserResourceDTO;
import com.openshift.internal.client.utils.CollectionUtils;
import com.openshift.internal.client.utils.IOpenShiftJsonConstants;

/**
 * @author André Dietisheim
 */
public class UserResource extends AbstractOpenShiftResource implements IUser {

	private final APIResource api;
	private final String rhLogin;
	private final String password;

	private List<SSHKeyResource> sshKeys;

	public UserResource(final APIResource api, final UserResourceDTO dto, final String password) {
		super(api.getService(), dto.getLinks());
		this.api = api;
		this.rhLogin = dto.getRhLogin();
		this.password = password;
	}

	public IOpenShiftConnection getConnection() {
		return api;
	}

	public String getRhlogin() {
		return rhLogin;
	}

	public String getPassword() {
		return password;
	}

	public String getAuthKey() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAuthIV() {
		// TODO Auto-generated method stub
		return null;
	}

	public IDomain createDomain(String name) throws OpenShiftException, SocketTimeoutException {
		return api.createDomain(name);
	}

	public List<IDomain> getDomains() throws OpenShiftException, SocketTimeoutException {
		return api.getDomains();
	}

	public IDomain getDefaultDomain() throws OpenShiftException, SocketTimeoutException {
		final List<IDomain> domains = api.getDomains();
		if (domains.size() > 0) {
			return domains.get(0);
		}
		return null;
	}

	public IDomain getDomain(String namespace) throws OpenShiftException, SocketTimeoutException {
		return api.getDomain(namespace);
	}

	public boolean hasDomain() throws OpenShiftException, SocketTimeoutException {
		return (api.getDomains().size() > 0);
	}

	public void refresh() throws OpenShiftException {
		// TODO Auto-generated method stub

	}

	public List<IOpenShiftSSHKey> getSSHKeys() throws SocketTimeoutException, OpenShiftUnknonwSSHKeyTypeException,
			OpenShiftException {
		List<IOpenShiftSSHKey> keys = new ArrayList<IOpenShiftSSHKey>();
		keys.addAll(getCachedOrLoadSSHKeys());
		return CollectionUtils.toUnmodifiableCopy(keys);
	}

	private List<SSHKeyResource> getCachedOrLoadSSHKeys() throws SocketTimeoutException, OpenShiftException,
			OpenShiftUnknonwSSHKeyTypeException {
		if (sshKeys == null) {
			this.sshKeys = loadKeys();
		}
		return sshKeys;
	}

	private List<SSHKeyResource> loadKeys() throws SocketTimeoutException, OpenShiftException,
			OpenShiftUnknonwSSHKeyTypeException {
		List<SSHKeyResource> keys = new ArrayList<SSHKeyResource>();
		List<KeyResourceDTO> keyDTOs = new GetSShKeysRequest().execute();
		for (KeyResourceDTO keyDTO : keyDTOs) {
			keys.add(new SSHKeyResource(keyDTO, this));
		}
		return keys;
	}

	public IOpenShiftSSHKey getSSHKeyByName(String name) throws SocketTimeoutException,
			OpenShiftUnknonwSSHKeyTypeException, OpenShiftException {
		IOpenShiftSSHKey matchingKey = null;
		if (name == null) {
			return null;
		}

		for (SSHKeyResource key : getCachedOrLoadSSHKeys()) {
			if (name.equals(key.getName())) {
				matchingKey = key;
				break;
			}
		}
		return matchingKey;
	}

	public IOpenShiftSSHKey getSSHKeyByPublicKey(String publicKey) throws SocketTimeoutException,
			OpenShiftUnknonwSSHKeyTypeException, OpenShiftException {
		IOpenShiftSSHKey matchingKey = null;
		if (publicKey == null) {
			return null;
		}

		for (SSHKeyResource key : getCachedOrLoadSSHKeys()) {
			if (publicKey.equals(key.getPublicKey())) {
				matchingKey = key;
				break;
			}
		}
		return matchingKey;
	}

	public boolean hasSSHKeyName(String name) throws SocketTimeoutException, OpenShiftUnknonwSSHKeyTypeException,
			OpenShiftException {
		return getSSHKeyByName(name) != null;
	}

	public boolean hasSSHPublicKey(String publicKey) throws SocketTimeoutException,
			OpenShiftUnknonwSSHKeyTypeException, OpenShiftException {
		return getSSHKeyByPublicKey(publicKey) != null;
	}

	/**
	 * Adds the given ssh key with the given name. Key names and public keys
	 * have to be unique. Throws OpenShiftSSHKeyException if either the key name
	 * or the public key are already used.
	 * 
	 * @param name
	 *            the name to identify the key
	 * @param key
	 *            the key to add
	 * @return
	 * @throws SocketTimeoutException
	 * @throws OpenShiftException
	 */
	public IOpenShiftSSHKey putSSHKey(String name, ISSHPublicKey key) throws SocketTimeoutException, OpenShiftException {
		if (hasSSHKeyName(name)) {
			throw new OpenShiftSSHKeyException(
					"Could not add new key {0} with the name {1}. There already is a key for this name, key names must be unique.",
					key.getPublicKey(), name);
		}
		if (hasSSHPublicKey(name)) {
			throw new OpenShiftSSHKeyException(
					"Could not add new key {0} with the name {1}. The key is already stored with a different name. Public key have to be unique.",
					key.getPublicKey(), name);
		}
		KeyResourceDTO keyDTO = new AddSShKeyRequest().execute(key.getKeyType(), name, key.getPublicKey());
		return put(keyDTO);
	}

	private SSHKeyResource put(KeyResourceDTO keyDTO) throws OpenShiftUnknonwSSHKeyTypeException {
		SSHKeyResource sshKey = new SSHKeyResource(keyDTO, this);
		sshKeys.add(sshKey);
		return sshKey;
	}

	protected void removeSSHKey(SSHKeyResource key) {
		sshKeys.remove(key);
	}

	private class GetSShKeysRequest extends ServiceRequest {

		public GetSShKeysRequest() throws SocketTimeoutException, OpenShiftException {
			super("LIST_KEYS");
		}

		public List<KeyResourceDTO> execute() throws SocketTimeoutException, OpenShiftException {
			return super.execute();
		}
	}

	private class AddSShKeyRequest extends ServiceRequest {

		public AddSShKeyRequest() throws SocketTimeoutException, OpenShiftException {
			super("ADD_KEY");
		}

		public KeyResourceDTO execute(SSHKeyType type, String name, String content)
				throws SocketTimeoutException, OpenShiftException {
			return super.execute(
					new ServiceParameter(IOpenShiftJsonConstants.PROPERTY_TYPE, type.getTypeId())
					, new ServiceParameter(IOpenShiftJsonConstants.PROPERTY_NAME, name)
					, new ServiceParameter(IOpenShiftJsonConstants.PROPERTY_CONTENT, content));
		}
	}

}

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
package com.openshift.internal.client;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import com.openshift.client.ISSHPublicKey;
import com.openshift.client.OpenShiftException;
import com.openshift.client.OpenShiftUnknonwSSHKeyTypeException;
import com.openshift.client.SSHKeyType;
import com.openshift.client.SSHPublicKey;
import com.openshift.internal.client.response.unmarshalling.dto.KeyResourceDTO;
import com.openshift.internal.client.response.unmarshalling.dto.UserResourceDTO;
import com.openshift.internal.client.utils.Assert;

/**
 * @author Andre Dietisheim
 */
public class UserResource extends AbstractOpenShiftResource {

	private String rhLogin;
	private List<ISSHPublicKey> sshKeys;

	public UserResource(IRestService service, UserResourceDTO dto) {
		super(service, dto.getLinks());
		this.rhLogin = dto.getRhLogin();
	}

	public String getRhLogin() {
		return rhLogin;
	}

	public List<ISSHPublicKey> getSSHKeys() throws SocketTimeoutException, OpenShiftUnknonwSSHKeyTypeException,
			OpenShiftException {
		if (sshKeys == null) {
			this.sshKeys = loadKeys();
		}
		return sshKeys;
	}

	public void addSSHKey(String name, ISSHPublicKey key) throws SocketTimeoutException, OpenShiftException {
		new AddSShKeyRequest().execute(key.getKeyType(), name, key.getPublicKey());
		Assert.isTrue(key instanceof AbstractSSHKey);
		AbstractSSHKey abstractSSHKey = (AbstractSSHKey) key;
		abstractSSHKey.setName(name);
	}

	private List<ISSHPublicKey> loadKeys() throws SocketTimeoutException, OpenShiftException,
			OpenShiftUnknonwSSHKeyTypeException {
		List<ISSHPublicKey> sshKeys = new ArrayList<ISSHPublicKey>();
		for (KeyResourceDTO keyDTO : new GetSShKeysRequest().execute()) {
			sshKeys.add(new SSHKeyResource(keyDTO, getService()));
		}
		return sshKeys;
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

		public List<KeyResourceDTO> execute(SSHKeyType type, String name, String content)
				throws SocketTimeoutException, OpenShiftException {
			return super.execute(
					new ServiceParameter("type", type.getTypeId())
					, new ServiceParameter("name", name)
					, new ServiceParameter("content", content));
		}
	}

}

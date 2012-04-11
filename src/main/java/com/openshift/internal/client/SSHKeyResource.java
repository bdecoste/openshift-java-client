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

import com.openshift.client.ISSHPublicKey;
import com.openshift.client.OpenShiftException;
import com.openshift.client.OpenShiftUnknonwSSHKeyTypeException;
import com.openshift.client.SSHKeyType;
import com.openshift.internal.client.response.unmarshalling.dto.KeyResourceDTO;

/**
 * @author Andre Dietisheim
 */
public class SSHKeyResource extends AbstractOpenShiftResource implements ISSHPublicKey {

	private String name;
	private SSHKeyType type;
	private String publicKey;

	protected SSHKeyResource(KeyResourceDTO dto, IRestService service) throws OpenShiftUnknonwSSHKeyTypeException {
		super(service, dto.getLinks());
		this.name = dto.getName();
		this.type = SSHKeyType.getByTypeId(dto.getType());
		this.publicKey = dto.getContent();
	}

	public void setType(SSHKeyType type) throws SocketTimeoutException, OpenShiftException {
		new UpdateKeyTypeRequest().execute(type);
		this.type = type;
	}
	
	public String getName() {
		return name;
	}

	public SSHKeyType getKeyType() {
		return type;
	}

	public void setPublicKey(String publicKey) throws SocketTimeoutException, OpenShiftException {
		new UpdateKeyContentRequest().execute(publicKey);
		this.publicKey = publicKey;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void destroy() throws SocketTimeoutException, OpenShiftException {
		new DeleteKeyRequest().execute();
		this.name = null;
		this.type = null;
		this.publicKey = null;
	}
	
	private class UpdateKeyTypeRequest extends ServiceRequest {

		private UpdateKeyTypeRequest() {
			super("UPDATE");
		}
		
		private void execute(SSHKeyType type) throws SocketTimeoutException, OpenShiftException {
			execute(new ServiceParameter("type", type.getTypeId()));
		}
	}

	private class UpdateKeyContentRequest extends ServiceRequest {

		private UpdateKeyContentRequest() {
			super("UPDATE");
		}
		
		private void execute(String publicKey) throws SocketTimeoutException, OpenShiftException {
			execute(new ServiceParameter("content", publicKey));
		}
	}

	private class DeleteKeyRequest extends ServiceRequest {

		private DeleteKeyRequest() {
			super("DELETE");
		}
		
		private void execute() throws SocketTimeoutException, OpenShiftException {
			super.execute();
		}
	}

}

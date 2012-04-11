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

import com.openshift.client.ISSHPublicKey;
import com.openshift.client.OpenShiftUnknonwSSHKeyTypeException;
import com.openshift.client.SSHKeyType;

/**
 * @author Andre Dietisheim
 */
public abstract class AbstractSSHKey implements ISSHPublicKey {

	private String name;
	private SSHKeyType keyType;

	protected AbstractSSHKey(String name, String sshKeyType) throws OpenShiftUnknonwSSHKeyTypeException {
		this(name, SSHKeyType.getByTypeId(sshKeyType));
	}

	protected AbstractSSHKey(String name, SSHKeyType sshKeyType) {
		this.name = name;
		this.keyType = sshKeyType;
	}

	public String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	public SSHKeyType getKeyType() {
		return keyType;
	}

	protected void setKeyType(String keyTypeId) throws OpenShiftUnknonwSSHKeyTypeException {
		setKeyType(SSHKeyType.getByTypeId(keyTypeId));
	}

	protected void setKeyType(SSHKeyType keyType) {
		this.keyType = keyType;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((keyType == null) ? 0 : keyType.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractSSHKey other = (AbstractSSHKey) obj;
		if (keyType != other.keyType)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AbstractSSHKey [" +
				"name=" + name
				+ ", keyType=" + keyType
				+ "]";
	}

}

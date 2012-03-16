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
package com.openshift.express.internal.client.response.unmarshalling.dto;

import java.util.List;

/**
 * @author Xavier Coulon
 */
public class DomainDTO {

	private final String namespace;
	private final List<Operation> operations;
	
	public DomainDTO(final String namespace, final List<Operation> operations) {
		this.namespace = namespace;
		this.operations = operations;
	}

	/**
	 * @return the namespace
	 */
	protected final String getNamespace() {
		return namespace;
	}

	/**
	 * @return the operations
	 */
	protected final List<Operation> getOperations() {
		return operations;
	}
}

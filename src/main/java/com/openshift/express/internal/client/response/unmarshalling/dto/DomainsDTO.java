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

public class DomainsDTO {
	
	private final List<DomainDTO> domains;
	
	private final List<Operation> operations;
	
	public DomainsDTO(final List<DomainDTO> domains, final List<Operation> operations) {
		this.domains = domains;
		this.operations = operations;
	}

	/**
	 * @return the domains
	 */
	public List<DomainDTO> getDomains() {
		return domains;
	}

	/**
	 * @return the operations
	 */
	public List<Operation> getOperations() {
		return operations;
	}

}

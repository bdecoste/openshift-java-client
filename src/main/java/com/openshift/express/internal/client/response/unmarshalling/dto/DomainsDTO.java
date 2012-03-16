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

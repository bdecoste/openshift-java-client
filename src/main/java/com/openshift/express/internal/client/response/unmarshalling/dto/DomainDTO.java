package com.openshift.express.internal.client.response.unmarshalling.dto;

import java.util.List;

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

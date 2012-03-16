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

import java.util.ArrayList;
import java.util.List;

import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

import com.openshift.express.client.OpenShiftException;
import com.openshift.express.internal.client.utils.IOpenShiftJsonConstants;

/**
 * @author Xavier Coulon
 */
public class ObjectFactory {

	@SuppressWarnings("unchecked")
	public static <T> T get(String content, Class<T> clazz) throws OpenShiftException {
		ModelNode node = getModelNode(content);
		if (clazz.isAssignableFrom(DomainsDTO.class)) {
			return (T) createDomainsDTO(node);
		}
		return null;
	}

	private static ModelNode getModelNode(String content) throws OpenShiftException {
		final ModelNode node = ModelNode.fromJSONString(content);
		if (!node.isDefined()) {
			throw new OpenShiftException("Failed to read response.");
		}

		if (!isStatusOk(node)) {
			String messages = node.get(IOpenShiftJsonConstants.PROPERTY_MESSAGES).asString();
			throw new OpenShiftException("Failed to perform operation:\n{0}", messages);
		}
		return node;
	}

	private static boolean isStatusOk(final ModelNode node) {
		return IOpenShiftJsonConstants.STATUS_OK.equals(
				node.get(IOpenShiftJsonConstants.PROPERTY_STATUS).asString());
	}

	private static DomainsDTO createDomainsDTO(ModelNode rootNode) throws OpenShiftException {
		if (!isDomainsType(rootNode)) {
			throw new OpenShiftException("Expected response type '{0}', but received '{1}'", 
					IOpenShiftJsonConstants.PROPERTY_DOMAINS,
					rootNode.get(IOpenShiftJsonConstants.PROPERTY_TYPE).asString());
		}
		final List<DomainDTO> domains = new ArrayList<DomainDTO>();
		final List<Operation> operations = new ArrayList<Operation>();
		for (ModelNode dataNode : rootNode.get(IOpenShiftJsonConstants.PROPERTY_DATA).asList()) {
			if (dataNode.getType() == ModelType.OBJECT) {
				domains.add(createDomainDTO(dataNode));
			} else {
				throw new OpenShiftException("Unexpected node type: {0}", dataNode.getType());
			}
		}

		return new DomainsDTO(domains, operations);
	}

	private static boolean isDomainsType(ModelNode rootNode) {
		return IOpenShiftJsonConstants.PROPERTY_DOMAINS.equals(
				rootNode.get(IOpenShiftJsonConstants.PROPERTY_TYPE).asString());
	}

	private static DomainDTO createDomainDTO(ModelNode domainNode) {
		final ModelNode namespaceNode = domainNode.get(IOpenShiftJsonConstants.PROPERTY_NAMESPACE);
		final String namespace = namespaceNode.isDefined() ? namespaceNode.asString() : null;
		List<Operation> operations = createLinks(domainNode.get(IOpenShiftJsonConstants.PROPERTY_LINKS).asList());
		return new DomainDTO(namespace, operations);
	}

	private static List<Operation> createLinks(List<ModelNode> linkNodes) {
		List<Operation> operations = new ArrayList<Operation>();
		return operations;
	}

}

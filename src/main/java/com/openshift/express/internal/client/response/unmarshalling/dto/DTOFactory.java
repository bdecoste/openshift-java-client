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

import static com.openshift.express.internal.client.utils.IOpenShiftJsonConstants.PROPERTY_DATA;
import static com.openshift.express.internal.client.utils.IOpenShiftJsonConstants.PROPERTY_DOMAIN;
import static com.openshift.express.internal.client.utils.IOpenShiftJsonConstants.PROPERTY_DOMAINS;
import static com.openshift.express.internal.client.utils.IOpenShiftJsonConstants.PROPERTY_LINKS;
import static com.openshift.express.internal.client.utils.IOpenShiftJsonConstants.PROPERTY_MESSAGES;
import static com.openshift.express.internal.client.utils.IOpenShiftJsonConstants.PROPERTY_NAMESPACE;
import static com.openshift.express.internal.client.utils.IOpenShiftJsonConstants.PROPERTY_STATUS;
import static com.openshift.express.internal.client.utils.IOpenShiftJsonConstants.PROPERTY_TYPE;
import static com.openshift.express.internal.client.utils.IOpenShiftJsonConstants.STATUS_OK;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

import com.openshift.express.client.OpenShiftException;

/**
 * @author Xavier Coulon
 */
public class DTOFactory {

	@SuppressWarnings("unchecked")
	public static <T> T get(final String content, final Class<T> clazz) throws OpenShiftException {
		ModelNode node = getModelNode(content);
		if (clazz.isAssignableFrom(DomainsDTO.class)) {
			return (T) createDomainsDTO(node);
		}
		if (clazz.isAssignableFrom(DomainDTO.class)) {
			return (T) createDomainDTO(node);
		}
		return null;
	}

	private static ModelNode getModelNode(final String content) throws OpenShiftException {
		final ModelNode node = ModelNode.fromJSONString(content);
		if (!node.isDefined()) {
			throw new OpenShiftException("Failed to read response.");
		}

		if (!isStatusOk(node)) {
			String messages = node.get(PROPERTY_MESSAGES).asString();
			throw new OpenShiftException("Failed to perform operation:\n{0}", messages);
		}
		return node;
	}

	private static boolean isStatusOk(final ModelNode node) {
		return STATUS_OK.equals(node.get(PROPERTY_STATUS).asString());
	}

	private static DomainsDTO createDomainsDTO(final ModelNode rootNode) throws OpenShiftException {
		if (!isDomainsType(rootNode)) {
			throw new OpenShiftException("Expected response type '{0}', but received '{1}'", PROPERTY_DOMAINS, rootNode
					.get(PROPERTY_TYPE).asString());
		}
		final List<DomainDTO> domains = new ArrayList<DomainDTO>();
		final List<Link> operations = new ArrayList<Link>();
		// temporarily supporting absence of 'data' node in the 'domain' response message
		// FIXME: simplify once openshift response is fixed
		if (rootNode.get(PROPERTY_DATA).isDefined()) {
			for (ModelNode dataNode : rootNode.get(PROPERTY_DATA).asList()) {
				if (dataNode.getType() == ModelType.OBJECT) {
					domains.add(createDomainDTO(dataNode));
				} else {
					throw new OpenShiftException("Unexpected node type: {0}", dataNode.getType());
				}
			}
		} else {
			final ModelNode domainNode = rootNode.get(PROPERTY_DOMAIN);
			if (domainNode.isDefined() && domainNode.getType() == ModelType.OBJECT) {
				domains.add(createDomainDTO(domainNode));
			} else {
				throw new OpenShiftException("Unexpected node type: {0}", domainNode.getType());
			}
		}

		return new DomainsDTO(domains, operations);
	}

	private static boolean isDomainsType(final ModelNode rootNode) {
		return PROPERTY_DOMAINS.equals(rootNode.get(PROPERTY_TYPE).asString());
	}

	private static DomainDTO createDomainDTO(final ModelNode domainNode) {
		final ModelNode dataNode = domainNode.get(PROPERTY_DATA);
		if(dataNode.isDefined()) {
			return createDomainDTO(dataNode);
		} 
		final ModelNode namespaceNode = domainNode.get(PROPERTY_NAMESPACE);
		final String namespace = namespaceNode.isDefined() ? namespaceNode.asString() : null;
		final Map<String, Link> operations = createLinks(domainNode.get(PROPERTY_LINKS).asList());
		return new DomainDTO(namespace, operations);
	}

	private static Map<String, Link> createLinks(final List<ModelNode> linkNodes) {
		Map<String, Link> links = new HashMap<String, Link>();
		for (ModelNode linkNode : linkNodes) {
			final String linkName = linkNode.asProperty().getName();
			final ModelNode valueNode = linkNode.asProperty().getValue();
			final String rel = valueNode.get("rel").asString();
			final String href = valueNode.get("href").asString();
			final String method = valueNode.get("method").asString();
			final List<LinkParam> requiredParams = createLinkParams(valueNode.get("required-params"));
			final List<LinkParam> optionalParams = createLinkParams(valueNode.get("optional-params"));
			links.put(linkName, new Link(rel, href, method, requiredParams, optionalParams));
		}
		return links;
	}

	private static List<LinkParam> createLinkParams(ModelNode linkParamNodes) {
		List<LinkParam> linkParams = new ArrayList<LinkParam>();
		if (linkParamNodes.isDefined()) {
			for (ModelNode linkParamNode : linkParamNodes.asList()) {
				linkParams.add(createLinkParam(linkParamNode));
			}
		}
		return linkParams;
	}

	private static LinkParam createLinkParam(ModelNode linkParamNode) {
		final String description = linkParamNode.get("description").asString();
		final String type = linkParamNode.get("type").asString();
		final String defaultValue = linkParamNode.get("default-value").asString();
		final String name = linkParamNode.get("name").asString();
		final List<String> validOptions = new ArrayList<String>();
		for (ModelNode validOptionNode : linkParamNode.get("valid-options").asList()) {
			validOptions.add(validOptionNode.asString());
		}
		return new LinkParam(name, type, defaultValue, description, validOptions);
	}
}

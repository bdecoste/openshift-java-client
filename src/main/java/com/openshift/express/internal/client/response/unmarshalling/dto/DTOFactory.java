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
import static com.openshift.express.internal.client.utils.IOpenShiftJsonConstants.PROPERTY_LINKS;
import static com.openshift.express.internal.client.utils.IOpenShiftJsonConstants.PROPERTY_NAMESPACE;
import static com.openshift.express.internal.client.utils.IOpenShiftJsonConstants.STATUS_CREATED;
import static com.openshift.express.internal.client.utils.IOpenShiftJsonConstants.STATUS_OK;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.jboss.dmr.Property;

import com.openshift.express.client.OpenShiftException;

/**
 * A factory for creating DTO objects.
 * 
 * @author Xavier Coulon
 */
public class DTOFactory {

	/**
	 * Gets the.
	 * 
	 * @param content
	 *            the content
	 * @return the response
	 * @throws OpenShiftException
	 *             the open shift exception
	 */
	public static Response get(final String content) throws OpenShiftException {
		final ModelNode rootNode = getModelNode(content);
		final Response response = createResponse(rootNode);
		if (isStatusOk(response)) {
			return response;
		}
		throw new OpenShiftException("Operation failed", response);
	}

	/**
	 * Checks if is status ok.
	 * 
	 * @param response
	 *            the response dto
	 * @return true, if is status ok
	 */
	private static boolean isStatusOk(final Response response) {
		return STATUS_OK.equals(response.getStatus()) || STATUS_CREATED.equals(response.getStatus());
	}

	/**
	 * Creates a new DTO object.
	 * 
	 * @param rootNode
	 *            the root node
	 * @return the response
	 * @throws OpenShiftException
	 *             the open shift exception
	 */
	private static Response createResponse(ModelNode rootNode) throws OpenShiftException {
		final String type = rootNode.get("type").asString();
		final String status = rootNode.get("status").asString();
		final List<String> messages = createMessages(rootNode.get("messages"));

		final EnumDataType dataType = EnumDataType.nullSafeValueOf(type);
		switch (dataType) {
		case domains:
			return new Response(status, messages, createDomainsDTO(rootNode), dataType);
		case domain:
			return new Response(status, messages, createDomainDTO(rootNode), dataType);
		case applications:
			return new Response(status, messages, createApplicationsDTO(rootNode), dataType);
		case application:
			return new Response(status, messages, createApplicationDTO(rootNode), dataType);
		case cartridges:
			return new Response(status, messages, createCartridgesDTO(rootNode), dataType);
		case cartridge:
			return new Response(status, messages, createCartridgeDTO(rootNode), dataType);
		default:
			return null;
		}
	}

	private static List<String> createMessages(ModelNode messagesNode) {
		List<String> messages = new ArrayList<String>();
		if (messagesNode.getType() == ModelType.LIST) {
			for (ModelNode messageNode : messagesNode.asList()) {
				messages.add(messageNode.asString());
			}
		}
		return messages;
	}

	/**
	 * Gets the model node.
	 * 
	 * @param content
	 *            the content
	 * @return the model node
	 * @throws OpenShiftException
	 *             the open shift exception
	 */
	private static ModelNode getModelNode(final String content) throws OpenShiftException {
		final ModelNode node = ModelNode.fromJSONString(content);
		if (!node.isDefined()) {
			throw new OpenShiftException("Failed to read response.");
		}

		return node;
	}

	/**
	 * Creates a new DTO object.
	 * 
	 * @param rootNode
	 *            the root node
	 * @return the list< domain dt o>
	 * @throws OpenShiftException
	 *             the open shift exception
	 */
	private static List<DomainDTO> createDomainsDTO(final ModelNode rootNode) throws OpenShiftException {
		final List<DomainDTO> domains = new ArrayList<DomainDTO>();
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

		return domains;
	}

	/**
	 * Creates a new DTO object.
	 * 
	 * @param domainNode
	 *            the domain node
	 * @return the domain dto
	 */
	private static DomainDTO createDomainDTO(final ModelNode domainNode) {
		final ModelNode dataNode = domainNode.get(PROPERTY_DATA);
		if (dataNode.isDefined()) {
			// loop inside 'data' node
			return createDomainDTO(dataNode);
		}
		final String namespace = getAsString(domainNode, PROPERTY_NAMESPACE);
		final Map<String, Link> links = createLinks(domainNode.get(PROPERTY_LINKS).asList());
		return new DomainDTO(namespace, links);
	}

	/**
	 * Creates a new DTO object.
	 * 
	 * @param rootNode
	 *            the domain node
	 * @return the list< application dt o>
	 */
	private static List<ApplicationDTO> createApplicationsDTO(final ModelNode rootNode) {
		final List<ApplicationDTO> applicationDTOs = new ArrayList<ApplicationDTO>();
		final ModelNode dataNode = rootNode.get(PROPERTY_DATA);
		if (dataNode.isDefined()) {
			for (ModelNode applicationNode : dataNode.asList()) {
				applicationDTOs.add(createApplicationDTO(applicationNode));
			}
		}
		return applicationDTOs;
	}

	/**
	 * Creates a new DTO object.
	 * 
	 * @param appNode
	 *            the app node
	 * @return the application dto
	 */
	private static ApplicationDTO createApplicationDTO(ModelNode appNode) {
		final ModelNode dataNode = appNode.get(PROPERTY_DATA);
		if (dataNode.isDefined()) {
			// loop inside 'data' node
			return createApplicationDTO(dataNode);
		}
		final String framework = getAsString(appNode, "framework");
		final String creationTime = getAsString(appNode, "creation_time");
		final String name = getAsString(appNode, "name");
		final String uuid = getAsString(appNode, "uuid");
		final String domainId = getAsString(appNode, "domain_id");
		final Map<String, Link> links = createLinks(appNode.get("links").asList());
		final List<String> aliases = createAliases(appNode.get("aliases").asList());
		final Map<String, String> embeddedCartridges = createEmbeddedCartridges(appNode.get("embedded").asList());

		return new ApplicationDTO(framework, domainId, creationTime, name, uuid, aliases, embeddedCartridges, links);
	}

	private static Map<String, String> createEmbeddedCartridges(List<ModelNode> embeddedCartridgeNodes) {
		final Map<String, String> embeddedCartridges = new HashMap<String, String>();
		if (embeddedCartridgeNodes != null) {
			for (ModelNode embeddedCartridgeNode : embeddedCartridgeNodes) {
				if (embeddedCartridgeNode.getType() == ModelType.PROPERTY) {
					final Property p = embeddedCartridgeNode.asProperty();
					embeddedCartridges.put(p.getName(), p.getValue().get("info").asString());
				}
			}
		}
		return embeddedCartridges;
	}

	private static List<CartridgeDTO> createCartridgesDTO(ModelNode rootNode) {
		final List<CartridgeDTO> cartridgesDTOs = new ArrayList<CartridgeDTO>();
		final ModelNode dataNode = rootNode.get(PROPERTY_DATA);
		if (dataNode.isDefined()) {
			for (ModelNode cartridgeNode : dataNode.asList()) {
				cartridgesDTOs.add(createCartridgeDTO(cartridgeNode));
			}
		}
		return cartridgesDTOs;
	}

	private static CartridgeDTO createCartridgeDTO(ModelNode cartridgeNode) {
		final ModelNode dataNode = cartridgeNode.get(PROPERTY_DATA);
		if (dataNode.isDefined()) {
			// loop inside 'data' node
			return createCartridgeDTO(dataNode);
		}
		final String name = getAsString(cartridgeNode, "name");
		final String type = getAsString(cartridgeNode, "type");
		final Map<String, Link> links = createLinks(cartridgeNode.get("links").asList());
		return new CartridgeDTO(name, type, links);
	}

	private static List<String> createAliases(List<ModelNode> aliasNodeList) {
		final List<String> aliases = new ArrayList<String>();
		for (ModelNode aliasNode : aliasNodeList) {
			aliases.add(aliasNode.asString());
		}
		return aliases;
	}

	/**
	 * Creates a new DTO object.
	 * 
	 * @param linkNodes
	 *            the link nodes
	 * @return the map< string, link>
	 */
	private static Map<String, Link> createLinks(final List<ModelNode> linkNodes) {
		Map<String, Link> links = new HashMap<String, Link>();
		for (ModelNode linkNode : linkNodes) {
			final String linkName = linkNode.asProperty().getName();
			final ModelNode valueNode = linkNode.asProperty().getValue();
			final String rel = valueNode.get("rel").asString();
			final String href = valueNode.get("href").asString();
			final String method = valueNode.get("method").asString();
			final List<LinkParam> requiredParams = createLinkParams(valueNode.get("required_params"));
			final List<LinkParam> optionalParams = createLinkParams(valueNode.get("optional_params"));
			links.put(linkName, new Link(rel, href, method, requiredParams, optionalParams));
		}
		return links;
	}

	/**
	 * Creates a new DTO object.
	 * 
	 * @param linkParamNodes
	 *            the link param nodes
	 * @return the list< link param>
	 */
	private static List<LinkParam> createLinkParams(ModelNode linkParamNodes) {
		List<LinkParam> linkParams = new ArrayList<LinkParam>();
		if (linkParamNodes.isDefined()) {
			for (ModelNode linkParamNode : linkParamNodes.asList()) {
				linkParams.add(createLinkParam(linkParamNode));
			}
		}
		return linkParams;
	}

	/**
	 * Creates a new DTO object.
	 * 
	 * @param linkParamNode
	 *            the link param node
	 * @return the link param
	 */
	private static LinkParam createLinkParam(ModelNode linkParamNode) {
		final String description = linkParamNode.get("description").asString();
		final String type = linkParamNode.get("type").asString();
		final String defaultValue = linkParamNode.get("default-value").asString();
		final String name = linkParamNode.get("name").asString();
		final List<String> validOptions = new ArrayList<String>();
		final ModelNode validOptionsNode = linkParamNode.get("valid_options");
		if (validOptionsNode.isDefined()) {
			switch (validOptionsNode.getType()) {
			case STRING: // if there's only one value, it is not serialized as a list, but just a string
				validOptions.add(validOptionsNode.asString());
				break;
			case LIST:
				for (ModelNode validOptionNode : validOptionsNode.asList()) {
					validOptions.add(validOptionNode.asString());
				}
				break;
			default:
				break;
			}
		}
		return new LinkParam(name, type, defaultValue, description, validOptions);
	}

	/**
	 * Returns the property identified by the given name in the given model node, or null if the named property is
	 * undefined.
	 * 
	 * @param node
	 *            the model node
	 * @param propertyName
	 *            the name of the property
	 * @return the property as a String
	 */
	private static String getAsString(final ModelNode node, String propertyName) {
		final ModelNode propertyNode = node.get(propertyName);
		return propertyNode.isDefined() ? propertyNode.asString() : null;
	}
}

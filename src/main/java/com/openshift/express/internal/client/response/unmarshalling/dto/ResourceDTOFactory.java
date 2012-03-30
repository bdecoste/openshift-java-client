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
import static com.openshift.express.internal.client.utils.IOpenShiftJsonConstants.PROPERTY_LOGIN;
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
public class ResourceDTOFactory {

	/**
	 * Gets the.
	 * 
	 * @param content
	 *            the content
	 * @return the response
	 * @throws OpenShiftException
	 *             the open shift exception
	 */
	public static RestResponse get(final String content) throws OpenShiftException {
		final ModelNode rootNode = getModelNode(content);
		return createResponse(rootNode);
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
	private static RestResponse createResponse(ModelNode rootNode) throws OpenShiftException {
		final String type = rootNode.get("type").asString();
		final String status = rootNode.get("status").asString();
		final List<String> messages = createMessages(rootNode.get("messages"));

		final EnumDataType dataType = EnumDataType.safeValueOf(type);
		// the response is after an error, only the messages are relevant
		if(dataType == null) {
			return new RestResponse(status, messages, null, null);
		}
		switch (dataType) {
		case user:
			return new RestResponse(status, messages, createUser(rootNode), dataType);
		case keys:
			return new RestResponse(status, messages, createKeys(rootNode), dataType);
		case key:
			return new RestResponse(status, messages, createKey(rootNode), dataType);
		case links:
			return new RestResponse(status, messages, createLinks(rootNode), dataType);
		case domains:
			return new RestResponse(status, messages, createDomainsDTO(rootNode), dataType);
		case domain:
			return new RestResponse(status, messages, createDomainDTO(rootNode), dataType);
		case applications:
			return new RestResponse(status, messages, createApplicationsDTO(rootNode), dataType);
		case application:
			return new RestResponse(status, messages, createApplicationDTO(rootNode), dataType);
		case cartridges:
			return new RestResponse(status, messages, createCartridgesDTO(rootNode), dataType);
		case cartridge:
			return new RestResponse(status, messages, createCartridgeDTO(rootNode), dataType);
		default:
			return null;
		}
	}

	/**
	 * Creates a new ResourceDTO object.
	 *
	 * @param messagesNode the messages node
	 * @return the list< string>
	 */
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
	 * Creates a new ResourceDTO object.
	 *
	 * @param rootNode the root node
	 * @return the user resource dto
	 */
	private static UserResourceDTO createUser(ModelNode rootNode) {
		final ModelNode dataNode = rootNode.get(PROPERTY_DATA);
		if (dataNode.isDefined()) {
			// loop inside 'data' node
			return createUser(dataNode);
		}
		final String namespace = getAsString(rootNode, PROPERTY_LOGIN);
		final Map<String, Link> links = createLinks(rootNode.get(PROPERTY_LINKS).asList());
		return new UserResourceDTO(namespace, links);
	}
	
	/**
	 * Creates a new ResourceDTO object.
	 *
	 * @param rootNode the root node
	 * @return the list< key resource dt o>
	 * @throws OpenShiftException the open shift exception
	 */
	private static List<KeyResourceDTO> createKeys(ModelNode rootNode) throws OpenShiftException {
		final List<KeyResourceDTO> keys = new ArrayList<KeyResourceDTO>();
		// temporarily supporting single and multiple values for 'keys' node
		if (rootNode.get(PROPERTY_DATA).isDefined()) {
			for (ModelNode dataNode : rootNode.get(PROPERTY_DATA).asList()) {
				if (dataNode.getType() == ModelType.OBJECT) {
					keys.add(createKey(dataNode));
				} 
			}
		}
		return keys;
	}

	
	/**
	 * Creates a new ResourceDTO object.
	 *
	 * @param keyNode the key node
	 * @return the key resource dto
	 */
	private static KeyResourceDTO createKey(ModelNode keyNode) {
		final ModelNode dataNode = keyNode.get(PROPERTY_DATA);
		if (dataNode.isDefined()) {
			// loop inside 'data' node
			return createKey(dataNode);
		}
		final String name = getAsString(keyNode, "name");
		final String type = getAsString(keyNode, "type");
		final String content = getAsString(keyNode, "content");
		final Map<String, Link> links = createLinks(keyNode.get(PROPERTY_LINKS).asList());
		return new KeyResourceDTO(name, type, content, links);
	}


	/**
	 * Creates a new set of indexed links.
	 * 
	 * @param rootNode
	 *            the root node
	 * @return the list< domain dt o>
	 * @throws OpenShiftException
	 *             the open shift exception
	 */
	private static Map<String, Link> createLinks(final ModelNode rootNode) throws OpenShiftException {
		final ModelNode dataNode = rootNode.get(PROPERTY_DATA);
		if (dataNode.isDefined()) {
			// loop inside 'data' node
			return createLinks(dataNode);
		}
		return createLinks(rootNode.asList());
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
	private static List<DomainResourceDTO> createDomainsDTO(final ModelNode rootNode) throws OpenShiftException {
		final List<DomainResourceDTO> domains = new ArrayList<DomainResourceDTO>();
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
	private static DomainResourceDTO createDomainDTO(final ModelNode domainNode) {
		final ModelNode dataNode = domainNode.get(PROPERTY_DATA);
		if (dataNode.isDefined()) {
			// loop inside 'data' node
			return createDomainDTO(dataNode);
		}
		final String namespace = getAsString(domainNode, PROPERTY_NAMESPACE);
		final Map<String, Link> links = createLinks(domainNode.get(PROPERTY_LINKS).asList());
		return new DomainResourceDTO(namespace, links);
	}

	/**
	 * Creates a new DTO object.
	 * 
	 * @param rootNode
	 *            the domain node
	 * @return the list< application dt o>
	 */
	private static List<ApplicationResourceDTO> createApplicationsDTO(final ModelNode rootNode) {
		final List<ApplicationResourceDTO> applicationDTOs = new ArrayList<ApplicationResourceDTO>();
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
	private static ApplicationResourceDTO createApplicationDTO(ModelNode appNode) {
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

		return new ApplicationResourceDTO(framework, domainId, creationTime, name, uuid, aliases, embeddedCartridges,
				links);
	}

	/**
	 * Creates a new ResourceDTO object.
	 *
	 * @param embeddedCartridgeNodes the embedded cartridge nodes
	 * @return the map< string, string>
	 */
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

	/**
	 * Creates a new ResourceDTO object.
	 *
	 * @param rootNode the root node
	 * @return the list< cartridge resource dt o>
	 */
	private static List<CartridgeResourceDTO> createCartridgesDTO(ModelNode rootNode) {
		final List<CartridgeResourceDTO> cartridgesDTOs = new ArrayList<CartridgeResourceDTO>();
		final ModelNode dataNode = rootNode.get(PROPERTY_DATA);
		if (dataNode.isDefined()) {
			for (ModelNode cartridgeNode : dataNode.asList()) {
				cartridgesDTOs.add(createCartridgeDTO(cartridgeNode));
			}
		}
		return cartridgesDTOs;
	}

	/**
	 * Creates a new ResourceDTO object.
	 *
	 * @param cartridgeNode the cartridge node
	 * @return the cartridge resource dto
	 */
	private static CartridgeResourceDTO createCartridgeDTO(ModelNode cartridgeNode) {
		final ModelNode dataNode = cartridgeNode.get(PROPERTY_DATA);
		if (dataNode.isDefined()) {
			// loop inside 'data' node
			return createCartridgeDTO(dataNode);
		}
		final String name = getAsString(cartridgeNode, "name");
		final String type = getAsString(cartridgeNode, "type");
		final Map<String, Link> links = createLinks(cartridgeNode.get("links").asList());
		return new CartridgeResourceDTO(name, type, links);
	}

	/**
	 * Creates a new ResourceDTO object.
	 *
	 * @param aliasNodeList the alias node list
	 * @return the list< string>
	 */
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
			if (valueNode.isDefined()) {
				final String rel = valueNode.get("rel").asString();
				final String href = valueNode.get("href").asString();
				final String method = valueNode.get("method").asString();
				final List<LinkParameter> requiredParams = createLinkParameters(valueNode.get("required_params"));
				final List<LinkParameter> optionalParams = createLinkParameters(valueNode.get("optional_params"));
				links.put(linkName, new Link(rel, href, method, requiredParams, optionalParams));
			}
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
	private static List<LinkParameter> createLinkParameters(ModelNode linkParamNodes) {
		List<LinkParameter> linkParams = new ArrayList<LinkParameter>();
		if (linkParamNodes.isDefined()) {
			for (ModelNode linkParamNode : linkParamNodes.asList()) {
				linkParams.add(createLinkParameter(linkParamNode));
			}
		}
		return linkParams;
	}

	/**
	 * Creates a new link parameter for the given link parameter node.
	 * 
	 * @param linkParamNode the model node that contains the link parameters
	 * @return the link parameter
	 */
	private static LinkParameter createLinkParameter(ModelNode linkParamNode) {
		final String description = linkParamNode.get("description").asString();
		final String type = linkParamNode.get("type").asString();
		final String defaultValue = linkParamNode.get("default_value").asString();
		final String name = linkParamNode.get("name").asString();
		return new LinkParameter(name, type, defaultValue, description, createValidOptions(linkParamNode));
	}

	/**
	 * Gets the valid options.
	 *
	 * @param linkParamNode the link param node
	 * @return the valid options
	 */
	private static List<String> createValidOptions(ModelNode linkParamNode) {
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
		return validOptions;
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

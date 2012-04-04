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

import static com.openshift.express.internal.client.utils.IOpenShiftJsonConstants.PROPERTY_ALIASES;
import static com.openshift.express.internal.client.utils.IOpenShiftJsonConstants.PROPERTY_CREATION_TIME;
import static com.openshift.express.internal.client.utils.IOpenShiftJsonConstants.PROPERTY_DATA;
import static com.openshift.express.internal.client.utils.IOpenShiftJsonConstants.PROPERTY_DOMAIN;
import static com.openshift.express.internal.client.utils.IOpenShiftJsonConstants.PROPERTY_DOMAIN_ID;
import static com.openshift.express.internal.client.utils.IOpenShiftJsonConstants.PROPERTY_EMBEDDED;
import static com.openshift.express.internal.client.utils.IOpenShiftJsonConstants.PROPERTY_FRAMEWORK;
import static com.openshift.express.internal.client.utils.IOpenShiftJsonConstants.PROPERTY_GEARS_COMPONENTS;
import static com.openshift.express.internal.client.utils.IOpenShiftJsonConstants.PROPERTY_GIT_URL;
import static com.openshift.express.internal.client.utils.IOpenShiftJsonConstants.PROPERTY_HREF;
import static com.openshift.express.internal.client.utils.IOpenShiftJsonConstants.PROPERTY_INFO;
import static com.openshift.express.internal.client.utils.IOpenShiftJsonConstants.PROPERTY_INTERNAL_PORT;
import static com.openshift.express.internal.client.utils.IOpenShiftJsonConstants.PROPERTY_LINKS;
import static com.openshift.express.internal.client.utils.IOpenShiftJsonConstants.PROPERTY_LOGIN;
import static com.openshift.express.internal.client.utils.IOpenShiftJsonConstants.PROPERTY_METHOD;
import static com.openshift.express.internal.client.utils.IOpenShiftJsonConstants.PROPERTY_NAME;
import static com.openshift.express.internal.client.utils.IOpenShiftJsonConstants.PROPERTY_NAMESPACE;
import static com.openshift.express.internal.client.utils.IOpenShiftJsonConstants.PROPERTY_OPTIONAL_PARAMS;
import static com.openshift.express.internal.client.utils.IOpenShiftJsonConstants.PROPERTY_PROXY_HOST;
import static com.openshift.express.internal.client.utils.IOpenShiftJsonConstants.PROPERTY_PROXY_PORT;
import static com.openshift.express.internal.client.utils.IOpenShiftJsonConstants.PROPERTY_REL;
import static com.openshift.express.internal.client.utils.IOpenShiftJsonConstants.PROPERTY_REQUIRED_PARAMS;
import static com.openshift.express.internal.client.utils.IOpenShiftJsonConstants.PROPERTY_SUFFIX;
import static com.openshift.express.internal.client.utils.IOpenShiftJsonConstants.PROPERTY_TYPE;
import static com.openshift.express.internal.client.utils.IOpenShiftJsonConstants.PROPERTY_UUID;
import static com.openshift.express.internal.client.utils.IOpenShiftJsonConstants.PROPERTY_VALID_OPTIONS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.jboss.dmr.Property;

import com.openshift.express.client.OpenShiftException;
import com.openshift.express.client.OpenShiftRequestParameterException;

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
		final String type = rootNode.get("type").asString();
		final String status = rootNode.get("status").asString();
		final List<String> messages = createMessages(rootNode.get("messages"));

		final EnumDataType dataType = EnumDataType.safeValueOf(type);
		// the response is after an error, only the messages are relevant
		if (dataType == null) {
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
			return new RestResponse(status, messages, createDomains(rootNode), dataType);
		case domain:
			return new RestResponse(status, messages, createDomain(rootNode), dataType);
		case applications:
			return new RestResponse(status, messages, createApplications(rootNode), dataType);
		case application:
			return new RestResponse(status, messages, createApplication(rootNode), dataType);
		case gears:
			return new RestResponse(status, messages, createGears(rootNode), dataType);
		case cartridges:
			return new RestResponse(status, messages, createCartridges(rootNode), dataType);
		case cartridge:
			return new RestResponse(status, messages, createCartridge(rootNode), dataType);
		default:
			return null;
		}
	}

	/**
	 * Creates a new ResourceDTO object.
	 * 
	 * @param messagesNode
	 *            the messages node
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
		if (content == null) {
			throw new OpenShiftException("Could not unmarshall response: no content.");
		}
		final ModelNode node = ModelNode.fromJSONString(content);
		if (!node.isDefined()) {
			throw new OpenShiftException("Could not unmarshall response: erroneous content.");
		}

		return node;
	}

	/**
	 * Creates a new ResourceDTO object.
	 * 
	 * @param userNode
	 *            the root node
	 * @return the user resource dto
	 * @throws OpenShiftException
	 */
	private static UserResourceDTO createUser(ModelNode userNode) throws OpenShiftException {
		if (userNode.has(PROPERTY_DATA)) {
			// loop inside 'data' node
			return createUser(userNode.get(PROPERTY_DATA));
		}
		final String namespace = getAsString(userNode, PROPERTY_LOGIN);
		final Map<String, Link> links = createLinks(userNode.get(PROPERTY_LINKS));
		return new UserResourceDTO(namespace, links);
	}

	/**
	 * Creates a new ResourceDTO object.
	 * 
	 * @param rootNode
	 *            the root node
	 * @return the list< key resource dt o>
	 * @throws OpenShiftException
	 *             the open shift exception
	 */
	private static List<KeyResourceDTO> createKeys(ModelNode rootNode) throws OpenShiftException {
		final List<KeyResourceDTO> keys = new ArrayList<KeyResourceDTO>();
		// temporarily supporting single and multiple values for 'keys' node
		if (rootNode.has(PROPERTY_DATA)) {
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
	 * @param keyNode
	 *            the key node
	 * @return the key resource dto
	 * @throws OpenShiftException
	 */
	private static KeyResourceDTO createKey(ModelNode keyNode) throws OpenShiftException {
		if (keyNode.has(PROPERTY_DATA)) {
			// loop inside 'data' node
			return createKey(keyNode.get(PROPERTY_DATA));
		}
		final String name = getAsString(keyNode, "name");
		final String type = getAsString(keyNode, "type");
		final String content = getAsString(keyNode, "content");
		final Map<String, Link> links = createLinks(keyNode.get(PROPERTY_LINKS));
		return new KeyResourceDTO(name, type, content, links);
	}

	/**
	 * Creates a new set of indexed links.
	 * 
	 * @param linksNode
	 *            the root node
	 * @return the list< domain dt o>
	 * @throws OpenShiftException
	 *             the open shift exception
	 */
	private static Map<String, Link> createLinks(final ModelNode linksNode) throws OpenShiftException {
		if (linksNode.has(PROPERTY_DATA)) {
			// loop inside 'data' node
			return createLinks(linksNode.get(PROPERTY_DATA));
		}
		Map<String, Link> links = new HashMap<String, Link>();
		if (linksNode.isDefined()) {
			for (ModelNode linkNode : linksNode.asList()) {
				final String linkName = linkNode.asProperty().getName();
				final ModelNode valueNode = linkNode.asProperty().getValue();
				if (valueNode.isDefined()) {
					final String rel = valueNode.get(PROPERTY_REL).asString();
					final String href = valueNode.get(PROPERTY_HREF).asString();
					final String method = valueNode.get(PROPERTY_METHOD).asString();
					final List<LinkParameter> requiredParams = createLinkParameters(valueNode
							.get(PROPERTY_REQUIRED_PARAMS));
					final List<LinkParameter> optionalParams = createLinkParameters(valueNode
							.get(PROPERTY_OPTIONAL_PARAMS));
					links.put(linkName, new Link(rel, href, method, requiredParams, optionalParams));
				}
			}
		}
		return links;
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
	private static List<DomainResourceDTO> createDomains(final ModelNode rootNode) throws OpenShiftException {
		final List<DomainResourceDTO> domains = new ArrayList<DomainResourceDTO>();
		// temporarily supporting absence of 'data' node in the 'domain' response message
		// FIXME: simplify once openshift response is fixed
		if (rootNode.has(PROPERTY_DATA)) {
			for (ModelNode dataNode : rootNode.get(PROPERTY_DATA).asList()) {
				if (dataNode.getType() == ModelType.OBJECT) {
					domains.add(createDomain(dataNode));
				} else {
					throw new OpenShiftException("Unexpected node type: {0}", dataNode.getType());
				}
			}
		} else {
			final ModelNode domainNode = rootNode.get(PROPERTY_DOMAIN);
			if (domainNode.isDefined() && domainNode.getType() == ModelType.OBJECT) {
				domains.add(createDomain(domainNode));
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
	 * @throws OpenShiftException
	 */
	private static DomainResourceDTO createDomain(final ModelNode domainNode) throws OpenShiftException {
		if (domainNode.has(PROPERTY_DATA)) {
			// loop inside 'data' node
			return createDomain(domainNode.get(PROPERTY_DATA));
		}
		final String namespace = getAsString(domainNode, PROPERTY_NAMESPACE);
		final String suffix = getAsString(domainNode, PROPERTY_SUFFIX);
		final Map<String, Link> links = createLinks(domainNode.get(PROPERTY_LINKS));
		return new DomainResourceDTO(namespace, suffix, links);
	}

	/**
	 * Creates a new DTO object.
	 * 
	 * @param rootNode
	 *            the domain node
	 * @return the list< application dt o>
	 * @throws OpenShiftException 
	 */
	private static List<ApplicationResourceDTO> createApplications(final ModelNode rootNode)
			throws OpenShiftException {
		final List<ApplicationResourceDTO> applicationDTOs = new ArrayList<ApplicationResourceDTO>();
		if (rootNode.has(PROPERTY_DATA)) {
			for (ModelNode applicationNode : rootNode.get(PROPERTY_DATA).asList()) {
				applicationDTOs.add(createApplication(applicationNode));
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
	 * @throws OpenShiftException 
	 */
	private static ApplicationResourceDTO createApplication(ModelNode appNode)
			throws OpenShiftException {
		if (appNode.has(PROPERTY_DATA)) {
			// loop inside 'data' node
			return createApplication(appNode.get(PROPERTY_DATA));
		}
		final String framework = getAsString(appNode, PROPERTY_FRAMEWORK);
		final String creationTime = getAsString(appNode, PROPERTY_CREATION_TIME);
		final String name = getAsString(appNode, PROPERTY_NAME);
		final String uuid = getAsString(appNode, PROPERTY_UUID);
		final String domainId = getAsString(appNode, PROPERTY_DOMAIN_ID);
		final Map<String, Link> links = createLinks(appNode.get(PROPERTY_LINKS));
		final List<String> aliases = createAliases(appNode.get(PROPERTY_ALIASES));
		final Map<String, String> embeddedCartridges = createEmbeddedCartridges(appNode.get(PROPERTY_EMBEDDED));
		final List<GearResourceDTO> gears = createGears(appNode);
		return new ApplicationResourceDTO(framework, domainId, creationTime, name, uuid, aliases, embeddedCartridges, gears,
				links);
	}

	private static List<GearResourceDTO> createGears(ModelNode gearsNode) {
		if (gearsNode.has(PROPERTY_DATA)) {
			// loop inside 'data' node
			return createGears(gearsNode.get(PROPERTY_DATA));
		}
		final List<GearResourceDTO> gears = new ArrayList<GearResourceDTO>();
		if(gearsNode.getType() == ModelType.LIST) {
			for(ModelNode childNode : gearsNode.asList()) {
				gears.add(createGear(childNode));
			}
		}
		return gears;
	}

	private static GearResourceDTO createGear(ModelNode gearNode) {
		if (gearNode.has(PROPERTY_DATA)) {
			// loop inside 'data' node
			return createGear(gearNode.get(PROPERTY_DATA));
		}
		final String uuid = getAsString(gearNode, PROPERTY_UUID);
		final String gitUrl = getAsString(gearNode, PROPERTY_GIT_URL);
		final List<GearsComponent> components = createGearComponents(gearNode.get(PROPERTY_GEARS_COMPONENTS));
		return new GearResourceDTO(uuid, components, gitUrl);
	}
	
	private static List<GearsComponent> createGearComponents(ModelNode gearsComponentNode) {
		final List<GearsComponent> components = new ArrayList<GearsComponent>();
		if(gearsComponentNode.getType() == ModelType.LIST) {
			for(ModelNode componentNode : gearsComponentNode.asList()) {
				final String name = getAsString(componentNode, PROPERTY_NAME);
				final String internalPort = getAsString(componentNode, PROPERTY_INTERNAL_PORT);
				final String proxyPort = getAsString(componentNode, PROPERTY_PROXY_PORT);
				final String proxyHost = getAsString(componentNode, PROPERTY_PROXY_HOST);
				components.add(new GearsComponent(name, internalPort, proxyHost, proxyPort));
			}
		}
		return components;
	}

	/**
	 * Creates a new ResourceDTO object.
	 * 
	 * @param embeddedCartridgeNodes
	 *            the embedded cartridge nodes
	 * @return the map< string, string>
	 */
	private static Map<String, String> createEmbeddedCartridges(ModelNode embeddedCartridgesNode) {
		final Map<String, String> embeddedCartridges = new HashMap<String, String>();
		if (embeddedCartridgesNode != null) {
			switch (embeddedCartridgesNode.getType()) {
			case OBJECT:
			case LIST:
				for (ModelNode embeddedCartridgeNode : embeddedCartridgesNode.asList()) {
					if (embeddedCartridgeNode.getType() == ModelType.PROPERTY) {
						final Property p = embeddedCartridgeNode.asProperty();
						embeddedCartridges.put(p.getName(), p.getValue().get(PROPERTY_INFO).asString());
					}
				}
				break;
			case PROPERTY:
				final Property p = embeddedCartridgesNode.asProperty();
				embeddedCartridges.put(p.getName(), p.getValue().get(PROPERTY_INFO).asString());
				break;
			}
		}
		return embeddedCartridges;
	}

	/**
	 * Creates a new ResourceDTO object.
	 * 
	 * @param rootNode
	 *            the root node
	 * @return the list< cartridge resource dt o>
	 * @throws OpenShiftException
	 */
	private static List<CartridgeResourceDTO> createCartridges(ModelNode rootNode) throws OpenShiftException {
		final List<CartridgeResourceDTO> cartridges = new ArrayList<CartridgeResourceDTO>();
		if (rootNode.has(PROPERTY_DATA)) {
			for (ModelNode cartridgeNode : rootNode.get(PROPERTY_DATA).asList()) {
				cartridges.add(createCartridge(cartridgeNode));
			}
		}
		return cartridges;
	}

	/**
	 * Creates a new ResourceDTO object.
	 * 
	 * @param cartridgeNode
	 *            the cartridge node
	 * @return the cartridge resource dto
	 * @throws OpenShiftException
	 */
	private static CartridgeResourceDTO createCartridge(ModelNode cartridgeNode) throws OpenShiftException {
		if (cartridgeNode.has(PROPERTY_DATA)) {
			// loop inside 'data' node
			return createCartridge(cartridgeNode.get(PROPERTY_DATA));
		}
		final String name = getAsString(cartridgeNode, PROPERTY_NAME);
		final String type = getAsString(cartridgeNode, PROPERTY_TYPE);
		final Map<String, Link> links = createLinks(cartridgeNode.get(PROPERTY_LINKS));
		return new CartridgeResourceDTO(name, type, links);
	}

	/**
	 * Creates a new ResourceDTO object.
	 * 
	 * @param aliasNodeList
	 *            the alias node list
	 * @return the list< string>
	 */
	private static List<String> createAliases(ModelNode aliasNodesList) {
		final List<String> aliases = new ArrayList<String>();
		switch (aliasNodesList.getType()) {
		case OBJECT:
		case LIST:
			for (ModelNode aliasNode : aliasNodesList.asList()) {
				aliases.add(aliasNode.asString());
			}
			break;
		default:
			aliases.add(aliasNodesList.asString());
		}
		return aliases;
	}

	/**
	 * Creates a new DTO object.
	 * 
	 * @param linkParamNodes
	 *            the link param nodes
	 * @return the list< link param>
	 * @throws OpenShiftRequestParameterException
	 */
	private static List<LinkParameter> createLinkParameters(ModelNode linkParamNodes)
			throws OpenShiftRequestParameterException {
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
	 * @param linkParamNode
	 *            the model node that contains the link parameters
	 * @return the link parameter
	 * @throws OpenShiftRequestParameterException
	 */
	private static LinkParameter createLinkParameter(ModelNode linkParamNode) throws OpenShiftRequestParameterException {
		final String description = linkParamNode.get("description").asString();
		final String type = linkParamNode.get("type").asString();
		final String defaultValue = linkParamNode.get("default_value").asString();
		final String name = linkParamNode.get("name").asString();
		return new LinkParameter(name, type, defaultValue, description, createValidOptions(linkParamNode));
	}

	/**
	 * Gets the valid options.
	 * 
	 * @param linkParamNode
	 *            the link param node
	 * @return the valid options
	 */
	private static List<String> createValidOptions(ModelNode linkParamNode) {
		final List<String> validOptions = new ArrayList<String>();
		final ModelNode validOptionsNode = linkParamNode.get(PROPERTY_VALID_OPTIONS);
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

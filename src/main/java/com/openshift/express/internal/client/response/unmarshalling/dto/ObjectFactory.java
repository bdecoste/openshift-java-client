package com.openshift.express.internal.client.response.unmarshalling.dto;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

import com.openshift.express.client.OpenShiftException;

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

		if (!"ok".equals(node.get("status").asString())) {
			String messages = node.get("messages").asString();
			throw new OpenShiftException("Failed to perform operation:\n{0}", messages);
		}
		return node;
	}

	private static DomainsDTO createDomainsDTO(ModelNode rootNode) throws OpenShiftException {
		if (!"domains".equals(rootNode.get("type").asString())) {
			throw new OpenShiftException("Expected response type '{0}', but received '{1}'", "domains", rootNode.get(
					"type").asString());
		}
		final List<DomainDTO> domains = new ArrayList<DomainDTO>();
		final List<Operation> operations = new ArrayList<Operation>();
		for (ModelNode dataNode : rootNode.get("data").asList()) {
			if(dataNode.getType() == ModelType.OBJECT) {
				domains.add(createDomainDTO(dataNode));
			} else {
				throw new OpenShiftException("Unexpected node type: {0}", dataNode.getType());
			}
		}

		return new DomainsDTO(domains, operations);
	}

	private static DomainDTO createDomainDTO(ModelNode domainNode) {
		final ModelNode namespaceNode = domainNode.get("namespace");
		final String namespace = namespaceNode.isDefined() ? namespaceNode.asString() : null;
		List<Operation> operations = createLinks(domainNode.get("links").asList());
		return new DomainDTO(namespace, operations);
	}

	private static List<Operation> createLinks(List<ModelNode> linkNodes) {
		List<Operation> operations = new ArrayList<Operation>();
		return operations;
	}

}

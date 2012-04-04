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
package com.openshift.client.utils;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;

/**
 * @author Andre Dietisheim
 */
public enum Samples {

	ADD_APPLICATION_CARTRIDGE_JSON("add-application-cartridge.json"),
	ADD_DOMAIN_JSON("add-domain.json"),
	ADD_DOMAIN_KO_JSON("add-domain-ko.json"),
	ADD_USER_KEY2_OK_JSON("add-user-key2-ok.json"),
	ADD_USER_KEY_KO_JSON("add-user-key-ko.json"),
	ADD_USER_KEY_OK_JSON("add-user-key-ok.json"),
	CREATE_APPLICATION_JSON("create-application.json"),
	CREATE_APPLICATION_XML("create-application.xml"),
	CREATE_DOMAIN_JSON("create-domain.json"),
	CREATE_DOMAIN_XML("create-domain.xml"),
	DELETE_DOMAIN_EXISTING_KO_NOTFOUND_JSON("delete-domain-existing-ko-notfound.json"),
	DELETE_DOMAIN_EXISTING_OK_JSON("delete-domain-existing-ok.json"),
	DELETE_USER_KEY_KO_JSON("delete-user-key-ko.json"),
	GET_APPCARTRIDGE_JSON("get-appcartridge.json"),
	GET_APPCARTRIDGES_WITH1ELEMENT_JSON("get-appcartridges-with1element.json"),
	GET_APPCARTRIDGES_WITH2ELEMENTS_JSON("get-appcartridges-with2elements.json"),
	GET_APPGEARS_WITH3COMPONENTS_JSON("get-appgears-with3components.json"),
	GET_APPLICATIONS_WITH2APPS_JSON("get-applications-with2apps.json"),
	GET_APPLICATIONS_WITH4APPS_JSON("get-applications-with4apps.json"),
	GET_APPLICATION_WITH1CARTRIDGE_JSON("get-application-with1Cartridge.json"),
	GET_APPLICATION_WITH2ALIASES_JSON("get-application-with2aliases.json"),
	GET_APPLICATION_WITH2CARTRIDGES_JSON("get-application-with2Cartridges.json"),
	GET_DOMAIN_JSON("get-domain.json"),
	GET_DOMAIN_NOTFOUND_JSON("get-domain-notfound.json"),
	GET_DOMAINS_1329997507457_JSON("get-domains-1329997507457.json"),
	GET_DOMAINS_1329997507457_XML("get-domains-1329997507457.xml"),
	GET_DOMAINS_1EXISTING_JSON("get-domains-1existing.json"),
	GET_DOMAINS_JSON("get-domains.json"),
	GET_DOMAINS_NOEXISTING_JSON("get-domains-noexisting.json"),
	GET_REST_API_JSON("get-rest-api.json"),
	GET_USER_JSON("get-user.json"),
	GET_USER_KEY_DEFAULT_JSON("get-user-key-default.json"),
	GET_USER_KEYS_MULTIPLE_JSON("get-user-keys-multiple.json"),
	GET_USER_KEYS_NONE_JSON("get-user-keys-none.json"),
	GET_USER_KEYS_SINGLE_JSON("get-user-keys-single.json"),
	REMOVE_DOMAIN_EXISTING_JSON("remove-domain-existing.json"),
	REMOVE_DOMAIN_UNEXISTING_JSON("remove-domain-unexisting.json"),
	UPDATE_USER_KEY_JSON("update-user-key.json"),
	UPDATE_DOMAIN_NAMESPACE("update-domain-namespace");

	private static final String SAMPLES_FOLDER = "/samples/";

	private String filePath;

	Samples(String fileName) {
		this.filePath = SAMPLES_FOLDER + fileName;
	}

	public String getContentAsString() throws Throwable {
		String content = null;
		try {
			final InputStream contentStream = Samples.class.getResourceAsStream(filePath);
			content = IOUtils.toString(contentStream);
		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException("Could not read file " + filePath + ": " + e.getMessage());
		}
		return content;
	}
}

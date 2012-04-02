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

import java.util.HashMap;
import java.util.List;

/**
 * The Class GearsResourceDTO.
 *
 * @author Xavier Coulon
 */
public class GearResourceDTO extends BaseResourceDTO {
	
	/** The gears uuid. */
	private final String uuid;
	
	/** The gears components. */
	private final List<GearsComponent> components;
	
	/** The gears git url. */
	private final String gitUrl;
	
	/**
	 * Instantiates a new gears resource dto.
	 *
	 * @param uuid the uuid
	 * @param components the components
	 * @param gitUrl the git url
	 */
	public GearResourceDTO(final String uuid, final List<GearsComponent> components, final String gitUrl) {
		super(new HashMap<String, Link>());
		this.uuid = uuid;
		this.components = components;
		this.gitUrl = gitUrl;
	}
	
	/**
	 * Gets the uuid.
	 *
	 * @return the uuid
	 */
	public final String getUuid() {
		return uuid;
	}

	/**
	 * Gets the components.
	 *
	 * @return the components
	 */
	public final List<GearsComponent> getComponents() {
		return components;
	}

	/**
	 * Gets the git url.
	 *
	 * @return the gitUrl
	 */
	public final String getGitUrl() {
		return gitUrl;
	}


}

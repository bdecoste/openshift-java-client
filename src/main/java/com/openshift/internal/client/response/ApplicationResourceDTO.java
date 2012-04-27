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
package com.openshift.internal.client.response;

import java.util.List;
import java.util.Map;

import com.openshift.client.IGearProfile;

/**
 * The Class ApplicationDTO.
 *
 * @author Xavier Coulon
 */
public class ApplicationResourceDTO extends BaseResourceDTO {

	/** the application's framework. */
	private final String framework;
	
	/** the application's domainId. */
	private final String domainId;
	
	/** the application's creation time. */
	private final String creationTime;
	
	/** the application's name. */
	private final String name;
	
	/** the application's UUID. */
	private final String uuid;
	
	/** The scalability enablement. */
	private final boolean scalable;

	/** The application gear profile. */
	private final IGearProfile gearProfile;

	/** the application's aliases. */
	private final List<String> aliases;
	
	/** the application's URL. */
	private final String applicationUrl;
	
	/** the application's git repository URL. */
	private final String gitUrl;
	
	/** the path at which this application may be queried for its health **/
	private final String healthCheckPath;

	/**
	 * Instantiates a new application dto.
	 *
	 * @param framework the framework
	 * @param domainId the domain id
	 * @param creationTime the creation time
	 * @param name the name
	 * @param uuid the uuid
	 * @param links the links
	 */
	public ApplicationResourceDTO(final String framework, final String domainId, final String creationTime,
			final String name, final IGearProfile gearProfile, final boolean scalable, final String uuid,
			final String applicationUrl, final String gitUrl, final String healthCheckPath, final List<String> aliases,
			final Map<String, Link> links, List<Message> creationLog) {
	super(links, creationLog);
		this.framework = framework;
		this.domainId = domainId;
		this.creationTime = creationTime;
		this.name = name;
		this.uuid = uuid;
		this.gearProfile = gearProfile;
		this.scalable = scalable;
		this.applicationUrl = applicationUrl;
		this.gitUrl = gitUrl;
		this.healthCheckPath = healthCheckPath;
		this.aliases = aliases;
	}

	/**
	 * Gets the framework.
	 *
	 * @return the framework
	 */
	public final String getFramework() {
		return framework;
	}

	/**
	 * Gets the domain id.
	 *
	 * @return the domainId
	 */
	public final String getDomainId() {
		return domainId;
	}

	/**
	 * Gets the creation time.
	 *
	 * @return the creationTime
	 */
	public final String getCreationTime() {
		return creationTime;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * @return the scalable
	 */
	public final boolean isScalable() {
		return scalable;
	}

	/**
	 * @return the gearProfile
	 */
	public final IGearProfile getGearProfile() {
		return gearProfile;
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
	 * @return the applicationUrl
	 */
	public final String getApplicationUrl() {
		return applicationUrl;
	}

	/**
	 * @return the path at which this application may be queried for its health
	 */
	public final String getHealthCheckPath() {
		return healthCheckPath;
	}

	/**
	 * @return the gitUrl
	 */
	public final String getGitUrl() {
		return gitUrl;
	}

	/**
	 * @return the aliases
	 */
	public List<String> getAliases() {
		return aliases;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ApplicationResourceDTO [name=" + name + ", framework=" + framework + "]";
	}

}

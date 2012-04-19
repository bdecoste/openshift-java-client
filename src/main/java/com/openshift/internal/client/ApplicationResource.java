/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package com.openshift.internal.client;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.openshift.client.ApplicationLogReader;
import com.openshift.client.IApplication;
import com.openshift.client.IApplicationGear;
import com.openshift.client.IApplicationGearComponent;
import com.openshift.client.ICartridge;
import com.openshift.client.IDomain;
import com.openshift.client.IEmbeddedCartridge;
import com.openshift.client.OpenShiftException;
import com.openshift.internal.client.response.unmarshalling.dto.ApplicationResourceDTO;
import com.openshift.internal.client.response.unmarshalling.dto.CartridgeResourceDTO;
import com.openshift.internal.client.response.unmarshalling.dto.GearComponentDTO;
import com.openshift.internal.client.response.unmarshalling.dto.GearResourceDTO;
import com.openshift.internal.client.response.unmarshalling.dto.Link;
import com.openshift.internal.client.utils.IOpenShiftJsonConstants;

/**
 * The Class Application.
 * 
 * @author André Dietisheim
 */
public class ApplicationResource extends AbstractOpenShiftResource implements IApplication {

	private static final String LINK_DELETE_APPLICATION = "DELETE";
	private static final String LINK_START_APPLICATION = "START";
	private static final String LINK_STOP_APPLICATION = "STOP";
	private static final String LINK_FORCE_STOP_APPLICATION = "FORCE_STOP";
	private static final String LINK_RESTART_APPLICATION = "RESTART";
	private static final String LINK_SCALE_UP = "SCALE_UP";
	private static final String LINK_SCALE_DOWN = "SCALE_DOWN";
	private static final String LINK_SHOW_PORT = "SHOW_PORT";
	private static final String LINK_EXPOSE_PORT = "EXPOSE_PORT";
	private static final String LINK_CONCEAL_PORT = "CONCEAL_PORT";
	private static final String LINK_ADD_ALIAS = "ADD_ALIAS";
	private static final String LINK_REMOVE_ALIAS = "REMOVE_ALIAS";
	private static final String LINK_ADD_CARTRIDGE = "ADD_CARTRIDGE";
	private static final String LINK_LIST_CARTRIDGES = "LIST_CARTRIDGES";
	private static final String LINK_LIST_GEARS = "GET_GEARS";

	/** The (unique) uuid of this application. */
	private final String uuid;

	/** The name of this application. */
	private final String name;

	/** The time at which this application was created. */
	private final String creationTime;

	/** The cartridge (application type/framework) of this application. */
	private final ICartridge cartridge;

	/** The creation log. */
	private final String creationLog;

	/** The domain this application belongs to. */
	private final DomainResource domain;

	/** The url of this application. */
	private final String applicationUrl;

	/** The pathat which the health of this application may be queried. */
	private String healthCheckPath;

	/** The url at which the git repo of this application may be reached. */
	private final String gitUrl;

	/** The aliases of this application. */
	private final List<String> aliases;

	/**
	 * List of configured embedded cartridges. <code>null</code> means list if
	 * not loaded yet.
	 */
	// TODO: replace by a map indexed by cartridge names ?
	private List<IEmbeddedCartridge> embeddedCartridges = null;

	/**
	 * List of configured gears. <code>null</code> means list if not loaded yet.
	 */
	// TODO: replace by a map indexed by cartridge names ?
	private List<IApplicationGear> gears = null;

	protected ApplicationResource(ApplicationResourceDTO dto, ICartridge cartridge, DomainResource domain) {
		this(dto.getName(), dto.getUuid(), dto.getCreationTime(), dto.getApplicationUrl(), dto.getGitUrl(),
				cartridge, dto.getAliases(), dto.getLinks(), domain);
	}

	/**
	 * Instantiates a new application.
	 * 
	 * @param name
	 *            the name
	 * @param uuid
	 *            the uuid
	 * @param creationTime
	 *            the creation time
	 * @param applicationUrl
	 *            the application url
	 * @param gitUrl
	 *            the git url
	 * @param cartridge
	 *            the cartridge (type/framework)
	 * @param aliases
	 *            the aliases
	 * @param links
	 *            the links
	 * @param domain
	 *            the domain this application belongs to
	 */
	protected ApplicationResource(final String name, final String uuid, final String creationTime,
			final String applicationUrl, final String gitUrl, final ICartridge cartridge, final List<String> aliases,
			final Map<String, Link> links, final DomainResource domain) {
		this(name, uuid, creationTime, null, applicationUrl, gitUrl, cartridge, aliases, links, domain);
	}

	/**
	 * Instantiates a new application.
	 * 
	 * @param name
	 *            the name
	 * @param uuid
	 *            the uuid
	 * @param creationTime
	 *            the creation time
	 * @param creationLog
	 *            the creation log
	 * @param applicationUrl
	 *            the application url
	 * @param gitUrl
	 *            the git url
	 * @param cartridge
	 *            the cartridge (type/framework)
	 * @param aliases
	 *            the aliases
	 * @param links
	 *            the links
	 * @param domain
	 *            the domain this application belongs to
	 */
	protected ApplicationResource(final String name, final String uuid, final String creationTime,
			final String creationLog,
			final String applicationUrl, final String gitUrl, final ICartridge cartridge, final List<String> aliases,
			final Map<String, Link> links, final DomainResource domain) {
		super(domain.getService(), links);
		this.name = name;
		this.uuid = uuid;
		this.creationTime = creationTime;
		this.creationLog = creationLog;
		this.cartridge = cartridge;
		this.applicationUrl = applicationUrl;
		this.gitUrl = gitUrl;
		this.domain = domain;
		this.aliases = aliases;
	}

	public String getName() {
		return name;
	}

	public String getUUID() {
		return uuid;
	}

	public ICartridge getCartridge() {
		return cartridge;
	}

	public String getCreationTime() {
		return creationTime;
	}

	public String getCreationLog() {
		return creationLog;
	}

	public IDomain getDomain() {
		return this.domain;
	}

	public void destroy() throws OpenShiftException, SocketTimeoutException {
		new DeleteApplicationRequest().execute();
		domain.removeApplication(this);
	}

	public void start() throws OpenShiftException, SocketTimeoutException {
		new StartApplicationRequest().execute();
	}

	public void restart() throws OpenShiftException, SocketTimeoutException {
		new RestartApplicationRequest().execute();
	}

	public void stop() throws OpenShiftException, SocketTimeoutException {
		stop(false);
	}

	public void stop(boolean force) throws OpenShiftException, SocketTimeoutException {
		if (force) {
			new ForceStopApplicationRequest().execute();
		} else {
			new StopApplicationRequest().execute();

		}
	}

	public void exposePort() throws SocketTimeoutException, OpenShiftException {
		new ExposePortRequest().execute();
	}

	public void concealPort() throws SocketTimeoutException, OpenShiftException {
		new ConcealPortRequest().execute();
	}

	public void showPort() throws SocketTimeoutException, OpenShiftException {
		new ShowPortRequest().execute();
	}

	public void getDescriptor() {
		throw new UnsupportedOperationException();
	}

	public void scaleDown() throws SocketTimeoutException, OpenShiftException {
		new ScaleDownRequest().execute();
	}

	public void scaleUp() throws SocketTimeoutException, OpenShiftException {
		new ScaleUpRequest().execute();
	}

	public void addAlias(String alias) throws SocketTimeoutException, OpenShiftException {
		ApplicationResourceDTO applicationDTO = new AddAliasRequest().execute(alias);
		this.aliases.clear();
		this.aliases.addAll(applicationDTO.getAliases());

	}

	public List<String> getAliases() {
		return Collections.unmodifiableList(this.aliases);
	}

	public void removeAlias(String alias) throws SocketTimeoutException, OpenShiftException {
		ApplicationResourceDTO applicationDTO = new RemoveAliasRequest().execute(alias);
		this.aliases.clear();
		this.aliases.addAll(applicationDTO.getAliases());
	}

	public ApplicationLogReader getLogReader() throws OpenShiftException {
		throw new UnsupportedOperationException();
		// ApplicationLogReader logReader = null;
		// if (logReaders.get(DEFAULT_LOGREADER) == null) {
		// logReader = new ApplicationLogReader(this, getInternalUser(),
		// service);
		// logReaders.put(DEFAULT_LOGREADER, logReader);
		// }
		// return logReader;
	}

	public ApplicationLogReader getLogReader(String logFile) throws OpenShiftException {
		throw new UnsupportedOperationException();
		// ApplicationLogReader logReader = null;
		// if (logReaders.get(logFile) == null) {
		// logReader = new ApplicationLogReader(this, getInternalUser(),
		// service, logFile);
		// logReaders.put(logFile, logReader);
		// }
		// return logReader;
	}

	public String getGitUrl() {
		return this.gitUrl;
	}

	public String getApplicationUrl() {
		return applicationUrl;
		// IDomain domain = getInternalUser().getDomain();
		// if (domain == null) {
		// return null;
		// }
		// return MessageFormat.format(APPLICATION_URL_PATTERN, name,
		// domain.getNamespace(), domain.getRhcDomain());
	}

	public String getHealthCheckUrl() {
		throw new UnsupportedOperationException();
	}

	public String getHealthCheckResponse() throws OpenShiftException {
		throw new OpenShiftException("NOT SUPPORTED FOR GENERIC APPLICATION");
	}

	public void addEmbeddedCartridge(String embeddedCartridgeName) throws OpenShiftException, SocketTimeoutException {
		final CartridgeResourceDTO embeddedCartridgeDTO = new AddEmbeddedCartridgeRequest()
				.execute(embeddedCartridgeName);
		addEmbeddedCartridge(new EmbeddableCartridgeResource(
				embeddedCartridgeDTO.getName(),
				embeddedCartridgeDTO.getType(),
				embeddedCartridgeDTO.getLinks(), this));
	}

	/**
	 * Adds the given embedded cartridge to this application.
	 * 
	 * @param cartridge
	 *            the embeddable cartridge that shall be added to this
	 *            application
	 */
	protected void addEmbeddedCartridge(IEmbeddedCartridge cartridge) {
		this.embeddedCartridges.add(cartridge);
	}

	public void addEmbeddedCartridges(List<String> embeddedCartridgeNames) throws OpenShiftException,
			SocketTimeoutException {
		for (String cartridge : embeddedCartridgeNames) {
			// TODO: catch exceptions when removing cartridges, contine removing
			// and report the exceptions that occurred<
			addEmbeddedCartridge(cartridge);
		}
	}

	protected void removeEmbeddedCartridge(IEmbeddedCartridge embeddedCartridge) throws OpenShiftException {
		this.embeddedCartridges.remove(embeddedCartridge);
	}

	public List<IEmbeddedCartridge> getEmbeddedCartridges() throws OpenShiftException, SocketTimeoutException {
		// load collection if necessary
		if (embeddedCartridges == null) {
			this.embeddedCartridges = new ArrayList<IEmbeddedCartridge>();
			List<CartridgeResourceDTO> embeddableCartridgeDTOs = new ListEmbeddableCartridgesRequest().execute();
			for (CartridgeResourceDTO embeddableCartridgeDTO : embeddableCartridgeDTOs) {
				IEmbeddedCartridge embeddableCartridge =
						new EmbeddableCartridgeResource(
								embeddableCartridgeDTO.getName(),
								embeddableCartridgeDTO.getType(),
								embeddableCartridgeDTO.getLinks(),
								this);
				this.embeddedCartridges.add(embeddableCartridge);
			}
		}
		return embeddedCartridges;
	}

	public boolean hasEmbeddedCartridge(String cartridgeName) throws OpenShiftException, SocketTimeoutException {
		return getEmbeddedCartridge(cartridgeName) != null;
	}

	public IEmbeddedCartridge getEmbeddedCartridge(String cartridgeName) throws OpenShiftException,
			SocketTimeoutException {
		IEmbeddedCartridge embeddedCartridge = null;
		for (IEmbeddedCartridge cartridge : getEmbeddedCartridges()) {
			if (cartridgeName.equals(cartridge.getName())) {
				embeddedCartridge = cartridge;
				break;
			}
		}
		return embeddedCartridge;
	}

	/**
	 * Returns the gears that this application is running on.
	 * 
	 * @return
	 * 
	 * @return the gears
	 * @throws OpenShiftException
	 * @throws SocketTimeoutException
	 */
	public List<IApplicationGear> getGears() throws SocketTimeoutException, OpenShiftException {
		// load collection if necessary
		if (gears == null) {
			this.gears = new ArrayList<IApplicationGear>();
			List<GearResourceDTO> gearDTOs = new ListGearsRequest().execute();
			for (GearResourceDTO gearDTO : gearDTOs) {
				final List<IApplicationGearComponent> components = new ArrayList<IApplicationGearComponent>();
				for (GearComponentDTO gearComponentDTO : gearDTO.getComponents()) {
					components.add(
							new ApplicationGearComponentResource(gearComponentDTO));
				}
				IApplicationGear gear =
						new ApplicationGearResource(
								gearDTO, components, this);
				this.gears.add(gear);
			}
		}
		return gears;
	}

	public boolean waitForAccessible(long timeout) throws OpenShiftException {
		throw new UnsupportedOperationException();
		// return service.waitForApplication(getHealthCheckUrl(), timeout,
		// getHealthCheckResponse());
	}

	public void refresh() throws SocketTimeoutException, OpenShiftException {
		this.embeddedCartridges = null;
		this.gears = null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null)
			return false;
		if (getClass() != object.getClass())
			return false;
		ApplicationResource other = (ApplicationResource) object;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name;
	}

	private class DeleteApplicationRequest extends ServiceRequest {

		protected DeleteApplicationRequest() {
			super(LINK_DELETE_APPLICATION);
		}
	}

	private class StartApplicationRequest extends ServiceRequest {

		protected StartApplicationRequest() {
			super(LINK_START_APPLICATION);
		}

		public <DTO> DTO execute() throws OpenShiftException, SocketTimeoutException {
			return super.execute(
					new ServiceParameter(
							IOpenShiftJsonConstants.PROPERTY_EVENT,
							IOpenShiftJsonConstants.VALUE_START));
		}
	}

	private class StopApplicationRequest extends ServiceRequest {

		protected StopApplicationRequest() {
			super(LINK_STOP_APPLICATION);
		}

		public <DTO> DTO execute() throws OpenShiftException, SocketTimeoutException {
			return super.execute(
					new ServiceParameter(
							IOpenShiftJsonConstants.PROPERTY_EVENT,
							IOpenShiftJsonConstants.VALUE_STOP));
		}
	}

	private class ForceStopApplicationRequest extends ServiceRequest {

		protected ForceStopApplicationRequest() {
			super(LINK_FORCE_STOP_APPLICATION);
		}

		public <DTO> DTO execute() throws OpenShiftException, SocketTimeoutException {
			return super.execute(
					new ServiceParameter(
							IOpenShiftJsonConstants.PROPERTY_EVENT,
							IOpenShiftJsonConstants.VALUE_FORCESTOP));
		}
	}

	private class RestartApplicationRequest extends ServiceRequest {

		protected RestartApplicationRequest() {
			super(LINK_RESTART_APPLICATION);
		}

		public <DTO> DTO execute() throws OpenShiftException, SocketTimeoutException {
			return super.execute(
					new ServiceParameter(
							IOpenShiftJsonConstants.PROPERTY_EVENT,
							IOpenShiftJsonConstants.VALUE_RESTART));
		}
	}

	private class ExposePortRequest extends ServiceRequest {

		protected ExposePortRequest() {
			super(LINK_EXPOSE_PORT);
		}

		public <DTO> DTO execute() throws OpenShiftException, SocketTimeoutException {
			return super.execute(
					new ServiceParameter(
							IOpenShiftJsonConstants.PROPERTY_EVENT,
							IOpenShiftJsonConstants.VALUE_EXPOSE_PORT));
		}
	}

	private class ConcealPortRequest extends ServiceRequest {

		protected ConcealPortRequest() {
			super(LINK_CONCEAL_PORT);
		}

		public <DTO> DTO execute() throws OpenShiftException, SocketTimeoutException {
			return super.execute(
					new ServiceParameter(
							IOpenShiftJsonConstants.PROPERTY_EVENT,
							IOpenShiftJsonConstants.VALUE_CONCEAL_PORT));
		}
	}

	private class ShowPortRequest extends ServiceRequest {

		protected ShowPortRequest() {
			super(LINK_SHOW_PORT);
		}

		public <DTO> DTO execute() throws OpenShiftException, SocketTimeoutException {
			return super.execute(
					new ServiceParameter(
							IOpenShiftJsonConstants.PROPERTY_EVENT,
							IOpenShiftJsonConstants.VALUE_SHOW_PORT));
		}
	}

	private class ScaleUpRequest extends ServiceRequest {

		protected ScaleUpRequest() {
			super(LINK_SCALE_UP);
		}

		public <DTO> DTO execute() throws OpenShiftException, SocketTimeoutException {
			return super.execute(
					new ServiceParameter(
							IOpenShiftJsonConstants.PROPERTY_EVENT,
							IOpenShiftJsonConstants.VALUE_SCALE_UP));
		}
	}

	private class ScaleDownRequest extends ServiceRequest {

		protected ScaleDownRequest() {
			super(LINK_SCALE_DOWN);
		}

		public <DTO> DTO execute() throws OpenShiftException, SocketTimeoutException {
			return super.execute(
					new ServiceParameter(
							IOpenShiftJsonConstants.PROPERTY_EVENT,
							IOpenShiftJsonConstants.VALUE_SCALE_DOWN));
		}
	}

	private class AddAliasRequest extends ServiceRequest {

		protected AddAliasRequest() {
			super(LINK_ADD_ALIAS);
		}

		public <DTO> DTO execute(String alias) throws OpenShiftException, SocketTimeoutException {
			return super.execute(
					new ServiceParameter(
							IOpenShiftJsonConstants.PROPERTY_EVENT,
							IOpenShiftJsonConstants.VALUE_ADD_ALIAS),
					new ServiceParameter(IOpenShiftJsonConstants.PROPERTY_ALIAS, alias));
		}
	}

	private class RemoveAliasRequest extends ServiceRequest {

		protected RemoveAliasRequest() {
			super(LINK_REMOVE_ALIAS);
		}

		public <DTO> DTO execute(String alias) throws OpenShiftException, SocketTimeoutException {
			return super.execute(
					new ServiceParameter(
							IOpenShiftJsonConstants.PROPERTY_EVENT,
							IOpenShiftJsonConstants.VALUE_REMOVE_ALIAS),
					new ServiceParameter(IOpenShiftJsonConstants.PROPERTY_ALIAS, alias));
		}
	}

	private class AddEmbeddedCartridgeRequest extends ServiceRequest {

		protected AddEmbeddedCartridgeRequest() {
			super(LINK_ADD_CARTRIDGE);
		}

		public <DTO> DTO execute(String embeddedCartridgeName) throws OpenShiftException, SocketTimeoutException {
			return super.execute(
					new ServiceParameter(
							IOpenShiftJsonConstants.PROPERTY_CARTRIDGE, embeddedCartridgeName));
		}
	}

	private class ListEmbeddableCartridgesRequest extends ServiceRequest {

		protected ListEmbeddableCartridgesRequest() {
			super(LINK_LIST_CARTRIDGES);
		}
	}

	private class ListGearsRequest extends ServiceRequest {

		protected ListGearsRequest() {
			super(LINK_LIST_GEARS);
		}
	}

}

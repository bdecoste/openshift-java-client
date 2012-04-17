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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.openshift.client.ApplicationLogReader;
import com.openshift.client.IApplication;
import com.openshift.client.IApplicationGear;
import com.openshift.client.IApplicationGearComponent;
import com.openshift.client.ICartridge;
import com.openshift.client.IDomain;
import com.openshift.client.IEmbeddableCartridge;
import com.openshift.client.OpenShiftException;
import com.openshift.internal.client.response.unmarshalling.dto.ApplicationResourceDTO;
import com.openshift.internal.client.response.unmarshalling.dto.CartridgeResourceDTO;
import com.openshift.internal.client.response.unmarshalling.dto.GearComponentDTO;
import com.openshift.internal.client.response.unmarshalling.dto.GearResourceDTO;
import com.openshift.internal.client.response.unmarshalling.dto.Link;

/**
 * The Class Application.
 * 
 * @author André Dietisheim
 */
public class ApplicationResource extends AbstractOpenShiftResource implements IApplication {

	/** The Constant DEFAULT_LOGREADER. */
	private static final String DEFAULT_LOGREADER = "defaultLogReader";

	/** The Constant LINK_DELETE_APPLICATION. */
	private static final String LINK_DELETE_APPLICATION = "DELETE";

	/** The Constant LINK_START_APPLICATION. */
	private static final String LINK_START_APPLICATION = "START";

	/** The Constant LINK_STOP_APPLICATION. */
	private static final String LINK_STOP_APPLICATION = "STOP";

	/** The Constant LINK_FORCE_STOP_APPLICATION. */
	private static final String LINK_FORCE_STOP_APPLICATION = "FORCE_STOP";

	/** The Constant LINK_RESTART_APPLICATION. */
	private static final String LINK_RESTART_APPLICATION = "RESTART";

	/** The Constant LINK_SCALE_UP. */
	private static final String LINK_SCALE_UP = "SCALE_UP";

	/** The Constant LINK_SCALE_DOWN. */
	private static final String LINK_SCALE_DOWN = "SCALE_DOWN";

	/** The Constant LINK_SHOW_PORT. */
	private static final String LINK_SHOW_PORT = "SHOW_PORT";

	/** The Constant LINK_EXPOSE_PORT. */
	private static final String LINK_EXPOSE_PORT = "EXPOSE_PORT";

	/** The Constant LINK_CONCEAL_PORT. */
	private static final String LINK_CONCEAL_PORT = "CONCEAL_PORT";

	/** The Constant LINK_ADD_ALIAS. */
	private static final String LINK_ADD_ALIAS = "ADD_ALIAS";

	/** The Constant LINK_REMOVE_ALIAS. */
	private static final String LINK_REMOVE_ALIAS = "REMOVE_ALIAS";

	/** The Constant LINK_ADD_CARTRIDGE. */
	private static final String LINK_ADD_CARTRIDGE = "ADD_CARTRIDGE";

	private static final String LINK_LIST_CARTRIDGES = "LIST_CARTRIDGES";

	private static final String LINK_LIST_GEARS = "GET_GEARS";

	/** The application uuid. */
	private final String uuid;

	/** The application name. */
	private final String name;

	/** The application creation time. */
	private final String creationTime;

	/** The application native cartridge. */
	private final String cartridge;

	/** The log readers. */
	private HashMap<String, ApplicationLogReader> logReaders = new HashMap<String, ApplicationLogReader>();
	// TODO : replace when pubsub/notification is available ?
	/** The creation log. */
	private final String creationLog;

	/** The domain. */
	private final DomainResource domain;

	/** The application url. */
	private final String applicationUrl;

	/** The health check path. */
	private String healthCheckPath;

	/** The git url. */
	private final String gitUrl;

	/** The aliases. */
	private final List<String> aliases;

	/** List of configured embedded cartridges. <code>null</code> means list if not loaded yet. */
	// TODO: replace by a map indexed by cartridge names ?
	private List<IEmbeddableCartridge> embeddedCartridges = null;

	/** List of configured gears. <code>null</code> means list if not loaded yet. */
	// TODO: replace by a map indexed by cartridge names ?
	private List<IApplicationGear> gears = null;

	protected ApplicationResource(ApplicationResourceDTO dto, String cartridge, DomainResource domain) {
		this(dto.getName(), dto.getUuid(),dto.getCreationTime(), dto.getApplicationUrl(), dto.getGitUrl(),
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
	 *            the cartridge
	 * @param aliases
	 *            the aliases
	 * @param links
	 *            the links
	 * @param domain
	 *            the domain
	 */
	protected ApplicationResource(final String name, final String uuid, final String creationTime, final String applicationUrl,
			final String gitUrl, final String cartridge, final List<String> aliases, final Map<String, Link> links,
			final DomainResource domain) {
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
	 *            the cartridge
	 * @param aliases
	 *            the aliases
	 * @param links
	 *            the links
	 * @param domain
	 *            the domain
	 */
	protected ApplicationResource(final String name, final String uuid, final String creationTime, final String creationLog,
			final String applicationUrl, final String gitUrl, final String cartridge, final List<String> aliases,
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

	/*
	 * (non-Javadoc)
	 * @see com.openshift.client.IApplication#getName()
	 */
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * @see com.openshift.client.IApplication#getUUID()
	 */
	public String getUUID() {
		return uuid;
	}

	/*
	 * (non-Javadoc)
	 * @see com.openshift.client.IApplication#getCartridge()
	 */
	public String getCartridge() {
		return cartridge;
	}

	/*
	 * (non-Javadoc)
	 * @see com.openshift.client.IApplication#getCreationTime()
	 */
	public String getCreationTime() throws OpenShiftException {
		return creationTime;
	}

	/*
	 * (non-Javadoc)
	 * @see com.openshift.client.IApplication#getCreationLog()
	 */
	public String getCreationLog() {
		return creationLog;
	}

	/*
	 * (non-Javadoc)
	 * @see com.openshift.client.IApplication#getDomain()
	 */
	public IDomain getDomain() {
		return this.domain;
	}

	/*
	 * (non-Javadoc)
	 * @see com.openshift.client.IApplication#destroy()
	 */
	public void destroy() throws OpenShiftException, SocketTimeoutException {
		new DeleteApplicationRequest().execute();
		domain.removeApplication(this);
	}

	/*
	 * (non-Javadoc)
	 * @see com.openshift.client.IApplication#start()
	 */
	public void start() throws OpenShiftException, SocketTimeoutException {
		new StartApplicationRequest().execute();
	}

	/*
	 * (non-Javadoc)
	 * @see com.openshift.client.IApplication#restart()
	 */
	public void restart() throws OpenShiftException, SocketTimeoutException {
		new RestartApplicationRequest().execute();
	}

	/*
	 * (non-Javadoc)
	 * @see com.openshift.client.IApplication#stop()
	 */
	public void stop() throws OpenShiftException, SocketTimeoutException {
		stop(false);
	}

	/*
	 * (non-Javadoc)
	 * @see com.openshift.client.IApplication#stop(boolean)
	 */
	public void stop(boolean force) throws OpenShiftException, SocketTimeoutException {
		if (force) {
			new ForceStopApplicationRequest().execute();
		} else {
			new StopApplicationRequest().execute();

		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.openshift.client.IApplication#exposePort()
	 */
	public void exposePort() throws SocketTimeoutException, OpenShiftException {
		new ExposePortRequest().execute();
	}

	/*
	 * (non-Javadoc)
	 * @see com.openshift.client.IApplication#concealPort()
	 */
	public void concealPort() throws SocketTimeoutException, OpenShiftException {
		new ConcealPortRequest().execute();
	}

	/*
	 * (non-Javadoc)
	 * @see com.openshift.client.IApplication#showPort()
	 */
	public void showPort() throws SocketTimeoutException, OpenShiftException {
		new ShowPortRequest().execute();
	}

	/**
	 * Gets the descriptor.
	 * 
	 * @return the descriptor
	 */
	public void getDescriptor() {
		throw new UnsupportedOperationException("Feature is not implemented yet");
	}

	/*
	 * (non-Javadoc)
	 * @see com.openshift.client.IApplication#scaleDown()
	 */
	public void scaleDown() throws SocketTimeoutException, OpenShiftException {
		new ScaleDownRequest().execute();
	}

	/*
	 * (non-Javadoc)
	 * @see com.openshift.client.IApplication#scaleUp()
	 */
	public void scaleUp() throws SocketTimeoutException, OpenShiftException {
		new ScaleUpRequest().execute();
	}

	/*
	 * (non-Javadoc)
	 * @see com.openshift.client.IApplication#addAlias(java.lang.String)
	 */
	public void addAlias(String alias) throws SocketTimeoutException, OpenShiftException {
		ApplicationResourceDTO applicationDTO = new AddAliasRequest().execute(alias);
		this.aliases.clear();
		this.aliases.addAll(applicationDTO.getAliases());

	}

	/*
	 * (non-Javadoc)
	 * @see com.openshift.client.IApplication#getAliases()
	 */
	public List<String> getAliases() {
		return Collections.unmodifiableList(this.aliases);
	}

	/*
	 * (non-Javadoc)
	 * @see com.openshift.client.IApplication#removeAlias(java.lang.String)
	 */
	public void removeAlias(String alias) throws SocketTimeoutException, OpenShiftException {
		ApplicationResourceDTO applicationDTO = new RemoveAliasRequest().execute(alias);
		this.aliases.clear();
		this.aliases.addAll(applicationDTO.getAliases());
	}

	/*
	 * (non-Javadoc)
	 * @see com.openshift.client.IApplication#getLogReader()
	 */
	public ApplicationLogReader getLogReader() throws OpenShiftException {
		throw new UnsupportedOperationException();
		// ApplicationLogReader logReader = null;
		// if (logReaders.get(DEFAULT_LOGREADER) == null) {
		// logReader = new ApplicationLogReader(this, getInternalUser(), service);
		// logReaders.put(DEFAULT_LOGREADER, logReader);
		// }
		// return logReader;
	}

	/*
	 * (non-Javadoc)
	 * @see com.openshift.client.IApplication#getLogReader(java.lang.String)
	 */
	public ApplicationLogReader getLogReader(String logFile) throws OpenShiftException {
		throw new UnsupportedOperationException();
		// ApplicationLogReader logReader = null;
		// if (logReaders.get(logFile) == null) {
		// logReader = new ApplicationLogReader(this, getInternalUser(), service, logFile);
		// logReaders.put(logFile, logReader);
		// }
		// return logReader;
	}

	/*
	 * (non-Javadoc)
	 * @see com.openshift.client.IApplication#getGitUri()
	 */
	public String getGitUri() {
		return this.gitUrl;
	}

	/*
	 * (non-Javadoc)
	 * @see com.openshift.client.IApplication#getApplicationUrl()
	 */
	public String getApplicationUrl() {
		return applicationUrl;
		// IDomain domain = getInternalUser().getDomain();
		// if (domain == null) {
		// return null;
		// }
		// return MessageFormat.format(APPLICATION_URL_PATTERN, name, domain.getNamespace(), domain.getRhcDomain());
	}

	/*
	 * (non-Javadoc)
	 * @see com.openshift.client.IApplication#getHealthCheckUrl()
	 */
	public String getHealthCheckUrl() {
		// throw new OpenShiftException("NOT SUPPORTED FOR GENERIC APPLICATION");
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.openshift.client.IApplication#getHealthCheckResponse()
	 */
	public String getHealthCheckResponse() throws OpenShiftException {
		throw new OpenShiftException("NOT SUPPORTED FOR GENERIC APPLICATION");
	}

	/*
	 * (non-Javadoc)
	 * @see com.openshift.client.IApplication#addEmbeddedCartridge(java.lang.String)
	 */
	public void addEmbeddedCartridge(String embeddedCartridgeName) throws OpenShiftException, SocketTimeoutException {
		final CartridgeResourceDTO embeddedCartridgeDTO = new AddEmbeddedCartridgeRequest()
				.execute(embeddedCartridgeName);
		addEmbeddedCartridge(new EmbeddableCartridgeResource(embeddedCartridgeDTO.getName(), embeddedCartridgeDTO.getType(),
				embeddedCartridgeDTO.getLinks(), this));
	}

	/**
	 * Adds the embedded cartridge.
	 * 
	 * @param cartridge
	 *            the cartridge
	 */
	protected void addEmbeddedCartridge(IEmbeddableCartridge cartridge) {
		this.embeddedCartridges.add(cartridge);
	}

	/*
	 * (non-Javadoc)
	 * @see com.openshift.client.IApplication#addEmbeddedCartridges(java.util.List)
	 */
	public void addEmbeddedCartridges(List<String> embeddedCartridgeNames) throws OpenShiftException,
			SocketTimeoutException {
		for (String cartridge : embeddedCartridgeNames) {
			// TODO: catch exceptions when removing cartridges, contine removing
			// and report the exceptions that occurred<
			addEmbeddedCartridge(cartridge);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.openshift.client.IApplication#removeEmbbedCartridge(com.openshift.client.IEmbeddableCartridge)
	 */
	public void removeEmbeddedCartridge(IEmbeddableCartridge embeddedCartridge) throws OpenShiftException {
		this.embeddedCartridges.remove(embeddedCartridge);
	}

	/*
	 * (non-Javadoc)
	 * @see com.openshift.client.IApplication#removeEmbbedCartridges(java.util.List)
	 */
	public void removeEmbddeedCartridges(List<IEmbeddableCartridge> embeddedCartridges) throws OpenShiftException {
		for (IEmbeddableCartridge cartridge : embeddedCartridges) {
			// TODO: catch exceptions when removing cartridges, contine removing
			// and report the exceptions that occurred<
			removeEmbeddedCartridge(cartridge);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.openshift.client.IApplication#getEmbeddedCartridges()
	 */
	public List<IEmbeddableCartridge> getEmbeddedCartridges() throws OpenShiftException, SocketTimeoutException {
		// load collection if necessary
		if (embeddedCartridges == null) {
			this.embeddedCartridges = new ArrayList<IEmbeddableCartridge>();
			List<CartridgeResourceDTO> embeddableCartridgeDTOs = new ListEmbeddableCartridgesRequest().execute();
			for (CartridgeResourceDTO embeddableCartridgeDTO : embeddableCartridgeDTOs) {
				IEmbeddableCartridge embeddableCartridge = new EmbeddableCartridgeResource(embeddableCartridgeDTO.getName(),
						embeddableCartridgeDTO.getType(), embeddableCartridgeDTO.getLinks(), this);
				this.embeddedCartridges.add(embeddableCartridge);
			}
		}
		return embeddedCartridges;
	}

	/*
	 * (non-Javadoc)
	 * @see com.openshift.client.IApplication#hasEmbeddedCartridge(java.lang.String)
	 */
	public boolean hasEmbeddedCartridge(String cartridgeName) throws OpenShiftException, SocketTimeoutException {
		return getEmbeddedCartridge(cartridgeName) != null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.openshift.client.IApplication#getEmbeddedCartridge(java.lang.String)
	 */
	public IEmbeddableCartridge getEmbeddedCartridge(String cartridgeName) throws OpenShiftException,
			SocketTimeoutException {
		IEmbeddableCartridge embeddedCartridge = null;
		for (IEmbeddableCartridge cartridge : getEmbeddedCartridges()) {
			if (cartridgeName.equals(cartridge.getName())) {
				embeddedCartridge = cartridge;
				break;
			}
		}
		return embeddedCartridge;
	}

	/**
	 * Gets the gears.
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
					components.add(new ApplicationGearComponentResource(gearComponentDTO.getName(), gearComponentDTO
							.getInternalPort(), gearComponentDTO.getProxyHost(), gearComponentDTO.getProxyPort()));
				}
				IApplicationGear gear = new ApplicationGearResource(gearDTO.getUuid(), gearDTO.getGitUrl(), components, this);
				this.gears.add(gear);
			}
		}
		return gears;
	}

	/*
	 * (non-Javadoc)
	 * @see com.openshift.client.IApplication#waitForAccessible(long)
	 */
	public boolean waitForAccessible(long timeout) throws OpenShiftException {
		throw new UnsupportedOperationException();
		// return service.waitForApplication(getHealthCheckUrl(), timeout, getHealthCheckResponse());
	}

	public void refresh() throws SocketTimeoutException, OpenShiftException {
		this.embeddedCartridges = null;
		this.gears = null;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return name;
	}

	/**
	 * The Class DeleteApplicationRequest.
	 */
	private class DeleteApplicationRequest extends ServiceRequest {

		/**
		 * Instantiates a new delete application request.
		 */
		protected DeleteApplicationRequest() {
			super(LINK_DELETE_APPLICATION);
		}

	}

	/**
	 * The Class StartApplicationRequest.
	 */
	private class StartApplicationRequest extends ServiceRequest {

		/**
		 * Instantiates a new start application request.
		 */
		protected StartApplicationRequest() {
			super(LINK_START_APPLICATION);
		}

		/**
		 * Execute.
		 * 
		 * @param <DTO>
		 *            the generic type
		 * @return the dTO
		 * @throws OpenShiftException
		 *             the open shift exception
		 * @throws SocketTimeoutException
		 *             the socket timeout exception
		 */
		public <DTO> DTO execute() throws OpenShiftException, SocketTimeoutException {
			return super.execute(new ServiceParameter("event", "start"));
		}
	}

	/**
	 * The Class StopApplicationRequest.
	 */
	private class StopApplicationRequest extends ServiceRequest {

		/**
		 * Instantiates a new stop application request.
		 */
		protected StopApplicationRequest() {
			super(LINK_STOP_APPLICATION);
		}

		/**
		 * Execute.
		 * 
		 * @param <DTO>
		 *            the generic type
		 * @return the dTO
		 * @throws OpenShiftException
		 *             the open shift exception
		 * @throws SocketTimeoutException
		 *             the socket timeout exception
		 */
		public <DTO> DTO execute() throws OpenShiftException, SocketTimeoutException {
			return super.execute(new ServiceParameter("event", "stop"));
		}

	}

	/**
	 * The Class ForceStopApplicationRequest.
	 */
	private class ForceStopApplicationRequest extends ServiceRequest {

		/**
		 * Instantiates a new force stop application request.
		 */
		protected ForceStopApplicationRequest() {
			super(LINK_FORCE_STOP_APPLICATION);
		}

		/**
		 * Execute.
		 * 
		 * @param <DTO>
		 *            the generic type
		 * @return the dTO
		 * @throws OpenShiftException
		 *             the open shift exception
		 * @throws SocketTimeoutException
		 *             the socket timeout exception
		 */
		public <DTO> DTO execute() throws OpenShiftException, SocketTimeoutException {
			return super.execute(new ServiceParameter("event", "force-stop"));
		}

	}

	/**
	 * The Class RestartApplicationRequest.
	 */
	private class RestartApplicationRequest extends ServiceRequest {

		/**
		 * Instantiates a new restart application request.
		 */
		protected RestartApplicationRequest() {
			super(LINK_RESTART_APPLICATION);
		}

		/**
		 * Execute.
		 * 
		 * @param <DTO>
		 *            the generic type
		 * @return the dTO
		 * @throws OpenShiftException
		 *             the open shift exception
		 * @throws SocketTimeoutException
		 *             the socket timeout exception
		 */
		public <DTO> DTO execute() throws OpenShiftException, SocketTimeoutException {
			return super.execute(new ServiceParameter("event", "restart"));
		}
	}

	/**
	 * The Class ExposePortRequest.
	 */
	private class ExposePortRequest extends ServiceRequest {

		/**
		 * Instantiates a new expose port request.
		 */
		protected ExposePortRequest() {
			super(LINK_EXPOSE_PORT);
		}

		/**
		 * Execute.
		 * 
		 * @param <DTO>
		 *            the generic type
		 * @return the dTO
		 * @throws OpenShiftException
		 *             the open shift exception
		 * @throws SocketTimeoutException
		 *             the socket timeout exception
		 */
		public <DTO> DTO execute() throws OpenShiftException, SocketTimeoutException {
			return super.execute(new ServiceParameter("event", "expose-port"));
		}
	}

	/**
	 * The Class ConcealPortRequest.
	 */
	private class ConcealPortRequest extends ServiceRequest {

		/**
		 * Instantiates a new conceal port request.
		 */
		protected ConcealPortRequest() {
			super(LINK_CONCEAL_PORT);
		}

		/**
		 * Execute.
		 * 
		 * @param <DTO>
		 *            the generic type
		 * @return the dTO
		 * @throws OpenShiftException
		 *             the open shift exception
		 * @throws SocketTimeoutException
		 *             the socket timeout exception
		 */
		public <DTO> DTO execute() throws OpenShiftException, SocketTimeoutException {
			return super.execute(new ServiceParameter("event", "conceal-port"));
		}
	}

	/**
	 * The Class ShowPortRequest.
	 */
	private class ShowPortRequest extends ServiceRequest {

		/**
		 * Instantiates a new show port request.
		 */
		protected ShowPortRequest() {
			super(LINK_SHOW_PORT);
		}

		/**
		 * Execute.
		 * 
		 * @param <DTO>
		 *            the generic type
		 * @return the dTO
		 * @throws OpenShiftException
		 *             the open shift exception
		 * @throws SocketTimeoutException
		 *             the socket timeout exception
		 */
		public <DTO> DTO execute() throws OpenShiftException, SocketTimeoutException {
			return super.execute(new ServiceParameter("event", "show-port"));
		}
	}

	/**
	 * The Class ScaleUpRequest.
	 */
	private class ScaleUpRequest extends ServiceRequest {

		/**
		 * Instantiates a new scale up request.
		 */
		protected ScaleUpRequest() {
			super(LINK_SCALE_UP);
		}

		/**
		 * Execute.
		 * 
		 * @param <DTO>
		 *            the generic type
		 * @return the dTO
		 * @throws OpenShiftException
		 *             the open shift exception
		 * @throws SocketTimeoutException
		 *             the socket timeout exception
		 */
		public <DTO> DTO execute() throws OpenShiftException, SocketTimeoutException {
			return super.execute(new ServiceParameter("event", "scale-up"));
		}
	}

	/**
	 * The Class ScaleDownRequest.
	 */
	private class ScaleDownRequest extends ServiceRequest {

		/**
		 * Instantiates a new scale down request.
		 */
		protected ScaleDownRequest() {
			super(LINK_SCALE_DOWN);
		}

		/**
		 * Execute.
		 * 
		 * @param <DTO>
		 *            the generic type
		 * @return the dTO
		 * @throws OpenShiftException
		 *             the open shift exception
		 * @throws SocketTimeoutException
		 *             the socket timeout exception
		 */
		public <DTO> DTO execute() throws OpenShiftException, SocketTimeoutException {
			return super.execute(new ServiceParameter("event", "scale-down"));
		}
	}

	/**
	 * The Class AddAliasRequest.
	 */
	private class AddAliasRequest extends ServiceRequest {

		/**
		 * Instantiates a new adds the alias request.
		 */
		protected AddAliasRequest() {
			super(LINK_ADD_ALIAS);
		}

		/**
		 * Execute.
		 * 
		 * @param <DTO>
		 *            the generic type
		 * @param alias
		 *            the alias
		 * @return the dTO
		 * @throws OpenShiftException
		 *             the open shift exception
		 * @throws SocketTimeoutException
		 *             the socket timeout exception
		 */
		public <DTO> DTO execute(String alias) throws OpenShiftException, SocketTimeoutException {
			return super.execute(new ServiceParameter("event", "add-alias"), new ServiceParameter("alias", alias));
		}
	}

	/**
	 * The Class RemoveAliasRequest.
	 */
	private class RemoveAliasRequest extends ServiceRequest {

		/**
		 * Instantiates a new removes the alias request.
		 */
		protected RemoveAliasRequest() {
			super(LINK_REMOVE_ALIAS);
		}

		/**
		 * Execute.
		 * 
		 * @param <DTO>
		 *            the generic type
		 * @param alias
		 *            the alias
		 * @return the dTO
		 * @throws OpenShiftException
		 *             the open shift exception
		 * @throws SocketTimeoutException
		 *             the socket timeout exception
		 */
		public <DTO> DTO execute(String alias) throws OpenShiftException, SocketTimeoutException {
			return super.execute(new ServiceParameter("event", "remove-alias"), new ServiceParameter("alias", alias));
		}
	}

	/**
	 * The Class AddEmbeddedCartridgeRequest.
	 */
	private class AddEmbeddedCartridgeRequest extends ServiceRequest {

		/**
		 * Instantiates a new adds the embedded cartridge request.
		 */
		protected AddEmbeddedCartridgeRequest() {
			super(LINK_ADD_CARTRIDGE);
		}

		/**
		 * Execute.
		 * 
		 * @param <DTO>
		 *            the generic type
		 * @param embeddedCartridgeName
		 *            the embedded cartridge name
		 * @return the dTO
		 * @throws OpenShiftException
		 *             the open shift exception
		 * @throws SocketTimeoutException
		 *             the socket timeout exception
		 */
		public <DTO> DTO execute(String embeddedCartridgeName) throws OpenShiftException, SocketTimeoutException {
			return super.execute(new ServiceParameter("cartridge", embeddedCartridgeName));
		}
	}

	private class ListEmbeddableCartridgesRequest extends ServiceRequest {

		protected ListEmbeddableCartridgesRequest() {
			super(LINK_LIST_CARTRIDGES);
		}

		/**
		 * Execute.
		 */
		public <DTO> DTO execute() throws OpenShiftException, SocketTimeoutException {
			return super.execute();
		}
	}

	private class ListGearsRequest extends ServiceRequest {

		protected ListGearsRequest() {
			super(LINK_LIST_GEARS);
		}

		/**
		 * Execute.
		 */
		public <DTO> DTO execute() throws OpenShiftException, SocketTimeoutException {
			return super.execute();
		}
	}

}

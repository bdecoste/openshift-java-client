package com.openshift.internal.client.ssh;

import java.util.Arrays;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.openshift.client.IApplication;
import com.openshift.client.IApplicationPortForwarding;
import com.openshift.client.OpenShiftSSHOperationException;

public class ApplicationPortForwarding implements IApplicationPortForwarding {

	/** the remote application. */
	private final IApplication application;

	/** the remote binding name. */
	private final String name;

	/** the remote binding address. */
	private final String remoteAddress;

	/** the remote binding port number. */
	private final String remotePort;

	/** the local binding address, or null if not configured yet. */
	private String localAddress;

	/** the local binding port number, or null if not configured yet. */
	private String localPort;

	public ApplicationPortForwarding(final IApplication application, final String name, final String remoteAddress,
			final String remotePort) {
		super();
		this.application = application;
		this.name = name;
		this.remoteAddress = remoteAddress;
		this.remotePort = remotePort;
	}

	/**
	 * {@inheritDoc}
	 */
	public void start(final Session session) throws OpenShiftSSHOperationException {
		if (localAddress == null || localAddress.isEmpty()) {
			throw new IllegalArgumentException("Cannot enable port-forwarding from an undefined local address");
		}
		if (localPort == null || localPort.isEmpty()) {
			throw new IllegalArgumentException("Cannot enable port-forwarding from an undefined local address");
		}
		// don't start it twice
		if (!isStarted(session)) {
			try {
				session.setPortForwardingL(localAddress, Integer.parseInt(this.localPort), this.remoteAddress,
						Integer.parseInt(this.remotePort));
			} catch (Exception e) {
				throw new OpenShiftSSHOperationException(e, "Failed to start port forwarding on {0}:{1}",
						this.localAddress, this.localPort);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void stop(final Session session) throws OpenShiftSSHOperationException {
		if (isStarted(session)) {
			try {
				session.delPortForwardingL(localAddress, Integer.parseInt(localPort));
			} catch (Exception e) {
				throw new OpenShiftSSHOperationException(e, "Failed to stop port forwarding on {0}:{1}",
						this.localAddress, this.localPort);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isStarted(final Session session) throws OpenShiftSSHOperationException {
		if (session == null || !session.isConnected()) {
			return false;
		}
		try {
			// returned format : localPort:remoteHost:remotePort
			final String[] portForwardingL = session.getPortForwardingL();
			final String key = this.localPort + ":" + this.remoteAddress + ":" + this.remotePort;
			Arrays.sort(portForwardingL);
			final int r = Arrays.binarySearch(portForwardingL, key);
			return r >= 0;
		} catch (JSchException e) {
			throw new OpenShiftSSHOperationException(e, "Failed to retrieve SSH ports forwarding");
		}
	}

	/**
	 * @return the application
	 */
	protected final IApplication getApplication() {
		return application;
	}

	/*
	 * (non-Javadoc)
	 * @see com.openshift.internal.client.ssh.IApplicationPortForwarding#getName()
	 */
	public final String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * @see com.openshift.internal.client.ssh.IApplicationPortForwarding#getLocalAddress()
	 */
	public final String getLocalAddress() {
		return localAddress;
	}

	/*
	 * (non-Javadoc)
	 * @see com.openshift.internal.client.ssh.IApplicationPortForwarding#setLocalAddress(java.lang.String)
	 */
	public final void setLocalAddress(String localAddress) {
		this.localAddress = localAddress;
	}

	/*
	 * (non-Javadoc)
	 * @see com.openshift.internal.client.ssh.IApplicationPortForwarding#getLocalPort()
	 */
	public final String getLocalPort() {
		return localPort;
	}

	/*
	 * (non-Javadoc)
	 * @see com.openshift.internal.client.ssh.IApplicationPortForwarding#setLocalPort(java.lang.String)
	 */
	public final void setLocalPort(String localPort) {
		this.localPort = localPort;
	}

	/*
	 * (non-Javadoc)
	 * @see com.openshift.internal.client.ssh.IApplicationPortForwarding#getRemoteAddress()
	 */
	public final String getRemoteAddress() {
		return remoteAddress;
	}

	/*
	 * (non-Javadoc)
	 * @see com.openshift.internal.client.ssh.IApplicationPortForwarding#getRemotePort()
	 */
	public final String getRemotePort() {
		return remotePort;
	}

	@Override
	public String toString() {
		return "ApplicationForwardablePort [" + name + ": " + localAddress + ":" + localPort + " -> " + remoteAddress
				+ ":" + remotePort + "]";
	}

}
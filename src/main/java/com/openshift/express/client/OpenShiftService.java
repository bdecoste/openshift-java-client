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
package com.openshift.express.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.List;
import java.util.Properties;

import com.openshift.express.client.IHttpClientRequest.GetRequest;
import com.openshift.express.client.utils.HostUtils;
import com.openshift.express.internal.client.UserInfo;
import com.openshift.express.internal.client.httpclient.HttpClientException;
import com.openshift.express.internal.client.httpclient.NotFoundException;
import com.openshift.express.internal.client.httpclient.UnauthorizedException;
import com.openshift.express.internal.client.httpclient.UrlConnectionHttpClientBuilder;
import com.openshift.express.internal.client.response.OpenShiftResponse;
import com.openshift.express.internal.client.response.unmarshalling.NakedResponseUnmarshaller;
import com.openshift.express.internal.client.response.unmarshalling.dto.ResourceDTOFactory;
import com.openshift.express.internal.client.response.unmarshalling.dto.RestResponse;
import com.openshift.express.internal.client.utils.StreamUtils;
import com.openshift.express.internal.client.utils.UrlBuilder;

/**
 * @author André Dietisheim
 */
public class OpenShiftService implements IOpenShiftService {

	private static final String SYSPROPERTY_ENABLE_SNI_EXTENSION = "jsse.enableSNIExtension";
	private static final String SYSPROPERTY_PROXY_PORT = "proxyPort";
	private static final String SYSPROPERTY_PROXY_HOST = "proxyHost";
	private static final String SYSPROPERTY_PROXY_SET = "proxySet";

	// TODO extract to properties file
	private static final String USERAGENT_FORMAT = "Java OpenShift/{0} ({1})";
	private static final long APPLICATION_WAIT_DELAY = 2;

	private String baseUrl;
	private String id;
	private boolean doSSLChecks = false;

	protected static String version = null;

	public OpenShiftService(String id, String baseUrl) {
		this.id = id;
		this.baseUrl = baseUrl;

		// JDK7 bug workaround
		System.setProperty(SYSPROPERTY_ENABLE_SNI_EXTENSION, "false");
	}

	public void setEnableSSLCertChecks(boolean doSSLChecks) {
		this.doSSLChecks = doSSLChecks;
	}

	public void setProxySet(boolean proxySet) {
		if (proxySet) {
			System.setProperty(SYSPROPERTY_PROXY_SET, "true");
		} else {
			System.setProperty(SYSPROPERTY_PROXY_SET, "false");
		}
	}

	public void setProxyHost(String proxyHost) {
		System.setProperty(SYSPROPERTY_PROXY_HOST, proxyHost);
	}

	public void setProxyPort(String proxyPort) {
		System.setProperty(SYSPROPERTY_PROXY_PORT, proxyPort);
	}

	public String getServiceUrl() {
		return baseUrl + SERVICE_PATH;
	}

	public String getPlatformUrl() {
		return baseUrl;
	}

	public RestResponse getDomains(String url, IUser user) throws OpenShiftException, MalformedURLException {
		String response = sendRequest(new GetRequest(getResourceUrl(url)), createClient(user), "");
		return ResourceDTOFactory.get(response);
	}

	private URL getResourceUrl(String url) throws MalformedURLException {
		if (url.indexOf(IHttpClient.COLON) >= 0) {
			return new URL(url);
		} else {
			return new UrlBuilder(getServiceUrl()).path(url).toUrl();
		}
	}

	protected void validateApplicationName(final String name)
			throws OpenShiftException {

		for (int i = 0; i < name.length(); ++i) {
			if (!Character.isLetterOrDigit(name.charAt(i))) {
				throw new InvalidNameOpenShiftException(
						"Application name \"{0}\" contains non-alphanumeric characters.", name);
			}
		}
	}

	protected void validateDomainName(final String name)
			throws OpenShiftException {

		for (int i = 0; i < name.length(); ++i) {
			if (!Character.isLetterOrDigit(name.charAt(i))) {
				throw new InvalidNameOpenShiftException("Domain name \"{0}\" contains non-alphanumeric characters",
						name);
			}
		}
	}

	public boolean waitForHostResolves(final String url, final long timeout) throws OpenShiftException {
		try {
			long startTime = System.currentTimeMillis();
			boolean canResolv = false;
			while (!(canResolv = HostUtils.canResolv(url))
					&& System.currentTimeMillis() < startTime + timeout) {
				Thread.sleep(APPLICATION_WAIT_DELAY);
			}
			return canResolv;
		} catch (InterruptedException e) {
			return false;
		} catch (MalformedURLException e) {
			throw new OpenShiftException(e, "Application URL {0} is invalid", url);
		}
	}

	private String sendRequest(final IHttpClientRequest request, IHttpClient client, String errorMessage)
			throws OpenShiftException {
		String url = request.getUrl().toString();
		try {
			return request.execute(client);
		} catch (MalformedURLException e) {
			throw new OpenShiftException(e, errorMessage);
		} catch (UnauthorizedException e) {
			throw new InvalidCredentialsOpenShiftException(url, e);
		} catch (NotFoundException e) {
			throw new NotFoundOpenShiftException(url, e);
		} catch (SocketTimeoutException e) {
			throw new OpenShiftEndpointException(url, e, errorMessage);
		} catch (HttpClientException e) {
			throw new OpenShiftEndpointException(url, e, createNakedResponse(e.getMessage()), errorMessage);
		}
	}

	private OpenShiftResponse<Object> createNakedResponse(String response) throws OpenShiftException {
		return new NakedResponseUnmarshaller().unmarshall(response);
	}

	public static String getVersion() {
		if (version == null) {
			InputStream is = null;
			try {
				Properties props = new Properties();
				is = OpenShiftService.class.getClassLoader().getResourceAsStream("version.properties");
				props.load(is);
				version = props.getProperty("version");
			} catch (IOException e) {
				version = "Unknown";
			} finally {
				StreamUtils.quietlyClose(is);
			}
		}

		return version;
	}

	private IHttpClient createClient(IUser user) {
		return new UrlConnectionHttpClientBuilder()
				.setCredentials(user.getRhlogin(), user.getPassword())
				.setUserAgent(MessageFormat.format(USERAGENT_FORMAT, getVersion(), id))
				.setSSLChecks(doSSLChecks)
				.client();
	}

	public boolean isValid(IUser user) throws OpenShiftException {
		throw new UnsupportedOperationException();
	}

	public List<ICartridge> getCartridges(IUser user) throws OpenShiftException {
		throw new UnsupportedOperationException();
	}

	public List<IEmbeddableCartridge> getEmbeddableCartridges(IUser user) throws OpenShiftException {
		throw new UnsupportedOperationException();
	}

	public IApplication createApplication(String name, ICartridge cartridge, IUser user) throws OpenShiftException {
		throw new UnsupportedOperationException();
	}

	public IJBossASApplication createJBossASApplication(String name, IUser user) throws OpenShiftException {
		throw new UnsupportedOperationException();
	}

	public IRubyApplication createRubyApplication(String name, IUser user) throws OpenShiftException {
		throw new UnsupportedOperationException();	
	}

	public IPythonApplication createPythonApplication(String name, IUser user) throws OpenShiftException {
		throw new UnsupportedOperationException();
	}

	public IPerlApplication createPerlApplication(String name, IUser user) throws OpenShiftException {
		throw new UnsupportedOperationException();
	}

	public IPHPApplication createPHPApplication(String name, IUser user) throws OpenShiftException {
		throw new UnsupportedOperationException();
	}

	public IJenkinsApplication createJenkinsApplication(String name, IUser user) throws OpenShiftException {
		throw new UnsupportedOperationException();
	}

	public INodeJSApplication createNodeJSApplication(String name, IUser user) throws OpenShiftException {
		throw new UnsupportedOperationException();
	}

	public IHAProxyApplication createHAProxyApplication(String name, IUser user) throws OpenShiftException {
		throw new UnsupportedOperationException();
	}

	public IRawApplication createRawApplication(String name, IUser user) throws OpenShiftException {
		throw new UnsupportedOperationException();
	}

	public IApplication createApplication(String name, ICartridge cartridge, IUser user, String nodeProfile)
			throws OpenShiftException {
		throw new UnsupportedOperationException();
	}

	public IJBossASApplication createJBossASApplication(String name, IUser user, String nodeProfile)
			throws OpenShiftException {
		throw new UnsupportedOperationException();
	}

	public IRubyApplication createRubyApplication(String name, IUser user, String nodeProfile)
			throws OpenShiftException {
		throw new UnsupportedOperationException();
	}

	public IPythonApplication createPythonApplication(String name, IUser user, String nodeProfile)
			throws OpenShiftException {
		throw new UnsupportedOperationException();
	}

	public IPerlApplication createPerlApplication(String name, IUser user, String nodeProfile)
			throws OpenShiftException {
		throw new UnsupportedOperationException();
	}

	public IPHPApplication createPHPApplication(String name, IUser user, String nodeProfile) throws OpenShiftException {
		throw new UnsupportedOperationException();
	}

	public IJenkinsApplication createJenkinsApplication(String name, IUser user, String nodeProfile)
			throws OpenShiftException {
		throw new UnsupportedOperationException();
	}

	public INodeJSApplication createNodeJSApplication(String name, IUser user, String nodeProfile)
			throws OpenShiftException {
		throw new UnsupportedOperationException();
	}

	public IHAProxyApplication createHAProxyApplication(String name, IUser user, String nodeProfile)
			throws OpenShiftException {
		throw new UnsupportedOperationException();
	}

	public IRawApplication createRawApplication(String name, IUser user, String nodeProfile) throws OpenShiftException {
		throw new UnsupportedOperationException();
	}

	public void destroyApplication(String name, ICartridge cartridge, IUser user) throws OpenShiftException {
		throw new UnsupportedOperationException();
	}

	public IApplication startApplication(String name, ICartridge cartridge, IUser user) throws OpenShiftException {
		throw new UnsupportedOperationException();
	}

	public IApplication restartApplication(String name, ICartridge cartridge, IUser user) throws OpenShiftException {
		throw new UnsupportedOperationException();
	}

	public IApplication stopApplication(String name, ICartridge cartridge, IUser user) throws OpenShiftException {
		throw new UnsupportedOperationException();
	}

	public IApplication threadDumpApplication(String name, ICartridge cartridge, IUser user) throws OpenShiftException {
		throw new UnsupportedOperationException();
	}

	public IEmbeddableCartridge addEmbeddedCartridge(String applicationName, IEmbeddableCartridge cartridge, IUser user)
			throws OpenShiftException {
		throw new UnsupportedOperationException();
	}

	public void removeEmbeddedCartridge(String applicationName, IEmbeddableCartridge cartridge, IUser user)
			throws OpenShiftException {
		throw new UnsupportedOperationException();
		
	}

	public String getStatus(String name, ICartridge cartridge, IUser user) throws OpenShiftException {
		throw new UnsupportedOperationException();
	}

	public String getStatus(String name, ICartridge cartridge, IUser user, String logFile, int numLines)
			throws OpenShiftException {
		throw new UnsupportedOperationException();
	}

	public IDomain changeDomain(String name, ISSHPublicKey sshKey, IUser user) throws OpenShiftException {
		throw new UnsupportedOperationException();
	}

	public IDomain createDomain(String name, ISSHPublicKey sshKey, IUser user) throws OpenShiftException {
		throw new UnsupportedOperationException();
	}

	public void destroyDomain(String name, IUser user) throws OpenShiftException {
		throw new UnsupportedOperationException();
	}

	public UserInfo getUserInfo(IUser user) throws OpenShiftException {
		throw new UnsupportedOperationException();
	}

	public boolean waitForApplication(String applicationHealthCheckUrl, long timeout, String expectedResponse)
			throws OpenShiftException {
		throw new UnsupportedOperationException();
	}
}

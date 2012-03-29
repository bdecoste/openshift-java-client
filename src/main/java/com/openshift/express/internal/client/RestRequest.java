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
package com.openshift.express.internal.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Properties;

import com.openshift.express.client.HttpMethod;
import com.openshift.express.client.IHttpClient;
import com.openshift.express.client.IUser;
import com.openshift.express.client.InvalidCredentialsOpenShiftException;
import com.openshift.express.client.NotFoundOpenShiftException;
import com.openshift.express.client.OpenShiftEndpointException;
import com.openshift.express.client.OpenShiftException;
import com.openshift.express.internal.client.httpclient.HttpClientException;
import com.openshift.express.internal.client.httpclient.NotFoundException;
import com.openshift.express.internal.client.httpclient.UnauthorizedException;
import com.openshift.express.internal.client.httpclient.UrlConnectionHttpClientBuilder;
import com.openshift.express.internal.client.response.OpenShiftResponse;
import com.openshift.express.internal.client.response.unmarshalling.NakedResponseUnmarshaller;
import com.openshift.express.internal.client.response.unmarshalling.dto.Link;
import com.openshift.express.internal.client.response.unmarshalling.dto.RequiredLinkParameter;
import com.openshift.express.internal.client.utils.StreamUtils;

/**
 * @author André Dietisheim
 */
public class RestRequest {

	private static final String SERVICE_PATH = "/broker/rest/";

	private static final String SYSPROPERTY_PROXY_PORT = "proxyPort";
	private static final String SYSPROPERTY_PROXY_HOST = "proxyHost";
	private static final String SYSPROPERTY_PROXY_SET = "proxySet";

	// TODO extract to properties file
	private static final String USERAGENT_FORMAT = "Java OpenShift/{0} ({1})";

	private String baseUrl;
	private String id;
	private IUser user;
	private boolean doSSLChecks = false;

	protected static String version = null;

	public RestRequest(String id, String baseUrl, IUser user) {
		this.id = id;
		this.baseUrl = baseUrl;
		this.user = user;
	}

	public String execute(Link link, HttpParameters parameters)
			throws OpenShiftException, MalformedURLException, UnsupportedEncodingException {
		validateParameters(parameters, link);
		HttpMethod httpMethod = link.getHttpMethod();
		try {
			IHttpClient client = createClient(user);
			URL url = new URL(link.getHref());
			String data = parameters.toUrlEncoded();
			switch (link.getHttpMethod()) {
			case GET:
				return client.get(url);
			case POST:
				return client.post(data, url);
			case PUT:
				return client.put(data, url);
			case DELETE:
				return client.delete(url);
			default:
				throw new OpenShiftException("Unexpected Http method {0}", httpMethod.toString());
			}
		} catch(UnsupportedEncodingException e) {
			throw new OpenShiftException(e, "Could not encode parameters: {0}", e.getMessage());
		} catch(MalformedURLException e) {
			throw new OpenShiftException(e, "Could not encode parameters: {0}", e.getMessage());
		} catch (UnauthorizedException e) {
			throw new InvalidCredentialsOpenShiftException(link.getHref(), e);
		} catch (NotFoundException e) {
			throw new NotFoundOpenShiftException(link.getHref(), e);
		} catch (SocketTimeoutException e) {
			throw new OpenShiftEndpointException(link.getHref(), e, e.getMessage());
		} catch (HttpClientException e) {
			throw new OpenShiftEndpointException(link.getHref(), e, createNakedResponse(e.getMessage()), e.getMessage());
		}
	}

	private void validateParameters(HttpParameters parameters, Link link) {
		for (RequiredLinkParameter requiredParameter : link.getRequiredParams()) {
			ensureRequiredParameter(requiredParameter, parameters);
		}
	}

	private void ensureRequiredParameter(RequiredLinkParameter parameter, HttpParameters parameters) {
		
	}

	private OpenShiftResponse<Object> createNakedResponse(String response) throws OpenShiftException {
		return new NakedResponseUnmarshaller().unmarshall(response);
	}

	private IHttpClient createClient(IUser user) {
		return new UrlConnectionHttpClientBuilder().setCredentials(user.getRhlogin(), user.getPassword())
				.setUserAgent(MessageFormat.format(USERAGENT_FORMAT, getVersion(), id)).setSSLChecks(doSSLChecks)
				.client();
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

	public static String getVersion() {
		if (version == null) {
			InputStream is = null;
			try {
				Properties props = new Properties();
				is = RestRequest.class.getClassLoader().getResourceAsStream("version.properties");
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
}

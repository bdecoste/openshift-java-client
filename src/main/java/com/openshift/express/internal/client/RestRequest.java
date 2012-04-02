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

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.MessageFormat;

import com.openshift.express.client.HttpMethod;
import com.openshift.express.client.IHttpClient;
import com.openshift.express.client.IUser;
import com.openshift.express.client.InvalidCredentialsOpenShiftException;
import com.openshift.express.client.NotFoundOpenShiftException;
import com.openshift.express.client.OpenShiftEndpointException;
import com.openshift.express.client.OpenShiftException;
import com.openshift.express.client.OpenShiftRequestParameterException;
import com.openshift.express.internal.client.httpclient.HttpClientException;
import com.openshift.express.internal.client.httpclient.NotFoundException;
import com.openshift.express.internal.client.httpclient.UnauthorizedException;
import com.openshift.express.internal.client.httpclient.UrlConnectionHttpClientBuilder;
import com.openshift.express.internal.client.response.OpenShiftResponse;
import com.openshift.express.internal.client.response.unmarshalling.NakedResponseUnmarshaller;
import com.openshift.express.internal.client.response.unmarshalling.dto.Link;
import com.openshift.express.internal.client.response.unmarshalling.dto.LinkParameter;
import com.openshift.express.internal.client.response.unmarshalling.dto.LinkParameterType;
import com.openshift.express.internal.client.utils.StringUtils;

/**
 * @author André Dietisheim
 */
public class RestRequest {

	private static final String SERVICE_PATH = "/broker/rest/";

	private static final String SYSPROPERTY_PROXY_PORT = "proxyPort";
	private static final String SYSPROPERTY_PROXY_HOST = "proxyHost";
	private static final String SYSPROPERTY_PROXY_SET = "proxySet";

	private String baseUrl;

	private IHttpClient client;

	protected static String version = null;

	public RestRequest(String id, String baseUrl, boolean doSSLChecks, IUser user) {
		this(baseUrl, doSSLChecks, new RestRequestProperties(), user);
	}

	private RestRequest(String baseUrl, boolean doSSLChecks, RestRequestProperties properties, IUser user) {
		this(baseUrl
				, new UrlConnectionHttpClientBuilder()
						.setCredentials(user.getRhlogin(), user.getPassword())
						.setUserAgent(MessageFormat.format(properties.getUseragent(), properties.getClientId(), properties.getVersion()))
						.setSSLChecks(doSSLChecks)
						.client());
	}

	public RestRequest(String baseUrl, IHttpClient client) {
		this.baseUrl = baseUrl;
		this.client = client;
	}

	public String execute(Link link, HttpParameters parameters)
			throws OpenShiftException, MalformedURLException, UnsupportedEncodingException {
		validateParameters(parameters, link);
		HttpMethod httpMethod = link.getHttpMethod();
		try {
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
		} catch (UnsupportedEncodingException e) {
			throw new OpenShiftException(e, "Could not encode parameters: {0}", e.getMessage());
		} catch (MalformedURLException e) {
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

	private void validateParameters(HttpParameters parameters, Link link)
			throws OpenShiftRequestParameterException {
		for (LinkParameter requiredParameter : link.getRequiredParams()) {
			validateRequiredParameter(requiredParameter, parameters, link);
		}
		for (LinkParameter optionalParameter : link.getOptionalParams()) {
			validateOptionalParameters(optionalParameter, link);
		}
	}

	private void validateRequiredParameter(LinkParameter parameter, HttpParameters parameters, Link link)
			throws OpenShiftRequestParameterException {
		if (!parameters.containsKey(parameter.getName())) {
			throw new OpenShiftRequestParameterException(
					"Requesting {0}: required request parameter \"{1}\" is missing", link.getHref(),
					parameter.getName());
		}

		Object parameterValue = parameters.get(parameter.getName());
		if (parameterValue == null
				|| isEmptyString(parameter, parameterValue)) {
			throw new OpenShiftRequestParameterException("Requesting {0}: required request parameter \"{1}\" is empty",
					link.getHref(), parameter.getName());
		}
		// TODO: check valid options (still reported in a very incosistent way)
	}

	private void validateOptionalParameters(LinkParameter optionalParameter, Link link) {
		// TODO: implement
	}
	
	private boolean isEmptyString(LinkParameter parameter, Object parameterValue) {
		return parameter.getType() == LinkParameterType.STRING
				&& StringUtils.isEmpty((String) parameterValue);
	}

	private OpenShiftResponse<Object> createNakedResponse(String response) throws OpenShiftException {
		return new NakedResponseUnmarshaller().unmarshall(response);
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
}

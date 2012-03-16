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
package com.openshift.express.internal.client.httpclient;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.openshift.express.client.IHttpClient;
import com.openshift.express.client.utils.Base64Coder;
import com.openshift.express.internal.client.utils.StreamUtils;

/**
 * @author André Dietisheim
 */
public class UrlConnectionHttpClientBuilder {

	private String userAgent;
	private boolean sslChecks = false;
	private String username;
	private String password;

	public UrlConnectionHttpClientBuilder setUserAgent(String userAgent) {
		this.userAgent = userAgent;
		return this;
	}
	
	public UrlConnectionHttpClientBuilder setSSLChecks(boolean check) {
		this.sslChecks = check;
		return this;
	}
	
	public UrlConnectionHttpClientBuilder setCredentials(String username, String password) {
		this.username = username;
		this.password = password;
		return this;
	}
	
	public IHttpClient setUrl(URL url) {
		return new UrlConnectionHttpClient(username, password, userAgent, sslChecks, url);
	}
	
	private class UrlConnectionHttpClient implements IHttpClient {

		private static final String HTTP_METHOD_PUT = "PUT";
		private static final String HTTP_METHOD_POST = "POST";
		private static final String HTTP_METHOD_DELETE = "DELETE";

		private static final String PROPERTY_CONTENT_TYPE = "Content-Type";
		private static final String CONTENT_TYPE_APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";
		private static final String PROPERTY_AUTHORIZATION = "Authorization";
		private static final String AUTHORIZATION_BASIC = "Basic";
		private static final String PROPERTY_ACCEPT = "Accept";
		private static final String ACCEPT_APPLICATION_JSON = "application/json";
		
		private static final int DEFAULT_CONNECT_TIMEOUT = 10 * 1024;
		private static final int DEFAULT_READ_TIMEOUT = 60 * 1024;
		private static final String SYSPROP_OPENSHIFT_CONNECT_TIMEOUT = "com.openshift.express.httpclient.timeout";
		private static final String SYSPROP_DEFAULT_CONNECT_TIMEOUT = "sun.net.client.defaultConnectTimeout";
		private static final String SYSPROP_DEFAULT_READ_TIMEOUT = "sun.net.client.defaultReadTimeout";

		private static final char SPACE = ' ';
		private static final char COLON = ':';

		private String userAgent;
		private boolean sslChecks;
		private String username;
		private String password;
		private URL url;
		
		public UrlConnectionHttpClient(String username, String password, String userAgent, boolean sslChecks, URL url) {
			this.username = username;
			this.password = password;
			this.userAgent = userAgent;
			this.sslChecks = sslChecks;
			this.url = url;
		}

		public String get() throws HttpClientException, SocketTimeoutException {
			HttpURLConnection connection = null;
			try {
				connection = createConnection(username, password, userAgent, url);
				return StreamUtils.readToString(connection.getInputStream());
			} catch (FileNotFoundException e) {
				throw new NotFoundException(
						MessageFormat.format("Could not find resource {0}", url.toString()), e);
			} catch (IOException e) {
				throw createException(e, connection);
			} finally {
				disconnect(connection);
			}
		}

		public String put(String data) throws HttpClientException, SocketTimeoutException {
			return write(data, HTTP_METHOD_PUT);
		}

		public String post(String data) throws HttpClientException, SocketTimeoutException {
			return write(data, HTTP_METHOD_POST);
		}

		public String delete() throws HttpClientException, SocketTimeoutException {
			return write(null, HTTP_METHOD_DELETE);
		}

		protected String write(String data, String requestMethod) throws SocketTimeoutException, HttpClientException {
			HttpURLConnection connection = null;
			try {
				connection = createConnection(username, password, userAgent, url);
				connection.setRequestMethod(requestMethod);
				connection.setDoOutput(true);
				if (data != null) {
					StreamUtils.writeTo(data.getBytes(), connection.getOutputStream());
				}
				return StreamUtils.readToString(connection.getInputStream());
			} catch (FileNotFoundException e) {
				throw new NotFoundException(
						MessageFormat.format("Could not find resource {0}", url.toString()), e);
			} catch (IOException e) {
				throw createException(e, connection);
			} finally {
				disconnect(connection);
			}

		}

		private void disconnect(HttpURLConnection connection) {
			if (connection != null) {
				connection.disconnect();
			}
		}

		private HttpClientException createException(IOException ioe, HttpURLConnection connection)
				throws SocketTimeoutException {
			try {
				int responseCode = connection.getResponseCode();
				String errorMessage = StreamUtils.readToString(connection.getErrorStream());
				switch (responseCode) {
				case STATUS_INTERNAL_SERVER_ERROR:
					return new InternalServerErrorException(errorMessage, ioe);
				case STATUS_BAD_REQUEST:
					return new BadRequestException(errorMessage, ioe);
				case STATUS_UNAUTHORIZED:
					return new UnauthorizedException(errorMessage, ioe);
				case STATUS_NOT_FOUND:
					return new NotFoundException(errorMessage, ioe);
				default:
					return new HttpClientException(errorMessage, ioe);
				}
			} catch (SocketTimeoutException e) {
				throw e;
			} catch (IOException e) {
				return new HttpClientException(e);
			}
		}


		private void setupAuthorisation(String username, String password, HttpURLConnection connection) {
			if (username == null) {
				return;
			}

			String credentials = Base64Coder.encodeString(
					new StringBuilder().append(username).append(COLON).append(password).toString());
			connection.setRequestProperty(PROPERTY_AUTHORIZATION,
					new StringBuilder().append(AUTHORIZATION_BASIC).append(SPACE).append(credentials).toString());
		}

		private void setupSSLChecks(URL url, HttpURLConnection connection) {
			if (isHttps(url)
					&& !sslChecks) {
				HttpsURLConnection httpsConnection = (HttpsURLConnection) connection;
				httpsConnection.setHostnameVerifier(new NoopHostnameVerifier());
				setPermissiveSSLSocketFactory(httpsConnection);
			}
		}

		private void setConnectTimeout(URLConnection connection) {
			int timeout = getSystemPropertyInteger(SYSPROP_OPENSHIFT_CONNECT_TIMEOUT);
			if (timeout > -1) {
				connection.setConnectTimeout(timeout);
				return;
			}
			timeout = getSystemPropertyInteger(SYSPROP_DEFAULT_CONNECT_TIMEOUT);
			if (timeout == -1) {
				connection.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT);
			}
		}

		private void setReadTimeout(URLConnection connection) {
			int timeout = getSystemPropertyInteger(SYSPROP_DEFAULT_READ_TIMEOUT);
			if (timeout == -1) {
				connection.setReadTimeout(DEFAULT_READ_TIMEOUT);
			}
		}

		private int getSystemPropertyInteger(String key) {
			try {
				return Integer.parseInt(System.getProperty(key));
			} catch (NumberFormatException e) {
				return -1;
			}
		}

		private boolean isHttps(URL url) {
			return "https".equals(url.getProtocol());
		}

		/**
		 * Sets a trust manager that will always trust.
		 * <p>
		 * TODO: dont swallog exceptions and setup things so that they dont disturb
		 * other components.
		 */
		private void setPermissiveSSLSocketFactory(HttpsURLConnection connection) {
			try {
				SSLContext sslContext = SSLContext.getInstance("SSL");
				sslContext.init(new KeyManager[0], new TrustManager[] { new PermissiveTrustManager() }, new SecureRandom());
				SSLSocketFactory socketFactory = sslContext.getSocketFactory();
				((HttpsURLConnection) connection).setSSLSocketFactory(socketFactory);
			} catch (KeyManagementException e) {
				// ignore
			} catch (NoSuchAlgorithmException e) {
				// ignore
			}
		}

		private HttpURLConnection createConnection(String username, String password, String userAgent, URL url)
				throws IOException {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			setupSSLChecks(url, connection);
			setupAuthorisation(username, password, connection);
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setAllowUserInteraction(false);
			setConnectTimeout(connection);
			setReadTimeout(connection);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty(PROPERTY_CONTENT_TYPE, CONTENT_TYPE_APPLICATION_FORM_URLENCODED);
			connection.setRequestProperty(USER_AGENT, userAgent);
			connection.setRequestProperty(PROPERTY_ACCEPT, ACCEPT_APPLICATION_JSON);
			return connection;
		}

		private class PermissiveTrustManager implements X509TrustManager {

			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}
		}
		
		private class NoopHostnameVerifier implements HostnameVerifier {

			public boolean verify(String hostname, SSLSession sslSession) {
				return true;
			}
		}

	}
	
}

/*************************************************************************
 * 
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.  The
 * ASF licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 * 
 *************************************************************************/
package com.openshift.express.internal.client.test.fakes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.openshift.express.internal.client.utils.StreamUtils;

/**
 * @author André Dietisheim
 */
public class ServerFake {

	public static final int DEFAULT_PORT = 8181;

	private ExecutorService executor;
	private int port;
	private String response;
	private ServerFakeSocket serverSocket;

	public ServerFake(int port) {
		this(port, null);
	}

	public ServerFake() {
		this(DEFAULT_PORT, null);
	}

	public ServerFake(String response) {
		this(DEFAULT_PORT, response);
	}

	/**
	 * 
	 * @param port
	 *            the port to listen to (address is always localhost)
	 * @param response
	 *            the reponse to return to the requesting socket. If
	 *            <code>null</code> the request string is returned.
	 * @see ServerFakeSocket#getResponse(Socket)
	 */
	public ServerFake(int port, String response) {
		this.port = port;
		this.response = response;
	}

	public void start() {
		executor = Executors.newFixedThreadPool(1);
		this.serverSocket = new ServerFakeSocket(port, response);
		executor.submit(serverSocket);
	}

	public String getUrl() {
		return MessageFormat.format("http://localhost:{0}/", String.valueOf(port));
	}

	public void stop() {
		executor.shutdownNow();
		serverSocket.shutdown();
	}

	private class ServerFakeSocket implements Runnable {
		private String response;
		private ServerSocket serverSocket;

		private ServerFakeSocket(int port, String response) {

			this.response = response;

			try {
				this.serverSocket = new ServerSocket(port);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void shutdown() {
			try {
				this.serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			Socket socket = null;
			OutputStream outputStream = null;
			try {
				socket = serverSocket.accept();
				String response = getResponse(socket);
				outputStream = socket.getOutputStream();
				outputStream.write(response.getBytes());
				outputStream.flush();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				StreamUtils.quietlyClose(outputStream);
			}
		}

		/**
		 * Returns the response instance variable if present. If not, the
		 * content of the inputStream is returned.
		 * 
		 * @param inputStream
		 * @return
		 * @throws IOException
		 */
		private String getResponse(Socket socket) throws IOException {
			if (response != null) {
				return response;
			}
			return readRequestToString(socket.getInputStream());
		}

		public String readRequestToString(InputStream inputStream) throws IOException {
			BufferedReader bufferedReader = null;
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			StringWriter writer = new StringWriter();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				if (line.isEmpty()) {
					break;
				}
				writer.write(line);
				writer.write('\n');
			}
			return writer.toString();
		}

	}
}
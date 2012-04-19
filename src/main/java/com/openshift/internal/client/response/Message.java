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

import java.text.MessageFormat;

/**
 * @author Andre Dietisheim
 */
public class Message {

	private String text;
	private String severity;
	private String parameter;
	private int exitCode;

	public Message(String text, String parameter, String severity, int exitCode) {
		this.text = text;
		this.severity = severity;
		this.parameter = parameter;
		this.exitCode = exitCode;
	}

	public String getParameter() {
		return parameter;
	}

	public String getText() {
		return text;
	}

	public String getSeverity() {
		return severity;
	}

	public int getExitCode() {
		return exitCode;
	}

	@Override
	public String toString() {
		return MessageFormat.format(
				"Operation failed with a(n) {0} and exit code {1}. Reason: {2}"
				, severity, exitCode, text);
	}
}

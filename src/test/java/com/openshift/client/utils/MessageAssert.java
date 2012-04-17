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
package com.openshift.client.utils;

import static org.junit.Assert.assertEquals;
import org.fest.assertions.AssertExtension;

import com.openshift.internal.client.response.unmarshalling.dto.Message;

/**
 * @author Andre Dietisheim
 */
public class MessageAssert implements AssertExtension {

	private Message message;

	public MessageAssert(Message message) {
		this.message = message;
	}

	public MessageAssert hasText(String text) {
		assertEquals(message.getText(), text);
		return this;
	}

	public MessageAssert hasSeverity(String severity) {
		assertEquals(message.getSeverity(), severity);
		return this;
	}

	public MessageAssert hasExitCode(int exitCode) {
		assertEquals(message.getExitCode(), exitCode);
		return this;
	}

	public MessageAssert hasParameter(String parameter) {
		assertEquals(message.getParameter(), parameter);
		return this;
	}
}

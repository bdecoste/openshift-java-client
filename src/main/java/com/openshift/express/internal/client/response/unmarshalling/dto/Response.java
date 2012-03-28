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
package com.openshift.express.internal.client.response.unmarshalling.dto;

import java.util.List;

/**
 * The Class Response.
 */
public class Response {

	/** The status. */
	final String status;

	/** The messages. */
	final List<String> messages;

	/** The data type. */
	final EnumDataType dataType;

	/** The data. */
	final Object data;

	/**
	 * Instantiates a new response.
	 *
	 * @param status the status
	 * @param messages the messages
	 * @param data the data
	 * @param dataType the data type
	 */
	public Response(final String status, final List<String> messages, final Object data, final EnumDataType dataType) {
		this.status = status;
		this.messages = messages;
		this.data = data;
		this.dataType = dataType;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public final String getStatus() {
		return status;
	}

	/**
	 * Gets the messages.
	 *
	 * @return the messages
	 */
	public final List<String> getMessages() {
		return messages;
	}

	/**
	 * Gets the data type.
	 *
	 * @return the dataType
	 */
	public final EnumDataType getDataType() {
		return dataType;
	}

	/**
	 * Gets the data.
	 *
	 * @param <T> the generic type
	 * @return the data, casted as the caller requires. To avoid ClassCastExceptions, caller may refer to the
	 * {@link Response#getDataType()} method to discover the actual type of the data.
	 */
	@SuppressWarnings("unchecked")
	public final <T> T getData() {
		return (T) data;
	}

}

/**
 * 
 */
package com.openshift.express.client;

import com.openshift.express.internal.client.response.unmarshalling.dto.Response;

/**
 * @author Xavier Coulon
 *
 */
public class OpenShiftOperationException extends OpenShiftException {

	/** serialVersionUID. */
	private static final long serialVersionUID = 2001488542842858414L;
	
	/** the response received by the client. */
	private final Response response;
	
	/**
	 * Full constructor
	 * @param response the response received by the client.
	 */
	public OpenShiftOperationException(Response response) {
		super((Throwable)null, "Operation failed", (Object[])null);
		this.response = response;
	}

	/**
	 * @return the response received by the client.
	 */
	public Response getResponse() {
		return response;
	}

}

package com.openshift.client;


public interface IOpenShiftResource {

	/**
	 * Returns the log that was created when the resource was created.
	 * 
	 * @return the log which reported the creation of this resource
	 */
	public String getCreationLog();

	/**
	 * Returns
	 * <code>true</true> if there is log about the creation of this resource. 
	 * Creation logs are only available at creation time. So resources that were retrieved 
	 * while they already existed wont have a creation log.
	 * 
	 * @return true if there's cretion log for this resource
	 */
	public boolean hasCreationLog();

}
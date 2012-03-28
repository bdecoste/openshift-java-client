package com.openshift.express.internal.client.response.unmarshalling.dto;

public interface ILinkNames {

	public static final String GET = "GET";

	public static final String UPDATE = "UPDATE";

	public static final String CREATE = "CREATE";

	public static final String DELETE = "DELETE";

	public static final String LIST_APPLICATIONS = "LIST_APPLICATIONS";

	public static final String ADD_APPLICATION = "ADD_APPLICATION";

	public static final String ADD_APPLICATION_FROM_TEMPLATE = "ADD_APPLICATION_FROM_TEMPLATE";

	public static final String SHOW_PORT = "SHOW_PORT";
	
	public static final String ADD_ALIAS = "ADD_ALIAS";
	
	public static final String REMOVE_ALIAS = "REMOVE_ALIAS";
	
	public static final String START = "START";
	
	public static final String STOP = "STOP";
	
	public static final String FORCE_STOP = "FORCE_STOP";
	
	public static final String RESTART = "RESTART";
	
	public static final String CONCEAL_PORT = "CONCEAL_PORT";
	
	public static final String EXPOSE_PORT = "EXPOSE_PORT";
	
	public static final String LIST_CARTRIDGES = "LIST_CARTRIDGES";
	
	public static final String ADD_CARTRIDGE = "ADD_CARTRIDGE";
	
	public static final String GET_DESCRIPTOR = "GET_DESCRIPTOR";
	
	public static final String GET_GEARS = "GET_GEARS";
}

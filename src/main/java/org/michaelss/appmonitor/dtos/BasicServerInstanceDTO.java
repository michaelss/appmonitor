package org.michaelss.appmonitor.dtos;

import org.michaelss.appmonitor.models.ServerType;

public class BasicServerInstanceDTO {

	private Integer id;

	private String description;
	
	private String host;
	
	private Integer port;
	
	private ServerType serverType;

	public BasicServerInstanceDTO(Integer id, String description, String host, Integer port, ServerType serverType) {
		super();
		this.id = id;
		this.description = description;
		this.host = host;
		this.port = port;
		this.serverType = serverType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public ServerType getServerType() {
		return serverType;
	}

	public void setServerType(ServerType serverType) {
		this.serverType = serverType;
	}
}

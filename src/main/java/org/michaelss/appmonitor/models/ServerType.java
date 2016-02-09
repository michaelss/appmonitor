package org.michaelss.appmonitor.models;

public enum ServerType {

	TOMCAT ("tomcat", "org.michaelss.appmonitor.connectors.TomcatConnector"),
	WILDFLY ("wildfly", "org.michaelss.appmonitor.connectors.WildflyConnector");
	
	private String name;
	private String connectorClass;
	
	ServerType(String name, String connectorClass) {
		this.name = name;
		this.connectorClass = connectorClass;
	}

	public String getName() {
		return name;
	}

	public String getConnectorClass() {
		return connectorClass;
	}
}

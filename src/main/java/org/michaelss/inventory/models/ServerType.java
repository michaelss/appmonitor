package org.michaelss.inventory.models;

public enum ServerType {

	TOMCAT ("tomcat", "org.michaelss.inventory.connectors.TomcatConnector"),
	WILDFLY ("wildfly", "org.michaelss.inventory.connectors.WildflyConnector");
	
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

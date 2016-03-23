package org.michaelss.appmonitor.models;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SystemRole {

	@Id
	private String name;
	
	@Deprecated //apenas para os frameworks
	public SystemRole() {
	}
	
	public SystemRole(String name) {
		this.name = name;
	}
}

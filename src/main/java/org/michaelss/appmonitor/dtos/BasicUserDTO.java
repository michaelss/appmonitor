package org.michaelss.appmonitor.dtos;

public class BasicUserDTO {

	private Integer id;

	private String username;
	
	private String fullname;
	
	public BasicUserDTO(Integer id, String username, String fullname) {
		super();
		this.id = id;
		this.username = username;
		this.fullname = fullname;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
}

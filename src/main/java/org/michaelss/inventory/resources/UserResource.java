package org.michaelss.inventory.resources;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

@Produces(MediaType.APPLICATION_JSON)
@Path("/")
public class UserResource {

	@PersistenceContext
	private EntityManager manager;
	
	@GET
	@Path("session/username")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getUsername() {
		Subject subject = SecurityUtils.getSubject();
		if (subject != null && subject.getPrincipals() != null) {
			String username = (String) subject.getPrincipals().oneByType(java.util.Map.class).get("givenName");
			return Response.ok(username).build();
		}
		return Response.status(Status.FORBIDDEN).build();
	}
}

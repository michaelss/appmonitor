package org.michaelss.appmonitor.resources;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("users")
@Produces(MediaType.TEXT_PLAIN)
public class UserResource {

	@GET
	@Path("/isAuthorized/{username}")
	public Response isAuthorized(@PathParam("username") String username, @Context HttpServletRequest request) {
		
		if (request.getSession().getAttribute("username") == null) {
			return Response.status(Status.FORBIDDEN).build();
		} else {
			return Response.ok().build();
		}
	}

	@POST
	@Path("/authenticate")
	public Response authenticate(@NotNull String username, @NotNull String password,
			@Context HttpServletRequest request) {
		
		if (username.equals("michael")) {
			return Response.status(Status.UNAUTHORIZED).build();
		} else {
			request.getSession().setAttribute("username", username);
			return Response.ok().build();
		}
	}

	@GET
	@Path("/invalidate")
	public void invalidate(@Context HttpServletRequest request) {
		request.getSession().invalidate();
	}
}

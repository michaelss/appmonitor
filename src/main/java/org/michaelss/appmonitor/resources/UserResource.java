package org.michaelss.appmonitor.resources;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.michaelss.appmonitor.dtos.BasicUserDTO;
import org.michaelss.appmonitor.models.User;

@Path("users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

	@PersistenceContext
	private EntityManager manager;

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
	public Response authenticate(@NotNull @FormParam("username") String username, @NotNull @FormParam("password") String password,
			@Context HttpServletRequest request) {

		try {
			 manager.createQuery("from User u where lower(u.username) = :username and lower(u.password) = :password",
							User.class)
					.setParameter("username", username.toLowerCase()).setParameter("password", password.toLowerCase())
					.getSingleResult();
			 request.getSession().setAttribute("username", username);
			return Response.ok().build();
		} catch (NoResultException ex) {
			return Response.status(Status.UNAUTHORIZED).build();
		}
	}

	@GET
	@Path("/invalidate")
	public void invalidate(@Context HttpServletRequest request) {
		request.getSession().invalidate();
	}
	
	@GET
	public List<BasicUserDTO> list() {
		return manager.createQuery(
						"select new org.michaelss.appmonitor.dtos.BasicUserDTO(u.id, u.username, "
								+ "u.fullname) from User as u",
						BasicUserDTO.class)
				.getResultList();
	}
	
	@GET
	@Path("/{id}")
	public BasicUserDTO get(@NotNull @PathParam("id") Integer id) {
		try {
			return manager.createQuery("select new org.michaelss.appmonitor.dtos.BasicUserDTO(u.id, u.username, "
								+ "u.fullname) from User u where u.id = :id", BasicUserDTO.class)
					.setParameter("id", id).getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

	@POST
	@Transactional
	public void add(User user) {
		manager.persist(user);
	}
	
	@POST
	@Transactional
	@Path("/edit")
	public Response edit(User user) {
		if (user.getPassword() != null) {
			try {
				User old = manager.find(User.class, user.getId());
				user.setPassword(old.getPassword());
			} catch (IllegalArgumentException e) {
				return Response.status(Status.BAD_REQUEST).build();
			}
		}
		manager.merge(user);
		return Response.ok().build();
	}
}

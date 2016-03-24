package org.michaelss.inventory.resources;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
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

import org.jboss.security.Base64Encoder;
import org.michaelss.inventory.dtos.BasicUserDTO;
import org.michaelss.inventory.models.User;

@Produces(MediaType.APPLICATION_JSON)
@Path("/")
public class UserResource {

	@PersistenceContext
	private EntityManager manager;

	@GET
	@Path("session/isAuthorized/{username}")
	public Response isAuthorized(@PathParam("username") String username, @Context HttpServletRequest request) {
		
		if (request.getUserPrincipal() == null || !request.getUserPrincipal().getName().equals(username)) {
			return Response.status(Status.FORBIDDEN).build();
		} else {
			return Response.ok().build();
		}
	}

	@GET
	@Path("session/invalidate")
	public void invalidate(@Context HttpServletRequest request) {
		try {
			request.logout();
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}
	
	@GET
	@Path("session")
	public String session(@Context HttpServletRequest request) {
		return (request.getUserPrincipal() == null) ? "vazio" : request.getUserPrincipal().getName();
	}
	
	
	@GET
	@Path("users")
	public List<BasicUserDTO> list() {
		return manager.createQuery(
						"select new org.michaelss.inventory.dtos.BasicUserDTO(u.id, u.username, "
								+ "u.fullname) from User as u",
						BasicUserDTO.class)
				.getResultList();
	}
	
	@GET
	@Path("users/{id}")
	public BasicUserDTO get(@NotNull @PathParam("id") Integer id) {
		try {
			return manager.createQuery("select new org.michaelss.inventory.dtos.BasicUserDTO(u.id, u.username, "
								+ "u.fullname) from User u where u.id = :id", BasicUserDTO.class)
					.setParameter("id", id).getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

	@POST
	@Transactional
	@Path("users")
	@Produces(MediaType.TEXT_PLAIN)
	public Response add(User user) throws NoSuchAlgorithmException, IOException {
		List<User> existing = manager.createQuery("from User u where u.username = :username", User.class)
				.setParameter("username", user.getUsername()).getResultList();
		
		if (existing != null && !existing.isEmpty()) {
			return Response.status(Status.BAD_REQUEST).entity("Duplicated username.").build();
		}
		user.setPassword(getHash(user.getPassword()));
		manager.persist(user);
		return Response.ok().build();
	}
	
	@POST
	@Transactional
	@Path("users/edit")
	public Response edit(User user) throws NoSuchAlgorithmException, IOException {
		if (user.getPassword() == null || user.getPassword().isEmpty()) {
			try {
				User old = manager.find(User.class, user.getId());
				user.setPassword(old.getPassword());
			} catch (IllegalArgumentException e) {
				return Response.status(Status.BAD_REQUEST).build();
			}
		} else {
			user.setPassword(getHash(user.getPassword()));
		}
		manager.merge(user);
		return Response.ok().build();
	}

	private String getHash(String text) throws NoSuchAlgorithmException, IOException {
		byte[] hash = MessageDigest.getInstance("SHA-256").digest(text.getBytes());
		return Base64Encoder.encode(hash);
	}
	
	@POST
	@Transactional
	@Path("users/remove")
	public void remove(@NotNull Integer id) {
		User user = manager.find(User.class, id);
		if (user != null) {
			manager.remove(user);
		}
	}
}

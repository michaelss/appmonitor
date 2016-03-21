package org.michaelss.appmonitor.resources;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

@Produces(MediaType.APPLICATION_JSON)
@Path("/")
public class UserResource {

	@PersistenceContext
	private EntityManager manager;

	@GET
	@Path("session/isAuthorized/{username}")
	public Response isAuthorized(@PathParam("username") String username, @Context HttpServletRequest request) {
		
		if (request.getSession().getAttribute("username") == null) {
			return Response.status(Status.FORBIDDEN).build();
		} else {
			return Response.ok().build();
		}
	}

	@POST
	@Path("session/authenticate")
	public Response authenticate(@NotNull @FormParam("username") String username, @NotNull @FormParam("password") String password,
			@Context HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		
		String hashPassword = getMD5(password);

		try {
			 manager.createQuery("from User u where lower(u.username) = :username and lower(u.password) = :password",
							User.class)
					.setParameter("username", username).setParameter("password", hashPassword)
					.getSingleResult();
			 request.getSession().setAttribute("username", username);
			return Response.ok().build();
		} catch (Exception ex) {
			return Response.status(Status.UNAUTHORIZED).build();
		}
	}

	@GET
	@Path("session/invalidate")
	public void invalidate(@Context HttpServletRequest request) {
		request.getSession().invalidate();
	}
	
	@GET
	@Path("users")
	public List<BasicUserDTO> list() {
		return manager.createQuery(
						"select new org.michaelss.appmonitor.dtos.BasicUserDTO(u.id, u.username, "
								+ "u.fullname) from User as u",
						BasicUserDTO.class)
				.getResultList();
	}
	
	@GET
	@Path("users/{id}")
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
	@Path("users")
	@Produces(MediaType.TEXT_PLAIN)
	public Response add(User user) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		List<User> existing = manager.createQuery("from User u where u.username = :username", User.class)
				.setParameter("username", user.getUsername()).getResultList();
		
		if (existing != null && !existing.isEmpty()) {
			return Response.status(Status.BAD_REQUEST).entity("Duplicated username.").build();
		}
		user.setPassword(getMD5(user.getPassword()));
		manager.persist(user);
		return Response.ok().build();
	}
	
	@POST
	@Transactional
	@Path("users/edit")
	public Response edit(User user) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		if (user.getPassword() == null || user.getPassword().isEmpty()) {
			try {
				User old = manager.find(User.class, user.getId());
				user.setPassword(old.getPassword());
			} catch (IllegalArgumentException e) {
				return Response.status(Status.BAD_REQUEST).build();
			}
		} else {
			user.setPassword(getMD5(user.getPassword()));
		}
		manager.merge(user);
		return Response.ok().build();
	}

	private String getMD5(String text) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		byte[] textBytes = text.getBytes("UTF-8");
		byte[] digest = MessageDigest.getInstance("MD5").digest(textBytes);
		BigInteger bigInt = new BigInteger(1,digest);
		String hashText = bigInt.toString(16);
		while (hashText.length() < 32 ) {
		  hashText = "0" + hashText;
		}
		return hashText;
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

	@GET
	@Path("users/all")
	public List<User> listAll() {
		return manager.createQuery("from User u", User.class).getResultList();
	}
}

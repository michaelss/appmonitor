package org.michaelss.appmonitor.resources;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.michaelss.appmonitor.dtos.BasicServerInstanceDTO;
import org.michaelss.appmonitor.models.ServerInstance;

@Path("servers")
@Produces({ MediaType.APPLICATION_JSON })
public class ServerInstanceResource {

	@PersistenceContext
	private EntityManager manager;

	@GET
	public List<BasicServerInstanceDTO> list() {
		return manager
				.createQuery(
						"select new org.michaelss.appmonitor.dtos.BasicServerInstanceDTO(s.id, s.description, "
								+ "s.host, s.port, s.serverType) from ServerInstance as s",
						BasicServerInstanceDTO.class)
				.getResultList();
	}

	@GET
	@Path("/{id}")
	public ServerInstance get(@NotNull @PathParam("id") Integer id) {
		try {
			return manager.createQuery("from ServerInstance s where s.id = :id", ServerInstance.class)
					.setParameter("id", id).getSingleResult();
		}
		catch (NoResultException ex) {
			return null;
		}
	}

	@POST
	@Transactional
	public void post(ServerInstance server) {
		manager.persist(server);
	}

	@POST
	@Transactional
	@Path("/edit")
	public void edit(ServerInstance server) {
		manager.merge(server);
	}

	@POST
	@Transactional
	@Path("/remove")
	public void remove(@NotNull Integer id) {
		ServerInstance serverInstance = manager.find(ServerInstance.class, id);
		if (serverInstance != null) {
			manager.remove(serverInstance);
		}
	}

}

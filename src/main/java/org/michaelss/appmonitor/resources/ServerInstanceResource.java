package org.michaelss.appmonitor.resources;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
		return manager.createQuery("select new org.michaelss.appmonitor.dtos.BasicServerInstanceDTO("
				+ "s.id, "
				+ "s.description, "
				+ "s.host, "
				+ "s.port, "
				+ "s.serverType) "
				+ "from ServerInstance as s", 
				BasicServerInstanceDTO.class).getResultList();
	}
	
	@POST
	@Transactional
	public void post(ServerInstance server) {
		manager.persist(server);
	}

}

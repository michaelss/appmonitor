package org.michaelss.appmonitor.resources;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpHost;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.michaelss.appmonitor.connectors.ServerConnector;
import org.michaelss.appmonitor.dtos.AppDTO;
import org.michaelss.appmonitor.models.ServerInstance;

@Path("servers")
@Produces({ MediaType.APPLICATION_JSON })
public class AppResource {
	
	@PersistenceContext
	private EntityManager manager;
	
	
	@GET
	@Path("/{id}/apps")
	public List<AppDTO> details(@NotNull @PathParam("id") Integer id) {
		
		//TODO: Put this code in a business logic class
		ServerInstance instance = manager.find(ServerInstance.class, id);
		if (instance == null) {
			return null;
		}
		
		String connectorClass = instance.getServerType().getConnectorClass();
		try {
			ServerConnector connector = (ServerConnector) Class.forName(connectorClass).newInstance();
			HttpHost hostData = new HttpHost(instance.getHost(), instance.getPort());
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(instance.getUsername(), instance.getPassword());
			return connector.getAppList(hostData, credentials);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			// TODO Adopt a better log solution
			e.printStackTrace();
			return null;
		}
	}

}

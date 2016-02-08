package org.michaelss.appmonitor.resources;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.michaelss.appmonitor.business.JBossClient;

@Path("app")
@Produces({ MediaType.APPLICATION_JSON })
public class AppResource {
	
	@Inject
	JBossClient jbossClient;
	
	
	@GET
	@Path("/details")
	public List<String> details() {
		return jbossClient.getApps();
	}

}

package org.michaelss.inventory.connectors;

import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.michaelss.inventory.dtos.AppDTO;

public interface ServerConnector {
	
	List<AppDTO> getAppList(HttpHost hostData, UsernamePasswordCredentials credentials);

}

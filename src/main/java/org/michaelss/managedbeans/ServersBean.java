package org.michaelss.managedbeans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.inject.Model;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

@Model
public class ServersBean {
	
	public List<Map<String, String>> getNames() throws IOException {
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
        		new AuthScope("localhost", 8081),
        		new UsernamePasswordCredentials("tomcat", "tomcat"));
        CloseableHttpClient httpclient = HttpClients.custom()
        	.setDefaultCredentialsProvider(credsProvider)
        	.build();

        HttpGet httpget = new HttpGet("http://localhost:8081/manager/text/list");

        CloseableHttpResponse response = httpclient.execute(httpget);
        List<String> appRawList = getAppList(EntityUtils.toString(response.getEntity()));

        List<Map<String, String>> appsList = appRawList.stream()
        		.map(this::getAppDetailsMap)
        		.collect(Collectors.toList());
        
        return appsList;
	}
	
	private List<String> getAppList(String appsString) {
		String[] itemArray = appsString.split("\n");
		return new ArrayList<String>(Arrays.asList(itemArray))
				.stream()
				.filter(i -> i.startsWith("/"))
				.collect(Collectors.toList());
	}
	
	private Map<String, String> getAppDetailsMap(String appDetailsRaw) {
		String[] details = appDetailsRaw.split(":");
		
		Map<String, String> detailsMap = new HashMap<>();
		detailsMap.put("name", details[3]);
		detailsMap.put("running", (details[1] == "running") ? "true" : "false");
		detailsMap.put("sessions", details[2]);
		
		return detailsMap;
	}
	
}
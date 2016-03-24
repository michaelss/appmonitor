package org.michaelss.inventory.connectors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.michaelss.inventory.dtos.AppDTO;

public class TomcatConnector implements ServerConnector {

	@Override
	public List<AppDTO> getAppList(HttpHost hostData, UsernamePasswordCredentials credentials) {

		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(hostData), credentials);
		CloseableHttpClient httpclient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();

		HttpGet httpget = new HttpGet(
				String.format("http://%s:%d/manager/text/list", hostData.getHostName(), hostData.getPort()));
		
		try {
			CloseableHttpResponse response = httpclient.execute(httpget);
			List<String> appRawList = getAppList(EntityUtils.toString(response.getEntity()));
			return appRawList.stream().map(this::getAppDTO).collect(Collectors.toList());
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}	
	}
	
	private List<String> getAppList(String appsString) {
		
		String[] itemArray = appsString.split("\n");
		return new ArrayList<String>(Arrays.asList(itemArray)).stream().filter(i -> i.startsWith("/"))
				.collect(Collectors.toList());
	}

	private AppDTO getAppDTO(String appDetailsRaw) {
		
		String[] details = appDetailsRaw.split(":");
		return new AppDTO(details[3], details[1].equals("running"));
	}

}
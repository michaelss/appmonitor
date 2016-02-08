package org.michaelss.appmonitor.managedbeans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.inject.Model;
import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

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
		credsProvider.setCredentials(new AuthScope("localhost", 8081),
				new UsernamePasswordCredentials("tomcat", "tomcat"));
		CloseableHttpClient httpclient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();

		HttpGet httpget = new HttpGet("http://localhost:8081/manager/text/list");

		CloseableHttpResponse response = httpclient.execute(httpget);
		List<String> appRawList = getAppList(EntityUtils.toString(response.getEntity()));

		List<Map<String, String>> appsList = appRawList.stream().map(this::getAppDetailsMap)
				.collect(Collectors.toList());

		return appsList;
	}

	private List<String> getAppList(String appsString) {
		String[] itemArray = appsString.split("\n");
		return new ArrayList<String>(Arrays.asList(itemArray)).stream().filter(i -> i.startsWith("/"))
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

	public String teste() throws Exception {

		System.out.println("Entrou no teste........");

		String serverURL = "service:jmx:remote+http://localhost:9990";

		HashMap env = new HashMap();
		String[] creds = {"admin", "m1s2s3$%"};
		env.put(JMXConnector.CREDENTIALS, creds);

		JMXServiceURL url = new JMXServiceURL(serverURL);
		JMXConnector jmxConnector = JMXConnectorFactory.connect(url, env);
		MBeanServerConnection connection = jmxConnector.getMBeanServerConnection();

		ObjectName name = new ObjectName("jboss.as:deployment=appmonitor.war");
		System.out.println(connection.getMBeanInfo(name).getDescription());

		ObjectName depl = new ObjectName("jboss.as:deployment=*");
		Set<ObjectInstance> obs = connection.queryMBeans(depl, null);

		obs.forEach(o -> {
			System.out.println(o.getObjectName().toString());
		});

		jmxConnector.close();

		return "/jboss-jmx.html?faces-redirect=true";
	}

}
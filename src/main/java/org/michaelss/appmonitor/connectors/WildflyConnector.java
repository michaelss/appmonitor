package org.michaelss.appmonitor.connectors;

import java.io.IOException;
import java.nio.channels.UnresolvedAddressException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.http.HttpHost;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.michaelss.appmonitor.dtos.AppDTO;

public class WildflyConnector implements ServerConnector {

	@Override
	public List<AppDTO> getAppList(HttpHost hostData, UsernamePasswordCredentials credentials) {
		
        String urlString =
            System.getProperty("jmx.service.url","service:jmx:remote+http://"
            		+ hostData.getHostName() + ":" 
            		+ hostData.getPort());
        
        try {
			JMXServiceURL serviceURL = new JMXServiceURL(urlString);
			JMXConnector jmxConnector = JMXConnectorFactory.connect(serviceURL, null);
			MBeanServerConnection connection = jmxConnector.getMBeanServerConnection();
			
			ObjectName depl = new ObjectName("jboss.as:deployment=*");
			Set<ObjectInstance> obs = connection.queryMBeans(depl, null);
			
			List<AppDTO> apps = new ArrayList<>();
			for (ObjectInstance o : obs) {
				ObjectName n = o.getObjectName();
				apps.add(new AppDTO(connection.getAttribute(n, "name").toString(),
						(Boolean) connection.getAttribute(n, "enabled")));
			}
			
			jmxConnector.close();
			
			return apps;
			
		} catch (MalformedObjectNameException | AttributeNotFoundException | InstanceNotFoundException | MBeanException
				| ReflectionException | UnresolvedAddressException | IOException e) {
			System.out.println(String.format("Error in %s: %s. Message: %s", this.getClass(), e.getClass(), e.getMessage()));
			return null;
		}
	}
}

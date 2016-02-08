package org.michaelss.appmonitor.business;

import java.io.IOException;
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

public class JBossClient {

	public List<String> getApps() {
        String host = "localhost";
        int port = 9990;  // management-web port
        String urlString =
            System.getProperty("jmx.service.url","service:jmx:remote+http://" + host + ":" + port);
        try {
			JMXServiceURL serviceURL = new JMXServiceURL(urlString);
			JMXConnector jmxConnector = JMXConnectorFactory.connect(serviceURL, null);
			MBeanServerConnection connection = jmxConnector.getMBeanServerConnection();
			
			ObjectName depl = new ObjectName("jboss.as:deployment=*");
			Set<ObjectInstance> obs = connection.queryMBeans(depl, null);
			
			List<String> apps = new ArrayList<>();
			for (ObjectInstance o : obs) {
				ObjectName n = o.getObjectName();
				apps.add(String.format("%s: %s", connection.getAttribute(n, "name"),
						connection.getAttribute(n, "enabled")));
			}
			
			jmxConnector.close();
			
			return apps;
			
		} catch (MalformedObjectNameException | AttributeNotFoundException | InstanceNotFoundException | MBeanException
				| ReflectionException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}

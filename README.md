# Inventory
Inventory is a simple yet very useful tool for viewing what is deployed on known Java application servers. On environments where there is a large amount of servers and applications, it's always difficult to find where a given application is hosted. This scenario gets worse when each application has a specific name in DNS, hidding the real server address from the user.

## Supported application servers (for discovery of applications)

Currently, the supported and tested application servers are:
* Tomcat 7+
* Wildfly 9.0.2

## Supported environment (for deployment of Inventory)

Currently, Inventory is ready for deployment in:
* Application server: [Wildfly](http://wildfly.org) 9.0.2 
* Database: [MySQL](http://mysql.com) or Oracle

## Main page

Here is a preview of the main page:

![main page preview](https://raw.githubusercontent.com/michaelss/inventory/master/doc/img/inventory-main.png)

## Installation instructions

1. Configure a MySQL or Oracle datasource in Wildfly and expose it under JNDI with the name "java:/persistence/inventoryDS". Do that by placing the following block inside `<datasources>` element in standalone.xml or standalone-full.xml. Remember to change the driver and credentials accordingly.

```xml
<datasource jndi-name="java:/persistence/inventoryDS" pool-name="inventoryDS" enabled="true" use-java-context="true">
    <connection-url>jdbc:mysql://localhost:3306/inventory</connection-url>
    <driver>mysql</driver>
    <pool>
	<min-pool-size>1</min-pool-size>
	<max-pool-size>5</max-pool-size>
    </pool>
    <security>
	<user-name>username</user-name>
	<password>password</password>
    </security>
</datasource>
<driver name="mysql" module="com.mysql">
    <xa-datasource-class>com.mysql.jdbc.Driver</xa-datasource-class>
</driver>
```
2. Inventory adopts [Apache Shiro](http://shiro.apache.org/) as authentication and authorization framework, and [Stormpath](https://stormpath.com/) as credentials provider, where you can create users and passwords. So, please sign up or sign in to Stormpath and create an application to represent your Inventory instance. A step-by-step guide on this can be found [here](http://shiro.apache.org/webapp-tutorial.html#sign-up-for-stormpath). Go only until the end of section "2a". If you do not want to build Inventory, place the mentioned "apiKey.properties" in "/opt/stormpath", otherwise, change the path in "src/main/webapp/WEB-INF/shiro.ini" file.

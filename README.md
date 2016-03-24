# Inventory
Inventory is a simple yet very useful tool for viewing what is deployed on known Java application servers. On environments where there is a large amount of servers and applications, it's always difficult to find where a given application is hosted. This scenario gets worse when each application has a specific name in DNS, hidding the real server address from the user.

## Supported application servers (for discovery of applications)

Currently, the supported and tested application servers are:
* Tomcat 7+
* Wildfly 9.0.2

## Supported environment (for deployment of Inventory)

Currently, Inventory is ready for deployment in:
* Application server: [Wildfly](http://wildfly.org) 9.0.2 
* Database: [MySQL](http://mysql.com)

## Main page

Here is a preview of the main page:

![main page preview](https://raw.githubusercontent.com/michaelss/inventory/master/doc/img/inventory-main.png)

## Installation instructions

1. Configure Wildfly with standalone-full.xml.
2. Configure a MySQL datasource in Wildfly and expose it under JNDI with the name "java:jboss/datasources/inventoryDS".
3. Place the following snippet in standalone-full.xml:

```xml
<security-domain name="database-login" cache-type="default">
    <authentication>
        <login-module code="Database" flag="required">
            <module-option name="dsJndiName" value="java:jboss/datasources/inventoryDS"/>
            <module-option name="principalsQuery" value="select password from User where username=?"/>
            <module-option name="rolesQuery" value="select 'ADMIN', 'Roles' from DUAL where 1 = ? or 1 = 1;"/>
            <module-option name="hashAlgorithm" value="SHA-256"/>
            <module-option name="hashEncoding" value="base64"/>
        </login-module>
    </authentication>
</security-domain>
```

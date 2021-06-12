# My Project

This is an service monitoring  project that can be used to check the status of services on Ubuntu.

The project is a standard Maven project, so you can import it to your IDE of choice. 

## Running and debugging the service manager application
### Running Keycloak
Download the keycloak from https://www.keycloak.org/downloads site 
From a terminal open the directory keycloak-12.x, then to start Keycloak run the following command.
On Linux run:
bin/standalone.sh -b  serverIp(192.168.100.177)
On Windows run:
bin/standalone.bat serverIp

Go to the http://localhost:8080/auth (Keycloak Admin Console) and login with the username:admin and password:admin.

### Running the Node.js  
Download the Node.js and run it.
 
 ### Running the application in production mode
mvn clean package -Pproduction 

### Running the jar file of the application from the command line.
java -jar serviceManager-1.0-SNAPSHOT.jar

### Running and debugging the application in Intellij IDEA
- Locate the Application.java class in the Project view. It is in the src folder, under the main package's root.
- Right click on the Application class
- Select "Debug 'Application.main()'" from the list

After the application has started, you can view your it at http://localhost(serverIp):3333/serviceManager in your browser. 
You can now also attach break points in code for debugging purposes, by clicking next to a line number in any source file.

### Running and debugging the application in Eclipse
- Locate the Application.java class in the Package explorer. It is in `src/main/java`, under the main package.
- Right click on the file and select `Debug As` --> `Java Application`.

Do not worry if the debugger breaks at a `SilentExitException`. This is a Spring Boot feature and happens on every startup.

After the application has started, you can view your it at http://localhost(serverIp):3333/serviceManager in your browser.
You can now also attach break points in code for debugging purposes, by clicking next to a line number in any source file.
## Project structure

- `MainView.java` in `src/main/java` contains the navigation setup. It uses [App Layout](https://vaadin.com/components/vaadin-app-layout).
- `views` package in `src/main/java` contains the server-side Java views of your application.
- `views` folder in `frontend/src/` contains the client-side JavaScript views of your application.



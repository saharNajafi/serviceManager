package org.tiddev.serviceManager.service;

import com.jcraft.jsch.*;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.IDToken;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import org.tiddev.serviceManager.exception.BusinessExceptionCode;
import org.tiddev.serviceManager.exception.ServiceManagerException;
import org.tiddev.serviceManager.model.ServiceInfo;
import org.tiddev.serviceManager.model.User;
import org.tiddev.serviceManager.util.OSUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import java.io.*;
import java.security.Principal;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

@Service
public class ServerManagerServiceImpl implements ServerManagerService {

	private static final Logger logger = LogManager.getLogger("ServerManagerLogger");

	public User getUserAttributes(HttpServletRequest request) {
		
		final Principal userPrincipal = request.getUserPrincipal();
		String ip = "";
		String userName = "";
		String password = "";
		User user = new User();
		if (userPrincipal instanceof KeycloakPrincipal) {
			KeycloakPrincipal kPrincipal = (KeycloakPrincipal) userPrincipal;
			IDToken token = kPrincipal.getKeycloakSecurityContext().getToken();
			Map<String, Object> customClaims = token.getOtherClaims();
			if (customClaims.containsKey("IP")) {
				ip = String.valueOf(customClaims.get("IP"));
				user.setAuthenticationServerIP(ip);
			}
			if (customClaims.containsKey("UserName")) {
				userName = String.valueOf(customClaims.get("UserName"));
				user.setUsername(userName);
			}
			if (customClaims.containsKey("Password")) {
				password = String.valueOf(customClaims.get("Password"));
				user.setPassword(password);
			}
		}
		logger.info("UserInfo: " + user.getUsername());
		return user;

	}


	public List<ServiceInfo> readFile(User user) {
		String fileName = null;
		if (OSUtils.isWindows()) {
			fileName = "C:\\temp\\services.csv";
		} else {
			fileName = "/home/user/tmp/services.csv";
		}
		List<ServiceInfo> services = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				if (!values[0].equals("name") && !values[1].equals("port") && !values[2].equals("ip")) {
					ServiceInfo serviceInfo = new ServiceInfo();
					serviceInfo.setServiceName(values[0]);
					serviceInfo.setPort(values[1]);
					serviceInfo.setIP(values[2]);
					serviceInfo.setUser(user);
					services.add(serviceInfo);
				}
			}
		} catch (Exception e) {
			logger.error("exception occurred in reading services from file: " + e);
			throw new ServiceManagerException(BusinessExceptionCode.SMS_001, BusinessExceptionCode.SMS_001_MSG, e);
		}
		return services;
	}

	public List<ServiceInfo> getServices(User user) {
		return readFile(user);
	}

	public ServiceInfo getServiceInfo(ServiceInfo serviceInfo) {
		String status = getServiceStatus(serviceInfo);
		serviceInfo.setServiceStatus(status.equals("active") ? "active" : "inactive");
		return serviceInfo;
	}

	public String getServiceStatus(ServiceInfo serviceInfo) {
		String status = "inactive";
		String serviceName = serviceInfo.getServiceName().contains(".service") ? serviceInfo.getServiceName()
				: serviceInfo.getServiceName().concat(".service");
		String command = "systemctl status" + " " + serviceName;
		String output = runCommand(serviceInfo.getUser(), command, serviceInfo.getIP());
		String[] outputs = output.split("\\n");
		for (int i = 0; i < outputs.length; i++) {
			if (outputs[i].contains("Active")) {
				status = outputs[i].substring(outputs[i].indexOf(":"), outputs[i].indexOf("("));
				status = status.contains(":") ? status.replace(":", "") : status;
			}
		}
		logger.info("service status: " + status);
		return status.trim();
	}

	public String getServiceSyslog(ServiceInfo serviceInfo) {
		String command = "sudo -S journalctl  -u" + " " + serviceInfo.getServiceName() + " " + "| tail -100";
		return runCommand(serviceInfo.getUser(), command, serviceInfo.getIP());
	}

	public String getTcpStatus(ServiceInfo serviceInfo) {
		String command = "netstat -a | grep" + " " + serviceInfo.getPort();
		return runCommand(serviceInfo.getUser(), command, serviceInfo.getIP());
	}

	public ServiceInfo changeServiceActive(ServiceInfo serviceInfo) {
		String serviceName = serviceInfo.getServiceName().contains(".service") ? serviceInfo.getServiceName()
				: serviceInfo.getServiceName().concat(".service");
		String command = "sudo -S systemctl" + " "
				+ (serviceInfo.getServiceStatus().equals("active") ? "stop" : "start") + " " + serviceName;
		runCommand(serviceInfo.getUser(), command, serviceInfo.getIP());
		return getServiceInfo(serviceInfo);
	}

	private String runCommand(User user, String command, String ip) {
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		int port = 22;
		Session session = null;
		Channel channel = null;
		String responseString = null;
		JSch jsch = new JSch();
		try {
			session = jsch.getSession(user.getUsername(), ip, port);
			session.setPassword(user.getPassword());
			session.setConfig(config);
			session.connect();
			channel = session.openChannel("exec");
			if (command.contains("sudo")) {
				((ChannelExec) channel).setCommand("echo" + " " + user.getPassword() + " |" + " " + command);
			} else {
				((ChannelExec) channel).setCommand(command);
			}
			ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
			channel.setOutputStream(responseStream);
			channel.connect();
			while (channel.isConnected()) {
				Thread.sleep(100);
			}
			responseString = new String(responseStream.toByteArray());
		} catch (InterruptedException e) {
			logger.error("an exception occurred in running command :  " + e);
			throw new ServiceManagerException(BusinessExceptionCode.SMS_002, BusinessExceptionCode.SMS_002_MSG, e);
		} catch (JSchException e) {
			logger.error("an exception occurred in running command :  " + e);
			throw new ServiceManagerException(BusinessExceptionCode.SMS_003, BusinessExceptionCode.SMS_002_MSG, e);
		} finally {
			if (session != null) {
				session.disconnect();
			}
			if (channel != null) {
				channel.disconnect();
			}
		}
		return responseString;
	}
}

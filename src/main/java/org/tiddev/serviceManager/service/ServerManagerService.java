package org.tiddev.serviceManager.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.tiddev.serviceManager.model.ServiceInfo;
import org.tiddev.serviceManager.model.User;

public interface ServerManagerService {

	public User getUserAttributes(HttpServletRequest request);

	public List<ServiceInfo> readFile(User user);

	public List<ServiceInfo> getServices(User user);

	public ServiceInfo getServiceInfo(ServiceInfo serviceInfo);

	public String getServiceStatus(ServiceInfo serviceInfo);

	public String getServiceSyslog(ServiceInfo serviceInfo);

	public String getTcpStatus(ServiceInfo serviceInfo);

	public ServiceInfo changeServiceActive(ServiceInfo serviceInfo);

}

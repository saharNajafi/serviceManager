package org.tiddev.serviceManager.model;

public class ServiceInfo {

    private String serviceName;
    private String port;
    private String IP;
    private String serviceStatus;
    private String TCPStatus;
    private String syslog ;
    private User user;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public String getTCPStatus() {
        return TCPStatus;
    }

    public void setTCPStatus(String TCPStatus) {
        this.TCPStatus = TCPStatus;
    }

    public String getSyslog() {
        return syslog;
    }

    public void setSyslog(String syslog) {
        this.syslog = syslog;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
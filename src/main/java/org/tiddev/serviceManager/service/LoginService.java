/*package org.tiddev.serviceManager.service;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.springframework.stereotype.Service;
import org.tiddev.serviceManager.exception.BusinessExceptionCode;
import org.tiddev.serviceManager.exception.ServiceManagerException;
import org.tiddev.serviceManager.util.OSUtils;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Properties;

@Service
public class LoginService {

    public String readFile() {
        String userIP = null;
        String fileName = null;
        if(OSUtils.isWindows()){
            fileName = "C:\\temp\\services.csv";
        }else {
            fileName = "C://temp//services.csv";
        }
        try (BufferedReader br =
                     new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (!values[0].equals("name") && !values[1].equals("port")
                        && !values[2].equals("ip") && !values[3].equals("userName")
                        && !values[4].equals("password")
                        && !values[5].equals("authenticationServer")) {
                    if (values[5] != null) {
                        userIP = values[5];
                        break;
                    }
                }
            }
        } catch (Exception e) {
            throw new ServiceManagerException(BusinessExceptionCode.LGS_001, BusinessExceptionCode.LGS_001_MSG, e);
        }
        return userIP;
    }

    public void authenticate(String user, String password, String ip) throws JSchException {
        int port = 22;
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        Session session = null;
        JSch jsch = new JSch();
        try {
            session = jsch.getSession(user, ip, port);
            session.setPassword(password);
            session.setConfig(config);
            session.connect();
        } catch (JSchException e) {
//        	   logger.error("exception occurred in uploadFile: " + e);
               throw new ServiceManagerException(BusinessExceptionCode.LGS_002, BusinessExceptionCode.LGS_002_MSG, e);
        } finally {
            if (session != null) {
                session.disconnect();
            }
        }
    }
}
*/
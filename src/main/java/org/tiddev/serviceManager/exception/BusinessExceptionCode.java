package org.tiddev.serviceManager.exception;

/**
 * Collection of constant values representing business layer error codes and
 * their corresponding messages (to log in console)
 *
 * @author
 * @author
 */
public class BusinessExceptionCode {

//	org.tiddev.serviceManager.service.LoginService
    public static final String LGS_001 = "TID_S_LGS_001";
    public static final String LGS_002 = "TID_S_LGS_002";
    public static final String LGS_001_MSG = "Unhandled exception occur in readFile operation";
    public static final String LGS_002_MSG = "Unhandled exception occur in authenticate operation";
    
//	org.tiddev.serviceManager.service.ServerManagerService
    public static final String SMS_001 = "TID_S_SMS_001";
    public static final String SMS_002 = "TID_S_SMS_002";
    public static final String SMS_003 = "TID_S_SMS_003";

    public static final String SMS_001_MSG = "Unhandled exception occur in readFile operation";
    public static final String SMS_002_MSG = "Unhandled exception occur in runCommand operation";

 
}

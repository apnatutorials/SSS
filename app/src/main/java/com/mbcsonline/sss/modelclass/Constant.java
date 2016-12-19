package com.mbcsonline.sss.modelclass;

public class Constant
{
    public static final int ROUTE_TYPE_HOME_TO_SCHOOL = 1;
    public static final int ROUTE_TYPE_SCHOOL_TO_HOME = 2 ;

    public static final int TYPE_BUS_IN = 1 ;
    public static final int TYPE_BUS_OUT = 2;


    // public static Account selectedAccountObject = null ;
    public static ServerResponse loginResponse = null ;
//    public static final String BASE_URL = "https://www.forestuk.in/sss/sss/apilogin/" ;
    public static final String BASE_URL = "http://acld.in/sts/apilogin/" ;
    public static String LOGIN_URL = BASE_URL + "login" ;
    public static String RECOVER_PASSWORD_URL = BASE_URL + "regeneratePassword" ;
    public static String REGENERATE_VERIFICATION_CODE_URL = BASE_URL + "regenerateVerificationCode" ;

    public static String GPS_URL = BASE_URL + "getGpsData" ;

    public static String LOGIN = "LOGIN" ;

    public static String VERIFICATION = "VERIFICATION" ;

    public static final String ACTION_LOGIN_OK = "loginok" ;
    public static final String ACTION_REGISTER_OK = "registerok" ;
    public static final String ACTION_CODE_VALIDATOR_OK = "codevalidatorok" ;
    public static final String ACTION_RECOVER_PASSWORD_OK = "recoverpasswordok" ;
    public static final String ACTION_RGENERATE_VERIFICATION_CODE_OK = "regenerateVerificationCode" ;
    public static final String ACTION_GET_GPS = "getGpsData" ;


    public static String REGISTER_PARENT_URL = BASE_URL + "registerParent" ;
    public static String CODE_VALIDATOR_URL = BASE_URL + "matchVerificationCode" ;

    public static String NETWORK_NOT_REACHABLE_JSON="{\"command\":\"foranyaction\",\"success\":false,\"message\":\"U2VydmVyIG5vdCByZWFjaGFibGUuIFBsZWFzZSBjaGVjayB5b3VyIG5ldHdvcmsgY29ubmVjdGlvbiBhbmQgdHJ5IGFmdGVyIHNvbWUgdGltZS4=\"}" ;

    public static String SERVER_RESPONDED_WITH_BLANK_RESPONSE_JSON="{\"command\":\"someresponse\",\"success\":false,\"message\":\"U2V2ZXIgcmVzcG9uZGVkIHdpdGggYmxhbmsgcmVzcG9uc2UuIFBsZWFzZSB0cnkgYWZ0ZXIgc29tZXRpbWUu\"}";

    public static String SERVER_OPERATION_CANCELED="{\"command\":\"someresponse\",\"success\":false,\"message\":\"T3BlcmF0aW9uIGNhbmNlbGVk\"}";

    String s = "{\"account\":{\"address\":\"\",\"email\":\"YnNpbmdoQGdtYWlsLmNvbQ\\u003d\\u003d\",\"id\":-1,\"name\":\"\",\"password\":\"b2d5eXloeWc\\u003d\",\"phone\":\"\",\"status\":0,\"type\":3},\"command\":\"loginok\",\"message\":\"\"}" ;

}

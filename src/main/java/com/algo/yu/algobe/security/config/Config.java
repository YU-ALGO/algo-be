package com.algo.yu.algobe.security.config;

public class Config {
    public static int SERVER_PORT = 8088;
    public static int WEB_PORT = 3000;
    public static int PY_PORT = 8000;

    public static String SERVER_DOMAIN = "be2.algo.r-e.kr";
    public static String DOMAIN = "algo.r-e.kr";
    public static String WEB_BASE_URL = "http://web."+DOMAIN+":"+WEB_PORT;
    //public static String WEB_BASE_URL = "http://be."+DOMAIN+":"+WEB_PORT;
    public static String SERVER_BASE_URL = "http://"+SERVER_DOMAIN+":"+SERVER_PORT;

    public static String PY_SERVER_DOMAIN = "be.algo.r-e.kr";
    public static String PY_BASE_URL = "http://"+PY_SERVER_DOMAIN+":"+PY_PORT;
    public static String PY_METHOD = "/recommend";
    public static String UPLOAD_FILE_PATH = "D://";
}

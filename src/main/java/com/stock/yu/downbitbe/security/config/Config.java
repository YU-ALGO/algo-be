package com.stock.yu.downbitbe.security.config;

public class Config {
    public static int SERVER_PORT = 8088;
    public static int WEB_PORT = 3000;

    public static String SERVER_DOMAIN = "be.algo.r-e.kr";
    public static String DOMAIN = "algo.r-e.kr";
    public static String WEB_BASE_URL = "http://web."+DOMAIN+":"+WEB_PORT;
    public static String SERVER_BASE_URL = "http://"+SERVER_DOMAIN+":"+SERVER_PORT;

    public static String UPLOAD_FILE_PATH = "D://";
}

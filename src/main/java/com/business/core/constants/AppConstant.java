package com.business.core.constants;

import java.text.SimpleDateFormat;

public class AppConstant {

    public interface Security {
        public String APP_NAME = "Business Listing API";
        public String SECRET_KEY = "T1Boem9KOUFUa0p4OVhiWE9qTU91WmRzMjVtYURwMjZNdi1MZm1ZVzVBalBrNElIOGdOMFVOdGlEV01UY3dxMTVUeW5LcUNxck5qcU1DOGVZQ0lLZHc9PQ==";
        public Long EXPIRE_IN = 36000000L;
        public String AUTHORIZATION = "Authorization";
        public String BEARER = "Bearer ";
        public String DEFUALT_KEY = "6YXvfUFfkZLqosDiYoEOwMYiG6M4Eb2sVdXozXW8tZZ90ZVA4B2BYzhf799hLmkdz4Q7Y2kbJSFLUfk9vYAyYXvMtDK4oDO4yhRh9xbAMAMrmnOE7HepUQ==";
        public byte[] SALT = { (byte) 0x21, (byte) 0x21, (byte) 0xF0, (byte) 0x55, (byte) 0xC3, (byte) 0x9F, (byte) 0x5A, (byte) 0x75};
        public int ITERATION_COUNT = 31;
    }

    public interface DateFormatter {
        public SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        public String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    }

    public interface Utf8CharSet {
        public String UTF_8 = "UTF-8";
    }
}

package com.appdeveloperblog.app.ws.mobileappws.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AppProperties { //untuk bisa read data dari application.properties dan check SecurityConstant

    @Autowired
    private Environment env;


    public String getTokenSecret(){
        return env.getProperty("tokenSecret");
    }

}

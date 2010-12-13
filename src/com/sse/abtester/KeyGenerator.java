package com.sse.abtester;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import lombok.ToString;

public @ToString class KeyGenerator implements IKeyGenerator {

    @Override
    public String getKey(HttpServletRequest request) {
        // TODO implement
        return "default_cookie_tracker_"+(new Date()).toString();
    }

}

package com.sse.abtester;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import lombok.ToString;

// TODO: Auto-generated Javadoc
/**
 * The Class KeyGenerator.
 */
public /* (non-Javadoc)
  * @see java.lang.Object#toString()
  */
 @ToString class KeyGenerator implements IKeyGenerator {

    /* (non-Javadoc)
     * @see com.sse.abtester.IKeyGenerator#getKey(HttpServletRequest)
     */
    @Override
    public String getKey(HttpServletRequest request) {
        // TODO implement
        return "default_cookie_tracker_"+(new Date()).toString();
    }

}

package com.sse.abtester;

import javax.servlet.http.HttpServletRequest;

/**
 * Allows for type-specific instance key generation.
 * Default is hashCode();
 */
public interface IKeyGenerator {

    /**
     * Gets the key.
     *
     * @param request the request
     * @return the key
     */
    String getKey(HttpServletRequest request);
}

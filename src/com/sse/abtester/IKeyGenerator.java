package com.sse.abtester;


import javax.servlet.http.HttpServletRequest;

public interface IKeyGenerator {
	String getKey(HttpServletRequest request);
}

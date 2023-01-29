package org.jamali.announcement.utils;

import java.util.Map;

public interface Client {
	String getCookieFolderName();
	Response doPost(String url, String referer, Object data, Map<String, String> headers, String ipAddress)
			throws Exception;
	Response doPost(String url, String referer, Object data, Map<String, String> headers)
			throws Exception;

	Response doGet(String url, String referer, Map<String, String> headers, String ipAddress) throws Exception;
	Response doGet(String url, String referer, Map<String, String> headers) throws Exception;

}

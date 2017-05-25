package cn.ljj.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletUtil {
	private static final String TAG = ServletUtil.class.getSimpleName();

	public static Cookie getCookieByName(HttpServletRequest request, String name) {
		if (request == null) {
			return null;
		}
		Cookie[] cookies = request.getCookies();
		if (cookies == null || cookies.length <= 0) {
			return null;
		}
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(name)) {
				return cookie;
			}
		}
		return null;
	}

	public static String getCookieValueByName(HttpServletRequest request, String name) {
		Cookie cookie = getCookieByName(request, name);
		if (cookie != null) {
			return cookie.getValue();
		}
		return null;
	}

	public static void dumpCookies(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null || cookies.length <= 0) {
			Logger.d(TAG, "dumpCookies: empty!");
			return;
		}
		for (int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			Logger.d(TAG,
					"dumpCookies [" + i + "] name:" + cookie.getName() + "; value:" + cookie.getValue() + "; domain:"
							+ cookie.getDomain() + "; path:" + cookie.getPath() + "; maxAge:" + cookie.getMaxAge()
							+ "; version:" + cookie.getVersion() + "; comment:" + cookie.getComment() + "; secure:"
							+ cookie.getSecure());
		}
	}

	public static void cleanCookie(HttpServletRequest request, HttpServletResponse response, String name) {
		Cookie cookie = getCookieByName(request, name);
		if (cookie != null) {
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}
	}

	public static Map<String, String> getHeaders(HttpServletRequest request) {
		Enumeration<String> allNames = request.getHeaderNames();
		HashMap<String, String> headers = new HashMap<String, String>();
		while (allNames.hasMoreElements()) {
			String name = allNames.nextElement();
			headers.put(name, request.getHeader(name));
		}
		return headers;
	}

	public static void dumpRequest(HttpServletRequest request) {
		dumpCookies(request);
		Logger.d(TAG, "dumpHeaders:" + getHeaders(request));
	}
}

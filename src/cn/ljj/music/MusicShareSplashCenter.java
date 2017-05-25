package cn.ljj.music;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.ljj.util.Logger;
import cn.ljj.util.ServletUtil;
import cn.ljj.util.UrlStringUtil;

/**
 * Servlet implementation class PrivacySplashCenter
 */
@WebServlet("/MusicShareSplashCenter")
public class MusicShareSplashCenter extends HttpServlet implements StaticDefines {
	private static final String TAG = MusicShareSplashCenter.class.getSimpleName();
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MusicShareSplashCenter() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletUtil.dumpRequest(request);
		tryMarkDownUser(request, response);
		String privacyTs = "" + System.currentTimeMillis();
		Cookie privacyCookie = new Cookie(KEY_COOKIE_PRIVACY_TIMESTAMP, privacyTs);
		privacyCookie.setMaxAge(30 * 60);
		privacyCookie.setDomain(DOMAIN);
		privacyCookie.setPath("/");
		response.addCookie(privacyCookie);
		Map<String, String> parameters = UrlStringUtil.parseQueryString(request.getQueryString());
		parameters.put(KEY_PARAM_PRIVACY_TIMESTAMP, privacyTs);
		String url = UrlStringUtil.buildUrl(SCHEME, DOMAIN, URL_PATH_PRIVATE, parameters);
		Logger.d(TAG, "sendRedirect:" + url);
		response.sendRedirect(url);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	private void tryMarkDownUser(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> parameters = UrlStringUtil.parseQueryString(request.getQueryString());
		String userId = parameters.get(KEY_PARAM_USER_ID);
		if (userId != null) {
			Cookie cookie = new Cookie(KEY_COOKIE_USER_ID, userId);
			cookie.setMaxAge(Integer.MAX_VALUE);
			cookie.setDomain(DOMAIN);
			cookie.setPath("/");
			response.addCookie(cookie);
			Logger.w(TAG, "tryMarkDownUser userId=" + userId);
		}
		String groupId = parameters.get(KEY_PARAM_GROUP_ID);
		if (groupId != null) {
			Cookie cookie = new Cookie(KEY_COOKIE_GROUP_ID, groupId);
			cookie.setMaxAge(Integer.MAX_VALUE);
			cookie.setDomain(DOMAIN);
			cookie.setPath("/");
			response.addCookie(cookie);
			Logger.w(TAG, "tryMarkDownUser groupId=" + groupId);
		}
	}
}

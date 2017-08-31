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

    @Override
    public void init() throws ServletException {
        super.init();
        String prefix = getServletContext().getRealPath("/");
        String log4jConfigFile = getServletContext().getInitParameter("log4j-init-file");
        if (log4jConfigFile != null) {
            Logger.init(prefix + log4jConfigFile);
        }
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletUtil.dumpRequest(request);
        tryMarkDownUser(request, response);
        String stopSplash = ServletUtil.getCookieValueByName(request, KEY_COOKIE_STOP_SPLASH);
        if ("1".equals(stopSplash)) { // stop splash
            Map<String, String> parameters = UrlStringUtil.parseQueryString(request.getQueryString());
            String title = parameters.get(KEY_PARAM_MUSIC);
            response.getWriter().write("<html><head><title>" + title
                    + "</title><meta charset=\"utf-8\"><head/><body></body></html>");
            return;
        }
        String privacyTs = "" + System.currentTimeMillis();
        Cookie privacyCookie = new Cookie(KEY_COOKIE_PRIVACY_TIMESTAMP, privacyTs);
        privacyCookie.setMaxAge(30 * 60);
        privacyCookie.setPath("/");
        response.addCookie(privacyCookie);
        StringBuffer sb = request.getRequestURL();
        sb.append("?").append(request.getQueryString()).append("&").append(KEY_PARAM_PRIVACY_TIMESTAMP).append("=")
                .append(privacyTs);
        String url = sb.toString().replace(URL_PATH_SPLASH, URL_PATH_PRIVATE);
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
            cookie.setPath("/");
            response.addCookie(cookie);
            Logger.w(TAG, "tryMarkDownUser userId=" + userId);
        }
        String groupId = parameters.get(KEY_PARAM_GROUP_ID);
        if (groupId != null) {
            Cookie cookie = new Cookie(KEY_COOKIE_GROUP_ID, groupId);
            cookie.setMaxAge(Integer.MAX_VALUE);
            cookie.setPath("/");
            response.addCookie(cookie);
            Logger.w(TAG, "tryMarkDownUser groupId=" + groupId);
        }
        String stopSplash = parameters.get(KEY_PARAM_STOP_SPLASH);
        if (stopSplash != null) {
            Cookie cookie = new Cookie(KEY_COOKIE_STOP_SPLASH, stopSplash);
            cookie.setMaxAge(Integer.MAX_VALUE);
            cookie.setPath("/");
            response.addCookie(cookie);
            Logger.w(TAG, "tryMarkDownUser stopSplash=" + stopSplash);
        }
    }
}

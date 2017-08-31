package cn.ljj.music;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.ljj.util.Logger;
import cn.ljj.util.ServletUtil;
import cn.ljj.util.UrlStringUtil;

/**
 * Servlet implementation class MusicServer
 */
@WebServlet("/MusicServer")
public class MusicServer extends LocalFilesBaseServlet implements StaticDefines {
    private static final String TAG = MusicServer.class.getSimpleName();
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public MusicServer() {

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

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Logger.d(TAG, "doGet");
        // check parameters to avoid share link
        Cookie privateCookie = ServletUtil.getCookieByName(request, KEY_COOKIE_PRIVACY_TIMESTAMP);
        Map<String, String> queryParameters = UrlStringUtil.parseQueryString(request.getQueryString());
        String queryTs = queryParameters.get(KEY_PARAM_PRIVACY_TIMESTAMP);
        if (privateCookie == null || queryTs == null || !queryTs.equals(privateCookie.getValue())) {
            StringBuffer sb = request.getRequestURL();
            sb.append("?").append(KEY_PARAM_ERROR_CODE).append("=").append(ERROE_CODE_CAN_NOT_SHARE);
            String url = sb.toString().replace(URL_PATH_PRIVATE, URL_PATH_ERROR);
            Logger.e(TAG, "privateCookie:" + privateCookie + ", queryTs=" + queryTs);
            if (privateCookie != null) {
                Logger.e(TAG, "privateCookie value:" + privateCookie.getValue());
            }
            response.sendRedirect(url);
            return;
        }
        // user, group id from cookie
        if (queryParameters.get(KEY_PARAM_USER_ID) == null) {
            String userId = ServletUtil.getCookieValueByName(request, KEY_COOKIE_USER_ID);
            if (userId != null) {
                queryParameters.put(KEY_PARAM_USER_ID, userId);
            }
        }
        if (queryParameters.get(KEY_PARAM_GROUP_ID) == null) {
            String groupId = ServletUtil.getCookieValueByName(request, KEY_COOKIE_GROUP_ID);
            if (groupId != null) {
                queryParameters.put(KEY_PARAM_GROUP_ID, groupId);
            }
        }
        // rebuild the query string from parameters
        String queryString = UrlStringUtil.urlStringByAppendingParameters("", queryParameters);
        String filePath = getLocalFilePath(request.getRequestURI(), queryString);
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            respondFileContent(filePath, response);
        } else {
            StringBuffer sb = request.getRequestURL();
            sb.append("?").append(KEY_PARAM_ERROR_CODE).append("=").append(ERROE_CODE_MUSIC_NOT_FOUND);
            String url = sb.toString().replace(URL_PATH_PRIVATE, URL_PATH_ERROR);
            response.sendRedirect(url);
        }
    }

    @Override
    protected String getLocalFilePath(String uri, String queryString) {
        Logger.d(TAG, "getLocalFilePath uri=" + uri + ", queryString=" + queryString);
        String baseUrlPath = getBaseUrlPath();
        int index = uri.indexOf(baseUrlPath);
        if (index == -1) {
            return null;
        }
        String relativePath = uri.substring(index + baseUrlPath.length());
        String absolutePath = null;
        if (relativePath.length() > 0) {
            if (!relativePath.startsWith("/")) {
                relativePath = "/" + relativePath;
            }
            if (!relativePath.endsWith("/")) {
                relativePath = relativePath + "/";
            }
            absolutePath = getBaseLocalPath() + relativePath.replace("/", File.separator);
        } else {
            absolutePath = getBaseLocalPath() + File.separator;
        }
        Map<String, String> parameters = UrlStringUtil.parseQueryString(queryString);
        String music = parameters.get(KEY_PARAM_MUSIC);
        if (music != null) {
            // user specific file
            String userId = parameters.get(KEY_PARAM_USER_ID);
            if (userId != null) {
                String tempPath = absolutePath + music + "_u-" + userId + ".html";
                if (new File(tempPath).exists()) {
                    Logger.d(TAG, "getLocalFilePath user tempPath=" + tempPath);
                    return tempPath;
                }
            }
            // group specific file
            String groupId = parameters.get(KEY_PARAM_GROUP_ID);
            if (groupId != null) {
                String tempPath = absolutePath + music + "_g-" + groupId + ".html";
                if (new File(tempPath).exists()) {
                    Logger.d(TAG, "getLocalFilePath group tempPath=" + tempPath);
                    return tempPath;
                }
            }
            absolutePath = absolutePath + music + ".html";
        }
        Logger.d(TAG, "getLocalFilePath final absolutePath=" + absolutePath);
        return absolutePath;
    }

    @Override
    protected String getBaseLocalPath() {
        return MUSIC_ROOT;
    }

    @Override
    protected String getBaseUrlPath() {
        return URL_PATH_PRIVATE;
    }
}

package cn.ljj.music;

import java.io.File;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import cn.ljj.util.Logger;
import cn.ljj.util.UrlStringUtil;

/**
 * Servlet implementation class MusicErrorPage
 */
@WebServlet("/MusicErrorPage")
public class MusicErrorPage extends LocalFilesBaseServlet implements StaticDefines {
    private static final String TAG = MusicErrorPage.class.getSimpleName();
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public MusicErrorPage() {
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

    @Override
    protected String getLocalFilePath(String uri, String queryString) {
        Logger.d(TAG, "getLocalFilePath uri=" + uri + ", queryString=" + queryString);
        String baseUrlPath = getBaseUrlPath();
        int index = uri.indexOf(baseUrlPath);
        if (index != -1) {
            String relativePath = uri.substring(index + baseUrlPath.length());
            relativePath = relativePath.replace("/", File.separator);
            Map<String, String> parameters = UrlStringUtil.parseQueryString(queryString);
            relativePath = relativePath + File.separator + parameters.get(KEY_PARAM_ERROR_CODE) + ".html";
            return getBaseLocalPath() + relativePath;
        } else {
            return null;
        }
    }

    @Override
    protected String getBaseLocalPath() {
        return MUSIC_ERROR_PAGES;
    }

    @Override
    protected String getBaseUrlPath() {
        return URL_PATH_ERROR;
    }

}

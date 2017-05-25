package cn.ljj.music;

import java.io.File;
import java.util.Map;

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
	protected String getLocalFilePath(String uri, String queryString) {
		Logger.d(TAG, "getLocalFilePath uri=" + uri + ", queryString=" + queryString);
		String baseUrlPath = getBaseUrlPath();
		if (uri.length() > baseUrlPath.length()) {
			String relativePath = uri.substring(baseUrlPath.length() + 1);
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

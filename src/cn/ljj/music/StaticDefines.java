package cn.ljj.music;

public interface StaticDefines {
	// Url path
	public static final String URL_PATH_SPLASH = "splash";
	public static final String URL_PATH_PRIVATE = "private";
	public static final String URL_PATH_ERROR = "error";
	// Cookie keys
	public static final String KEY_COOKIE_PRIVACY_TIMESTAMP = "privacy_splash_ts";
	public static final String KEY_COOKIE_USER_ID = "user_id";
	public static final String KEY_COOKIE_GROUP_ID = "group_id";
	public static final String KEY_COOKIE_STOP_SPLASH = "stop_splash";
	// Query String keys
	public static final String KEY_PARAM_PRIVACY_TIMESTAMP = "p_s_ts";
	public static final String KEY_PARAM_ERROR_CODE = "erroe_code";
	public static final String KEY_PARAM_MUSIC = "music";
	public static final String KEY_PARAM_USER_ID = "user_id";
	public static final String KEY_PARAM_GROUP_ID = "group_id";
	public static final String KEY_PARAM_STOP_SPLASH = "stop_splash";
	// Local path
	public static final String MUSIC_ROOT = System.getProperty("user.home") + "/Servers/Music";
	public static final String MUSIC_ERROR_PAGES = MUSIC_ROOT + "/error";
	// Error code
	public static final int ERROE_CODE_CAN_NOT_SHARE = 401;
	public static final int ERROE_CODE_MUSIC_NOT_FOUND = 404;
}

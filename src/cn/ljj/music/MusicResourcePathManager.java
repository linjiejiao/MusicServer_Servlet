package cn.ljj.music;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import cn.ljj.util.Logger;

public class MusicResourcePathManager implements StaticDefines {
	private static final String TAG = MusicResourcePathManager.class.getSimpleName();
	private static final String URI_PREFIX = "/MusicServer/private";
	public String musicServerRoot = null;
	
	public MusicResourcePathManager(){
		musicServerRoot = MUSIC_ROOT;
	}

	public String getResourcePath(String uri, String queryString){
		Logger.d(TAG, "uri=" + uri + ", queryString=" + queryString);
		try {
			uri = URLDecoder.decode(uri, "utf-8");
			if(queryString != null){
				queryString = URLDecoder.decode(queryString, "utf-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		Logger.d(TAG, "decoded uri=" + uri + ", queryString=" + queryString);
		if(!uri.startsWith(URI_PREFIX)){
			Logger.e(TAG, "unSupport uri=" + uri);
			return null;
		}
		String relativePath = null;
		if(uri.length() > URI_PREFIX.length()){
			relativePath = uri.substring(URI_PREFIX.length() + 1);
		}else{
			relativePath = "";
		}
		uri = uri.replace("/", File.separator);
		Logger.d(TAG, "relativePath=" + relativePath);
		String musicFullPath = null;
		if(relativePath.length() > 0 && !musicServerRoot.equals(File.separator)){
			musicFullPath = musicServerRoot + File.separator + relativePath;
		}else{
			musicFullPath = musicServerRoot;
		}
		Logger.d(TAG, "musicFullPath=" + musicFullPath);
		File directFile = new File(musicFullPath);
		if(directFile.isDirectory()){	//point to a music folder
			String defaultFile = musicFullPath + File.separator + "index.html";
			Logger.d(TAG, "defaultFile=" + defaultFile);
			if(new File(defaultFile).exists()){
				return defaultFile;
			}
		}else if(directFile.exists()){ //point to exact file
			return musicFullPath;
		}
		return null;
	}
}

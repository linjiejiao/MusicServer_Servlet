package cn.ljj.music;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.ServletContext;

public class MusicResourcePathManager {
	private static final String URI_PREFIX = "/MusicServer";
	public String musicServerRoot = null;
	
	public MusicResourcePathManager(ServletContext context){
		musicServerRoot = context.getInitParameter("music_page_root");
	}

	public String getResourcePath(String uri, String queryString){
		System.out.println("uri=" + uri + ", queryString=" + queryString);
		try {
			uri = URLDecoder.decode(uri, "utf-8");
			if(queryString != null){
				queryString = URLDecoder.decode(queryString, "utf-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		System.out.println("decoded uri=" + uri + ", queryString=" + queryString);
		if(!uri.startsWith(URI_PREFIX)){
			System.out.println("unSupport uri=" + uri);
			return null;
		}
		String relativePath = null;
		if(uri.length() > URI_PREFIX.length()){
			relativePath = uri.substring(URI_PREFIX.length() + 1);
		}else{
			relativePath = "";
		}
		uri = uri.replace("/", File.separator);
		System.out.println("relativePath=" + relativePath);
		String musicFullPath = null;
		if(relativePath.length() > 0 && !musicServerRoot.equals(File.separator)){
			musicFullPath = musicServerRoot + File.separator + relativePath;
		}else{
			musicFullPath = musicServerRoot;
		}
		System.out.println("musicFullPath=" + musicFullPath);
		File directFile = new File(musicFullPath);
		if(directFile.isDirectory()){	//point to a music folder
			String defaultFile = musicFullPath + File.separator + "index.html";
			System.out.println("defaultFile=" + defaultFile);
			if(new File(defaultFile).exists()){
				return defaultFile;
			}
		}else if(directFile.exists()){ //point to exact file
			return musicFullPath;
		}
		return null;
	}
}

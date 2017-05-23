package cn.ljj.music;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MusicServer
 */
@WebServlet("/MusicServer")
public class MusicServer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MusicResourcePathManager musicResourcePathManager = null;

	/**
	 * Default constructor.
	 */
	public MusicServer() {
	}
	
	private MusicResourcePathManager getMusicResourcePathManager() {
		if(musicResourcePathManager == null){
			musicResourcePathManager = new MusicResourcePathManager(getServletContext());
		}
		return musicResourcePathManager;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String queryString = request.getQueryString();
		String uri = request.getRequestURI();
		String resourcePath = getMusicResourcePathManager().getResourcePath(uri, queryString);
		if(resourcePath == null){
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		InputStream input = new FileInputStream(resourcePath);
		byte[] buffer = new byte[1024];
		int len = input.read(buffer, 0, 1024);
		while(len > 0){
			response.getOutputStream().write(buffer, 0, len);
			len = input.read(buffer, 0, 1024);
		}
		input.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("doPost");
		doGet(request, response);
	}

	public void doGetHEader(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		Enumeration<String> e = request.getHeaderNames();
		while (e.hasMoreElements()) {
			String name = (String) e.nextElement();
			String value = request.getHeader(name);
			out.println(name + " = " + value);
			System.out.println(name + " = " + value);
		}
	}
}

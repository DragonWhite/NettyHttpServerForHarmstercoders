package ua.net.la.dragonwhite.httpserver;

/**
 * Hello world!
 *
 */
public class App {
	private static int port = 8080;
	public static void main(String[] args) {
		HttpServer server = new HttpServer(port);
		try {
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
}

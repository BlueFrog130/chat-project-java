public class App {
	public static void main(String[] args) throws Exception {
		System.out.println("Working Directory = " + System.getProperty("user.dir"));
		Server server;
		if (args.length > 0) {
			int port = Integer.parseInt(args[0]);
			server = new Server(port);
		} else {
			server = new Server();
		}
		server.start();
	}
}

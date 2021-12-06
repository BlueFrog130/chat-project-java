import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Chat server
 */
public class Server {
	private int port;
	private ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();
	private NameGenerator nameGenerator = new NameGenerator();

	public Server() throws IOException {
		port = 8000;
	}

	public Server(int port) throws IOException {
		this.port = port;
	}

	/**
	 * Listens for users to connect and adds them to the users set
	 */
	public void start() {
		try (ServerSocket server = new ServerSocket(port)) {
			System.out.println("Server listening on port " + port);
			while (true) {
				Socket socket = server.accept();

				String randomName = nameGenerator.randomUserName(users.keySet());
				User user = new User(socket, this, randomName);
				user.start();
				System.out.println(user.getUserName() + " connected");
				broadcast("users:" + user.getUserName());
				users.put(randomName, user);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void broadcast(String message) {
		for (User user : users.values()) {
			user.send(message);
		}
	}

	public void remove(User user) {
		users.remove(user.getName());
		System.out.println(user.getUserName() + " left");
	}

	public String getUsers() {
		StringBuilder sb = new StringBuilder();
		for (String name : users.keySet()) {
			sb.append(name).append(',');
		}
		return sb.toString();
	}
}

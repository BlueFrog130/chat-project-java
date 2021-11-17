import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

/**
 * Chat server
 */
public class Server {
	private int port;
	private Set<User> users = new HashSet<>();
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

				User user = new User(socket, this, nameGenerator.randomUserName());
				System.out.println(user.getUserName() + " connected");
				broadcast("users:" + user.getUserName());
				users.add(user);
				user.start();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void broadcast(String message) {
		for (User user : users) {
			user.send(message);
		}
	}

	public void remove(User user) {
		users.remove(user);
		System.out.println(user.getUserName() + " left");
	}

	public String getUsers() {
		StringBuilder sb = new StringBuilder();
		for (User u : users) {
			sb.append(u.getUserName()).append(',');
		}
		return sb.toString();
	}
}

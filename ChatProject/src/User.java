import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class User extends Thread {
	private Socket socket;
	private Server server;
	private PrintWriter writer;
	private String userName;

	public User(Socket socket, Server server, String userName) {
		this.socket = socket;
		this.server = server;
		this.userName = userName;
	}

	public String getUserName() {
		return this.userName;
	}

	public void run() {
		try {
			InputStream in = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader((in)));

			OutputStream out = socket.getOutputStream();
			writer = new PrintWriter(out, true);

			send("name:" + userName);
			send("users:" + server.getUsers());

			String serverMessage = "message:{server}" + userName + " joined the chat";
			server.broadcast(serverMessage);

			String clientMessage;
			while (true) {
				clientMessage = reader.readLine().trim();
				serverMessage = "message:{" + userName + "}" + clientMessage;
				server.broadcast(serverMessage);
			}
		} catch (SocketException ex) {
			server.remove(this);
			server.broadcast("leave:" + userName);
			server.broadcast("message:{server}" + userName + " left the chat");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void send(String message) {
		writer.println(message);
	}
}

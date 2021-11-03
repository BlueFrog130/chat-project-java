import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
	private String hostname;
	private int port;
	private String userName;
	private ArrayList<String> users = new ArrayList<>();

	public Client(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
	}

	public void start() {
		try {
			Socket socket = new Socket(hostname, port);
			System.out.println("Connected to chat server");
			new ReadThread(socket, this).start();
			new WriteThread(socket, this).start();
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String name) {
		userName = name;
	}

	public void addUsers(String[] users) {
		for (String name : users)
			this.users.add(name);
	}
}

class ReadThread extends Thread {
	private BufferedReader reader;
	private Client client;

	public ReadThread(Socket socket, Client client) {
		this.client = client;

		try {
			InputStream in = socket.getInputStream();
			reader = new BufferedReader(new InputStreamReader(in));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			try {
				Response response = new Response(reader.readLine());
				switch (response.getCommand()) {
				case NAME:
					client.setUserName(response.getData());
					break;
				case MESSAGE:
					if (!response.getUser().equals(client.getUserName()))
						System.out.printf("[%s]: %s\n", response.getUser(), response.getData());
					break;
				case USERS:
					String[] users = response.getData().split(",");
					client.addUsers(users);
					break;
				default:
					break;
				}
			} catch (IOException ex) {
				ex.printStackTrace();
				break;
			}
		}
	}
}

class WriteThread extends Thread {
	private PrintWriter writer;
	private Client client;

	public WriteThread(Socket socket, Client client) {
		this.client = client;

		try {
			OutputStream out = socket.getOutputStream();
			writer = new PrintWriter(out, true);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void run() {
		Console console = System.console();
		String text;
		try {
			while (true) {
				text = console.readLine("[" + client.getUserName() + "]: ");
				writer.println(text);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
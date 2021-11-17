import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class Client {
	private String hostname;
	private int port;
	private StringProperty userName = new SimpleStringProperty("");
	private ObservableList<String> users = FXCollections.observableArrayList();
	private ObservableList<Response> messages = FXCollections.observableArrayList();
	private WriteThread writer;

	public Client(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
	}

	public void start() throws UnknownHostException, IOException {
		Socket socket = new Socket(hostname, port);
		new ReadThread(socket, this).start();
		writer = new WriteThread(socket);
		writer.start();
	}

	public StringProperty userNameProperty() {
		return userName;
	}

	public void addUsers(String[] users) {
		for (String name : users)
			this.users.add(name);
	}

	public void removeUser(String user) {
		this.users.remove(user);
	}

	public void addMessage(Response response) {
		messages.add(response);
	}

	public void addMessageListener(ListChangeListener<Response> listener) {
		messages.addListener(listener);
	}

	public void addUserListener(ListChangeListener<String> listener) {
		users.addListener(listener);
	}

	public void send(String message) {
		writer.send(message);
	}

	public ObservableList<String> getUsersList() {
		return users;
	}

	public ObservableList<Response> getMessages() {
		return messages;
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
				Response response = new Response(reader.readLine(), client.userNameProperty().get());
				switch (response.getCommand()) {
				case NAME:
					Platform.runLater(() -> {
						client.userNameProperty().set(response.getData());
					});
					break;
				case MESSAGE:
					Platform.runLater(() -> {
						client.addMessage(response);
					});
					break;
				case USERS:
					Platform.runLater(() -> {
						String[] users = response.getData().split(",");
						client.addUsers(users);
					});
					break;
				case LEAVE:
					Platform.runLater(() -> {
						client.removeUser(response.getData());
					});
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

	public WriteThread(Socket socket) {
		try {
			OutputStream out = socket.getOutputStream();
			writer = new PrintWriter(out, true);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void send(String message) {
		writer.println(message);
	}
}
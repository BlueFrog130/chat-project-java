import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class Client {
	private String hostname;
	private int port;
	private StringProperty userName = new SimpleStringProperty();
	private ObservableList<String> users = FXCollections.observableArrayList();
	public ArrayList<ClientEvent> onResponseListeners = new ArrayList<>();

	public Client(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
	}

	public void start() throws UnknownHostException, IOException {
		Socket socket = new Socket(hostname, port);
		System.out.println("Connected to chat server");
		new ReadThread(socket, this).start();
		new WriteThread(socket, this).start();
	}

	public StringProperty userNameProperty() {
		return userName;
	}

	public void addUsers(String[] users) {
		for (String name : users)
			this.users.add(name);
	}

	public void addResponseListener(ClientEvent event) {
		onResponseListeners.add(event);
	}

	public void addUserListener(ListChangeListener<String> listChangeListener) {
		users.addListener(listChangeListener);
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
					client.userNameProperty().set(response.getUser());
					break;
				case MESSAGE:
					client.onResponseListeners.forEach(e -> e.onClientEvent(response));
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

	public void send(String message) {
		writer.println(message);
	}
}
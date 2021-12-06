import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class Bot {
	private String hostname;
	private int port;
	private long lastWrite = -1;
	private long lastRead = -1;
	private Socket socket;
	private BufferedWriter outputWriter;
	private int id;
	private HashMap<String, String> config;
	private String name;

	public Bot(int id, HashMap<String, String> config, BufferedWriter outputWriter)
			throws UnknownHostException, IOException {
		this.hostname = config.containsKey("HOSTNAME") ? config.get("HOSTNAME") : "localhost";
		this.port = config.containsKey("PORT") ? Integer.parseInt(config.get("PORT")) : 8000;
		this.id = id;
		this.outputWriter = outputWriter;
		this.config = config;
		this.socket = new Socket(this.hostname, this.port);
		new ReadThread(this).start();
		WriteThread writer = new WriteThread(this);
		Timer timer = new Timer();
		timer.schedule(writer, (int) (Math.random() * 5000) + (10 * Integer.parseInt(config.get("USERS"))),
				config.containsKey("MESSAGE_INTERVAL") ? Integer.parseInt(config.get("MESSAGE_INTERVAL")) : 5000);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getLastRead() {
		return lastRead;
	}

	public void setLastRead(long lastRead) {
		this.lastRead = lastRead;
	}

	public long getLastWrite() {
		return lastWrite;
	}

	public void setLastWrite(long lastWrite) {
		this.lastWrite = lastWrite;
	}

	public Socket getSocket() {
		return this.socket;
	}

	public void log(String msg) throws IOException {
		outputWriter.write(String.format("%d,%s,%f,%s\n", id, name, (getLastRead() - getLastWrite()) / 1000000.0, msg));
		lastRead = lastWrite = -1;
		outputWriter.flush();
	}

	public String getConfigOption(String key) {
		return config.getOrDefault(key, null);
	}

	public boolean containsConfigOption(String key) {
		return config.containsKey(key);
	}

	public int getId() {
		return this.id;
	}
}

class ReadThread extends Thread {
	private Bot bot;
	private BufferedReader reader;

	public ReadThread(Bot bot) {
		this.bot = bot;
		try {
			InputStream in = bot.getSocket().getInputStream();
			reader = new BufferedReader(new InputStreamReader(in));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			try {
				Response response = new Response(reader.readLine());
				if (response.getCommand() == Command.NAME)
					bot.setName(response.getData());
				else if (response.getCommand() == Command.MESSAGE
						&& response.getUser().equals(bot.getName())
						&& bot.getLastWrite() > -1
						&& bot.getLastRead() == -1) {
					bot.setLastRead(System.nanoTime());
					bot.log(response.getData());
				} else if (bot.getLastRead() > -1) {
					System.out.printf("Bot %d read error: Last read has not been reset\n");
				}
			} catch (IOException ex) {
				ex.printStackTrace();
				break;
			}
		}
	}
}

class WriteThread extends TimerTask {
	private Bot bot;
	private PrintWriter writer;

	public WriteThread(Bot bot) {
		this.bot = bot;
		try {
			this.writer = new PrintWriter(bot.getSocket().getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		writer.println(this.bot.containsConfigOption("MESSAGE") ? this.bot.getConfigOption("MESSAGE") : "Hello!");
		// System.out.println(String.format("Bot %d sent message", this.bot.getId()));
		if (this.bot.getLastWrite() != -1) {
			System.out.println(String.format("Bot %d has not logged since last write", bot.getId()));
		} else {
			bot.setLastWrite(System.nanoTime());
		}
	}
}
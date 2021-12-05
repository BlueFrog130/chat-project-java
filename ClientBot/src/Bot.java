import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.TimerTask;

public class Bot {
	private String hostname;
	private int port;
	private long lastWrite = -1;
	private long lastRead = -1;
	private Socket socket;
	private ReadThread reader;
	private WriteThread writer;
	private BufferedWriter outputWriter;
	private int id;

	public Bot(int id, HashMap<String, String> config, BufferedWriter outputWriter)
			throws UnknownHostException, IOException {
		this.hostname = config.containsKey("HOSTNAME") ? config.get("HOSTNAME") : "localhost";
		this.port = config.containsKey("PORT") ? Integer.parseInt(config.get("PORT")) : 8000;
		this.id = id;
		this.outputWriter = outputWriter;
		this.socket = new Socket(this.hostname, this.port);
		this.reader = new ReadThread(this);
		this.writer = new WriteThread(this);
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

	public void log() throws IOException {
		outputWriter.write(String.format("%d:RTT=%d", id, lastRead - lastRead));
		lastRead = lastWrite = -1;
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
				if (response.getCommand() == Command.MESSAGE && bot.getLastWrite() > -1) {
					bot.setLastRead(System.currentTimeMillis());
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

	public WriteThread(Bot bot) {
		this.bot = bot;
	}

	public void run() {

	}
}
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

public class App {
	private static final HashMap<String, String> config = new HashMap<>();
	private static final String[] required = new String[] { "USERS" };

	public static void main(String[] args) throws Exception {
		File conf = new File("conf.txt");
		File output = new File("out.csv");
		if (!output.exists()) {
			output.createNewFile();
		} else {
			output.delete();
			output.createNewFile();
		}

		BufferedReader reader = new BufferedReader(new FileReader(conf));
		reader.lines().forEach(line -> {
			String[] split = line.split("( |=)", 2);
			config.put(split[0], split[1]);
		});
		reader.close();

		for (String req : required) {
			if (!config.containsKey(req)) {
				throw new Exception(String.format("Missing required key %s in conf.txt", req));
			}
		}

		BufferedWriter writer = new BufferedWriter(new FileWriter(output));

		writer.write("id,name,rtt,message\n");
		writer.flush();

		for (int i = 0; i < Integer.parseInt(config.get("USERS")); i++) {
			try {
				new Bot(i, config, writer);
			} catch (Exception e) {
				System.out.printf("Bot %d could not connect\n", i);
			}
			Thread.sleep(10);
		}

		Thread.sleep(config.containsKey("TIME") ? Integer.parseInt(config.get("TIME")) : 30000);
		System.out.println("Done");
		System.exit(0);
	}
}

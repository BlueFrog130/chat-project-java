import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Response {
	private Command command;
	private String user;
	private String data;
	private boolean self;

	public Response(String response, String userName) {
		Pattern p = Pattern.compile("(.+?):(?:\\{(.+?)\\})?(.*)$");
		Matcher m = p.matcher(response);
		if (m.matches()) {
			command = Command.valueOf(m.group(1).toUpperCase());
			user = m.group(2);
			data = m.group(3);
		}

		self = user == null ? false : user.equals(userName);
	}

	public Command getCommand() {
		return command;
	}

	public String getUser() {
		return user;
	}

	public String getData() {
		return data;
	}

	public boolean isSelf() {
		return self;
	}
}

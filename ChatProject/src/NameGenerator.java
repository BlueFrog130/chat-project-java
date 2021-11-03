import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class NameGenerator {
	private ArrayList<String> colors = new ArrayList<>();
	private ArrayList<String> animals = new ArrayList<>();

	public NameGenerator() throws FileNotFoundException {
		Scanner sc = new Scanner(new File("animals.txt"));
		while (sc.hasNextLine())
			animals.add(sc.nextLine());
		sc = new Scanner(new File("colors.txt"));
		while (sc.hasNextLine())
			colors.add(sc.nextLine());
	}

	public String randomUserName() {
		return colors.get((int) (Math.random() * colors.size()) + 1)
				+ animals.get((int) (Math.random() * animals.size()) + 1);
	}
}

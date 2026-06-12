package design_patterns.behavioral.iterator;

public class Client {

    public static void main(String[] args) {
		Iterator<ThemeColor> iter = ThemeColor.getIterator();

		while (iter.hasNext()) {
			ThemeColor themeColor = iter.next();
			System.out.println(themeColor);
		}
    }

}

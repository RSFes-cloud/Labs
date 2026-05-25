import java.util.regex.*;
import java.util.Scanner;

public class WordFinder {
    public static void main(String[] args) {
        String text = "Java is a popular programming language. JavaScript is also widely used. Just keep practicing!";

        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите начальную букву для поиска: ");
        String letter = scanner.nextLine().substring(0, 1);


        Pattern pattern = Pattern.compile("(?i)\\b" + letter + "\\w*\\b");
        Matcher matcher = pattern.matcher(text);

        System.out.println("Слова, начинающиеся на букву '" + letter + "':");
        boolean found = false;
        while (matcher.find()) {
            System.out.println(matcher.group());
            found = true;
        }

        if (!found) {
            System.out.println("Совпадений не найдено.");
        }
    }
}
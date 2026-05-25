import java.util.regex.*;
import java.util.Scanner;

public class IPValidator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите IP-адрес: ");
        String ip = scanner.nextLine();

        // Регулярное выражение для октета от 0 до 255
        String octet = "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)";
        Pattern pattern = Pattern.compile("^(" + octet + "\\.){3}" + octet + "$");
        Matcher matcher = pattern.matcher(ip);

        if (matcher.matches()) {
            System.out.println("IP-адрес введен корректно.");
        } else {
            System.out.println("Ошибка: Некорректный формат IP-адреса.");
        }
    }
}
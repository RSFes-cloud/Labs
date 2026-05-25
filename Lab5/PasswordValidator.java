import java.util.regex.*;
import java.util.Scanner;

public class PasswordValidator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        // ^ - начало строки
        // (?=.*[A-Z]) - хотя бы одна заглавная буква
        // (?=.*\\d) - хотя бы одна цифра
        // [A-Za-z\\d]{8,16} - только латинские буквы и цифры, длина от 8 до 16
        // $ - конец строки
        Pattern pattern = Pattern.compile("^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,16}$");
        Matcher matcher = pattern.matcher(password);

        if (matcher.matches()) {
            System.out.println("Пароль надежный и введен корректно.");
        } else {
            System.out.println("Ошибка: Пароль не соответствует требованиям безопасности.");
        }
    }
}
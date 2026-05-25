import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

// Пользовательский класс исключения
class CustomDivisionException extends Exception {
    public CustomDivisionException(String message) {
        super(message);
    }
}

public class DivisionLogger {
    // Метод для логгирования исключений в текстовый файл
    public static void logException(Exception e) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("exception_log.txt", true))) {
            writer.println(LocalDateTime.now() + " | Выброшено исключение: " + e.getClass().getName());
            writer.println("Сообщение: " + e.getMessage());
            writer.println("--------------------------------------------------");
        } catch (IOException ioException) {
            System.out.println("Критическая ошибка: Не удалось произвести запись в лог-файл.");
        }
    }

    // Метод деления, который может выбросить пользовательское исключение
    public static double divideNumbers(int a, int b) throws CustomDivisionException {
        if (b == 0) {
            throw new CustomDivisionException("Недопустимая операция: попытка деления на ноль.");
        }
        return (double) a / b;
    }

    public static void main(String[] args) {
        int number1 = 10;
        int number2 = 0;

        try {
            double result = divideNumbers(number1, number2);
            System.out.println("Результат деления: " + result);
        } catch (CustomDivisionException e) {
            System.out.println("Ошибка в программе: " + e.getMessage());
            System.out.println("Информация об ошибке записана в лог-файл.");
            // Вызов обработчика логгирования
            logException(e);
        }
    }
}
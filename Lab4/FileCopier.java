import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileCopier {
    public static void main(String[] args) {
        File sourceFile = new File("source.txt");
        File destFile = new File("destination.txt");

        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            // Попытка открытия файлов
            fis = new FileInputStream(sourceFile);
            fos = new FileOutputStream(destFile);

            int data;
            while ((data = fis.read()) != -1) {
                fos.write(data);
            }
            System.out.println("Файл успешно скопирован.");

        } catch (FileNotFoundException e) {
            System.out.println("Ошибка при открытии файлов: Исходный файл не найден или нет прав на запись.");
        } catch (IOException e) {
            System.out.println("Произошла ошибка ввода-вывода при работе с файлами.");
        } finally {
            // Блок finally выполняется всегда, здесь мы безопасно закрываем файлы [cite: 289]
            try {
                if (fis != null) {
                    fis.close();
                }
                if (fos != null) {
                    fos.close();
                }
                System.out.println("Файлы успешно закрыты (если были открыты).");
            } catch (IOException e) {
                System.out.println("Ошибка при закрытии файлов: " + e.getMessage());
            }
        }
    }
}
public class ArrayAverage {
    public static void main(String[] args) {
        // Массив строк, содержащий одно неверное значение ("четыре") для демонстрации ошибки
        String[] arr = {"1", "2", "3", "четыре", "5"};
        int sum = 0;
        int validElementsCount = 0;

        try {
            // Умышленно используем <= чтобы спровоцировать выход за границы массива в конце
            for (int i = 0; i <= arr.length; i++) {
                // Преобразуем строку в число
                sum += Integer.parseInt(arr[i]);
                validElementsCount++;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Ошибка: Выход за границы массива.");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: Неверные данные, элемент массива не является числом.");
        }

        // Вывод результата для успешно обработанных элементов
        if (validElementsCount > 0) {
            double average = (double) sum / validElementsCount;
            System.out.println("Среднее арифметическое валидных элементов: " + average);
        } else {
            System.out.println("В массиве нет валидных чисел.");
        }
    }
}
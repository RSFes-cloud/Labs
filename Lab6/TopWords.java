import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class TopWords {
    public static void main(String[] args) {

        String filePath = "text.txt";
        File file = new File(filePath);
        Scanner scanner = null;

        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден. Пожалуйста, создайте text.txt в корне проекта.");
            return;
        }


        Map<String, Integer> wordCounts = new HashMap<>();


        while (scanner.hasNext()) {

            String word = scanner.next().toLowerCase().replaceAll("[^a-zа-я0-9]", "");
            if (!word.isEmpty()) {
                wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
            }
        }


        scanner.close();


        List<Map.Entry<String, Integer>> list = new ArrayList<>(wordCounts.entrySet());


        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                // Сортировка по убыванию
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        // выводим топ-10 слов
        System.out.println("Топ-10 самых часто встречающихся слов:");
        int count = Math.min(10, list.size());
        for (int i = 0; i < count; i++) {
            System.out.println((i + 1) + ". " + list.get(i).getKey() + " - " + list.get(i).getValue() + " раз(а)");
        }
    }
}
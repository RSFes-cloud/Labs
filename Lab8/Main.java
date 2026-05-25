import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface DataProcessor {
}


class FilterProcessor {
    @DataProcessor
    public String filterEmpty(String data) {
        return (data == null || data.trim().isEmpty()) ? null : data;
    }
}

class TransformProcessor {
    @DataProcessor
    public String toUpperCase(String data) {
        return data != null ? data.toUpperCase() : null;
    }
}

class DecorateProcessor {
    @DataProcessor
    public String decorate(String data) {
        return data != null ? "[" + data + "]" : null;
    }
}

class DataManager {
    private final List<Object> processors = new ArrayList<>();
    private List<String> data = new ArrayList<>();

    public void registerDataProcessor(Object processor) {
        processors.add(processor);
    }


    public void loadData(String source) {
        try {
            data = Files.readAllLines(Paths.get(source));
            System.out.println("Данные успешно загружены из " + source);
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке данных: " + e.getMessage());
        }
    }

    public void processData() {

        ExecutorService executor = Executors.newFixedThreadPool(4);

        List<Method> annotatedMethods = new ArrayList<>();
        List<Object> targetProcessors = new ArrayList<>();

        for (Object processor : processors) {
            for (Method method : processor.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(DataProcessor.class)) {
                    annotatedMethods.add(method);
                    targetProcessors.add(processor);
                }
            }
        }

        try {
            List<Callable<String>> tasks = data.stream().map(item -> (Callable<String>) () -> {
                String current = item;
                for (int i = 0; i < annotatedMethods.size(); i++) {
                    if (current == null) break;
                    current = (String) annotatedMethods.get(i).invoke(targetProcessors.get(i), current);
                }
                return current;
            }).collect(Collectors.toList());

            List<Future<String>> futures = executor.invokeAll(tasks);
            List<String> processedData = new ArrayList<>();


            for (Future<String> future : futures) {
                String result = future.get();
                if (result != null) {
                    processedData.add(result);
                }
            }
            this.data = processedData;
            System.out.println("Данные успешно обработаны.");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    public void saveData(String destination) {
        try {
            Files.write(Paths.get(destination), data);
            System.out.println("Результат сохранен в " + destination);
        } catch (Exception e) {
            System.err.println("Ошибка при сохранении данных: " + e.getMessage());
        }
    }
}

// 5. Тестирование приложения
public class Main {
    public static void main(String[] args) {
        String sourceFile = "input.txt";
        String destFile = "output.txt";

        // Подготавливаем тестовые данные (создаем исходный файл)
        try {
            List<String> rawData = Arrays.asList(
                    "первая строка",
                    "   ", // Эта строка должна отфильтроваться
                    "java stream api",
                    "многопоточность",
                    ""
            );
            Files.write(Paths.get(sourceFile), rawData);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DataManager dataManager = new DataManager();

        // Регистрируем обработчики
        dataManager.registerDataProcessor(new FilterProcessor());
        dataManager.registerDataProcessor(new TransformProcessor());
        dataManager.registerDataProcessor(new DecorateProcessor());

        // Запускаем конвейер
        dataManager.loadData(sourceFile);
        dataManager.processData();
        dataManager.saveData(destFile);
    }
}
# ОБЪЯСНЕНИЕ ЛАБОРАТОРНОЙ РАБОТЫ №8
# Аннотации, Stream API, java.util.concurrent

---

## ОБЩАЯ ИДЕЯ ПРОГРАММЫ

Мы создаём приложение, которое читает текстовый файл, обрабатывает строки тремя способами одновременно (в разных потоках) и сохраняет результаты в новый файл.

Структура проекта — 4 файла:
```
DataProcessor.java   — аннотация-метка @DataProcessor
DataProcessors.java  — три метода обработки данных
DataManager.java     — загрузка, параллельный запуск, сохранение
Main.java            — точка входа, запускает всё по шагам
```

---
---

# ЗАДАЧА 1 — Создать аннотацию @DataProcessor

---

## Код

```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DataProcessor {
    String description() default "Обработчик данных";
}
```

---

## Что нужно сделать

Создать пользовательскую аннотацию `@DataProcessor`, которую можно вешать на методы. Она будет служить меткой — DataManager позже найдёт все помеченные методы через Reflection и запустит их в потоках.

---

## Как работает в целом

Аннотация — это просто метка. Сама по себе она ничего не делает. Но благодаря настройке `RUNTIME` эта метка остаётся доступной во время работы программы. DataManager читает все методы класса DataProcessors и спрашивает: "есть ли на этом методе метка @DataProcessor?" — если да, запускает его. Без аннотации DataManager не знал бы, какие методы являются обработчиками.

---

## Объяснение кода по частям

### Часть 1 — Импорты

```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
```

| Строка | Что импортируем | Зачем |
|---|---|---|
| `ElementType` | Перечисление мест применения | Чтобы указать — только на методы |
| `Retention` | Аннотация управления временем жизни | Чтобы поставить @Retention на нашу аннотацию |
| `RetentionPolicy` | Перечисление вариантов хранения | Чтобы выбрать RUNTIME |
| `Target` | Аннотация ограничения места | Чтобы поставить @Target на нашу аннотацию |

### Часть 2 — Мета-аннотации (аннотации над аннотацией)

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
```

**`@Retention(RetentionPolicy.RUNTIME)`**
Определяет, как долго аннотация "живёт". Три варианта:
- `SOURCE` — удаляется компилятором, только в исходном коде
- `CLASS` — остаётся в .class файле, но недоступна во время работы программы
- `RUNTIME` — доступна во время работы программы ✅ (нам нужно именно это, чтобы Reflection мог её найти)

**`@Target(ElementType.METHOD)`**
Ограничивает места применения. Если попробовать поставить @DataProcessor на класс или поле — компилятор выдаст ошибку. Это защита от случайного неправильного использования.

### Часть 3 — Объявление аннотации

```java
public @interface DataProcessor {
    String description() default "Обработчик данных";
}
```

**`public @interface DataProcessor`**
`@interface` — специальный синтаксис для объявления аннотации. Не путать с обычным `interface`! Это принципиально другая конструкция.

**`String description() default "Обработчик данных";`**
Элемент аннотации — атрибут, который заполняется при использовании.
- Выглядит как метод без тела
- `default "Обработчик данных"` — значение по умолчанию, если атрибут не указан

---

## Мини-пример

```java
// Как используется аннотация:
public List<String> filter(List<String> data) { ...}

// Как DataManager читает атрибут description через Reflection:
DataProcessor ann = method.getAnnotation(DataProcessor.class);
System.out.

println(ann.description()); // выведет: "Фильтрация пустых строк"

// Если description не указан — используется значение default:
public List<String> process(List<String> data) { ...}
// ann.description() вернёт: "Обработчик данных"
```

---
---

# ЗАДАЧА 2 — Создать обработчики данных с аннотацией и Stream API

---

## Код

```java
import java.util.List;
import java.util.stream.Collectors;

public class DataProcessors {

    public List<String> filter(List<String> data) {
        return data.stream()
                .filter(line -> line.trim().length() >= 5)
                .collect(Collectors.toList());
    }

    public List<String> transform(List<String> data) {
        return data.stream()
                .map(line -> line.trim().toUpperCase())
                .collect(Collectors.toList());
    }

    public List<String> aggregate(List<String> data) {
        return data.stream()
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}
```

---

## Что нужно сделать

Создать класс с тремя методами-обработчиками, каждый из которых помечен `@DataProcessor` и обрабатывает список строк по-своему, используя Stream API. DataManager найдёт эти методы автоматически по метке.

---

## Как работает в целом

Каждый метод получает на вход один и тот же список строк из файла и возвращает новый список — результат обработки. Внутри каждого метода используется Stream API: список превращается в поток, над потоком выполняется цепочка операций, результат собирается обратно в список. Все три метода работают независимо друг от друга — это позволяет запустить их параллельно в разных потоках.

---

## Объяснение кода по частям

### Метод filter() — Фильтрация

```java

public List<String> filter(List<String> data) {
    return data.stream()
            .filter(line -> line.trim().length() >= 5)
            .collect(Collectors.toList());
}
```

**`@DataProcessor(description = "Фильтрация коротких строк")`**
Вешаем метку на метод. DataManager найдёт этот метод по метке и запустит его в отдельном потоке.

**`public List<String> filter(List<String> data)`**
Метод принимает список строк и возвращает список строк. `List<String>` — это список, где каждый элемент — строка (`<String>` — это дженерик, уточнение типа).

**`data.stream()`**
Превращаем список в Stream — "конвейер" для обработки элементов. Сам по себе не хранит данные, только описывает, что нужно сделать. Выполняется лениво — только когда дойдём до терминальной операции.

**`.filter(line -> line.trim().length() >= 5)`**
Промежуточная операция. Оставляем только строки с длиной >= 5 символов:
- `line` — текущий элемент (каждая строка по очереди)
- `line.trim()` — убираем пробелы по краям: `"  ok  "` → `"ok"` (чтобы строка из пробелов не считалась длинной)
- `.length()` — количество символов
- `>= 5` — условие: если false — строка удаляется из потока

**`.collect(Collectors.toList())`**
Терминальная операция — запускает весь конвейер и собирает результат в новый список.

---

### Метод transform() — Трансформация

```java

public List<String> transform(List<String> data) {
    return data.stream()
            .map(line -> line.trim().toUpperCase())
            .collect(Collectors.toList());
}
```

**`.map(line -> line.trim().toUpperCase())`**
Промежуточная операция. `map` преобразует каждый элемент:
- `line.trim()` — убирает пробелы в начале и конце: `"  hello  "` → `"hello"`
- `.toUpperCase()` — переводит все буквы в верхний регистр: `"hello"` → `"HELLO"`
Результат: `"  hello world  "` → `"HELLO WORLD"`

---

### Метод aggregate() — Агрегация

```java

public List<String> aggregate(List<String> data) {
    return data.stream()
            .distinct()
            .sorted()
            .collect(Collectors.toList());
}
```

**`.distinct()`**
Убирает повторяющиеся строки. Сравнивает через `equals()`. Если "apple" встречается 3 раза — останется одна.

**`.sorted()`**
Сортирует строки в алфавитном порядке (по коду символов: заглавные A-Z раньше строчных a-z).

---

## Мини-пример — как работает Stream API

```java
List<String> words = Arrays.asList("  hi  ", "hello", "hi", "world", "ok");

// filter — оставляем строки длиной >= 5 (после trim):
// "  hi  ".trim() = "hi" (2) — удаляется
// "hello".trim() = "hello" (5) — остаётся
// "world".trim() = "world" (5) — остаётся
// итог: ["hello", "world"]

// map — trim + toUpperCase:
// "  hi  " → "HI"
// "hello"  → "HELLO"
// "world"  → "WORLD"
// итог: ["HI", "HELLO", "WORLD"]

// distinct + sorted — убрать дубликаты, отсортировать:
// ["  hi  ", "hello", "hi", "world", "ok"] → убрать "hi" (дубль) → ["  hi  ", "hello", "world", "ok"]
// после sorted: ["  hi  ", "hello", "ok", "world"]
```

---
---

# ЗАДАЧА 3 — Создать DataManager: загрузка, многопоточная обработка, сохранение

---

## Код

```java
import java.io.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class DataManager {

    private Object processor;
    private List<String> data = new ArrayList<>();
    private List<String> results = new CopyOnWriteArrayList<>();

    public void registerDataProcessor(Object processor) {
        this.processor = processor;
        System.out.println("Зарегистрирован: " + processor.getClass().getSimpleName());
    }

    public void loadData(String source) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(source))) {
            data = reader.lines().collect(Collectors.toList());
        }
        System.out.println("Загружено строк: " + data.size());
    }

    @SuppressWarnings("unchecked")
    public void processData() throws Exception {
        ExecutorService executor = Executors.newCachedThreadPool();

        for (Method method : processor.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(DataProcessor.class)) {

                executor.execute(() -> {
                    try {
                        System.out.println("[" + Thread.currentThread().getName() + "] "
                                + method.getAnnotation(DataProcessor.class).description());
                        List<String> methodResult = (List<String>) method.invoke(processor, data);
                        results.addAll(methodResult);
                    } catch (Exception e) {
                        System.err.println("Ошибка: " + e.getMessage());
                    }
                });

            }
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println("Обработка завершена. Строк в результате: " + results.size());
    }

    public void saveData(String destination) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(destination))) {
            for (String line : results) {
                writer.write(line);
                writer.newLine();
            }
        }
        System.out.println("Сохранено в: " + destination);
    }
}
```

---

## Что нужно сделать

Создать класс DataManager с четырьмя методами:
- `registerDataProcessor` — сохранить объект-обработчик
- `loadData` — прочитать строки из файла в список
- `processData` — найти все методы с @DataProcessor через Reflection и запустить каждый в отдельном потоке
- `saveData` — записать результаты в файл

---

## Как работает в целом

DataManager не знает заранее, какие методы есть в DataProcessors. Вместо жёсткого вызова `processor.filter(data)` он использует Reflection — сканирует все методы класса и ищет те, что помечены @DataProcessor. Найденные методы он запускает параллельно через пул потоков ExecutorService. После того как все потоки завершаются, результаты из всех методов объединены в одном списке и записываются в файл.

---

## Объяснение кода по частям

### Часть 1 — Импорты

```java
import java.io.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
```

| Импорт | Что даёт |
|---|---|
| `java.io.*` | BufferedReader, FileReader, BufferedWriter, FileWriter, IOException |
| `java.lang.reflect.Method` | Класс Method для работы с методами через Reflection |
| `java.util.*` | ArrayList, List, Arrays и другие коллекции |
| `java.util.concurrent.*` | ExecutorService, Executors, CopyOnWriteArrayList, TimeUnit |
| `java.util.stream.Collectors` | Collectors.toList() для сборки Stream в список |

---

### Часть 2 — Поля класса

```java
private Object processor;
private List<String> data = new ArrayList<>();
private List<String> results = new CopyOnWriteArrayList<>();
```

**`private Object processor`**
Хранит зарегистрированный объект-обработчик. Тип `Object` — самый общий тип в Java, является родителем всех классов. Используем его, чтобы метод `registerDataProcessor` мог принять объект любого класса.

**`private List<String> data = new ArrayList<>()`**
Список строк из файла. Обычный ArrayList — безопасен, потому что в него пишет только главный поток при `loadData`.

**`private List<String> results = new CopyOnWriteArrayList<>()`**
Список для результатов обработки. Используем `CopyOnWriteArrayList` из `java.util.concurrent`, потому что в него одновременно будут писать несколько рабочих потоков. Обычный ArrayList при одновременной записи из разных потоков сломается — потеря данных или исключение. `CopyOnWriteArrayList` при каждой операции записи создаёт новую копию внутреннего массива — операция атомарна и безопасна для любого числа потоков.

---

### Часть 3 — registerDataProcessor()

```java
public void registerDataProcessor(Object processor) {
    this.processor = processor;
    System.out.println("Зарегистрирован: " + processor.getClass().getSimpleName());
}
```

**`this.processor = processor`**
`this.processor` — поле класса. `processor` (без this) — параметр метода. Они называются одинаково, поэтому `this` нужен, чтобы Java поняла: присваиваем полю класса, а не параметру.

**`processor.getClass().getSimpleName()`**
Уже Reflection! `getClass()` возвращает объект типа `Class` — описание класса объекта. `.getSimpleName()` возвращает простое имя: "DataProcessors" (без имени пакета).

---

### Часть 4 — loadData()

```java
public void loadData(String source) throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(source))) {
        data = reader.lines().collect(Collectors.toList());
    }
    System.out.println("Загружено строк: " + data.size());
}
```

**`throws IOException`**
Работа с файлами может выбросить исключение (например, файл не найден). Ключевое слово `throws` в сигнатуре метода означает: "этот метод может выбросить исключение, вызывающий код должен это учесть". Main объявит `throws Exception` и вопрос снят.

**`try (BufferedReader reader = new BufferedReader(new FileReader(source)))`**
Конструкция `try-with-resources`. После закрытия скобки `}` Java автоматически вызовет `reader.close()` — файл закроется сам, даже если произошла ошибка.
- `new FileReader(source)` — открывает файл для посимвольного чтения
- `new BufferedReader(...)` — оборачивает в буфер: читает файл большими блоками, что быстрее, чем по одному символу. Также даёт метод `lines()`

**`reader.lines().collect(Collectors.toList())`**
`reader.lines()` — возвращает `Stream<String>`, где каждый элемент — одна строка файла.
`.collect(Collectors.toList())` — терминальная операция, собирает поток в `List<String>`.

---

### Часть 5 — processData()

```java
@SuppressWarnings("unchecked")
public void processData() throws Exception {
    ExecutorService executor = Executors.newCachedThreadPool();

    for (Method method : processor.getClass().getDeclaredMethods()) {
        if (method.isAnnotationPresent(DataProcessor.class)) {

            executor.execute(() -> {
                try {
                    System.out.println("[" + Thread.currentThread().getName() + "] "
                            + method.getAnnotation(DataProcessor.class).description());
                    List<String> methodResult = (List<String>) method.invoke(processor, data);
                    results.addAll(methodResult);
                } catch (Exception e) {
                    System.err.println("Ошибка: " + e.getMessage());
                }
            });

        }
    }

    executor.shutdown();
    executor.awaitTermination(1, TimeUnit.MINUTES);
    System.out.println("Обработка завершена. Строк в результате: " + results.size());
}
```

**`@SuppressWarnings("unchecked")`**
Компилятор предупреждает о строке `(List<String>) method.invoke(...)` — он не может проверить правильность приведения типов при компиляции (это выяснится только в runtime). Мы уверены, что тип верный, поэтому подавляем предупреждение.

**`throws Exception`**
`awaitTermination` бросает `InterruptedException`, Reflection бросает несколько типов исключений. `Exception` — общий родитель всех, объявляем его для простоты вместо перечисления каждого.

**`ExecutorService executor = Executors.newCachedThreadPool()`**
`ExecutorService` — интерфейс для управления пулом потоков (из `java.util.concurrent`).
`Executors.newCachedThreadPool()` — фабричный метод, создающий пул, который при необходимости сам создаёт новые потоки. Нам не нужно заранее знать, сколько @DataProcessor методов есть в классе — пул адаптируется.

**`processor.getClass().getDeclaredMethods()`**
Reflection! Получаем массив всех методов класса в виде объектов `Method[]`.
- `processor.getClass()` — объект `Class`, описывающий класс объекта processor
- `.getDeclaredMethods()` — возвращает все методы, объявленные в этом классе (все модификаторы: public, private и т.д.)

**`method.isAnnotationPresent(DataProcessor.class)`**
Reflection! Проверяем наличие аннотации @DataProcessor на методе.
- `DataProcessor.class` — литерал класса аннотации
- Возвращает `true` / `false`
- Только методы с `true` идут в пул потоков

**`executor.execute(() -> { ... })`**
Запускает лямбду `() -> {...}` в одном из потоков пула. Метод `execute()` не ждёт выполнения — он немедленно возвращает управление и продолжает цикл. Так три метода оказываются запущены почти одновременно в трёх разных потоках.

**`Thread.currentThread().getName()`**
Возвращает имя потока, в котором сейчас выполняется этот код: например `"pool-1-thread-2"`. Нужно для вывода в консоль — наглядно видно, что разные обработчики работают в разных потоках.

**`method.getAnnotation(DataProcessor.class).description()`**
Reflection! Получаем объект аннотации с этого метода и читаем её атрибут `description()`.

**`(List<String>) method.invoke(processor, data)`**
Reflection! Вызов метода через объект `Method` — без явного знания имени метода в коде:
- `processor` — объект, у которого вызываем метод (наш DataProcessors)
- `data` — аргумент для метода (список строк)
- Возвращает `Object` → приводим к `List<String>` принудительно

**`results.addAll(methodResult)`**
Добавляем все строки из результата в общий список. Потокобезопасно, т.к. `results` — `CopyOnWriteArrayList`.

**`executor.shutdown()`**
Говорим пулу: "новых задач не будет". Уже запущенные потоки продолжают работу, новые задачи пул принимать перестаёт.

**`executor.awaitTermination(1, TimeUnit.MINUTES)`**
Главный поток останавливается здесь и ждёт, пока все рабочие потоки закончат. Максимальное время ожидания — 1 минута. Без этой строки `saveData()` мог бы вызваться раньше, чем потоки обработают данные — файл оказался бы пустым.

---

### Часть 6 — saveData()

```java
public void saveData(String destination) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(destination))) {
        for (String line : results) {
            writer.write(line);
            writer.newLine();
        }
    }
    System.out.println("Сохранено в: " + destination);
}
```

**`new FileWriter(destination)`**
Открывает файл для записи. Если файл не существует — создаёт. Если существует — перезаписывает с нуля.

**`new BufferedWriter(...)`**
Буферизированная запись: данные накапливаются в памяти и сбрасываются на диск разом. Быстрее, чем записывать каждую строку напрямую на диск.

**`writer.write(line)`** — записывает строку.

**`writer.newLine()`** — записывает символ переноса строки. Лучше, чем `"\n"` — корректно работает на Windows (`\r\n`) и Linux (`\n`).

---

## Мини-пример — Reflection в действии

```java
// Представим, что у нас есть класс:
class MyProcessors {
    public List<String> doFilter(List<String> data) {
        return data;
    }

    public List<String> helper(List<String> data) {
        return data;
    } // без @DataProcessor
}

// Что делает DataManager внутри processData():
Object obj = new MyProcessors();

for(
Method m :obj.

getClass().

getDeclaredMethods()){
        // m принимает значения: doFilter, helper — оба метода

        if(m.

isAnnotationPresent(DataProcessor .class)){
// true  для doFilter  → запустим в потоке
// false для helper    → пропустим

String desc = m.getAnnotation(DataProcessor.class).description();
// desc = "Фильтр"

List<String> result = (List<String>) m.invoke(obj, someData);
// вызвали doFilter(someData) не зная его имени!
    }
            }
```

---
---

# ЗАДАЧА 4 — Создать Main: запустить всё по шагам

---

## Код

```java
public class Main {
    public static void main(String[] args) throws Exception {

        System.out.println("=== Лабораторная работа №8 ===\n");

        DataManager manager = new DataManager();

        manager.registerDataProcessor(new DataProcessors());

        manager.loadData("input.txt");

        manager.processData();

        manager.saveData("output.txt");

        System.out.println("\n=== Готово! ===");
    }
}
```

---

## Что нужно сделать

Написать точку входа программы. Main запускает все четыре шага в правильном порядке, используя DataManager.

---

## Как работает в целом

Main — это дирижёр. Он не содержит никакой бизнес-логики: только создаёт DataManager и вызывает его методы в нужном порядке. Весь сложный код (Reflection, потоки, файлы) спрятан внутри DataManager — Main об этом ничего не знает.

---

## Объяснение кода по частям

### Часть 1 — Сигнатура метода main

```java
public static void main(String[] args) throws Exception {
```

**`public`** — метод доступен извне, JVM вызывает его при запуске программы.

**`static`** — метод можно вызвать без создания объекта класса Main. JVM не создаёт объект Main — она вызывает main напрямую.

**`void`** — метод ничего не возвращает.

**`String[] args`** — массив аргументов командной строки. Мы не используем, но сигнатура обязательна — иначе JVM не найдёт точку входа и выбросит `NoSuchMethodError`.

**`throws Exception`** — методы DataManager объявляют `throws IOException` и `throws Exception`. Мы не пишем try-catch, а просто передаём ответственность выше — в JVM. Если возникнет ошибка, программа завершится с сообщением об ошибке в консоли.

---

### Часть 2 — Пять шагов программы

```java
DataManager manager = new DataManager();                  // шаг 1
manager.registerDataProcessor(new DataProcessors());      // шаг 2
manager.loadData("input.txt");                            // шаг 3
manager.processData();                                    // шаг 4
manager.saveData("output.txt");                           // шаг 5
```

| Шаг | Что происходит |
|---|---|
| 1 | Создаём объект DataManager |
| 2 | Создаём объект DataProcessors и передаём в DataManager — он сохраняет его в поле `processor` |
| 3 | DataManager читает файл input.txt и сохраняет строки в список `data` |
| 4 | DataManager через Reflection находит методы с @DataProcessor и запускает каждый в потоке. Ждёт завершения всех потоков. |
| 5 | DataManager записывает все результаты из списка `results` в файл output.txt |

---

## Мини-пример — что будет в консоли при запуске

```
=== Лабораторная работа №8 ===

Зарегистрирован: DataProcessors

Загружено строк: 19

[pool-1-thread-2] Фильтрация коротких строк
[pool-1-thread-3] Агрегация: удаление дубликатов и сортировка
[pool-1-thread-1] Трансформация: trim + toUpperCase
Обработка завершена. Строк в результате: 55

Сохранено в: output.txt

=== Готово! ===
```

Обратите внимание: потоки запускаются не по порядку (thread-2, thread-3, thread-1) — это нормально. Порядок запуска потоков определяет операционная система, и он непредсказуем. При каждом запуске порядок может быть разным.

---
---

# ИТОГОВАЯ СХЕМА — КАК ВСЁ РАБОТАЕТ ВМЕСТЕ

```
Main
 │
 ├─ 1. new DataManager()
 │
 ├─ 2. registerDataProcessor(new DataProcessors())
 │         DataManager сохраняет объект в поле processor
 │
 ├─ 3. loadData("input.txt")
 │         FileReader → BufferedReader → reader.lines() → data[]
 │         data = ["hello world", "Java", "  ok  ", ...]
 │
 ├─ 4. processData()
 │         │
 │         ├─ Reflection: processor.getClass().getDeclaredMethods()
 │         │    └─ найдено: [filter, transform, aggregate]
 │         │         все три помечены @DataProcessor ✅
 │         │
 │         ├─ executor.execute → [pool-1-thread-1] filter(data)
 │         │                          └─ Stream: .filter(...) → результат в results
 │         │
 │         ├─ executor.execute → [pool-1-thread-2] transform(data)
 │         │                          └─ Stream: .map(...) → результат в results
 │         │
 │         ├─ executor.execute → [pool-1-thread-3] aggregate(data)
 │         │                          └─ Stream: .distinct().sorted() → результат в results
 │         │
 │         │    ↑ три потока работают параллельно, пишут в CopyOnWriteArrayList
 │         │
 │         ├─ executor.shutdown()          ← новых задач нет
 │         └─ executor.awaitTermination()  ← ждём все потоки
 │
 └─ 5. saveData("output.txt")
           results[] → BufferedWriter → output.txt
```

---

# КАК ЗАПУСТИТЬ

```bash
# 1. Убедитесь, что в папке есть файл input.txt с текстом

# 2. Скомпилировать все файлы сразу:
javac *.java

# 3. Запустить:
java Main

# 4. Посмотреть результат:
cat output.txt       # Linux / Mac
type output.txt      # Windows
```

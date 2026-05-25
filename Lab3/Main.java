import java.util.HashMap;

// Класс, содержащий поля: имя, фамилия, возраст и средний балл
class Student {
    private String firstName;
    private String lastName;
    private int age;
    private double gpa;

    public Student(String firstName, String lastName, int age, double gpa) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gpa = gpa;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (Возраст: " + age + ", Средний балл: " + gpa + ")";
    }
}

public class Main {
    public static void main(String[] args) {

        // Реализация хэш-таблицы для хранения информации о студентах
        HashMap<String, Student> students = new HashMap<>();

        // 1. Операция вставки студента
        students.put("101", new Student("Иван", "Иванов", 20, 4.5));
        students.put("102", new Student("Анна", "Смирнова", 19, 4.9));
        students.put("103", new Student("Петр", "Васильев", 21, 3.8));

        System.out.println("Все студенты добавлены.");

        // 2. Операция поиска студента по номеру зачетки
        String searchId = "102";
        Student foundStudent = students.get(searchId);
        System.out.println("Результат поиска по зачетке " + searchId + ": " + foundStudent);

        // 3. Операция удаления студента по номеру зачетки
        String deleteId = "103";
        students.remove(deleteId);
        System.out.println("Студент с зачеткой " + deleteId + " был удален.");

        // Проверка оставшихся студентов
        System.out.println("Оставшиеся студенты в базе: " + students);
    }
}
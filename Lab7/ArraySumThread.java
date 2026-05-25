public class ArraySumThread {
    public static void main(String[] args) {
        int[] array = {4, 8, 15, 16, 23, 42, 7, 9};

        SumThread thread1 = new SumThread(array, 0, array.length / 2);
        SumThread thread2 = new SumThread(array, array.length / 2, array.length);

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        int totalSum = thread1.getSum() + thread2.getSum();

        System.out.println("Сумма элементов массива: " + totalSum);
    }
}

class SumThread extends Thread {
    private final int[] array;
    private final int start;
    private final int end;
    private int sum;

    public SumThread(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
        this.sum = 0;
    }

    public void run() {
        for (int i = start; i < end; i++) {
            sum += array[i];
        }
    }

    public int getSum() {
        return sum;
    }
}
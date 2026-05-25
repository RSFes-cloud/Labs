public class MatrixMaxThread {
    public static void main(String[] args) {
        int[][] matrix = {
                {4, 8, 15},
                {16, 23, 42},
                {7, 9, 3}
        };

        MaxThread[] threads = new MaxThread[matrix.length];

        for (int i = 0; i < matrix.length; i++) {
            threads[i] = new MaxThread(matrix[i]);
            threads[i].start();
        }

        int max = matrix[0][0];

        for (MaxThread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            if (thread.getMax() > max) {
                max = thread.getMax();
            }
        }

        System.out.println("Наибольший элемент матрицы: " + max);
    }
}

class MaxThread extends Thread {
    private final int[] row;
    private int max;

    public MaxThread(int[] row) {
        this.row = row;
        this.max = row[0];
    }

    public void run() {
        for (int value : row) {
            if (value > max) {
                max = value;
            }
        }
    }

    public int getMax() {
        return max;
    }
}
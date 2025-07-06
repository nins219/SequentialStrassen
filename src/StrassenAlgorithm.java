
import java.sql.SQLOutput;
import java.util.Random;
import java.util.Scanner;

public class StrassenAlgorithm {

        // Generate a random matrix
        public static int[][] generateMatrix(int n) {
            Random random = new Random();
            int[][] matrix = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    matrix[i][j] = random.nextInt(10); // Values between 0 and 9
                }
            }
            return matrix;
        }

        // Add two matrices
        public static int[][] addMatrices(int[][] A, int[][] B) {
            int n = A.length;
            int[][] result = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    result[i][j] = A[i][j] + B[i][j];
                }
            }
            return result;
        }

        // Subtract two matrices
        public static int[][] subMatrices(int[][] A, int[][] B) {
            int n = A.length;
            int[][] result = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    result[i][j] = A[i][j] - B[i][j];
                }
            }
            return result;
        }

        public static int[][] strassen(int[][] A, int[][] B) {
            int n = A.length;
            if (n == 1) {
                int[][] result = new int[1][1];
                result[0][0] = A[0][0] * B[0][0];
                return result;
            }

            // Divide matrices into quadrants
            int newSize = n / 2;
            int[][] A11 = new int[newSize][newSize];
            int[][] A12 = new int[newSize][newSize];
            int[][] A21 = new int[newSize][newSize];
            int[][] A22 = new int[newSize][newSize];

            int[][] B11 = new int[newSize][newSize];
            int[][] B12 = new int[newSize][newSize];
            int[][] B21 = new int[newSize][newSize];
            int[][] B22 = new int[newSize][newSize];

            // Populate quadrants
            for (int i = 0; i < newSize; i++) {
                for (int j = 0; j < newSize; j++) {
                    A11[i][j] = A[i][j];
                    A12[i][j] = A[i][j + newSize];
                    A21[i][j] = A[i + newSize][j];
                    A22[i][j] = A[i + newSize][j + newSize];

                    B11[i][j] = B[i][j];
                    B12[i][j] = B[i][j + newSize];
                    B21[i][j] = B[i + newSize][j];
                    B22[i][j] = B[i + newSize][j + newSize];
                }
            }

            // Calculate M1 to M7
            int[][] M1 = strassen(addMatrices(A11, A22), addMatrices(B11, B22));
            int[][] M2 = strassen(addMatrices(A21, A22), B11);
            int[][] M3 = strassen(A11, subMatrices(B12, B22));
            int[][] M4 = strassen(A22, subMatrices(B21, B11));
            int[][] M5 = strassen(addMatrices(A11, A12), B22);
            int[][] M6 = strassen(subMatrices(A21, A11), addMatrices(B11, B12));
            int[][] M7 = strassen(subMatrices(A12, A22), addMatrices(B21, B22));

            // Combine results into C
            int[][] C11 = addMatrices(subMatrices(addMatrices(M1, M4), M5), M7);
            int[][] C12 = addMatrices(M3, M5);
            int[][] C21 = addMatrices(M2, M4);
            int[][] C22 = addMatrices(subMatrices(addMatrices(M1, M3), M2), M6);

            // Combine quadrants into one result matrix
            int[][] C = new int[n][n];
            for (int i = 0; i < newSize; i++) {
                for (int j = 0; j < newSize; j++) {
                    C[i][j] = C11[i][j];
                    C[i][j + newSize] = C12[i][j];
                    C[i + newSize][j] = C21[i][j];
                    C[i + newSize][j + newSize] = C22[i][j];
                }
            }

            return C;
        }

        // Adjust size to the next power of 2
        public static int nextPowerOfTwo(int n) {
            int power = 1;
            while (power < n) {
                power *= 2;
            }
            return power;
        }

    public static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int val : row) {
                System.out.printf("%4d", val);
            }
            System.out.println();
        }
    }

        public static void main(String[] args) {
            Scanner scan  = new Scanner(System.in);
            System.out.println("Enter matrix size:");
            int n = scan.nextInt();

            int adjustSize = nextPowerOfTwo(n);

            int[][] A = generateMatrix(adjustSize);
            int[][] B = generateMatrix(adjustSize);

            long startTime = System.nanoTime();
            int[][] C = strassen(A, B);
            long endTime = System.nanoTime();

            System.out.println("Execution Time: " + (endTime - startTime) / 1e6 + " ms");

            printMatrix(C);
            // Optional: print the matrices (A, B, and C)
        }
    }
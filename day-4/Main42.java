import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main42 {
    public static void main(String... args) {
        char[][] matrix = new char[140][140];
        
        try (BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream("day-4/input4.txt")))) {
            String line;
            int i = 0;
            while((line = bf.readLine()) != null) {
                matrix[i] = line.toCharArray();
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int count = 0;
        for(int row = 0; row < matrix.length; row++) {
            for(int column = 0; column < matrix[row].length; column++) {
                // Everything starts with an A
                if (findChar(matrix, row, column, 'A')) {
                    if (findChar(matrix, row, column, -1, -1, 'M') && findChar(matrix, row, column, 1, 1, 'S') || findChar(matrix, row, column, -1, -1, 'S') && findChar(matrix, row, column, 1, 1, 'M')) {
                        if (findChar(matrix, row, column, -1, 1, 'M') && findChar(matrix, row, column, 1, -1, 'S') || findChar(matrix, row, column, -1, 1, 'S') && findChar(matrix, row, column, 1, -1, 'M')) {
                            count++;
                        }
                    }
                }
            }
        }

        System.out.println("Count: " + count);
    }

    private static boolean findChar(char[][] matrix, int row, int column, char target) {
        return findChar(matrix, row, column, 0, 0, target);
    }

    private static boolean findChar(char[][] matrix, int row, int column, int deltaR, int deltaC, char target) {
        try {
            int newR = row + deltaR;
            int newC = column + deltaC;

            // Base case: wrong char
            if (matrix[newR][newC] != target) {
                return false;
            }

            return true;

        } catch (ArrayIndexOutOfBoundsException e) {
            // Base case: out of matrix
            return false;
        }
    }
}
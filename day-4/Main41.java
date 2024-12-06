import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

public class Main41 {
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

        // Everything starts with an X
        char target = 'X';

        int count = 0;
        for(int row = 0; row < matrix.length; row++) {
            for(int column = 0; column < matrix[row].length; column++) {
                if(findChar(matrix, row, column, -1, 0, target)) { // N
                    count++;
                }
                if(findChar(matrix, row, column, -1, 1, target)) { // NE
                    count++;
                }
                if(findChar(matrix, row, column, 0, 1, target)) { // E
                    count++;
                }
                if(findChar(matrix, row, column, 1, 1, target)) { // SE
                    count++;
                }
                if(findChar(matrix, row, column, 1, 0, target)) { // S
                    count++;
                }
                if(findChar(matrix, row, column, 1, -1, target)) { // SW
                    count++;
                }
                if(findChar(matrix, row, column, 0, -1, target)) { // W
                    count++;
                }
                if(findChar(matrix, row, column, -1, -1, target)) { // NW
                    count++;
                }
            }
        }

        System.out.println("Count: " + count);
    }

    private static boolean findChar(char[][] matrix, int row, int column, int deltaR, int deltaC, char target) {
        try {
            // Base case: wrong char
            if (matrix[row][column] != target) {
                return false;
            }

            // Base case: S of XMAS
            if (target == 'S') {
                return true;
            }

            // Base case: no XMAS
            Optional<Character> nextTarget = nextChar(target);
            if (nextTarget.isEmpty()) {
                return false;
            }

            // Do recursion
            int newR = row + deltaR;
            int newC = column + deltaC;
            return findChar(matrix, newR, newC, deltaR, deltaC, nextTarget.get());
        
        } catch (ArrayIndexOutOfBoundsException e) {
            // Base case: out of matrix
            return false;
        }
    }

    private static Optional<Character> nextChar(char c) {
        return Optional.ofNullable(switch (c) {
            case 'X' -> 'M';
            case 'M' -> 'A';
            case 'A' -> 'S';
            default -> null;
        });
    }
}
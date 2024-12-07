import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Main61 {
    public static void main(String... args) {
        char[][] squares = null;
        int x = 0;
        int y = 0;
        
        try (BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream("day-6/input6.txt")))) {
            List<String> lines = bf.lines().toList();
            
            x = lines.get(0).length();
            y = lines.size();
            System.out.println("x = " + x);
            System.out.println("y = " + y);
            squares = new char[x][y];
            for(int i = 0; i < x; i++) {
                squares[i] = lines.get(i).toCharArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Find guard
        Position guard = null;
        outerloop:
        for(int i = 0; i < x; i++) {
            for(int j = 0; j < y; j++) {
                if (isGuard(squares[i][j])) {
                    guard = new Position(i, j);
                    break outerloop;
                }
            }
        }

        // Mark all position of the guard
        boolean onField = true;
        while(onField) {
            try {
                moveGuard(squares, guard);
            } catch(ArrayIndexOutOfBoundsException e) {
                System.out.println("Guard left the field");
                onField = false;
            }
        }

        int count = 0;
        for(int i = 0; i < x; i++) {
            for(int j = 0; j < y; j++) {
                if(squares[i][j] == 'X') {
                    count++;
                }
            }
        }

        System.out.println("Count: " + count);
    }


    private static boolean isGuard(char c) {
        return c == '<' || c == '^' || c == '>' || c == 'v';
    }

    private static boolean isObstacle(char c) {
        return c == '#';
    }

    private static void moveGuard(char[][] squares, Position position) {
        // Mark current position as visited
        Increment increment = getIncrement(squares[position.x][position.y]);

        Position nextPosition = new Position(position.x + increment.deltaX, position.y + increment.deltaY);

        try {
            // Next step is free
            if(!isObstacle(squares[nextPosition.x][nextPosition.y])) {
                char guard = squares[position.x][position.y];
                // Mark current position as visited
                squares[position.x][position.y] = 'X';
                // Move guard
                squares[nextPosition.x][nextPosition.y] = guard;
                // Recursion
                moveGuard(squares, nextPosition);
                return;
            }

            // Next step is occupied
            if(isObstacle(squares[nextPosition.x][nextPosition.y])) {
                System.out.println("Found obstacle");
                // Turn right
                char guard = squares[position.x][position.y];
                squares[position.x][position.y] = turnRight(guard);
                // Recursion
                moveGuard(squares, position);
            }
        } catch(ArrayIndexOutOfBoundsException e) {
            // Base case
            // Mark current position as visited
            squares[position.x][position.y] = 'X';

            throw e;
        }
    }

    private static Increment getIncrement(char c) {
        return switch(c) {
            case '<' -> new Increment(0, -1);
            case '^' -> new Increment(-1, 0);
            case '>' -> new Increment(0, 1);
            case 'v' -> new Increment(1, 0);
            default -> new Increment(0, 0);
        };
    }

    private static char turnRight(char c) {
        return switch(c) {
            case '<' -> '^';
            case '^' -> '>';
            case '>' -> 'v';
            case 'v' -> '<';
            default -> c;
        };
    }

    private static class Position {
        int x;
        int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static class Increment {
        int deltaX;
        int deltaY;

        public Increment(int deltaX, int deltaY) {
            this.deltaX = deltaX;
            this.deltaY = deltaY;
        }
    }
}
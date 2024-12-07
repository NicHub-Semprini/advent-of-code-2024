import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Main62 {
    public static void main(String... args) {
        char[][] originalSquares = null;
        int x = 0;
        int y = 0;
        
        try (BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream("day-6/input6.txt")))) {
            List<String> lines = bf.lines().toList();
            
            x = lines.get(0).length();
            y = lines.size();
            System.out.println("x = " + x);
            System.out.println("y = " + y);
            originalSquares = new char[x][y];
            for(int i = 0; i < x; i++) {
                originalSquares[i] = lines.get(i).toCharArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Find guard
        char[][] squares = clone(originalSquares);
        Position guard = null;
        outerloop:
        for(int i = 0; i < x; i++) {
            for(int j = 0; j < y; j++) {
                if (isGuard(squares[i][j])) {
                    guard = new Position(i, j, squares[i][j]);
                    break outerloop;
                }
            }
        }

        // Mark all position of the guard
        Set<Position> uniquePositions = new HashSet<>();
        boolean onField = true;
        Position position = guard;
        while(onField) {
            try {
                // Mark current position as visited
                Increment increment = getIncrement(squares[position.x][position.y]);
                uniquePositions.add(new Position(position.x, position.y, squares[position.x][position.y]));
                Position nextPosition = new Position(position.x + increment.deltaX, position.y + increment.deltaY);

                // Next step is free
                if(!isObstacle(squares[nextPosition.x][nextPosition.y])) {
                    char guardC = squares[position.x][position.y];
                    // Mark current position as visited
                    squares[position.x][position.y] = 'X';
                    // Move guard
                    squares[nextPosition.x][nextPosition.y] = guardC;
                    position = nextPosition;
                    
                    continue;
                }

                // Next step is occupied
                if(isObstacle(squares[nextPosition.x][nextPosition.y])) {
                    // Turn right
                    char guardC = squares[position.x][position.y];
                    squares[position.x][position.y] = turnRight(guardC);
                    
                    continue;
                }
            } catch(ArrayIndexOutOfBoundsException e) {
                System.out.println("Guard left the field");
                onField = false;
            }
        }

        // Remove initial position
        uniquePositions = removeSpawn(uniquePositions, guard);
        System.out.println("Removed first position: " + guard.x + ", " + guard.y);

        // Add obstacle and check if loop
        Set<Position> obstaclePositions = new HashSet<>();
        for(Position p : uniquePositions) {
            squares = clone(originalSquares);
            squares[p.x][p.y] = 'O';
            Set<Position> newUniquePositions = new HashSet<>();
            position = guard;
            boolean canRun = true;
            while(canRun) {
                try {
                    // Mark current position as visited
                    Increment increment = getIncrement(squares[position.x][position.y]);
                    boolean added = newUniquePositions.add(new Position(position.x, position.y, squares[position.x][position.y]));
                    if(!added) {
                        throw new LoopException();
                    }

                    Position nextPosition = new Position(position.x + increment.deltaX, position.y + increment.deltaY);

                    // Next step is free
                    if(!isObstacle(squares[nextPosition.x][nextPosition.y])) {
                        char guardC = squares[position.x][position.y];
                        // Mark current position as visited
                        squares[position.x][position.y] = 'X';
                        // Move guard
                        squares[nextPosition.x][nextPosition.y] = guardC;
                        position = nextPosition;
                        
                        continue;
                    }

                    // Next step is occupied
                    if(isObstacle(squares[nextPosition.x][nextPosition.y])) {
                        // Turn right
                        char guardC = squares[position.x][position.y];
                        squares[position.x][position.y] = turnRight(guardC);
                        
                        continue;
                    }
                } catch(LoopException e) {
                    System.out.println("Loop found: " + p.x + ", " + p.y);
                    obstaclePositions.add(new Position(p.x, p.y, 'O'));
                    canRun = false;
                } catch(ArrayIndexOutOfBoundsException e) {
                    canRun = false;
                }
            }
        }

        for(Position obstacle : obstaclePositions) {
            squares = clone(originalSquares);
            squares[obstacle.x][obstacle.y] = obstacle.c;
        }

        int count = obstaclePositions.size();

        System.out.println("Count: " + count);
    }

    private static Set<Position> removeSpawn(Set<Position> positions, Position spawn) {
        // Remove spawn position regardless for the direction
        return positions.stream().filter(p -> !(p.x == spawn.x && p.y == spawn.y)).collect(Collectors.toSet());
    }

    private static char[][] clone(char[][] original) {
        char[][] copy = new char[original.length][original[0].length];
        for(int i = 0; i < copy.length; i++) {
            for(int j = 0; j < copy[0].length; j++) {
                copy[i][j] = original[i][j];
            }
        }
        
        return copy;
    }

    private static boolean isGuard(char c) {
        return c == '<' || c == '^' || c == '>' || c == 'v';
    }

    private static boolean isObstacle(char c) {
        return c == '#' || c == 'O';
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
        Character c;

        public Position(int x, int y) {
            this(x, y, null);
        }

        public Position(int x, int y, Character c) {
            this.x = x;
            this.y = y;
            this.c = c;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Position p) {
                return p.x == this.x && p.y == this.y && p.c == this.c;
            }
            
            return false;
        }

        @Override
        public int hashCode() {
            return 37 * 8 + (this.x + this.y + this.c);
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

    private static class LoopException extends RuntimeException {
        public LoopException() {
            super();
        }
    }
}
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main81 {
    public static void main(String... args) {
        char[][] map = new char[50][50];
        Map<Character, List<Position>> antennas = new HashMap<>();
        Set<Position> antinodes = new HashSet<>();

        try (BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream("day-8/input8.txt")))) {
            int x = 0;
            String line;
            while((line = bf.readLine()) != null) {
                map[x] = line.toCharArray();
                for(int y = 0; y < map[x].length; y++) {
                    char pos = map[x][y];
                    if (pos != '.') {
                        List<Position> positions = antennas.getOrDefault(pos, new ArrayList<>());
                        positions.add(new Position(x, y));
                        antennas.put(pos, positions);
                    }
                }
                x++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        printMap(map, new HashSet<>());

        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[x].length; y++) {
                char c = map[x][y];
                Position pos = new Position(x, y);

                for(char antenna : antennas.keySet()) {
                    // Calculate distances and slopes
                    Map<Position, Double> slopes = new HashMap<>();
                    Map<Position, Double> distances = new HashMap<>();
                    antennas.get(antenna).forEach(p -> {
                        slopes.put(p, slope(pos, p));
                        distances.put(p, distance(pos, p));
                    });

                    while (!slopes.isEmpty()) {
                        Position p = slopes.keySet().iterator().next();
                        double slope = slopes.remove(p);
                        double distance = distances.get(p);

                        // Look for same slopes
                        slopes.entrySet().stream().filter(e -> e.getValue().equals(slope)).forEach(e -> {
                            // Look for double distance
                            double d = distances.get(e.getKey());
                            if (distance / d == 2 || d / distance == 2) {
//                                System.out.println("Antinodo! a1: " + p + " - a2: " + e.getKey());
//                                System.out.println("pos: " + pos + " - pendenza: " + slope + " - d1: " + distance + " - d2: " + d);
                                antinodes.add(pos);
                            }
                        });
                    }
                }
            }
        }

//        System.out.println();
//        printMap(map, antinodes);

        System.out.println("Antinodes: " + antinodes.size());
    }

    public static double slope(Position p1, Position p2) {
        return slope(p1.x, p1.y, p2.x, p2.y);
    }
    
    public static double slope(int x1, int y1, int x2, int y2) {
        if (x2 == x1) {
            return Integer.MAX_VALUE;
        }

        double deltaX = x2 - x1;
        double deltaY = y2 - y1;

        return deltaY / deltaX;
    }

    public static double distance(Position p1, Position p2) {
        return distance(p1.x, p1.y, p2.x, p2.y);
    }
    
    public static double distance(int x1, int y1, int x2, int y2) {
        double deltaX = Math.pow(x2 - x1, 2);
        double deltaY = Math.pow(y2 - y1, 2);

        return Math.sqrt(deltaX + deltaY);
    }

    public static void printMap(char[][] map, Set<Position> antinodes) {
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[x].length; y++) {
                if (map[x][y] == '.' && antinodes.contains(new Position(x, y))) {
                    System.out.print('#');
                } else {
                    System.out.print(map[x][y]);
                }
            }
            System.out.println();
        }
    }

    public static class Position {
        int x;
        int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return x == position.x && y == position.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "(" + (x+1) + ", " + (y+1) + ")";
        }
    }
}
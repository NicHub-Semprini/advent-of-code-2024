import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main102
{
    public static void main(String... args)
    {
        String fileName = "day-10/input10.txt";
        int dim = 55;

        // Read input
        int[][] map = new int[dim][dim];
        try (BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(fileName))))
        {
            String line;
            int i = 0;
            while ((line = bf.readLine()) != null)
            {
                for (int j = 0; j < line.length(); j++)
                {
                    map[i][j] = Integer.parseInt("" + line.charAt(j));
                }
                i++;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        // Explore the map
        int total = 0;
        for (int x = 0; x < dim; x++)
        {
            for (int y = 0; y < dim; y++)
            {

                // Valid trails start with '0'
                if (map[x][y] != 0)
                {
                    continue;
                }

                int[][] mapTmp = copyMap(map);
                List<Position> trails = validTrails(mapTmp, x, y, new ArrayList<>());
                total = total + trails.size();
            }
        }

        System.out.println("Total trails: " + total);
    }

    public static int[][] copyMap(int[][] source)
    {
        int[][] copy = new int[source.length][source[0].length];

        for (int i = 0; i < copy.length; i++)
        {
            for (int j = 0; j < copy[i].length; j++)
            {
                copy[i][j] = source[i][j];
            }
        }

        return copy;
    }

    public static List<Position> validTrails(int[][] map, int x, int y, List<Position> trails)
    {
        int newX;
        int newY;

        // Save current high
        int current = map[x][y];

        // Base case: '9'
        if (current == 9)
        {
            trails.add(new Position(x, y));
            return trails;
        }

        // N
        {
            newX = x - 1;
            newY = y;
            if (isInMap(newX, map[0].length) && map[newX][newY] == current + 1)
            {
                int[][] mapTmp = copyMap(map);
                mapTmp[x][y] = -1;
                trails = validTrails(mapTmp, newX, newY, trails);
            }
        }
        // E
        {
            newX = x;
            newY = y + 1;
            if (isInMap(newY, map[0].length) && map[newX][newY] == current + 1)
            {
                int[][] mapTmp = copyMap(map);
                mapTmp[x][y] = -1;
                trails = validTrails(mapTmp, newX, newY, trails);
            }
        }
        // S
        {
            newX = x + 1;
            newY = y;
            if (isInMap(newX, map[0].length) && map[newX][newY] == current + 1)
            {
                int[][] mapTmp = copyMap(map);
                mapTmp[x][y] = -1;
                trails = validTrails(mapTmp, newX, newY, trails);
            }
        }
        // W
        {
            newX = x;
            newY = y - 1;
            if (isInMap(newY, map[0].length) && map[newX][newY] == current + 1)
            {
                int[][] mapTmp = copyMap(map);
                mapTmp[x][y] = -1;
                trails = validTrails(mapTmp, newX, newY, trails);
            }
        }

        return trails;
    }

    public static boolean isInMap(int p, int dim)
    {
        return p >= 0 && p < dim;
    }

    public static class Position
    {
        int x;
        int y;

        public Position(int x, int y)
        {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Position position = (Position) o;
            return x == position.x && y == position.y;
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(x, y);
        }
    }
}

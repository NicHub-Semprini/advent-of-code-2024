import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Main111
{
    public static void main(String... args)
    {
        String fileName = "day-11/input11.txt";

        // Read input
        List<Long> stones = new ArrayList<>();
        try (BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(fileName))))
        {
            StringTokenizer st = new StringTokenizer(bf.readLine());
            while (st.hasMoreTokens())
            {
                stones.add(Long.parseLong(st.nextToken()));
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        // Bink 25 times
        for (int i = 0; i < 25; i++)
        {

            // Apply rules for one stone at time
            List<Long> newStones = new ArrayList<>();
            for (int j = 0; j < stones.size(); j++)
            {
                long stone = stones.get(j);

                if (stone == 0L)
                { // Rule 1: 0 -> 1
                    newStones.add(1L);
                }
                else if (Long.toString(stone).length() % 2L == 0)
                { // Rule 2: split
                    String stoneStr = Long.toString(stone);
                    long stone1 = Long.parseLong(stoneStr.substring(0, stoneStr.length() / 2));
                    long stone2 = Long.parseLong(stoneStr.substring(stoneStr.length() / 2));
                    newStones.add(stone1);
                    newStones.add(stone2);
                }
                else
                { // Rule 3: multiply
                    newStones.add(stone * 2024L);
                }
            }
            stones = newStones;
        }

        System.out.println("Stones: " + stones.size());
    }
}

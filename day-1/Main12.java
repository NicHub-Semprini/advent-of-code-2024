import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class Main12 {
    public static void main(String... args) {
        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();

        try (BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream("day-1/input1.txt")))) {
            bf.lines().forEach(line -> {
                StringTokenizer st = new StringTokenizer(line);
                left.add(Integer.parseInt(st.nextToken()));
                right.add(Integer.parseInt(st.nextToken()));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<Integer, Integer> map = new HashMap<>();
        for(int i = 0; i < right.size(); i++) {
            int x = right.get(i);
            int value = map.getOrDefault(x, 0);
            map.put(x, value + 1);
        }

        int similarity = 0;
        for(int i = 0; i < left.size(); i++) {
            
            int x = left.get(i);
            int count = map.getOrDefault(x, 0);

            // It's OK to be paranoic
            if (count < 0) {
                System.out.println("Missing value: " + x);
            }

            similarity = similarity + x * count;
        }

        System.out.println("Similarity: " + similarity);
    }
}
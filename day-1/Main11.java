import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

public class Main11 {
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

        left.sort(Comparator.naturalOrder());
        right.sort(Comparator.naturalOrder());

        int distance = 0;
        for(int i = 0; i < left.size(); i++) {
            
            int delta = right.get(i) - left.get(i);

            // It's OK to be paranoic
            if (delta < 0) {
                System.out.println("Negative delta: " + delta);
                delta = Math.abs(delta);
            }

            distance = distance + delta;
        }

        System.out.println("Distance: " + distance);
    }
}
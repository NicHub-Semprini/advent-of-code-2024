import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main21 {
    public static void main(String... args) {
        List<Integer[]> reports = new ArrayList<>();

        try (BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream("day-2/input2.txt")))) {
            bf.lines().forEach(line -> {
                String[] report = line.split(" ");
                Integer[] levels = new Integer[report.length];
                for(int i = 0; i < report.length; i++) {
                    levels[i] = Integer.parseInt(report[i]);
                }

                reports.add(levels);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        int safeReports = 0;
        for (Integer[] levels : reports) {

            // One-Direction check ♪♫
            int direction;
            if (levels.length < 2) {
                System.out.println("Report contains only 1 level: " + Arrays.asList(levels));
                continue;
            } else if (levels[0] == levels[1]) {
                System.out.println("Report is nor descending or ascending: " + Arrays.asList(levels));
                continue;
            } else if (levels[0] < levels[1]) {
                direction = 1;
            } else {
                direction = -1;
            }

            boolean safe = true;
            for(int i = 0; i < levels.length - 1; i++) {
                
                int delta = (levels[i + 1] - levels[i]) * direction;
                
                // Constraints check
                if (!(delta > 0 && delta < 4)) {
                    System.out.println("Report is unsafe: " + Arrays.asList(levels));
                    safe = false;
                    break;
                }
            }

            if (safe) {
                safeReports++;
            }
        }

        System.out.println("Safe reports: " + safeReports);
    }
}
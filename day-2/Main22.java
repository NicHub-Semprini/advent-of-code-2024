import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main22 {
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

            boolean safe = false;
            if (levels.length < 2) {
                System.out.println("Report contains only 1 level: " + Arrays.asList(levels));
            } else {
                
                safe = isSafe(levels, 1) || isSafe(levels, -1);
                
                if(!safe) {
                    System.out.println("Report is unsafe: " + Arrays.asList(levels));

                    for(int i = 0; i < levels.length; i++) {
                        Integer[] newLevels = new Integer[levels.length - 1];
                        for(int j = 0; j < newLevels.length; j++) {
                            if (j < i) {
                                newLevels[j] = levels[j];
                            } else {
                                newLevels[j] = levels[j + 1];
                            }
                        }

                        // Try both directions
                        System.out.println("New try with report: " + Arrays.asList(newLevels));
                        if(isSafe(newLevels, 1) || isSafe(newLevels, -1)) {
                            safe = true;
                            break;
                        }
                    }
                }

            }
            
            if (safe) {
                safeReports++;
            }
        }

        System.out.println("Safe reports: " + safeReports);
    }

    private static boolean isSafe(Integer[] levels, int direction) {
        for(int i = 0; i < levels.length - 1; i++) {
            
            int delta = (levels[i + 1] - levels[i]) * direction;
            
            // Constraints check
            if (!(delta > 0 && delta < 4)) {
                return false;
            }
        }

        return true;
    }
}
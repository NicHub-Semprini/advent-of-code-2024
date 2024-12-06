import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main52 {
    public static void main(String... args) {
        List<Pattern> rules = new ArrayList<>();
        List<String> lines = new ArrayList<>();

        try (BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream("day-5/input5.txt")))) {
            String line;

            // Read rules
            while (!(line = bf.readLine()).isEmpty()) {
                String[] numbers = line.split("\\|");
                Pattern pattern = Pattern.compile("(?<left>" + numbers[1] + ").+(?<right>" + numbers[0] + ")");
                rules.add(pattern);
                System.out.println(line + " -> " + pattern);
            }

            // Read pages
            while ((line = bf.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // First remove the valid lines
        List<String> invalidLines = new ArrayList<>();
        for (String line : lines) {
            for (Pattern rule : rules) {
                Matcher matcher = rule.matcher(line);
                if (matcher.find()) {
                    System.out.println("Invalid line: " + line + " - " + matcher.group());
                    invalidLines.add(line);
                    break;
                }
            }
        }

        // Work on invalid lines
        int sum = 0;
        for (String line : invalidLines) {
            boolean valid;
            do {
                // Start with a positive attitude
                valid = true;
                // Apply all rules, stopping each time to the first not satisfied
                for (Pattern rule : rules) {
                    Matcher matcher = rule.matcher(line);
                    if (matcher.find()) {
                        System.out.println("Invalid line: " + line + " - " + matcher.group());
                        System.out.println("rule: " + rule.pattern());
                        System.out.println("groups: " + matcher.groupCount());
                        String left = matcher.group("left");
                        String right = matcher.group("right");
                        System.out.println("left: " + left);
                        System.out.println("right: " + right);
                        int leftI = line.indexOf(left);
                        int rightI = line.indexOf(right);
                        StringBuilder sb = new StringBuilder();
                        sb.append(line.substring(0, leftI))
                            .append(right)
                            .append(line.substring(leftI + left.length(), rightI)).append(left)
                            .append(line.substring(rightI + right.length(), line.length()));
                        line = sb.toString();
                        valid = false;
                        break;
                    }
                }
            } while (!valid);

            if (valid) {
                String[] pages = line.split(",");
                String middle = pages[pages.length / 2];
                System.out.println("Valid line: " + line + " - " + middle);
                sum = sum + Integer.parseInt(middle);
            }
        }

        System.out.println("Sum: " + sum);
    }
}
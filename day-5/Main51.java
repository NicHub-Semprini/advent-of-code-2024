import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main51 {
    public static void main(String... args) {
        List<Pattern> rules = new ArrayList<>();
        List<String> lines = new ArrayList<>();
        
        try (BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream("day-5/input5.txt")))) {
            String line;
            
            // Read rules
            while(!(line = bf.readLine()).isEmpty()) {
                String[] numbers = line.split("\\|");
                Pattern pattern = Pattern.compile(numbers[1] + ".+" + numbers[0]);
                rules.add(pattern);
                System.out.println(line + " -> " + pattern);
            }
            
            // Read pages
            while((line = bf.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int sum = 0;
        for(String line : lines) {
            boolean valid = true;
            for (Pattern rule : rules) {
                Matcher matcher = rule.matcher(line);
                if (matcher.find()) {
                    System.out.println("Invalid line: " + line + " - " + matcher.group());
                    valid = false;
                    break;
                }
            }

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
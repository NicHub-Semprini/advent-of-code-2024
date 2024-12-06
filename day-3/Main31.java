import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main31 {
    public static void main(String... args) {
        List<MatchResult> records = new ArrayList<>();

        try (BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream("day-3/input3.txt")))) {
            bf.lines().forEach(line -> {
                Pattern pattern = Pattern.compile("mul\\(([1-9][0-9]{0,2}),([1-9][0-9]{0,2})\\)");
                Matcher matcher = pattern.matcher(line);
                matcher.results().forEach(records::add);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        long sum = 0;
        for (MatchResult match : records) {
            int x = Integer.parseInt(match.group(1));
            int y = Integer.parseInt(match.group(2));
            
            sum = sum + x * y;
        }

        System.out.println("Sum: " + sum);
    }
}
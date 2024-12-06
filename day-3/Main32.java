import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main32 {
    public static void main(String... args) {
        List<MatchResult> records = new ArrayList<>();

        Pattern commandPattern = Pattern.compile("(mul\\([1-9][0-9]{0,2},[1-9][0-9]{0,2}\\)|do\\(\\)|don't\\(\\))");
        Pattern multPattern = Pattern.compile("mul\\(([1-9][0-9]{0,2}),([1-9][0-9]{0,2})\\)");
        Pattern doPattern = Pattern.compile("do\\(\\)");
        Pattern dontPattern = Pattern.compile("don't\\(\\)");

        try (BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream("day-3/input3.txt")))) {
            bf.lines().forEach(line -> {
                Matcher matcher = commandPattern.matcher(line);
                matcher.results().forEach(records::add);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        long sum = 0;
        boolean enable = true;
        for (MatchResult match : records) {
            String sequence = match.group();
            Matcher matcher = multPattern.matcher(sequence);
            if (matcher.find() && enable) {
                int x = Integer.parseInt(matcher.group(1));
                int y = Integer.parseInt(matcher.group(2));
                sum = sum + x * y;
            } else if (doPattern.matcher(sequence).find()) {
                enable = true;
            } else if (dontPattern.matcher(sequence).find()) {
                enable = false;
            }
        }

        System.out.println("Sum: " + sum);
    }
}
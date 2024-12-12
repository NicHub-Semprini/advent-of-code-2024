import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main72 {
    public static void main(String... args) {
        Map<Long, List<Long>> inputMap = new HashMap<>();

        try (BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream("day-7/input7.txt")))) {
            bf.lines().forEach(line -> {
                List<Long> numbers = new ArrayList<>();
                StringTokenizer st = new StringTokenizer(line);
                inputMap.put(Long.parseLong(st.nextToken(":").trim()), numbers);
                st = new StringTokenizer(st.nextToken());
                while (st.hasMoreTokens()) {
                    numbers.add(Long.parseLong(st.nextToken().trim()));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        long sum = 0;
        Map<Long, List<Operation>> solutionMap = new HashMap<>();
        for (Map.Entry<Long, List<Long>> entry : inputMap.entrySet()) {
            Long expected = entry.getKey();
            List<Long> inputs = entry.getValue();
            List<Operation> operations =
                    solveEquation(expected, inputs, inputs.get(0), inputs.subList(1, inputs.size()), new ArrayList<>());

            // Check if a solution was found
            if (!operations.isEmpty()) {
                //                System.out.println(expected + " - solved equations!");
                //                printEquation(expected, inputs, operations);
                solutionMap.put(expected, operations);
                sum = sum + expected;
            }
        }
        System.out.println();
        System.out.println("Sum: " + sum);
        System.out.println("Solved equations: " + solutionMap.size() + " / " + inputMap.size());
    }

    public static List<Operation> solveEquation(
            long expected, List<Long> originalInputs, long total, List<Long> inputs, List<Operation> operations) {
        // Base case: no inputs
        if (inputs.isEmpty()) {
            if (total == expected) { // Solution found
                return operations;
            } else { // Solution not found
                //                System.out.println(expected + " - no more inputs and total != expected: " + total);
                //                System.out.print(expected + " - ");
                //                printEquation(total, originalInputs, operations);
                return List.of();
            }
        }

        // Base case: capped
        if (total >= expected && inputs.stream().anyMatch(i -> i != 1L)) {
            //            System.out.println(expected + " - there are still inputs but total >= expected: " + total);
            //            System.out.print(expected + " - ");
            //            printEquation(total, originalInputs, operations);
            return List.of();
        }

        // Continue solving
        long x = inputs.get(0);
        for (Operation op : Operation.values()) {
            // Create temporary copy for operations
            List<Operation> tempOps = new ArrayList<>(operations);
            tempOps.add(op);
            tempOps = solveEquation(
                    expected, originalInputs, op.apply(total, x), inputs.subList(1, inputs.size()), tempOps);

            // Base case: solution found
            if (!tempOps.isEmpty()) {
                return tempOps;
            }

            // Try next operation
        }

        // Base case: no more operations to try
        //        System.out.println(expected + " - no more operations");
        return List.of();
    }

    public static void printEquation(long total, List<Long> inputs, List<Operation> operations) {
        System.out.print(total + " = ");
        int i = 0;
        for (i = 0; i < operations.size(); i++) {
            System.out.print(inputs.get(i) + " " + operations.get(i) + " ");
        }
        System.out.println(inputs.get(i));
    }

    public enum Operation {
        SUM("+"),
        MUL("*"),
        CONCAT("||"),
        ;

        public final String symbol;

        Operation(String symbol) {
            this.symbol = symbol;
        }

        @Override
        public String toString() {
            return this.symbol;
        }

        public Long apply(Long x, Long y) {
            return switch (this) {
                case SUM -> x + y;
                case MUL -> x * y;
                case CONCAT -> Long.parseLong(x.toString() + y.toString());
            };
        }
    }
}
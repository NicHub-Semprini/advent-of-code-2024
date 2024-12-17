import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main92 {
    public static void main(String... args) {
        String fileName = "day-9/input9.txt";

        // Read input
        int blocks = 0;
        try (BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {
            int n;
            while((n = bf.read()) != 10) { // Read until '\n'
                blocks = blocks + (n - 48); // '0' is 48 as int
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }

        // Expand compact memory representation
        int[] expanded = new int[blocks];
        int fileId = 0;
        try (BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {
            int i = 0;
            int n;
            int pos = 0;
            while((n = bf.read()) != 10) {
                int j;
                for (j = 0; j < n - 48; j++) {
                    if (pos % 2 == 0) {
                        expanded[i + j] = fileId;
                    } else {
                        expanded[i + j] = -1;
                    }
                }
                if (pos % 2 != 0) {
                    fileId++;
                }
                i = i + j;
                pos++;
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }

        // Move blocks
        for (int i = fileId; i >= 0; i--) {
            int lastFileBlock = getIndexOfLastFileBlock(expanded, i);

            // Never trust
            if (lastFileBlock == -1) {
                continue;
            }

            int blockStarting = getStartingIndexOfBlock(expanded, lastFileBlock);
            int blockSize = lastFileBlock - blockStarting + 1;

            int blankStarting = findWhiteSpace(expanded, blockStarting, blockSize);

            // Some files cannot be moved
            if (blankStarting == -1) {
                continue;
            }

            for (int j = 0; j < blockSize; j++) {
                // Replace blank with file
                expanded[blankStarting + j] = i;
                // Replace file with blank
                expanded[blockStarting + j] = -1;
            }
        }

        // Compute checksum
        long checksum = computeChecksum(expanded);
        System.out.println("Checksum: " + checksum);
    }

    public static int getIndexOfLastFileBlock(int[] input) {
        for (int i = input.length - 1; i >= 0; i--) {
            if (input[i] != -1) {
                return i;
            }
        }

        return -1;
    }

    public static int getIndexOfLastFileBlock(int[] input, int target) {
        for (int i = input.length - 1; i >= 0; i--) {
            if (input[i] == target) {
                return i;
            }
        }

        return -1;
    }

    public static int getStartingIndexOfBlock(int[] input, int lastIndexOfBlock) {
        int target = input[lastIndexOfBlock];

        for (int i = lastIndexOfBlock -1; i >= 0; i--) {
            if (input[i] != target) {
                return i + 1;
            }
        }
         return -1;
    }

    public static int findWhiteSpace(int[] input, int maxIndex, int size) {
        int index = -1;
        int count = 0;

        // Scan input from the beginning
        for (int i = 0; i < maxIndex; i++) {
            if (input[i] == -1) {
                // Keep track of blank size
                count++;
                if (index == -1) { // Blank region started
                    index = i;
                }
            } else { // Reset region
                index = -1;
                count = 0;
            }

            // Blank region is large enough, exit
            if (count == size) {
                return index;
            }
        }

        return -1;
    }

    public static long computeChecksum(int[] input) {
        long checksum = 0;
        int lastFileBlock = getIndexOfLastFileBlock(input);
        for (int i = 0; i <= lastFileBlock; i++) {
            // Skip blanks
            if (input[i] != -1) {
                checksum = checksum + i * input[i];
            }
        }

        return checksum;
    }
}

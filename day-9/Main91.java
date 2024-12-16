import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main91 {
    public static void main(String... args) {
        String fileName = "day-9/input-small.txt";

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
        char[] expanded = new char[blocks];
        try (BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {
            int i = 0;
            int n;
            int pos = 0;
            int fileId = 48;
            while((n = bf.read()) != 10) {
                int j;
                for (j = 0; j < n - 48; j++) {
                    if (pos % 2 == 0) {
                        expanded[i + j] = (char) fileId;
                    } else {
                        expanded[i + j] = '.';
                    }
                }
                if (pos % 2 == 0) {
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
        for (int i = 0; i < expanded.length; i++) {

            int lastFileBlock = getIndexOfLastFileBlock(expanded);

            // All blocks have been compacted, stop looping
            if (lastFileBlock <= i) {
                break;
            }

            // Replace only empty memory
            if (expanded[i] == '.') {
                expanded[i] = expanded[lastFileBlock];
                expanded[lastFileBlock] = '.';
            }
        }

        // Compute checksum
        long checksum = computeChecksum(expanded);
        System.out.println("Checksum: " + checksum);
    }

    public static int getIndexOfLastFileBlock(char[] input) {
        for (int i = input.length - 1; i >= 0; i--) {
            if (input[i] != '.') {
                return i;
            }
        }

        return -1;
    }

    public static long computeChecksum(char[] input) {
        long checksum = 0;
        int lastFileBlock = getIndexOfLastFileBlock(input);
        for (int i = 0; i <= lastFileBlock; i++) {
            checksum = checksum + i * (input[i] - 48L);
        }

        return checksum;
    }

    public static class File {
        char fileId;
        int size;
        int startingBlock;

        public File(char fileId, int size, int startingBlock) {
            this.fileId = fileId;
            this.size = size;
            this.startingBlock = startingBlock;
        }

        public char getFileId() {
            return this.fileId;
        }
    }
}

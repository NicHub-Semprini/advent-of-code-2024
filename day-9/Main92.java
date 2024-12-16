import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main92 {
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
        List<File> files = new ArrayList<>(); // fileId in descending order
        Map<Integer, Integer> emptySpaceSizeMap = new TreeMap<>(Comparator.naturalOrder()); // empty space in ascending order
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
                    files.add(new File((char) fileId, n - 48, i));
                    fileId++;
                } else {
                    if (n != 48) {
                        // Take note of empty space starting position and size
                        emptySpaceSizeMap.put(i, n - 48);
                    }
                }
                i = i + j;
                pos++;
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }

        // Sort file by ID descending
        files.sort(Comparator.comparing(File::getFileId).reversed());

        System.out.println(expanded);
        System.out.println(files);
        System.out.println(emptySpaceSizeMap);

        // Move blocks
        for (int i = 0; i < expanded.length; i++) {

            // All files have been moved once, stop looping
            if (files.isEmpty()) {
                break;
            }

            int lastFileBlock = getIndexOfLastFileBlock(expanded);

            // All blocks have been compacted, stop looping
            if (lastFileBlock <= i) {
                break;
            }

            // Replace only empty memory
            if (expanded[i] == '.') {
                int emptySpaceSize = emptySpaceSizeMap.get(i);
                Optional<File> fileOpt = files.stream().filter(f -> f.size <= emptySpaceSize).findFirst();

                // There is enough space for a file
                if (fileOpt.isPresent()) {
                    File file = fileOpt.get();

                    // Move file
                    for (int j = i; j - i < file.size; j++) {
                        expanded[j] = file.fileId;
                    }
                    files.remove(file);

                    // Fill old file position with blanks
                    for (int j = file.startingBlock; j - file.startingBlock < file.size; j++) {
                        expanded[j] = '.';
                    }

                    // Update blanks map
                    emptySpaceSizeMap.remove(i);
                    // Check remaining empty space
                    if (file.size < emptySpaceSize) {
                        int nextEmptyStarting = i + file.size;
                        // Check if the next empty space is behind the corner
                        if (emptySpaceSizeMap.containsKey(nextEmptyStarting + 1)) {
                            // Merge empty spaces
                            int nextEmptySize = emptySpaceSizeMap.remove(nextEmptyStarting + 1);
                            emptySpaceSizeMap.put(nextEmptyStarting, emptySpaceSize - file.size + nextEmptySize);
                        } else {
                            emptySpaceSizeMap.put(i + file.size, emptySpaceSize - file.size);
                        }
                    }
                    // Check old space file
                    if (emptySpaceSizeMap.containsKey(file.startingBlock + file.size + 1)) {
                        // Merge empty spaces
                        int nextEmptySize = emptySpaceSizeMap.remove(file.startingBlock + file.size + 1);
                        emptySpaceSizeMap.put(file.startingBlock, file.size + nextEmptySize);
                    } else {
                        emptySpaceSizeMap.put(file.startingBlock, file.size);
                    }
                }
                System.out.println(expanded);
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

        @Override
        public String toString() {
            return "File{" +
                    "fileId=" + fileId +
                    ", size=" + size +
                    ", startingBlock=" + startingBlock +
                    '}';
        }
    }
}

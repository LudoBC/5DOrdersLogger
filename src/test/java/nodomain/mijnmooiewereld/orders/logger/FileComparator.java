package nodomain.mijnmooiewereld.orders.logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileComparator {

    private static final int BUFFER_SIZE = 8192; // 8KB buffer (optimized for most systems)

    public static boolean haveSameContent(Path path1, Path path2) throws IOException {
        // Step 1: Basic checks (existence and size)
        if (!Files.exists(path1) || !Files.exists(path2)) {
            throw new IOException("One or both files do not exist");
        }

        long size1 = Files.size(path1);
        long size2 = Files.size(path2);
        if (size1 != size2) {
            return false;
        }

        // If files are empty, they are identical
        if (size1 == 0) {
            return true;
        }

        // Step 2: Byte-by-byte comparison
        try (InputStream in1 = new FileInputStream(path1.toFile());
             InputStream in2 = new FileInputStream(path2.toFile())) {

            byte[] buffer1 = new byte[BUFFER_SIZE];
            byte[] buffer2 = new byte[BUFFER_SIZE];

            int bytesRead;
            while ((bytesRead = in1.read(buffer1)) != -1) {
                // Read same number of bytes from second file
                int bytesRead2 = in2.read(buffer2);
                if (bytesRead != bytesRead2) {
                    return false; // Shouldn't happen if sizes match, but safe guard
                }

                // Compare buffer contents
                for (int i = 0; i < bytesRead; i++) {
                    if (buffer1[i] != buffer2[i]) {
                        return false; // Mismatch found
                    }
                }
            }
        }

        return true; // All bytes matched
    }
}

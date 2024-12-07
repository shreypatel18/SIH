package text.extraction.getext.externalAPIS;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class testLoader {

    public byte[] getimage() {
        // Set the path to the local image
        String imagePath = "C:\\testimage.jpg"; // Replace with the actual path

        try {
            // Read the image file into a byte array
            File file = new File(imagePath);
            if (file.exists() && file.isFile()) {
                byte[] imageBytes = new FileInputStream(file).readAllBytes();
                // Print the image hash code
                return imageBytes;
            } else {
                System.err.println("File does not exist at the specified path.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

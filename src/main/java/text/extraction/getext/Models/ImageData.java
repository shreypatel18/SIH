package text.extraction.getext.Models;

// Class to store image data (byte array and file name)
public class ImageData {
    private byte[] imageBytes;
    private String fileName;

    public ImageData(byte[] imageBytes, String fileName) {
        this.imageBytes = imageBytes;
        this.fileName = fileName;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public String getFileName() {
        return fileName;
    }
}

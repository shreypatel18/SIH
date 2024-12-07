package text.extraction.getext.externalAPIS;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import java.util.List;

public class FaceMatcher {


    public static MatchResult detectAndMatch(byte[] photoABytes, List<byte[]> photosListBytes) {
        // Load Haar Cascade for face detection
        String cascadePath = "C:\\Users\\shreykumar\\opencv\\build\\etc\\haarcascades\\haarcascade_frontalface_default.xml";
        CascadeClassifier faceCascade = new CascadeClassifier(cascadePath);
        System.out.println(faceCascade);
        // Decode the byte arrays into images
        Mat photoA = Imgcodecs.imdecode(new MatOfByte(photoABytes), Imgcodecs.IMREAD_COLOR);
        System.out.println(faceCascade);
        System.out.println(photoA);
        Mat faceA = detectFace(photoA, faceCascade);
        System.out.println(faceA);
        if (faceA == null) {
            return  new MatchResult(false, -1);
        }

        System.out.println(photosListBytes.size());
        // Compare with faces in the list

        for (int i = 0; i < photosListBytes.size(); i++) {
            Mat photo = Imgcodecs.imdecode(new MatOfByte(photosListBytes.get(i)), Imgcodecs.IMREAD_COLOR);
            Mat face = detectFace(photo, faceCascade);
            System.out.println(face);
            System.out.println(compareFaces(faceA,face));
            if (face != null && compareFaces(faceA, face)) {
                return new MatchResult(true, i);
            }
        }

        return new MatchResult(false, -1);
    }

    private static Mat detectFace(Mat image, CascadeClassifier faceCascade) {
        Mat gray = new Mat();
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);

        MatOfRect faces = new MatOfRect();
        faceCascade.detectMultiScale(gray, faces, 1.1, 5);

        Rect[] facesArray = faces.toArray();
        if (facesArray.length > 0) {
            return new Mat(gray, facesArray[0]); // Return the first detected face
        }

        return null; // No face detected
    }

    private static boolean compareFaces(Mat faceA, Mat faceB) {
        // Resize faces to the same size for comparison
        Size standardSize = new Size(100, 100);
        Imgproc.resize(faceA, faceA, standardSize);
        Imgproc.resize(faceB, faceB, standardSize);

        // Compute Mean Squared Error (MSE) as a simple similarity measure
        Mat diff = new Mat();
        Core.absdiff(faceA, faceB, diff);
        diff.convertTo(diff, CvType.CV_32F);

        Mat squared = new Mat();
        Core.multiply(diff, diff, squared);

        Scalar sum = Core.sumElems(squared);
        double mse = sum.val[0] / (standardSize.width * standardSize.height);
        System.out.println("mse"+mse);
        // Adjust threshold based on your requirements
        return mse < 1000; // Lower value indicates higher similarity
    }

    // Helper class to store match result
    public static class MatchResult {
        public boolean isMatched;
        public int matchedIndex;

        public MatchResult(boolean isMatched, int matchedIndex) {
            this.isMatched = isMatched;
            this.matchedIndex = matchedIndex;
        }
    }

    public static MatchResult main(byte[] x, List<byte[]> y) {

        System.out.println("Java Library Path: " + System.getProperty("java.library.path"));
        // Correct path to the Haar cascade
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("OpenCV native library loaded successfully.");
        String haarCascadePath = "C:\\Users\\shreykumar\\opencv\\build\\etc\\haarcascades\\haarcascade_frontalface_default.xml";
        System.out.println("Library path: " + System.getProperty("java.library.path"));
        // Initialize the classifier



        CascadeClassifier faceDetector = new CascadeClassifier();

        // Validate if Haar cascade is loaded
        if (faceDetector.load(haarCascadePath)) {
            System.out.println("Haar cascade loaded successfully.");
        } else {
            System.err.println("Failed to load Haar cascade file.");
        }
        // Example usage
        byte[] photoABytes =x; // Byte array for photo A
        List<byte[]> photosListBytes = y; // List of byte arrays for other photos
        MatchResult result = detectAndMatch(photoABytes, photosListBytes);


        if (result.isMatched) {
            System.out.println("Match found at index: " + result.matchedIndex);
            return  result;

        } else {
            System.out.println("No match found.");
        }
        return  null;
    }
}

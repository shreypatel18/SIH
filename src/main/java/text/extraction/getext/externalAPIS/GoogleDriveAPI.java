package text.extraction.getext.externalAPIS;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.api.services.drive.Drive.Builder;

import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import text.extraction.getext.Models.ImageData;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class GoogleDriveAPI {

    private static final String CREDENTIALS_FILE_PATH = "C:\\Users\\shreykumar\\Downloads\\sihproject-443904-06d488e79be8.json";

    public static Drive getDriveService() throws IOException {
        // Load the credentials
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(CREDENTIALS_FILE_PATH))
                .createScoped(Collections.singleton(DriveScopes.DRIVE_READONLY));

        // Set up the Drive service
        return new Builder(
                new com.google.api.client.http.javanet.NetHttpTransport(),
                new com.google.api.client.json.jackson2.JacksonFactory(),
                new HttpCredentialsAdapter(credentials)
        ).setApplicationName("Google Drive API Example").build();
    }

    public static List<ImageData> listImagesAsByteArrays(Drive service, String folderId) {
        List<ImageData> imageDataList = new ArrayList<>();
        String pageToken = null;

        try {
            do {
                FileList result = service.files().list()
                        .setQ("'" + folderId + "' in parents and mimeType contains 'image/'")
                        .setSpaces("drive")
                        .setFields("nextPageToken, files(id, name)")
                        .setPageToken(pageToken)
                        .execute();

                for (File file : result.getFiles()) {
                    System.out.println("Downloading image: " + file.getName());
                    byte[] imageBytes = downloadFileAsBytes(service, file.getId());
                    if (imageBytes != null) {
                        imageDataList.add(new ImageData(imageBytes, file.getName()));
                    }
                }

                pageToken = result.getNextPageToken();
            } while (pageToken != null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imageDataList;
    }

    public static byte[] downloadFileAsBytes(Drive service, String fileId) {
        try {
            InputStream is = service.files().get(fileId).executeMedia().getContent();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(data)) != -1) {
                buffer.write(data, 0, bytesRead);
            }
            is.close();
            return buffer.toByteArray();
        } catch (Exception e) {
            System.err.println("Failed to download file with ID: " + fileId);
            e.printStackTrace();
        }
        return null;
    }

    public static List<ImageData> getAllImages(String folderId) {
        try {
            Drive service = getDriveService();
            List<ImageData> imageDataList = listImagesAsByteArrays(service, folderId);

            System.out.println("Total images downloaded: " + imageDataList.size());

            for (ImageData imageData : imageDataList) {
                System.out.println("File Name: " + imageData.getFileName());
                System.out.println("Image Bytes Hash Code: " + imageData.getImageBytes().hashCode());
            }
            return imageDataList;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<ImageData> getListOfAllImages() {
        // Replace this with the folder ID of your shared "images" folder
        String sharedFolderId = "1kfpdwfE_mvWxlN4G0I0YAOZp7_5qj7No";
        List<ImageData> listofimages = getAllImages(sharedFolderId);
        return listofimages;
    }
}

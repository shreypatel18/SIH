package text.extraction.getext.service;
import text.extraction.getext.Models.ImageData;
import text.extraction.getext.externalAPIS.*;
import java.util.ArrayList;
import java.util.List;

public class Service {

    testLoader temp = new testLoader();
    List<ImageData> images = GoogleDriveAPI.getListOfAllImages();

    List<byte[]> temp1 = new ArrayList<>();

    public void getbytearraylist(){
        for(ImageData x: images){
            System.out.println(x.getImageBytes());
            temp1.add(x.getImageBytes());
        }
        for(byte[] x: temp1){
            System.out.println(x);
        }
    }

    byte[] sampleImage = temp.getimage();

   public void temp() throws Exception {
       System.out.println(temp1.size());
       FaceMatcher.main(sampleImage, temp1);
    }
}

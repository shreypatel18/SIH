package text.extraction.getext;

import jakarta.annotation.PostConstruct;
import org.opencv.core.Core;
import org.springframework.context.annotation.Configuration;
import text.extraction.getext.service.Service;

@Configuration
public class OpenCVConfig {

    @PostConstruct
    public void initializeOpenCV() {
        try {

            Service s = new Service();
            s.getbytearraylist();
            s.temp();
        } catch (Exception e) {
            System.err.println("Error loading OpenCV native library.");
            e.printStackTrace();
        }
    }
}

package muula.pocketpuppyschooljobs.orchestrator;

import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FirstOrchestrator {

    @Scheduled(fixedDelay = 10000)
    public void run() {
        log.info("running mock job");
        try {
            testUrl();
        } catch (Exception e) {
            log.info("Couldn't load image", e);
        }
    }

    private void testUrl() throws Exception {
        URL url = new URL("https://i.ibb.co/80JHVHS/ab274e9d573c.jpg");
        BufferedImage image = ImageIO.read(url);
        int height = image.getHeight();
        int width = image.getWidth();

        System.out.println("Height: " + height);
        System.out.println("Width: " + width);
        System.out.println("DONE!");

    }
}

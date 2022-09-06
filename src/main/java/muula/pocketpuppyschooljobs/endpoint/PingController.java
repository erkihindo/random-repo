package muula.pocketpuppyschooljobs.endpoint;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PingController {

    @ResponseBody
    @PostMapping(value = "/ping")
    public ResponseEntity<Boolean> ping() {
        log.info("Endpoint called");
        return ResponseEntity.ok(Boolean.TRUE);
    }
}

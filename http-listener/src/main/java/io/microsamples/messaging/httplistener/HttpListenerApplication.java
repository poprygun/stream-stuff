package io.microsamples.messaging.httplistener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class HttpListenerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HttpListenerApplication.class, args);
    }

}

@RestController
@Slf4j
class ListenerController {

	@GetMapping("/echo")
	public ResponseEntity echo(@RequestParam(defaultValue = "0") int delay) throws InterruptedException {
		TimeUnit.SECONDS.sleep(delay);
		final String response = "Echo message...";
		log.info(response);
		return ResponseEntity.ok(response);
	}
}

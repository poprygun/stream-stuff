package io.microsamples.messaging.httplistener;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

	private Long count = 0l ;

	@GetMapping("/echo")
	public ResponseEntity echo(@RequestParam(defaultValue = "0") int delay) throws InterruptedException {
		TimeUnit.SECONDS.sleep(delay);
		count += 1;
		final String response = "Echo message...number " + count;
		log.info(response);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/reset")
	public ResponseEntity reset(){
		count = 0l;
		return ResponseEntity.ok("Counter Reseted.");
	}
}


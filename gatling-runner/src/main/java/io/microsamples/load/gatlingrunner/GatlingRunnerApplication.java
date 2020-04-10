package io.microsamples.load.gatlingrunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.gatling.app.Gatling;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.security.Permission;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableAsync
public class GatlingRunnerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatlingRunnerApplication.class, args);
    }
}

@Service
class AsyncService {
    @Async
    public void doWork(Runnable runnable) {
        runnable.run();
    }
}

class MySecurityManager extends SecurityManager {
    @Override
    public void checkExit(int status) {
        throw new SecurityException();
    }

    @Override
    public void checkPermission(Permission perm) {
        // Allow other activities by default
    }
}

@Controller
@Slf4j
class LoadController {

    @Value("${reports.dir:src/main/resources/static}")
    private String reportsDir;

    private AsyncService service;

    private ObjectMapper objectMapper;

    LoadController(AsyncService service, ObjectMapper objectMapper) {
        this.service = service;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/run-test")
    private ResponseEntity<String> runTest(@RequestParam(value = "simulation"
            , required = false, defaultValue = "io.microsamples.testz.simulation.SayItSimulation") String simulation) {

        log.info("Running simulation: {} in {}", simulation, reportsDir);

        return executeGatlingScenario(simulation);
    }

    private ResponseEntity<String> executeGatlingScenario(String simulation) {
        String[] args = {"-s", simulation, "-rf", reportsDir};

        runTestNoExit(args);

        return ResponseEntity.ok("Report Scheduled...  Check /reports in few minutes");
    }

    private void runTestNoExit(String[] args) {
        MySecurityManager secManager = new MySecurityManager();
        System.setSecurityManager(secManager);
        service.doWork(() -> {
            try {
                Gatling.main(args);
            } catch (SecurityException se) {
                //prevent program from exiting
            }
        });
    }

    @GetMapping("/reports")
    public String reports(Model model) {
        File[] directories = new File(reportsDir).listFiles(File::isDirectory);
        final List<String> reports = Arrays.asList(directories).stream().map(File::getName).collect(Collectors.toList());
        model.addAttribute("reports", reports);
        return "index";
    }
}

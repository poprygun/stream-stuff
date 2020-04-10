package io.microsamples.load.gatlingrunner;

import io.gatling.app.Gatling;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Disabled
class GatlingRunnerApplicationTests {

	@Test
	void listStatic(){
		File[] directories = new File("src/main/resources/static").listFiles(File::isDirectory);
		final List<String> collect = Arrays.asList(directories).stream().map(File::getName).collect(Collectors.toList());
	}

	@Test
	void contextLoads() {
		String[] args = {"-s", "io.microsamples.testz.simulation.GetRootsSimulation"
				, "-rf", "src/main/resources/static"};
		Gatling.main(args);
	}

}

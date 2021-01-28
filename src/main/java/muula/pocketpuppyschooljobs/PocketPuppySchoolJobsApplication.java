package muula.pocketpuppyschooljobs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PocketPuppySchoolJobsApplication {

	public static void main(String[] args) {
		SpringApplication.run(PocketPuppySchoolJobsApplication.class, args);
	}

}

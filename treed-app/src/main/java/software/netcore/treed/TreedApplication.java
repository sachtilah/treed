package software.netcore.treed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import software.netcore.treed.data.DataConfiguration;

@SpringBootApplication
@Import(DataConfiguration.class)
public class TreedApplication {

	public static void main(String[] args) {
		SpringApplication.run(TreedApplication.class, args);
	}
}

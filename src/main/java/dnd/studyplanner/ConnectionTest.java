package dnd.studyplanner;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ConnectionTest {

	@GetMapping("/test")
	public String ec2ConnectionTest() {
		return "Hello world!";
	}
}

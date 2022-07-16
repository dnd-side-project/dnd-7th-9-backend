package dnd.studyplanner;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ConnectionTest {

	@GetMapping("/test")
	public String ec2ConnectionTest() {
		System.out.println("Hello world!");
		System.out.println("Hello world!");
		System.out.println("Hello world!");
		System.out.println("Hello world!");
		return "Hello world!";
	}
}

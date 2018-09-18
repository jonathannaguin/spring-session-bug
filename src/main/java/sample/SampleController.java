package sample;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SampleController
{
	@RequestMapping("/hello")
	public String welcome() {
		return "hello";
	}


	@RequestMapping("/expired")
	public String expired() {
		return "expired";
	}

}

package at.dauswege.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api")
public class TestController {

	@RequestMapping(value = "test")
	public String doTest() {
		return "test";
	}

}

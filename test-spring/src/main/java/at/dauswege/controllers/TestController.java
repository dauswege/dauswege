package at.dauswege.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.dauswege.dto.TestDto;

@RestController
@RequestMapping(value = "api")
public class TestController {

  @RequestMapping(value = "test")
  public TestDto doTest() {
    return new TestDto(100L, "test anme");
  }

}

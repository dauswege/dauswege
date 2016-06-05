package at.dauswege;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

  @Autowired
  private Environment environment;

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {

    registry.addViewController("test.html").setViewName("test");
    registry.addViewController("wedding/viewer.html").setViewName("wedding/viewer");

  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    String myExternalFilePath =
        "file:///" + environment.getProperty(TestSpringApplication.PICFOLDER_PROP) + "/";
    registry.addResourceHandler("/pics/**").addResourceLocations(
        myExternalFilePath);

    super.addResourceHandlers(registry);
  }
}

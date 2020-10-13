package com.takeaway.assignment.gameofthree;

import com.takeaway.assignment.gameofthree.util.ApplicationConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan(ApplicationConstant.BASE_PROPERTIES_PACKAGE)
@SpringBootApplication
public class GameofthreeApplication {

  public static void main(String[] args) {
    SpringApplication.run(GameofthreeApplication.class, args);
  }

}

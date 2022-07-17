package dev.untitled1.actorworkflow.configuration;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import dev.untitled1.actorworkflow.extension.SpringExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class ApplicationConfiguration {

  // The application context is needed to initialize the Akka Spring  Extension
  @Autowired
  private ApplicationContext applicationContext;
  @Autowired
  private SpringExt springExt;


  /**
   * Actor system singleton for this application.
   */
  @Bean
  public ActorSystem actorSystem() {
    final ActorSystem system = ActorSystem.create("AkkaTaskProcessing", akkaConfiguration());

    // Initialize the application context in the Akka Spring Extension
    springExt.initialize(applicationContext);
    return system;
  }

  /**
   * Read configuration from application.conf file
   */
  @Bean
  public Config akkaConfiguration() {
    return ConfigFactory.load();
  }

}

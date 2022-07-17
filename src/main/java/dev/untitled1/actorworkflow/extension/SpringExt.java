package dev.untitled1.actorworkflow.extension;

import akka.actor.Extension;
import akka.actor.Props;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Extension to tell Akka how to create beans via Spring.
 */
@Component
public class SpringExt implements Extension {

  private ApplicationContext applicationContext;

  /**
   * Used to initialize the Spring application context for the extension.
   */
  public void initialize(final ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  /**
   * Create a Props for the specified actorBeanName using the
   * SpringActorProducer class.
   */
  public Props props(final String actorBeanName) {
    return Props.create(SpringActorProducer.class,
        applicationContext, actorBeanName);
  }

  public Props props(final String actorBeanName, Class<?> inputType, Class<?> workflowMessage) {
    return Props.create(SpringActorProducer.class,
        applicationContext, actorBeanName, inputType, workflowMessage);
  }

}
package dev.untitled1.actorworkflow;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import dev.untitled1.actorworkflow.actors.WorkflowActor;
import dev.untitled1.actorworkflow.definition.CalculatorWorkflow;
import dev.untitled1.actorworkflow.definition.WorkflowFactory;
import dev.untitled1.actorworkflow.domain.message.CalculatorWorkflowMessage;
import dev.untitled1.actorworkflow.domain.message.StartWorkflowMessage;
import dev.untitled1.actorworkflow.extension.SpringActorFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class DemoApplication {

  public static void main(final String[] args) {
    final ApplicationContext context = SpringApplication.run(DemoApplication.class, args);
    final ActorSystem system = context.getBean(ActorSystem.class);
    final LoggingAdapter log = Logging.getLogger(system, "ActorWorkflowDemoApplication");

    initActors(context);

    log.info("Started");
  }

  private static void initActors(final ApplicationContext context) {
    final SpringActorFactory springActorFactory = context.getBean(SpringActorFactory.class);
    final WorkflowFactory workflowFactory = context.getBean(WorkflowFactory.class);

    // start workflow; check the logs.
    final ActorRef workflowActor = springActorFactory.createActor(WorkflowActor.ACTOR_NAME);

    // create workflow
    final StartWorkflowMessage<CalculatorWorkflowMessage, CalculatorWorkflow>
        calculatorWorkflow = workflowFactory.createCalculatorWorkflow(workflowActor, 0.0f);

    // kick it off
    workflowActor.tell(calculatorWorkflow, ActorRef.noSender());
  }
}

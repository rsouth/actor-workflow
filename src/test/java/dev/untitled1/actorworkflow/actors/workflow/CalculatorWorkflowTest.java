package dev.untitled1.actorworkflow.actors.workflow;

import static org.junit.jupiter.api.Assertions.assertEquals;

import akka.actor.ActorRef;
import com.google.common.collect.Lists;
import dev.untitled1.actorworkflow.actors.BaseActorTest;
import dev.untitled1.actorworkflow.actors.WorkflowActor;
import dev.untitled1.actorworkflow.actors.participants.AdditionActor;
import dev.untitled1.actorworkflow.actors.participants.SubtractionActor;
import dev.untitled1.actorworkflow.definition.CalculatorWorkflow;
import dev.untitled1.actorworkflow.domain.message.CalculatorWorkflowMessage;
import dev.untitled1.actorworkflow.domain.message.StartWorkflowMessage;
import dev.untitled1.actorworkflow.domain.requests.CalculatorRequest;
import dev.untitled1.actorworkflow.domain.responses.CalculatorResponse;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CalculatorWorkflowTest extends BaseActorTest {

  private ActorRef workflowActor;
  private ActorRef testActor;

  @BeforeEach
  void beforeEach() {
    workflowActor = springActorFactory.createActor(WorkflowActor.ACTOR_NAME);
    testActor = testKit.getTestActor();
  }

  @AfterEach
  void afterEach() {
    actorSystem.stop(workflowActor);
  }


  @Test
  public void testCalculator() {
    // workflow
    final ArrayList<CalculatorWorkflow.CalculatorOperation> calculatorOperations = Lists.newArrayList(
        new CalculatorWorkflow.CalculatorOperation(CalculatorWorkflow.Operation.ADD, 20f),
        new CalculatorWorkflow.CalculatorOperation(CalculatorWorkflow.Operation.SUBTRACT, 10f),
        new CalculatorWorkflow.CalculatorOperation(CalculatorWorkflow.Operation.ADD, 5f)
    );
    final StartWorkflowMessage<CalculatorWorkflowMessage, CalculatorWorkflow>
        calculatorWorkflow = createCalculatorWorkflow(testActor, calculatorOperations);

    workflowActor.tell(calculatorWorkflow, testActor);

    final CalculatorResponse
        response = testKit.expectMsgClass(CalculatorResponse.class);
    testKit.expectNoMessage();


    // Assert
    Assertions.assertEquals(calculatorWorkflow.workflowMessage().getRequest(), response.getRequest());
    assertEquals(15f, response.getCalculationResult());
  }

  @Test
  public void testCalculatorWithInvalidInput() {
    // workflow
    final ArrayList<CalculatorWorkflow.CalculatorOperation> calculatorOperations = Lists.newArrayList(
        // this is invalid because the first operation is expected to be ADD
        new CalculatorWorkflow.CalculatorOperation(CalculatorWorkflow.Operation.SUBTRACT, 20f)
    );
    final StartWorkflowMessage<CalculatorWorkflowMessage, CalculatorWorkflow>
        calculatorWorkflow = createCalculatorWorkflow(testActor, calculatorOperations);

    workflowActor.tell(calculatorWorkflow, testActor);

    final CalculatorWorkflowMessage
        errorResponse = testKit.expectMsgClass(CalculatorWorkflowMessage.class);
    assertEquals(1, errorResponse.getErrors().size());
    testKit.expectNoMessage();
  }


  public StartWorkflowMessage<CalculatorWorkflowMessage, CalculatorWorkflow> createCalculatorWorkflow(
      final ActorRef replyTo,
      final List<CalculatorWorkflow.CalculatorOperation> operations) {
    final CalculatorRequest
        calculatorRequest = new CalculatorRequest(replyTo, operations);
    final CalculatorWorkflow calculatorWorkflow = new CalculatorWorkflow();
    final CalculatorWorkflowMessage build =
        (CalculatorWorkflowMessage) CalculatorWorkflowMessage.Builder
            .aWorkflowMessage()
            .withCalculationResult(0f) // start with 0
            .withRequest(calculatorRequest)
            .withWorkflowActors(calculatorWorkflowActors())
            .withWorkflow(calculatorWorkflow)
            .withRequestingActor(replyTo)
            .build();

    return new StartWorkflowMessage<>(build);
  }

  private List<String> calculatorWorkflowActors() {
    return Lists.newArrayList(
        AdditionActor.ACTOR_NAME,
        SubtractionActor.ACTOR_NAME,
        AdditionActor.ACTOR_NAME
    );
  }

}

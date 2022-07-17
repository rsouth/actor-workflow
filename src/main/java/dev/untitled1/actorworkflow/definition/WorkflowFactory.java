package dev.untitled1.actorworkflow.definition;

import akka.actor.ActorRef;
import com.google.common.collect.Lists;
import dev.untitled1.actorworkflow.actors.participants.AdditionActor;
import dev.untitled1.actorworkflow.actors.participants.SubtractionActor;
import dev.untitled1.actorworkflow.domain.message.CalculatorWorkflowMessage;
import dev.untitled1.actorworkflow.domain.message.StartWorkflowMessage;
import dev.untitled1.actorworkflow.domain.requests.CalculatorRequest;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public final class WorkflowFactory {

  public StartWorkflowMessage<CalculatorWorkflowMessage, CalculatorWorkflow> createCalculatorWorkflow(
      final ActorRef replyTo, final float initialValue) {
    final List<CalculatorWorkflow.CalculatorOperation> operations = Lists.newArrayList(
        new CalculatorWorkflow.CalculatorOperation(CalculatorWorkflow.Operation.ADD, 10.0f),
        new CalculatorWorkflow.CalculatorOperation(CalculatorWorkflow.Operation.SUBTRACT, 5.0f)
    );

    final CalculatorRequest request = new CalculatorRequest(replyTo, operations);
    final CalculatorWorkflowMessage build = (CalculatorWorkflowMessage) CalculatorWorkflowMessage.Builder
        .aWorkflowMessage()
        .withCalculationResult(initialValue)
        .withRequest(request)
        .withWorkflowActors(orderCreationWorkflowActors())
        .withWorkflow(new CalculatorWorkflow())
        .withRequestingActor(replyTo)
        .build();

    return new StartWorkflowMessage<>(build);
  }

  private List<String> orderCreationWorkflowActors() {
    return Lists.newArrayList(
        AdditionActor.ACTOR_NAME,
        SubtractionActor.ACTOR_NAME
    );
  }

}

package dev.untitled1.actorworkflow.actors.participants;

import static java.text.MessageFormat.format;

import dev.untitled1.actorworkflow.actors.WorkflowParticipant;
import dev.untitled1.actorworkflow.definition.CalculatorWorkflow;
import dev.untitled1.actorworkflow.domain.message.CalculatorWorkflowMessage;
import dev.untitled1.actorworkflow.domain.message.WorkflowError;
import dev.untitled1.actorworkflow.domain.message.WorkflowMessage;
import dev.untitled1.actorworkflow.domain.requests.CalculatorRequest;
import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class SubtractionActor extends WorkflowParticipant<CalculatorWorkflowMessage, CalculatorWorkflow> {

  public static final String ACTOR_NAME = "subtractionActor";
  private final Logger log = LoggerFactory.getLogger(SubtractionActor.class);

  @Override
  protected void handleWorkflowMessage(@Nonnull CalculatorWorkflowMessage workflowMessage) {
    log.info("Processing {}", workflowMessage);
    Float intermediateResult = workflowMessage.getCalculationResult();
    log.info("Has intermediateResult {}", intermediateResult);
    CalculatorWorkflow.CalculatorOperation op =
        ((CalculatorRequest) workflowMessage.getRequest()).getOperations()
            .remove(0);

    if (CalculatorWorkflow.Operation.SUBTRACT == op.operation) {
      log.info("Operation is {}, operand is {}", op.operation, op.operand);
      Float calculationResult = intermediateResult - op.operand;
      final CalculatorWorkflowMessage build =
          CalculatorWorkflowMessage.Builder.aWorkflowMessage()
              .from(workflowMessage)
              .withCalculationResult(calculationResult)
              .build();
      log.info("Built reply wfm: {}", build);
      getSender().tell(build, getSelf());

    } else {
      log.info("Operation is {}, returning input {}", op.operation, intermediateResult);
      final WorkflowError error = new WorkflowError(
          format("{0} does not support operation {1}", ACTOR_NAME, op.operation));
      final WorkflowMessage<CalculatorWorkflowMessage, CalculatorWorkflow> build =
          CalculatorWorkflowMessage.Builder.aWorkflowMessage()
              .from(workflowMessage)
              .withError(error)
              .build();
      getSelf().tell(build, getSelf());
    }
  }

  @Override
  protected void handleWorkflowFailure(@Nonnull CalculatorWorkflowMessage workflowMessage) {
    // todo getSender().tell(SomeErrorType maybe CalculationErrorResult?, getSelf())
    log.error("dunno why we ended up here.");
    workflowMessage.getRequestingActor().tell(workflowMessage, getSelf());
  }
}

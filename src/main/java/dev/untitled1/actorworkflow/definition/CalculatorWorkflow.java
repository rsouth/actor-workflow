package dev.untitled1.actorworkflow.definition;

import akka.actor.ActorRef;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import dev.untitled1.actorworkflow.domain.message.CalculatorWorkflowMessage;
import dev.untitled1.actorworkflow.domain.message.WorkflowMessage;
import dev.untitled1.actorworkflow.domain.responses.CalculatorResponse;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demo workflow
 */
public final class CalculatorWorkflow extends AbstractWorkflow<CalculatorWorkflowMessage, CalculatorWorkflow> {

  private final Logger log = LoggerFactory.getLogger(CalculatorWorkflow.class);

  public CalculatorWorkflow() {
    super();
  }

  @Override
  protected List<? extends Predicate<? super CalculatorWorkflowMessage>> initPredicates() {
    return Lists.newArrayList(
        Objects::nonNull,
        CalculatorWorkflowMessage.class::isInstance
    );
  }

  @Override
  public void handleWorkflowCompleted(final @Nonnull ActorRef fromActor,
                                      final @Nonnull WorkflowMessage<CalculatorWorkflowMessage, CalculatorWorkflow> workflowMessage) {
    final CalculatorResponse response = new CalculatorResponse(workflowMessage.getRequest(),
        ((CalculatorWorkflowMessage) workflowMessage).getCalculationResult());

    log.info("Completing request with {}", response);
    workflowMessage.getRequestingActor().tell(response, fromActor);
  }

  @Override
  public void handleWorkflowFailure(final ActorRef fromActor,
                                    final CalculatorWorkflowMessage workflowMessage,
                                    final List<String> errorMessages) {
    final CalculatorResponse response =
        new CalculatorResponse(workflowMessage.getRequest(), 0f); // todo add error messages on here
    workflowMessage.getRequestingActor().tell(response, fromActor);
  }

  public enum Operation {
    ADD, SUBTRACT
  }

  public static class CalculatorOperation {

    public Operation operation;

    public Float operand;

    public CalculatorOperation(final Operation operation, final Float operand) {
      this.operation = operation;
      this.operand = operand;
    }
  }

}


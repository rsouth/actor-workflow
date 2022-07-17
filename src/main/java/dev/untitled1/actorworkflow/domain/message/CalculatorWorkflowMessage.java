package dev.untitled1.actorworkflow.domain.message;

import akka.actor.ActorRef;
import dev.untitled1.actorworkflow.definition.AbstractWorkflow;
import dev.untitled1.actorworkflow.definition.CalculatorWorkflow;
import dev.untitled1.actorworkflow.domain.requests.AbstractRequest;
import java.util.List;
import javax.annotation.Nonnull;

public class CalculatorWorkflowMessage
    extends WorkflowMessage<CalculatorWorkflowMessage, CalculatorWorkflow> {

  private Float calculationResult;

  public CalculatorWorkflowMessage(
      List<String> workflowActors,
      AbstractWorkflow<CalculatorWorkflowMessage, CalculatorWorkflow> workflow,
      ActorRef requestingActor,
      AbstractRequest request,
      Float calculationResult,
      List<WorkflowError> errors
  ) {
    super(workflowActors, (CalculatorWorkflow) workflow, requestingActor, request, errors);
    this.calculationResult = calculationResult;
  }

  public Float getCalculationResult() {
    return this.calculationResult;
  }

  public static final class Builder<M extends WorkflowMessage<M, W>, W extends AbstractWorkflow<M, W>>
      extends WorkFlowMessageBuilder<CalculatorWorkflowMessage, CalculatorWorkflow> {

    private Float calculationResult;

    private Builder() {
    }


    // todo aWorkflowMessage should give a workflow a specific ID so it's followable in logs
    public static Builder<CalculatorWorkflowMessage, CalculatorWorkflow> aWorkflowMessage() {
      return new Builder<>();
    }

    public Builder<M, W> from(M workflowMessage) {
      final Builder<M, W> builder =
          (Builder<M, W>) super
              .withWorkflow((CalculatorWorkflow) workflowMessage.getWorkflow())
              .withRequest(workflowMessage.getRequest())
              .withWorkflowActors(workflowMessage.getWorkflowActors())
              .withRequestingActor(workflowMessage.getRequestingActor());

      if (workflowMessage instanceof CalculatorWorkflowMessage w) {
        builder.withCalculationResult(w.getCalculationResult());
      }

      return builder;
    }

    public Builder<M, W> withCalculationResult(
        final @Nonnull Float calculationResult) {
      this.calculationResult = calculationResult;
      return this;
    }

    public CalculatorWorkflowMessage build() {
      return new CalculatorWorkflowMessage(
          workflowActors,
          workflow,
          requestingActor,
          request,
          calculationResult,
          errors
      );
    }

  }

}

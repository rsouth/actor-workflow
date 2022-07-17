package dev.untitled1.actorworkflow.actors;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import dev.untitled1.actorworkflow.definition.AbstractWorkflow;
import dev.untitled1.actorworkflow.domain.message.WorkflowMessage;
import javax.annotation.Nonnull;

public abstract class WorkflowParticipant<M extends WorkflowMessage<M, W>, W extends AbstractWorkflow<M, W>>
    extends AbstractActor {

  private final LoggingAdapter log = Logging.getLogger(this);

  @Override
  public Receive createReceive() {
    return ReceiveBuilder.create()
        .match(WorkflowMessage.class, this::routeWorkflowMessage)
        .matchAny(this::unknownMessage)
        .build();
  }

  private void unknownMessage(final @Nonnull Object msg) {
    log.error("Unknown message received: {}", msg);
  }

  protected void routeWorkflowMessage(final @Nonnull WorkflowMessage<M, W> workflowMessage) {
    if (workflowMessage.getErrors().isEmpty()) {
      handleWorkflowMessage((M) workflowMessage);
    } else {
      handleWorkflowFailure((M) workflowMessage);
    }
  }

  protected abstract void handleWorkflowMessage(final @Nonnull M workflowMessage);

  protected abstract void handleWorkflowFailure(final @Nonnull M workflowMessage);

}

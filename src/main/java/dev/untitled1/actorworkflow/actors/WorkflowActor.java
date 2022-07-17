package dev.untitled1.actorworkflow.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import dev.untitled1.actorworkflow.definition.AbstractWorkflow;
import dev.untitled1.actorworkflow.domain.WorkflowCriticalError;
import dev.untitled1.actorworkflow.domain.message.StartWorkflowMessage;
import dev.untitled1.actorworkflow.domain.message.WorkflowMessage;
import dev.untitled1.actorworkflow.domain.requests.AbstractRequest;
import dev.untitled1.actorworkflow.extension.SpringActorFactory;
import java.util.List;
import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * WorkflowActor processes workflows.
 */
@Component
@Scope("prototype")
public class WorkflowActor<M extends WorkflowMessage<M, W>, W extends AbstractWorkflow<M, W>> extends AbstractActor {

  public static final String ACTOR_NAME = "workflowActor";

  private final Logger log = LoggerFactory.getLogger(WorkflowActor.class);

  private final SpringActorFactory springActorFactory;

  @Autowired
  public WorkflowActor(final SpringActorFactory springActorFactory) {
    this.springActorFactory = springActorFactory;
  }

  @Override
  @SuppressWarnings("unchecked")
  public Receive createReceive() {
    return ReceiveBuilder.create()
        .match(StartWorkflowMessage.class, this::startWorkflow)
        .match(WorkflowMessage.class, this::handleWorkflowMessage)
        .build();
  }


  /**
   * Start the Workflow.
   *
   * @param startWorkflow containing information required to process the workflow
   */
  protected void startWorkflow(final @Nonnull StartWorkflowMessage<M, W> startWorkflow) {
    log.info("startWorkflow received {}", startWorkflow);

    if (validatePreconditions(startWorkflow)) {
      log.info("startWorkflow AFTER validatePreconditions for {}", startWorkflow);
      getSelf().tell(startWorkflow.workflowMessage(), ActorRef.noSender());
    }
  }

  /**
   * Process each workflow message being passed back from WorkflowParticipant actors.
   *
   * @param workflowMessage containing intermediate step data
   */
  protected void handleWorkflowMessage(final WorkflowMessage<M, W> workflowMessage) {
    log.info("handleWorkflowMessage received {}", workflowMessage);
    if (!workflowMessage.getWorkflowActors().isEmpty()) {
      log.info("Remaining workflow actors are: {}", workflowMessage.getWorkflowActors());

      // continue the workflow with the next actor
      final String actorName = workflowMessage.getNextWorkflowActor(); //.getWorkflowActors().remove(0);
      final ActorRef actor = this.springActorFactory.createActor(actorName);
      log.info("Created actor {} and telling {}", actorName, workflowMessage);
      actor.tell(workflowMessage, getSelf());

    } else {
      log.info("Completed all workflow actors.");
      // "complete" means either success or fail - on a per intention/order basis
      processWorkflowCompleted(workflowMessage);
    }
  }


  /**
   * Ensure that it's safe to run the workflow based on the initial input.
   *
   * @param startWorkflowMessage containing the data to start the workflow
   * @return true if the workflow can safely start
   */
  protected boolean validatePreconditions(final @Nonnull StartWorkflowMessage<M, W> startWorkflowMessage) {
    final M workflowMessage = startWorkflowMessage.workflowMessage();
    final AbstractRequest request = startWorkflowMessage.workflowMessage().getRequest();

    // check Request; not recoverable, send WorkflowCriticalError to sender
    if (request == null) {
      log.error("Request is NULL on {}", startWorkflowMessage);
      final WorkflowCriticalError error =
          new WorkflowCriticalError("ValidationResult.W_NULL_REQUEST");
      getSender().tell(error, getSelf());
      return false;
    }

    // check WorkflowMessage; not recoverable, send WorkflowCriticalError to requesting actor
    if (workflowMessage == null) {
      log.error("Workflow is NULL on {}", startWorkflowMessage);
      final WorkflowCriticalError error =
          new WorkflowCriticalError("ValidationResult.W_NULL_WORKFLOW_MESSAGE");
      request.getRequestingActor().tell(error, getSelf());
      return false;
    }

    // check Workflow; not recoverable, send WorkflowCriticalError to sender
    final AbstractWorkflow<M, W> workflow = workflowMessage.getWorkflow();
    if (workflow == null) {
      log.error("Workflow is null on {}", workflowMessage);
      final WorkflowCriticalError error =
          new WorkflowCriticalError("ValidationResult.W_NULL_WORKFLOW");
      getSender().tell(error, getSelf());
      return false;
    }

    // apply workflow precondition predicates to the WorkflowMessage
    final List<? extends Predicate<? super M>> preconditions =
        startWorkflowMessage.workflowMessage().getWorkflow().getPreconditions();
    log.info("Evaluating {} preconditions for {}", preconditions.size(), workflowMessage);

    return Predicates.and(preconditions).test(workflowMessage);
  }

  protected void processWorkflowFailure(final @Nonnull M workflowMessage,
                                        final @Nonnull List<String> errorMessages) {
    workflowMessage.getWorkflow().handleWorkflowFailure(getSelf(), workflowMessage, errorMessages);
  }

  protected void processWorkflowCompleted(final @Nonnull WorkflowMessage<M, W> workflowMessage) {
    log.info("processWorkflowCompleted");
    workflowMessage.getWorkflow().handleWorkflowCompleted(getSelf(), workflowMessage);
  }

}

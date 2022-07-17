package dev.untitled1.actorworkflow.definition;

import akka.actor.ActorRef;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import dev.untitled1.actorworkflow.domain.message.WorkflowMessage;
import java.util.List;
import javax.annotation.Nonnull;

/**
 * Workflow definition.
 *
 * @param <M> workflow message
 * @param <W> workflow definition
 */
public abstract class AbstractWorkflow<M extends WorkflowMessage<M, W>, W extends AbstractWorkflow<M, W>> {

  /**
   * Preconditions to be evaluated before proceeding in the workflow.
   */
  private final List<? extends Predicate<? super M>> predicates;

  public AbstractWorkflow() {
    this.predicates = initPredicates();
  }

  protected abstract List<? extends Predicate<? super M>> initPredicates();

  public final ImmutableList<? extends Predicate<? super M>> getPreconditions() {
    return ImmutableList.copyOf(this.predicates);
  }

  public abstract void handleWorkflowCompleted(final @Nonnull ActorRef fromActor,
                                               final @Nonnull WorkflowMessage<M, W> workflowMessage);

  public abstract void handleWorkflowFailure(final ActorRef fromActor,
                                             final M workflowMessage,
                                             final List<String> errorMessages);

}

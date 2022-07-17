package dev.untitled1.actorworkflow.domain.message;

import akka.actor.ActorRef;
import com.google.common.collect.Lists;
import dev.untitled1.actorworkflow.definition.AbstractWorkflow;
import dev.untitled1.actorworkflow.domain.requests.AbstractRequest;
import java.util.ArrayList;
import java.util.List;

public abstract class WorkFlowMessageBuilder<M extends WorkflowMessage<M, W>, W extends AbstractWorkflow<M, W>> {

  protected List<String> workflowActors;

  protected AbstractWorkflow<M, W> workflow;

  protected ActorRef requestingActor;

  protected AbstractRequest request;

  protected List<WorkflowError> errors = new ArrayList<>();

  public WorkFlowMessageBuilder() {
  }

  public WorkFlowMessageBuilder<M, W> withWorkflowActors(final List<String> workflowActors) {
    this.workflowActors = Lists.newArrayList(workflowActors);
    return this;
  }

  public WorkFlowMessageBuilder<M, W> withWorkflow(W workflow) {
    this.workflow = workflow;
    return this;
  }

  public WorkFlowMessageBuilder<M, W> withRequestingActor(final ActorRef requestingActor) {
    this.requestingActor = requestingActor;
    return this;
  }

  public WorkFlowMessageBuilder<M, W> withRequest(final AbstractRequest request) {
    this.request = request;
    return this;
  }

  public WorkFlowMessageBuilder<M, W> withError(final WorkflowError error) {
    this.errors.add(error);
    return this;
  }

  public abstract WorkflowMessage<M, W> build();

}

package dev.untitled1.actorworkflow.domain.message;

import akka.actor.ActorRef;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import dev.untitled1.actorworkflow.definition.AbstractWorkflow;
import dev.untitled1.actorworkflow.domain.requests.AbstractRequest;
import java.util.List;

public class WorkflowMessage<M extends WorkflowMessage<M, W>, W extends AbstractWorkflow<M, W>> {
  private final List<String> workflowActors;
  private final AbstractWorkflow<M, W> workflow;
  private final ActorRef requestingActor;
  private final AbstractRequest request;

  private final List<WorkflowError> errors;

  public WorkflowMessage(
      final List<String> workflowActors,
      final W workflow,
      final ActorRef requestingActor,
      final AbstractRequest request,
      final List<WorkflowError> errors) {
    this.workflowActors = Lists.newArrayList(workflowActors);
    this.workflow = workflow;
    this.requestingActor = requestingActor;
    this.request = request;
    this.errors = Lists.newArrayList(errors);
  }

  public List<String> getWorkflowActors() {
    return Lists.newArrayList(workflowActors);
  }

  public String getNextWorkflowActor() {
    if (this.workflowActors.isEmpty()) {
      return ""; // encapsulate 'workflowactor' string as WorkflowActorName or similar????
    }
    return this.workflowActors.remove(0);
  }

  public W getWorkflow() {
    return (W) workflow;
  }

  public ActorRef getRequestingActor() {
    return requestingActor;
  }

  public AbstractRequest getRequest() {
    return request;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("workflowActors", workflowActors)
        .add("workflow", workflow)
        .add("requestingActor", requestingActor)
        .add("request", request)
        .add("errors", errors)
        .toString();
  }

  public List<WorkflowError> getErrors() {
    return Lists.newArrayList(this.errors);
  }
}

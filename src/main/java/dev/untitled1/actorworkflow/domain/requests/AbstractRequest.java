package dev.untitled1.actorworkflow.domain.requests;

import akka.actor.ActorRef;
import com.google.common.base.MoreObjects;

/**
 * Base class for 'requests' - might be 'WorkflowRequests' todo review this.
 */
public abstract class AbstractRequest {

  private final ActorRef requestingActor;

  public AbstractRequest(final ActorRef requestingActor) {
    this.requestingActor = requestingActor;
  }

  public ActorRef getRequestingActor() {
    return requestingActor;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("requestingActor", requestingActor)
        .toString();
  }
}

package dev.untitled1.actorworkflow.domain.responses;

import com.google.common.base.MoreObjects;
import dev.untitled1.actorworkflow.domain.requests.AbstractRequest;

/**
 * Abstract type for responses - may actually be 'workflow responses' todo review this.
 */
public abstract class AbstractResponse {

  private final AbstractRequest request;

  protected AbstractResponse(final AbstractRequest request) {
    this.request = request;
  }

  public AbstractRequest getRequest() {
    return request;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("request", request)
        .toString();
  }

}

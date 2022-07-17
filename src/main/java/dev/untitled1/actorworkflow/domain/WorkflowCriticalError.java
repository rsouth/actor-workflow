package dev.untitled1.actorworkflow.domain;

import com.google.common.base.MoreObjects;

public record WorkflowCriticalError(String errorMessage) {

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("errorMessage", errorMessage)
        .toString();
  }
}

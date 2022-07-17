package dev.untitled1.actorworkflow.domain.message;

public class WorkflowError {
  final String message;

  public WorkflowError(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}

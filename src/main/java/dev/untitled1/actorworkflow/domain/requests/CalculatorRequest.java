package dev.untitled1.actorworkflow.domain.requests;

import akka.actor.ActorRef;
import com.google.common.collect.Lists;
import dev.untitled1.actorworkflow.definition.CalculatorWorkflow;
import java.util.List;

public class CalculatorRequest extends AbstractRequest {

  private final List<CalculatorWorkflow.CalculatorOperation> operations;

  public CalculatorRequest(ActorRef requestingActor, List<CalculatorWorkflow.CalculatorOperation> operations) {
    super(requestingActor);
    this.operations = Lists.newArrayList(operations);
  }

  public List<CalculatorWorkflow.CalculatorOperation> getOperations() {
    return this.operations;
  }
}

package dev.untitled1.actorworkflow.domain.responses;

import dev.untitled1.actorworkflow.domain.requests.AbstractRequest;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class CalculatorResponse extends AbstractResponse {

  private final Float calculationResult;

  public CalculatorResponse(AbstractRequest request, Float calculationResult) {
    super(request);
    this.calculationResult = calculationResult;
  }

  public Float getCalculationResult() {
    return this.calculationResult;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("calculationResult", calculationResult)
        .toString();
  }
}

package dev.untitled1.actorworkflow.domain.message;

import dev.untitled1.actorworkflow.definition.AbstractWorkflow;

public record StartWorkflowMessage<M extends WorkflowMessage<M, W>, W extends AbstractWorkflow<M, W>>(
    M workflowMessage/*, AbstractRequest request*/) {

}

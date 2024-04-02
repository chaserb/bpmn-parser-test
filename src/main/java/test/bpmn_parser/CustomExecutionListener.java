/*
 * Copyright (C) 2024 Fastpath Solutions, LLC - All rights reserved.
 */
package test.bpmn_parser;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;

import java.io.Serial;
import java.util.Objects;

public class CustomExecutionListener implements ExecutionListener {

    @Serial
    private static final long serialVersionUID = -1441723390732372577L;

    @Override
    public void notify(final DelegateExecution execution) {
        if (execution.getCurrentActivityId() == null) {
            execution.setVariable("processStart", "listenerInvoked");
        } else if (Objects.equals(execution.getCurrentActivityId(), "myProcess-script")) {
            execution.setVariable("scriptEnd", "listenerInvoked");
            execution.setVariable("scriptEndFooValue", execution.getVariable("foo"));
        }
    }

}

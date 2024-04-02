package test.bpmn_parser;

import org.flowable.engine.HistoryService;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.test.Deployment;
import org.flowable.engine.test.FlowableTest;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@FlowableTest
public class CustomParseHandlerTest {

    private static final String TENANT = "some.tenant";

    @BeforeEach
    void setup(final ProcessEngineConfiguration peConfig) {
        if (peConfig instanceof final ProcessEngineConfigurationImpl impl) {
            impl.setPostBpmnParseHandlers(List.of(new CustomPreParseHandler()));
        }
    }

    @Test
    @Deployment(tenantId = TENANT, resources = "processes/process-staticallyEmbeddedListeners.bpmn20.xml")
    public void testStaticListenersBehaveTheWayIdExpectDynamicListenersToBehave(final RuntimeService runtimeService,
            final HistoryService historyService) {
        validateProcessExecution(runtimeService, historyService);
    }

    @Test
    @Deployment(tenantId = TENANT, resources = "processes/process-dynamicallyEmbeddedListeners.bpmn20.xml")
    public void testParserDynamicallyEmbedsExecutionListener(final RuntimeService runtimeService,
            final HistoryService historyService) {
        validateProcessExecution(runtimeService, historyService);
    }

    private void validateProcessExecution(final RuntimeService runtimeService, final HistoryService historyService) {
        final ProcessInstance proc = runtimeService.startProcessInstanceByKeyAndTenantId("myProcessId", TENANT);
        final String procId = proc.getProcessInstanceId();

        final HistoricVariableInstance fooVariable = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(procId)
                .variableName("foo")
                .singleResult();
        final HistoricVariableInstance processStartVariable = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(procId)
                .variableName("processStart")
                .singleResult();
        final HistoricVariableInstance scriptEndVariable = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(procId)
                .variableName("scriptEnd")
                .singleResult();
        final HistoricVariableInstance scriptEndFooVariable = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(procId)
                .variableName("scriptEndFooValue")
                .singleResult();

        assertThat(fooVariable).isNotNull();
        assertThat(fooVariable.getValue()).isEqualTo("bar");
        assertThat(processStartVariable).isNotNull();
        assertThat(processStartVariable.getValue()).isEqualTo("listenerInvoked");
        assertThat(scriptEndVariable).isNotNull();
        assertThat(scriptEndVariable.getValue()).isEqualTo("listenerInvoked");
        assertThat(scriptEndFooVariable).isNotNull();
        assertThat(scriptEndFooVariable.getValue()).isEqualTo("bar");
    }

}

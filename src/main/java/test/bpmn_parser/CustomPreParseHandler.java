package test.bpmn_parser;

import org.flowable.bpmn.model.BaseElement;
import org.flowable.bpmn.model.ExtensionAttribute;
import org.flowable.bpmn.model.ExtensionElement;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.ScriptTask;
import org.flowable.engine.impl.bpmn.parser.BpmnParse;
import org.flowable.engine.parse.BpmnParseHandler;

import java.util.Collection;
import java.util.List;

import static org.flowable.bpmn.constants.BpmnXMLConstants.ATTRIBUTE_LISTENER_CLASS;
import static org.flowable.bpmn.constants.BpmnXMLConstants.ATTRIBUTE_LISTENER_EVENT;
import static org.flowable.bpmn.constants.BpmnXMLConstants.ELEMENT_EXECUTION_LISTENER;
import static org.flowable.bpmn.constants.BpmnXMLConstants.FLOWABLE_EXTENSIONS_NAMESPACE;
import static org.flowable.bpmn.constants.BpmnXMLConstants.FLOWABLE_EXTENSIONS_PREFIX;
import static org.flowable.engine.delegate.BaseExecutionListener.EVENTNAME_END;
import static org.flowable.engine.delegate.BaseExecutionListener.EVENTNAME_START;

/**
 * Parse handler that adds our execution listener to our Process and ScriptTask
 * elements at the time they are deserialized from the BPMN XML stored in the
 * database.
 * <p/>
 * Think of this like an AOP cut-point where instead of updating all our XML
 * documents manually, this programmatically adds what we need at runtime
 * as the documents are loaded into memory.
 *
 * @author chasebarrett
 * @since 2024-03-24
 */
public class CustomPreParseHandler implements BpmnParseHandler {

    private final ExtensionElement startExtensionElement;
    private final ExtensionElement endExtensionElement;

    public CustomPreParseHandler() {
        final ExtensionAttribute classAttr = new ExtensionAttribute(ATTRIBUTE_LISTENER_CLASS);
        classAttr.setValue("test.bpmn_parser.CustomExecutionListener");
        final ExtensionAttribute startEventAttr = new ExtensionAttribute(ATTRIBUTE_LISTENER_EVENT);
        startEventAttr.setValue(EVENTNAME_START);
        final ExtensionAttribute endEventAttr = new ExtensionAttribute(ATTRIBUTE_LISTENER_EVENT);
        endEventAttr.setValue(EVENTNAME_END);

        startExtensionElement = new ExtensionElement();
        startExtensionElement.setName(ELEMENT_EXECUTION_LISTENER);
        startExtensionElement.setNamespace(FLOWABLE_EXTENSIONS_NAMESPACE);
        startExtensionElement.setNamespacePrefix(FLOWABLE_EXTENSIONS_PREFIX);
        startExtensionElement.addAttribute(classAttr);
        startExtensionElement.addAttribute(startEventAttr);

        endExtensionElement = new ExtensionElement();
        endExtensionElement.setName(ELEMENT_EXECUTION_LISTENER);
        endExtensionElement.setNamespace(FLOWABLE_EXTENSIONS_NAMESPACE);
        endExtensionElement.setNamespacePrefix(FLOWABLE_EXTENSIONS_PREFIX);
        endExtensionElement.addAttribute(classAttr);
        endExtensionElement.addAttribute(endEventAttr);
    }

    @Override
    public Collection<Class<? extends BaseElement>> getHandledTypes() {
        return List.of(Process.class, ScriptTask.class);
    }

    @Override
    public void parse(final BpmnParse bpmnParse, final BaseElement element) {
        if (element instanceof Process) {
            element.addExtensionElement(startExtensionElement);
        } else {
            element.addExtensionElement(endExtensionElement);
        }
    }

}

This is a test of the [BpmnParseHandler](https://github.com/flowable/flowable-engine/blob/main/modules/flowable-engine/src/main/java/org/flowable/engine/parse/BpmnParseHandler.java) interface. Spefically, it demonstrates how to use the parse handler in the manner described [here](https://www.flowable.com/open-source/docs/bpmn/ch18-Advanced/#hooking-into-process-parsing) to register an [ExecutionListener](https://github.com/flowable/flowable-engine/blob/main/modules/flowable-engine/src/main/java/org/flowable/engine/delegate/ExecutionListener.java) that responds to:

* The start of a new process
* The end of a script task

I put this repo together because I'm not able to register execution listeners with the BPMN Parse Handler, but I am able to do so with execution listener extensions coded into the BPMN XML. 

Build the project with `mvn clean install` at the repo root, and you'll see there are [two tests](https://github.com/chaserb/bpmn-parser-test/blob/main/src/test/java/test/bpmn_parser/CustomParseHandlerTest.java). The encoded BPMN XML test behaves as expected, but the dynamic extension registration fails to produce an execution listener. 

Here's a screencap from the debugger of the test that passes:
![successful execution listener registration](src/test/resources/misc/Debugger%20-%20Static%20Listener%20Extensions%20Are%20Translated%20to%20Runtime%20Listeners.png)

Here's a screencap from the debugger of the test that fails:
![failed execution listener registration](src/test/resources/misc/Debugger%20-%20Dynamic%20Listener%20Extensions%20Are%20Not%20Translated%20to%20Runtime%20Listeners.png)

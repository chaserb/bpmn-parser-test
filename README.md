This is a test of the [BpmnParseHandler](https://github.com/flowable/flowable-engine/blob/main/modules/flowable-engine/src/main/java/org/flowable/engine/parse/BpmnParseHandler.java) interface, and specifically, how to use the parse handler to register an [EventListener](https://github.com/flowable/flowable-engine/blob/main/modules/flowable-bpmn-model/src/main/java/org/flowable/bpmn/model/EventListener.java) that responds to:

* The start of a new process
* The end of a script task

I put this repo together because I'm not able to register event listeners with the BPMN Parse Handler, but I am able to do so with event listener extensions coded into the BPMN XML. 

Build the project with `mvn clean install` at the repo root, and you'll see there are two tests. The encoded BPMN XML test behaves as expected, but the dynamic extension registration fails to produce an event listener. 

Here's a screencap from the debugger of the test that passes:
![successful event listener registration](src/test/resources/misc/Debugger%20-%20Static%20Listener%20Extensions%20Are%20Translated%20to%20Runtime%20Listeners.png)

Here's a screencap from the debugger of the test that fails:
![failed event listener registration](src/test/resources/misc/Debugger%20-%20Dynamic%20Listener%20Extensions%20Are%20Not%20Translated%20to%20Runtime%20Listeners.png)

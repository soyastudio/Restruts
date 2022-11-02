package soya.framework.restruts.pattern;

import soya.framework.action.orchestration.eventbus.ActionDispatchSubscriber;
import soya.framework.action.orchestration.eventbus.EventSubscriber;

@EventSubscriber(address = "eventbus://abc")
public class ActionEventDispatcher extends ActionDispatchSubscriber {

}

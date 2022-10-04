package soya.framework.action.dispatch.pattern;

import soya.framework.action.dispatch.eventbus.ActionDispatchSubscriber;
import soya.framework.action.dispatch.eventbus.EventSubscriber;

@EventSubscriber(address = "eventbus://abc")
public class ActionEventDispatcher extends ActionDispatchSubscriber {

}

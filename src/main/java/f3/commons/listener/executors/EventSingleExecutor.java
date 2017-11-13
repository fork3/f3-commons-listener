/*
 * Copyright (c) 2010-2016 fork3
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES 
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE 
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR 
 * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package f3.commons.listener.executors;

import java.util.Set;
import java.util.function.Consumer;

import f3.commons.listener.IEventExecutor;
import f3.commons.listener.event.Listener;
import f3.commons.listener.predicate.Event;
import f3.commons.listener.predicate.ICallStrategy;
import f3.commons.listener.predicate.Predicate;

/**
 * @author n3k0nation
 *
 */
public class EventSingleExecutor<T> implements IEventExecutor<T> {
	public EventSingleExecutor() {
	}
	
	@Override
	@SafeVarargs
	public final <Type extends Listener<T>> void callEvent(Class<Type> clazz, Consumer<Type> consumer, Set<Listener<T>>...observes) {
		for(int i = 0; i < observes.length; i++) {
			callEvent(observes[i], clazz, consumer);
		}
	}
	
	@SuppressWarnings("unchecked")
	private <Type extends Listener<T>> void callEvent(Set<Listener<T>> observes, Class<Type> clazz, Consumer<Type> consumer) {
		for(Listener<T> eventable : observes) {
			if(!clazz.isInstance(eventable))
				continue;
			
			consumer.accept((Type) eventable);
		}
	}
	
	@SuppressWarnings("unchecked")
	private <Type extends Predicate<T>> boolean callPredicate(Set<Listener<T>> observes, Class<Type> clazz, Event event, ICallStrategy strategy) {
		for(Listener<T> eventable : observes) {
			if(!clazz.isInstance(eventable))
				continue;
			
			final Type predicate = (Type) eventable;
			predicate.call(event);
			if(!strategy.isAllowNextProcessing(event)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	@SafeVarargs
	public final <Type extends Predicate<T>> void callPredicate(Class<Type> clazz, Event event, ICallStrategy strategy, Set<Listener<T>>...observes) {
		for(int i = 0; i < observes.length; i++) {
			callPredicate(observes[i], clazz, event , strategy);
		}
	}
}

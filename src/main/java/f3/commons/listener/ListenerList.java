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
package f3.commons.listener;

import java.util.Set;
import java.util.function.Consumer;

import f3.commons.listener.event.Listener;
import f3.commons.listener.predicate.CallStrategy;
import f3.commons.listener.predicate.Event;
import f3.commons.listener.predicate.ICallStrategy;
import f3.commons.listener.predicate.Predicate;
import lombok.Getter;
import lombok.NonNull;

/**
 * @author n3k0nation
 *
 */
public abstract class ListenerList<T> {
	@Getter private final Set<Listener<T>> observes;
	@Getter private final IEventExecutor<T> executor;
	@Getter private final T owner;
	
	protected ListenerList(T owner, IEventExecutor<T> executor, Set<Listener<T>> observes) {
		this.owner = owner;
		this.executor = executor;
		this.observes = observes;
	}
	
	@SuppressWarnings("unchecked")
	protected final <Type extends Listener<T>> void callEvent(Class<Type> clazz, Consumer<Type> consumer) {
		executor.callEvent(clazz, consumer, observes);
	}
	
	/** call default and custom */
	@SuppressWarnings("unchecked")
	protected final <Type extends Listener<T>> void callEvent(Class<Type> clazz, Consumer<Type> consumer, Set<Listener<T>> global) {
		executor.callEvent(clazz, consumer, observes, global);
	}
	
	@SuppressWarnings("unchecked")
	protected final <Type extends Predicate<T>> void callPredicate(Class<Type> clazz, Event event, ICallStrategy strategy) {
		executor.callPredicate(clazz, event, strategy, observes);
	}
	
	/** call predicate default and custom */
	@SuppressWarnings("unchecked")
	protected final <Type extends Predicate<T>> void callPredicate(Class<Type> clazz, Event event, ICallStrategy strategy, Set<Listener<T>> global) {
		executor.callPredicate(clazz, event, strategy, observes, global);
	}
	
	protected final <Type extends Predicate<T>> void callPredicate(Class<Type> clazz, Event event) {
		callPredicate(clazz, event, CallStrategy.getDefault());
	}
	
	/** call predicate default and custom with default call strategy */
	protected final <Type extends Predicate<T>> void callPredicate(Class<Type> clazz, Event event, Set<Listener<T>> global) {
		callPredicate(clazz, event, CallStrategy.getDefault(), global);
	}
	
	public boolean add(@NonNull Listener<T> eventable) {
		return observes.add(eventable);
	}
	
	public boolean remove(@NonNull Listener<T> eventable) {
		return observes.remove(eventable);
	}
	
	public void close() {
		for(Listener<T> eventable : observes) {
			eventable.close();
		}
	}
}

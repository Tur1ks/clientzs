package ravage.client.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class EventManager {
    private static EventManager instance;
    private Map<Class<? extends Event>, List<Consumer<Event>>> listeners = new HashMap<>();

    public EventManager() {
        instance = this;
    }

    public <T extends Event> void register(Class<T> eventClass, Consumer<T> listener) {
        if (!listeners.containsKey(eventClass)) {
            listeners.put(eventClass, new ArrayList<>());
        }
        listeners.get(eventClass).add((Consumer<Event>) listener);
    }

    public <T extends Event> void unregister(Class<T> eventClass, Consumer<T> listener) {
        if (listeners.containsKey(eventClass)) {
            listeners.get(eventClass).remove(listener);
        }
    }

    public <T extends Event> void post(T event) {
        if (listeners.containsKey(event.getClass())) {
            for (Consumer<Event> listener : listeners.get(event.getClass())) {
                listener.accept(event);
            }
        }
    }

    public static EventManager getInstance() {
        return instance;
    }












    private static final EventManager INSTANCE = new EventManager();
    private final List<EventListener> listeners1 = new CopyOnWriteArrayList<>();

    public void register(EventListener listener) {
        if (!listeners1.contains(listener)) {
            listeners1.add(listener);
        }
    }

    public void unregister(EventListener listener) {
        listeners1.remove(listener);
    }
    public void call(Event event) {
        for (EventListener listener : listeners1) {
            try {
                listener.onEvent(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
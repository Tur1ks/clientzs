package ravage.client.event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Event {
    private boolean cancelled;

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
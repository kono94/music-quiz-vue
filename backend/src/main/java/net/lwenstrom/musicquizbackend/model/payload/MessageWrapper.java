package net.lwenstrom.musicquizbackend.model.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.lwenstrom.musicquizbackend.model.Event;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageWrapper<T> {
   private Event event;
   private T payload;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}

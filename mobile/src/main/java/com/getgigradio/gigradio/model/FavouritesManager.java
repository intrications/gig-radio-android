package com.getgigradio.gigradio.model;

import android.content.Context;

import com.colintmiller.simplenosql.NoSQL;
import com.colintmiller.simplenosql.NoSQLEntity;
import com.getgigradio.gigradio.model.songkick.event.Event;

public class FavouritesManager {

    private static final String FAVOURITE_EVENTS = "favourite_events";

    public void saveAsFavourite(Context context, Event event) {
        NoSQLEntity<Event> entity = new NoSQLEntity<>(FAVOURITE_EVENTS,
                event.getId().toString());
        entity.setData(event);
        NoSQL.with(context).using(Event.class).save(entity);
    }

    public void removeFromFavourites(Context context, Event event) {
        NoSQL.with(context).using(Event.class)
                .bucketId(FAVOURITE_EVENTS)
                .entityId(event.getId().toString())
                .delete();
    }

    public void isFavourite(Context context, Event event, IsFavouriteRetrievalCallback callback) {
        NoSQL.with(context).using(Event.class)
                .bucketId(FAVOURITE_EVENTS)
                .entityId(event.getId().toString())
                .retrieve(events -> callback.retrievedResults(events.size() > 0));
    }

    public interface IsFavouriteRetrievalCallback {

        public void retrievedResults(boolean isFavourite);
    }
}

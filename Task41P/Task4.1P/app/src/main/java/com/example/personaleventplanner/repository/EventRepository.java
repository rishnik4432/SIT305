package com.example.personaleventplanner.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.example.personaleventplanner.data.Event;
import com.example.personaleventplanner.data.EventDao;
import com.example.personaleventplanner.data.EventDatabase;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class EventRepository {
    private EventDao eventDao;
    private LiveData<List<Event>> allEvents;
    private ExecutorService executorService;

    public EventRepository(Context context) {
        EventDatabase db = EventDatabase.getDatabase(context);
        eventDao = db.eventDao();
        allEvents = eventDao.getAllEvents();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Event>> getAllEvents() {
        return allEvents;
    }

    public void insert(final Event event) {
        executorService.execute(() -> eventDao.insert(event));
    }

    public void update(final Event event) {
        executorService.execute(() -> eventDao.update(event));
    }

    public void delete(final Event event) {
        executorService.execute(() -> eventDao.delete(event));
    }

    public Event getEventById(long id) {
        // Must be called on background thread – we'll handle in ViewModel
        return eventDao.getEventById(id);
    }
}

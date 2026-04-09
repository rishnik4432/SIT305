package com.example.personaleventplanner.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.personaleventplanner.data.Event;
import com.example.personaleventplanner.repository.EventRepository;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class EventViewModel extends AndroidViewModel {
    private EventRepository repository;
    private LiveData<List<Event>> allEvents;
    private ExecutorService executorService;

    public EventViewModel(Application application) {
        super(application);
        repository = new EventRepository(application);
        allEvents = repository.getAllEvents();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Event>> getAllEvents() {
        return allEvents;
    }

    public void insert(Event event) {
        executorService.execute(() -> repository.insert(event));
    }

    public void update(Event event) {
        executorService.execute(() -> repository.update(event));
    }

    public void delete(Event event) {
        executorService.execute(() -> repository.delete(event));
    }

    // For editing: fetch event by id (blocking, but done on background thread)
    public Event getEventById(long id) {
        return repository.getEventById(id);
    }
}

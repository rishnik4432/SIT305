package com.example.personaleventplanner.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.personaleventplanner.R;
import com.example.personaleventplanner.data.Event;
import com.example.personaleventplanner.ui.adapters.EventAdapter;
import com.example.personaleventplanner.viewmodel.EventViewModel;
import java.util.List;

public class EventListFragment extends Fragment {
    private EventViewModel viewModel;
    private EventAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerViewEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EventAdapter();
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);

        viewModel.getAllEvents().observe(getViewLifecycleOwner(), new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                adapter.submitList(events);
            }
        });

        adapter.setOnItemClickListener(new EventAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(Event event) {
                Bundle bundle = new Bundle();
                bundle.putLong("eventId", event.getId());
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_navigation_event_list_to_navigation_add_event, bundle);
            }

            @Override
            public void onDeleteClick(Event event) {
                viewModel.delete(event);
                Toast.makeText(getContext(), "Event deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //Force adapter to refresh its data from the ViewModel's current LiveData
        adapter.notifyDataSetChanged();
    }
}
package com.example.personaleventplanner.ui.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.example.personaleventplanner.R;
import com.example.personaleventplanner.data.Event;
import com.example.personaleventplanner.viewmodel.EventViewModel;
import java.util.Calendar;
import java.util.Date;

public class AddEditEventFragment extends Fragment {
    private EditText editTitle, editLocation;
    private Spinner spinnerCategory;
    private Button btnPickDateTime, btnSave;
    private TextView tvSelectedDateTime;
    private EventViewModel viewModel;
    private Date selectedDateTime = null;
    private boolean isEditMode = false;
    private long existingEventId = -1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_edit_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTitle = view.findViewById(R.id.editTitle);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        editLocation = view.findViewById(R.id.editLocation);
        btnPickDateTime = view.findViewById(R.id.btnPickDateTime);
        tvSelectedDateTime = view.findViewById(R.id.tvSelectedDateTime);
        btnSave = view.findViewById(R.id.btnSave);

        //Reduce lag by disabling suggestions
        editTitle.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        editLocation.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);


        Log.d("AddEditEvent", "Fragment created");

        viewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);

        // Setup category spinner
        String[] categories = {"Work", "Social", "Travel"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
        //Force dropdown to open on touch
        spinnerCategory.setOnTouchListener((v, event) -> {
            v.performClick();
            return false;
        });

        // Check if editing
        if (getArguments() != null && getArguments().containsKey("eventId")) {
            isEditMode = true;
            existingEventId = getArguments().getLong("eventId");
            loadEventData();
        }

        btnPickDateTime.setOnClickListener(v -> showDateTimePicker());
        btnSave.setOnClickListener(v -> saveEvent());
    }

    private void showDateTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePicker = new DatePickerDialog(requireActivity(),
                (view, year, month, dayOfMonth) -> {
                    TimePickerDialog timePicker = new TimePickerDialog(requireActivity(),
                            (timeView, hourOfDay, minute) -> {
                                Calendar selectedCal = Calendar.getInstance();
                                selectedCal.set(year, month, dayOfMonth, hourOfDay, minute);
                                selectedDateTime = selectedCal.getTime();
                                tvSelectedDateTime.setText(android.text.format.DateFormat.format("MMM dd, yyyy HH:mm", selectedDateTime));
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true);
                    timePicker.show();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePicker.show();
    }

    private void loadEventData() {
        new Thread(() -> {
            Event event = viewModel.getEventById(existingEventId);
            if (event != null) {
                requireActivity().runOnUiThread(() -> {
                    editTitle.setText(event.getTitle());
                    String[] categories = {"Work", "Social", "Travel"};
                    for (int i = 0; i < categories.length; i++) {
                        if (categories[i].equals(event.getCategory())) {
                            spinnerCategory.setSelection(i);
                            break;
                        }
                    }
                    editLocation.setText(event.getLocation());
                    selectedDateTime = event.getDateTime();
                    tvSelectedDateTime.setText(android.text.format.DateFormat.format("MMM dd, yyyy HH:mm", selectedDateTime));
                });
            }
        }).start();
    }

    private void saveEvent() {
        String title = editTitle.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();
        String location = editLocation.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(requireContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedDateTime == null) {
            Toast.makeText(requireContext(), "Please select date & time", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedDateTime.before(new Date())) {
            Toast.makeText(requireContext(), "Cannot select a past date", Toast.LENGTH_SHORT).show();
            return;
        }

        Event event = new Event(title, category, location, selectedDateTime);
        if (isEditMode) {
            event.setId(existingEventId);
            viewModel.update(event);
            Toast.makeText(requireContext(), "Event updated", Toast.LENGTH_SHORT).show();
            Log.d("AddEditEvent", "Event updated: " + event.getTitle());
        } else {
            viewModel.insert(event);
            Toast.makeText(requireContext(), "Event added", Toast.LENGTH_SHORT).show();
            Log.d("AddEditEvent", "Event inserted: " + event.getTitle());
        }
        Navigation.findNavController(requireView()).navigateUp();
    }
}

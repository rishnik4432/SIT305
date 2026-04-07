package com.example.task21p;

import android.os.Bundle;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerSource, spinnerDest;
    private EditText editTextValue;
    private TextView textViewResult;
    private Button btnConvert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerSource = findViewById(R.id.spinnerSource);
        spinnerDest = findViewById(R.id.spinnerDest);
        editTextValue = findViewById(R.id.editTextValue);
        textViewResult = findViewById(R.id.textViewResult);
        btnConvert = findViewById(R.id.btnConvert);

        //Create the list of UnitItems with headers
        List<UnitItem> unitItems = getUnitItems();

        //Create custom adapter
        UnitAdapter adapter = new UnitAdapter(this, unitItems);

        //Set adapter to both spinners
        spinnerSource.setAdapter(adapter);
        spinnerDest.setAdapter(adapter);

        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                performConversion();
            }
        });
    }

    private List<UnitItem> getUnitItems() {
        List<UnitItem> items = new ArrayList<>();

        //Currency section
        items.add(new UnitItem(UnitItem.TYPE_HEADER, "Currency"));
        items.add(new UnitItem(UnitItem.TYPE_UNIT, "USD"));
        items.add(new UnitItem(UnitItem.TYPE_UNIT, "AUD"));

        //Fuel Efficiency section
        items.add(new UnitItem(UnitItem.TYPE_HEADER, "Fuel Efficiency"));
        items.add(new UnitItem(UnitItem.TYPE_UNIT, "mpg"));
        items.add(new UnitItem(UnitItem.TYPE_UNIT, "km/L"));

        //Temperature section
        items.add(new UnitItem(UnitItem.TYPE_HEADER, "Temperature"));
        items.add(new UnitItem(UnitItem.TYPE_UNIT, "Celsius"));
        items.add(new UnitItem(UnitItem.TYPE_UNIT, "Fahrenheit"));

        return items;
    }
    private class UnitAdapter extends ArrayAdapter<UnitItem> {
        public UnitAdapter(Context context, List<UnitItem> items) {
            super(context, 0, items);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent, false);
        }
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent, true);
        }
        private View getCustomView(int position, View convertView, ViewGroup parent, boolean isDropDown) {
            UnitItem item = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            }
            TextView textView = convertView.findViewById(android.R.id.text1);
            textView.setText(item.getName());

            if (item.getType() == UnitItem.TYPE_HEADER) {
                textView.setTypeface(null, Typeface.BOLD); //make header bold
                textView.setBackgroundColor(getContext().getResources().getColor(android.R.color.darker_gray));
            } else {
                textView.setTypeface(null, Typeface.NORMAL);
                textView.setBackgroundColor(getContext().getResources().getColor(android.R.color.transparent));
            }
            return convertView;
        }
        @Override
        public boolean isEnabled(int position) {
            //Disable headers (cannot be selected)
            return getItem(position).getType() == UnitItem.TYPE_UNIT;
        }
    }

    private void performConversion() {
        //Get selected UnitItem objects
        UnitItem sourceItem = (UnitItem) spinnerSource.getSelectedItem();
        UnitItem destItem = (UnitItem) spinnerDest.getSelectedItem();

        //Extract the unit names
        String source = sourceItem.getName();
        String dest = destItem.getName();

        String inputStr = editTextValue.getText().toString().trim();

        //validate empty input
        if (inputStr.isEmpty()) {
            Toast.makeText(this, "Please enter a value", Toast.LENGTH_SHORT).show();
            return;
        }

        double inputValue;
        try {
            inputValue = Double.parseDouble(inputStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number format", Toast.LENGTH_SHORT).show();
            return;
        }

        //Check for negative fuel efficiency
        if ((source.equals("mpg") || source.equals("km/L") || dest.equals("mpg") || dest.equals("km/L")) && inputValue < 0) {
            Toast.makeText(this, "Fuel efficiency cannot be negative", Toast.LENGTH_SHORT).show();
            return;
        }

        //Check if units are compatible
        if (!areCompatible(source, dest)) {
            Toast.makeText(this, "Incompatible unit types", Toast.LENGTH_SHORT).show();
            return;
        }

        //Perform conversion
        double result = convert(source, dest, inputValue);

        //Display result
        textViewResult.setText(String.format("%.4f %s", result, dest));
    }

    private boolean areCompatible(String unit1, String unit2) {
        //Define unit categories
        if (unit1.equals(unit2)) return true; //identity always allowed

        String[] currencies = {"USD", "AUD"};
        String[] fuel = {"mpg", "km/L"};
        String[] temperature = {"Celsius", "Fahrenheit"};

        return (contains(currencies, unit1) && contains(currencies, unit2)) ||
                (contains(fuel, unit1) && contains(fuel, unit2)) ||
                (contains(temperature, unit1) && contains(temperature, unit2));
    }

    private boolean contains(String[] array, String value) {
        for (String s : array) {
            if (s.equals(value)) return true;
        }
        return false;
    }

    private double convert(String source, String dest, double value) {
        //Identify conversion
        if (source.equals(dest)) return value;

        //currency conversion
        //Rates: 1 USD = 1.55AUD
        double usdRate = getUSDRate(source);
        double destRate = getUSDRate(dest);
        if (usdRate != -1 && destRate != -1) {
            //Convert source to USD, then to dest
            double usdValue = value / usdRate;
            return usdValue * destRate;
        }

        //Fuel efficiency: mpg - km/L, km/L - mpg
        if (source.equals("mpg") && dest.equals("km/L")) return value * 0.425;
        if (source.equals("km/L") && dest.equals("mpg")) return value / 0.425;

        //Temperature conversion: Celsius - Fahrenheit, Fahrenheit - Celsius
        if (source.equals("Celsius") && dest.equals("Fahrenheit")) return value * 1.8 + 32;
        if (source.equals("Fahrenheit") && dest.equals("Celsius")) return (value - 32) / 1.8;

        //should not reach here if compatibility check passed
        return 0;
    }

    private double getUSDRate(String currency) {
        switch (currency) {
            case "USD" : return 1.0;
            case "AUD" : return 1.55;
            default: return -1; // not a currency
        }
    }
}
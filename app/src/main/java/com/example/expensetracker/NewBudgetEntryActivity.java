package com.example.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.ChipGroup;

/*
 * The NewBudgetEntryActivity is used to add a new BudgetEntry. Data from the different inputs is
 * stored and then sent via an intent back to the Class that called it.
 *
 * Corresponding Layout: activity_new_budget_entry
 *
 * Author: Shivam Sood
 * Date: 2020-06-20
 */

public class NewBudgetEntryActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    // Extra reply tags
    public static final String EXTRA_NAME = "ENTRY_NAME";
    public static final String EXTRA_VALUE = "ENTRY_VALUE";
    public static final String EXTRA_ISEXPENSE = "ENTRY_ISEXPENSE";
    public static final String EXTRA_FREQUENCY = "ENTRY_FREQUENCY";
    public static final String EXTRA_EXPENSE_TYPE = "ENTRY_EXPENSE_TYPE";

    // Variables to store inputted data
    private boolean isExpense = true;
    private int entryFrequency, entryExpenseType;
    private double entryValue;
    private String entryName;

    // View components used as inputs
    private EditText mTxtEntryName, mTxtEntryValue;
    private ChipGroup expenseTypeGroup;

    private Intent replyIntent;

    /*
     * Method runs on activity creation and is responsible for setting up the default state of the
     * activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_budget_entry);

        // Get TextView components
        mTxtEntryName = findViewById(R.id.entryName_editText);
        mTxtEntryValue = findViewById(R.id.entryValue_editText);

        // Create the spinner
        Spinner frequencySpinner = findViewById(R.id.frequency_spinner);
        if (frequencySpinner != null) {
            frequencySpinner.setOnItemSelectedListener(this);
        }

        // Create ArrayAdapter using the string array and default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.labels_array, android.R.layout.simple_spinner_item);

        // Specify layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // apply the adapter to the spinner
        if (frequencySpinner != null) {
            frequencySpinner.setAdapter(adapter);
        }

        // get add entry button and set a button on click listener
        final Button button = findViewById(R.id.addEntry_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                replyIntent = new Intent();
                // if edit texts are empty cancel adding new entry
                if (TextUtils.isEmpty(mTxtEntryValue.getText()) ||
                        TextUtils.isEmpty(mTxtEntryName.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    try {
                        // get data from different Views and return that data
                        entryName = mTxtEntryName.getText().toString();
                        entryValue = Double.parseDouble(mTxtEntryValue.getText().toString());
                        replyIntent.putExtra(EXTRA_NAME, entryName);
                        replyIntent.putExtra(EXTRA_VALUE, entryValue);
                        replyIntent.putExtra(EXTRA_ISEXPENSE, isExpense);
                        replyIntent.putExtra(EXTRA_FREQUENCY, entryFrequency);
                        replyIntent.putExtra(EXTRA_EXPENSE_TYPE, entryExpenseType);
                        setResult(RESULT_OK, replyIntent);
                        // if entryValue is not a double cancel adding new activity
                    } catch (IllegalArgumentException e) {
                        setResult(RESULT_CANCELED, replyIntent);
                    }
                }
                finish();
            }
        });

        // Get chip group and set onCheckedChange Listener
        expenseTypeGroup = findViewById(R.id.expense_type_chip_group);

        expenseTypeGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            // Depending on the chip selected store the corresponding entryExpenseType value
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rent_chip:
                        entryExpenseType = BudgetEntry.RENT_EXPENSE;
                        break;
                    case R.id.food_chip:
                        entryExpenseType = BudgetEntry.FOOD_EXPENSE;
                        break;
                    case R.id.bills_chip:
                        entryExpenseType = BudgetEntry.BILLS_EXPENSE;
                        break;
                    case R.id.shopping_chip:
                        entryExpenseType = BudgetEntry.SHOPPING_EXPENSE;
                        break;
                    case R.id.other_chip:
                        entryExpenseType = BudgetEntry.OTHER_EXPENSE;
                        break;
                    default:
                        // Do Nothing
                        break;
                }
            }
        });
    }

    /*
     * Method to manage radio buttons clicks
     */
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        if (view.getId() == R.id.expense_radioButton) {
            // If the expense radio button is checked then store value in boolean isExpense
            if (checked) {
                isExpense = true;
                // Show expenseType ChipGroup for expenses
                expenseTypeGroup.setVisibility(View.VISIBLE);
            }
        } else if (view.getId() == R.id.revenue_radioButton){
            if (checked) {
                isExpense = false;
                // Hide expenseType ChipGroup since it cannot be used for income entries.
                // Set entryExpenseType to NONE to insure value is not considered
                expenseTypeGroup.setVisibility(View.GONE);
                entryExpenseType = BudgetEntry.NONE;
            }
        }
    }

    /*
     * Method to handle spinner selections
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Depending on the spinner position set the corresponding entry frequency value
        switch (position) {
            case 0:
                entryFrequency = BudgetEntry.DAILY;
                break;
            case 1:
                entryFrequency = BudgetEntry.WEEKLY;
                break;
            case 2:
                entryFrequency = BudgetEntry.MONTHLY;
                break;
            case 3:
                entryFrequency = BudgetEntry.YEARLY;
                break;
            case 4:
                entryFrequency = BudgetEntry.ONE_TIME;
                break;
            default:
                // Do nothing
                break;
        }
    }

    /*
     * Item will always be selected.
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }
}

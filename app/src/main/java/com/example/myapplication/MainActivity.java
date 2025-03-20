package com.example.myapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'myapplication' library on application startup.
    static {
        System.loadLibrary("myapplication");
    }

    private ActivityMainBinding binding;
    private LinearLayout linesContainer;
    private CheckBox lastChecked = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        linesContainer = binding.linesContainer;

        Button showSelectedButton = binding.showSelectedButton;
        showSelectedButton.setOnClickListener(v -> showSelectedLine());

        loadLinesFromJNI();
    }

    private void loadLinesFromJNI() {
        String inputStringFromJNI = stringFromJNI();
        if (TextUtils.isEmpty(inputStringFromJNI)) {
            Log.w("MyTag", "String from JNI is null or empty.");
            return;
        }

        String[] lines = inputStringFromJNI.split("\\n");
        for (String line : lines) {
            String trimmedLine = line.trim();
            if (!trimmedLine.isEmpty()) {
                addLineToLayout(trimmedLine);
            }
        }
    }

    private void addLineToLayout(String line) {
        // Create a horizontal LinearLayout to hold the CheckBox and TextView
        LinearLayout lineLayout = new LinearLayout(this);
        lineLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        lineLayout.setOrientation(LinearLayout.HORIZONTAL);

        // Create the CheckBox
        CheckBox checkBox = new CheckBox(this);
        checkBox.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (lastChecked != null && lastChecked != checkBox) {
                    lastChecked.setChecked(false);
                }
                lastChecked = checkBox;
            }
        });

        // Create the TextView
        TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setText(line);
        textView.setTextSize(18);

        // Add the CheckBox and TextView to the horizontal LinearLayout
        lineLayout.addView(checkBox);
        lineLayout.addView(textView);

        // Add the horizontal LinearLayout to the main vertical LinearLayout
        linesContainer.addView(lineLayout);
    }
public String selected_packg;
    public void showSelectedLine() {
        String selectedLine = "";
        for (int i = 0; i < linesContainer.getChildCount(); i++) {
            LinearLayout lineLayout = (LinearLayout) linesContainer.getChildAt(i);
            CheckBox checkBox = (CheckBox) lineLayout.getChildAt(0);
            TextView textView = (TextView) lineLayout.getChildAt(1);

            if (checkBox.isChecked()) {
                selectedLine = textView.getText().toString();
                break; // Exit the loop after finding the selected line
            }
        }

        Toast.makeText(this, "Selected  \n now check the Downloads\\extractor folder" , Toast.LENGTH_LONG).show();

         selected_packg = selectedLine.toString();
         // strip "packege " text from the string

        selected_packg = selected_packg.substring(8);

        // make the layout clear
        linesContainer.removeAllViews();
        // get the text from native function pathfromjni
        // show them in a text view
        String paths = getAppPath(selected_packg);
        TextView textView = new TextView(this);
        textView.setText(paths);
        linesContainer.addView(textView);
        // read the paths line by line by loop
        String[] lines = paths.split("\n");
        for (String line : lines) {
            String trimmedLine = line.trim();
            if (!trimmedLine.isEmpty()) {

                TextView textView__ = new TextView(this);
                textView__.setText("\n\n\n"+"empty"+line);
                linesContainer.addView(textView__);
                extractor(line.substring(8));
            }
            else {
                extractor(line.substring(8));
                Toast.makeText(this, "PATH: \n" + line, Toast.LENGTH_LONG).show();
                // show this line in a text view
                TextView textView_ = new TextView(this);
                textView_.setText("full"+line);
                linesContainer.addView(textView_);
            }
        }}

    /**
     * A native method that is implemented by the 'myapplication' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    public native String getAppPath(String selected_packg);
    public native String extractor(String line);
}
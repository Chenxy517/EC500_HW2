package ec500.hw2.p2;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
/**
 * Created by Group: Dnisde on 10.31.22
 *
 * Alternative way of construction of Help information page:
 * Create a new activity that used to showing the page of Instruction
 * that telling users how to use the APP in step by step.
 */
public class HelpActivity extends AppCompatActivity {

    private Button btnReturn;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupwindow);

        return_Main();

        // get the "HELP information" content from String HTML.
        String htmlAsString = getString(R.string.msg_instruction);
        Spanned htmlAsSpanned = Html.fromHtml(htmlAsString); // used by TextView

        //Set the HTML TextView content:
        EditText multiple_TextView = findViewById(R.id.instruction);
        multiple_TextView.setText(htmlAsSpanned);

        multiple_TextView.setFocusable(false);
        multiple_TextView.setClickable(true);

        makeToast("Need Help? Scroll Down to check all instructions.");
    }

    /**
     * Using intent: (Abstract description of an operation to be performed.)
     * to jump pages between "MainActivity" and "HelpActivity".
     */
    public void return_Main() {
        btnReturn = (Button) findViewById(R.id.Return);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Intent intent = new Intent();
                // Jump to Main Activity:
                intent.setClass(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });
    }

    /**
     * Show a Toast of the given string
     *
     * @param str The string to show in the Toast
     */
    public void makeToast(String str) {
        runOnUiThread(() -> Toast.makeText(this, str, Toast.LENGTH_LONG).show());
    }
}

package ec500.hw2.p0;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Group: Dnisde on 10.31.22
 *
 * Alternative way of construction of Help information page:
 * Create a new activity that used to showing the page of Instruction
 * that telling users how to use the APP in step by step.
 */
public class HelpActivity extends AppCompatActivity {

    private Button return_btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupwindow);

        return_Main();
    }

    /**
     * Using intent: (Abstract description of an operation to be performed.)
     * to jump pages between "MainActivity" and "HelpActivity".
     */
    public void return_Main() {
        return_btn = (Button) findViewById(R.id.Return);
        return_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                // Jump to Help Activity
                intent.setClass(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });
    }

}

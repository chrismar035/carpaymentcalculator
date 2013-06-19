package com.chrismar035.carpaymentcalculator;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        final Button calculate = (Button) findViewById(R.id.button);
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculate_and_display_payment();
            }
        });

        final TextView apr_view = (TextView) findViewById(R.id.apr);
        apr_view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                switch(keyCode)
                {
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                    case KeyEvent.KEYCODE_ENTER:
                        calculate_and_display_payment();
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void calculate_and_display_payment() {

        if(errors_present() == false) {
            TextView total_amount_view = (TextView) findViewById(R.id.total_amount);
            Float total_amount = Float.parseFloat(total_amount_view.getText().toString());

            TextView months_view = (TextView) findViewById(R.id.months);
            Float months = Float.parseFloat(months_view.getText().toString());

            TextView apr_view = (TextView) findViewById(R.id.apr);
            Float apr = Float.parseFloat(apr_view.getText().toString());

            double payment = calculatePayment(total_amount, months, apr);

            String payment_string = formatPayment(payment);

            TextView results_content = (TextView) findViewById(R.id.results_content);
            results_content.setText(payment_string);
        }

    }

    public boolean errors_present() {
        boolean errors_present = false;

        TextView total_amount_view = (TextView) findViewById(R.id.total_amount);
        if(total_amount_view.getText().toString().equals("")) {
            errors_present = true;
            total_amount_view.setError("how much will you be borrowing?");
        }

        TextView months_view = (TextView) findViewById(R.id.months);
        if(months_view.getText().toString().equals("")) {
            errors_present = true;
            months_view.setError("how long the loan be?");
        }

        TextView apr_view = (TextView) findViewById(R.id.apr);
        if(apr_view.getText().toString().equals("")) {
            errors_present = true;
            apr_view.setError("what APR will you be getting?");
        }

        return errors_present;
    }

    public double calculatePayment(double principal, double term, double apr) {
        if(apr >= 1) { apr = apr / 100; }
        apr = apr / 12;

        double denominator;
        denominator = Math.pow(1 + apr, term) - 1;
        double first_part;
        first_part = apr + (apr / denominator);
        if(principal < 1000) { principal = principal * 1000; }

        return first_part * principal;
    }

    public String formatPayment(double payment) {
        NumberFormat formatter;
        formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(payment);
    }
}

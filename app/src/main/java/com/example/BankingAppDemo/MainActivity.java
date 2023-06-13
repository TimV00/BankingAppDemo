package com.example.BankingAppDemo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    int i;
    int currentScreen;
    String history = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        currentScreen = 0;
    }

    // code to confirm the user credentials and changing to the transactions screen
    public void loginClick(View view){
        final EditText usernameEditText = findViewById(R.id.usernameEdit);
        final EditText passwordEditText = findViewById(R.id.passwordEdit);

        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        SharedPreferences loginData = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String currentUser = loginData.getString("userName", "");
        String currentPass = loginData.getString("password", "");
        Integer currentBal = loginData.getInt("balance", 0);

        if(username.equalsIgnoreCase(currentUser) && password.equals(currentPass)){
            Toast.makeText(MainActivity.this,"Login Successful", Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_transactions);
            currentScreen = 2;
            TextView usernameView = (TextView) findViewById(R.id.tv_username);
            usernameView.setText(currentUser);
            TextView balanceView = (TextView) findViewById(R.id.tv_balance);
            balanceView.setText(""+currentBal);
        }else
        {
            Toast.makeText(MainActivity.this,"Invalid Username or Password", Toast.LENGTH_LONG).show();
        }

    }

    // return to the previous screen
    public void returnClick(View view){
        if ((currentScreen == 1) || (currentScreen == 2)){
            setContentView(R.layout.activity_login);
            currentScreen = 0;
        } else if (currentScreen == 3){
            setContentView(R.layout.activity_transactions);
            currentScreen = 2;
            SharedPreferences loginData = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            String currentUser = loginData.getString("userName", "");
            String currentPass = loginData.getString("password", "");
            Integer currentBal = loginData.getInt("balance", 0);
            TextView usernameView = (TextView) findViewById(R.id.tv_username);
            usernameView.setText(currentUser);
            TextView balanceView = (TextView) findViewById(R.id.tv_balance);
            balanceView.setText(""+currentBal);
        }
    }

    // change the layout to show the registration screen
    public void registerScreen(View view){
        setContentView(R.layout.activity_registration);
        currentScreen = 1;
    }

    // change the layout to show the transaction history screen
    public void historyScreen(View view){
        setContentView(R.layout.activity_history);
        currentScreen = 3;
        TextView historyView = (TextView) findViewById(R.id.tv_history);
        historyView.setText(history);
    }

    // code to save the user information using shared preferences
    public void registerClick(View view) {
        final EditText usernameEditText = findViewById(R.id.newUsernameEdit);
        final EditText passwordEditText = findViewById(R.id.newPasswordEdit);
        final EditText depositEditText = findViewById(R.id.deposit);

        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String initDeposit = depositEditText.getText().toString();
        int bal = 0;
        if (!"".equals(initDeposit)) {
            bal = Integer.parseInt(initDeposit);
        }

        SharedPreferences loginData = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = loginData.edit();
        if (username.equals("") || password.equals("") || initDeposit.equals("")) {
            Toast.makeText(this, "Please make sure all fields are filled in", Toast.LENGTH_LONG).show();
        } else {
            editor.putString("userName", username);
            editor.putString("password", password);
            editor.putInt("balance", bal);
            editor.apply();
            Toast.makeText(this, "You have registered successfully, please login", Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_login);
            currentScreen = 0;
        }
    }

    // Code to add the deposit amount to the total balance
    public void depositClick(View view){
        final EditText balanceEditText = findViewById(R.id.balanceEdit);
        String newDeposit = balanceEditText.getText().toString();
        int deposit = 0;
        if (!"".equals(deposit)) {
            deposit = Integer.parseInt(newDeposit);
        }
        SharedPreferences loginData = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        Integer currentBal = loginData.getInt("balance", 0);

        currentBal+=deposit;
        SharedPreferences.Editor editor = loginData.edit();
        editor.putInt("balance", currentBal);
        editor.apply();
        Toast.makeText(this, "Deposit Successful", Toast.LENGTH_LONG).show();
        TextView balanceView = (TextView) findViewById(R.id.tv_balance);
        balanceView.setText(""+currentBal);
        updateHistory(0, deposit);
    }

    // Code to subtract the withdraw amount from the total balance
    public void withdrawClick(View view){
        final EditText balanceEditText = findViewById(R.id.balanceEdit);
        String newWithdrawal = balanceEditText.getText().toString();
        int withdrawal = 0;
        if (!"".equals(withdrawal)) {
            withdrawal = Integer.parseInt(newWithdrawal);
        }
        SharedPreferences loginData = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        Integer currentBal = loginData.getInt("balance", 0);

        if ((currentBal-withdrawal) <= 0){
            Toast.makeText(this, "Balance Too Low For Withdrawal Amount", Toast.LENGTH_LONG).show();
        } else{
            currentBal-=withdrawal;
            SharedPreferences.Editor editor = loginData.edit();
            editor.putInt("balance", currentBal);
            editor.apply();
            Toast.makeText(this, "Withdrawal Successful", Toast.LENGTH_LONG).show();
            TextView balanceView = (TextView) findViewById(R.id.tv_balance);
            balanceView.setText(""+currentBal);
            updateHistory(1, withdrawal);
        }
    }

    // Append the history string with the latest action done
    public void updateHistory(int type, int amount) {
        TextView historyTextView = findViewById(R.id.tv_history);
        if (type == 1){
            history += "Withdraw Amount: " + amount + "\n";
        } else{
            history += "Deposit Amount: " + amount + "\n";
        }
    }
}
package staddle.com.staddle.activity;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import staddle.com.staddle.R;

public class MobileLoginActivity extends AppCompatActivity {


    EditText MobileNumberInput;
    Button proceedBtn;
    String mobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_login);
        init();
        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobileNumber=MobileNumberInput.getText().toString();
                if(mobileNumber.length()==10){
                    makeIntent(mobileNumber);
                }else if(mobileNumber.length()==0){
                    Toast.makeText(getApplicationContext(),"Please enter a proper mobile number",Toast.LENGTH_LONG).show();
                }else{
                   Toast.makeText(getApplicationContext(),"Please enter a proper mobile number",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void init(){
        MobileNumberInput=findViewById(R.id.mobileeditetxt);
        proceedBtn=findViewById(R.id.proceedbtnmobile);
    }
    private void makeIntent(String mobileNumber){
        Intent intent=new Intent(MobileLoginActivity.this,MobileOtpActivity.class);
        intent.putExtra("mobile_number",mobileNumber);
        startActivity(intent);
    }

}

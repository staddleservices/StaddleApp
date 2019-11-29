package staddle.com.staddle.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;

import staddle.com.staddle.R;
import staddle.com.staddle.fragment.ShoppingFragment;

public class SchedulingActivity extends AppCompatActivity implements  DatePickerDialog.OnDateSetListener {

    DatePicker datePicker;
    Context context;

    //TimePickerTextViews
    TextView tp1;
    TextView tp2;
    TextView tp3;
    TextView tp4;
    TextView tp5;
    TextView tp6;
    TextView tp7;
    TextView tp8;
    TextView tp9;
    TextView tp10;
    TextView tp11;
    TextView tp12;
    TextView tp13;
    TextView tp14;
    Button dateSelectBtn;

    String selectedTime="";
    String SelectedDate="";
    String Nickname;
    String Address;

    EditText editTextDate;
    String date;
    int atHome=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduling);
        init();
        Intent intent=getIntent();
        Nickname=intent.getStringExtra("nickname");
        Address=intent.getStringExtra("address");
        atHome=Integer.parseInt(intent.getStringExtra("atHome"));
        editTextDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

                DatePickerDialog dialog = new DatePickerDialog(SchedulingActivity.this, SchedulingActivity.this::onDateSet,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        dateSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(atHome==1){
                    Intent intent = new Intent();
                    intent.putExtra("date", SelectedDate);
                    intent.putExtra("time", selectedTime);
                    setResult(ShoppingFragment.CAT1RESCODESALON, intent);
                    finish();
                }
                Intent intent = new Intent();
                intent.putExtra(AllAddressActivity.SELECTEDDATEKEY, SelectedDate);
                intent.putExtra(AllAddressActivity.SELECTEDTIMEKEY, selectedTime);
                intent.putExtra("nickname",Nickname);
                intent.putExtra("address",Address);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){

                    case R.id.tp1:
                    {
                        selectedTime="10 - 10:30 am";
                        tp1.setTextColor(getResources().getColor(R.color.app_color_2));
                        tp2.setTextColor(getResources().getColor(R.color.graybtns));
                        tp3.setTextColor(getResources().getColor(R.color.graybtns));
                        tp4.setTextColor(getResources().getColor(R.color.graybtns));
                        tp5.setTextColor(getResources().getColor(R.color.graybtns));
                        tp6.setTextColor(getResources().getColor(R.color.graybtns));
                        tp7.setTextColor(getResources().getColor(R.color.graybtns));
                        tp8.setTextColor(getResources().getColor(R.color.graybtns));
                        tp9.setTextColor(getResources().getColor(R.color.graybtns));
                        tp10.setTextColor(getResources().getColor(R.color.graybtns));
                        tp11.setTextColor(getResources().getColor(R.color.graybtns));
                        tp12.setTextColor(getResources().getColor(R.color.graybtns));
                        tp13.setTextColor(getResources().getColor(R.color.graybtns));
                        tp14.setTextColor(getResources().getColor(R.color.graybtns));
                        Toast.makeText(context, selectedTime, Toast.LENGTH_SHORT).show();

                        break;
                    }

                    case R.id.tp2:
                    {
                        selectedTime="10:30 - 11 am";
                        tp1.setTextColor(getResources().getColor(R.color.graybtns));
                        tp2.setTextColor(getResources().getColor(R.color.app_color_2));
                        tp3.setTextColor(getResources().getColor(R.color.graybtns));
                        tp4.setTextColor(getResources().getColor(R.color.graybtns));
                        tp5.setTextColor(getResources().getColor(R.color.graybtns));
                        tp6.setTextColor(getResources().getColor(R.color.graybtns));
                        tp7.setTextColor(getResources().getColor(R.color.graybtns));
                        tp8.setTextColor(getResources().getColor(R.color.graybtns));
                        tp9.setTextColor(getResources().getColor(R.color.graybtns));
                        tp10.setTextColor(getResources().getColor(R.color.graybtns));
                        tp11.setTextColor(getResources().getColor(R.color.graybtns));
                        tp12.setTextColor(getResources().getColor(R.color.graybtns));
                        tp13.setTextColor(getResources().getColor(R.color.graybtns));
                        tp14.setTextColor(getResources().getColor(R.color.graybtns));
                        Toast.makeText(context, selectedTime, Toast.LENGTH_SHORT).show();

                        break;
                    }
                    case R.id.tp3:
                    {
                        selectedTime="11 - 11:30 am";
                        tp1.setTextColor(getResources().getColor(R.color.graybtns));
                        tp2.setTextColor(getResources().getColor(R.color.graybtns));
                        tp3.setTextColor(getResources().getColor(R.color.app_color_2));
                        tp4.setTextColor(getResources().getColor(R.color.graybtns));
                        tp5.setTextColor(getResources().getColor(R.color.graybtns));
                        tp6.setTextColor(getResources().getColor(R.color.graybtns));
                        tp7.setTextColor(getResources().getColor(R.color.graybtns));
                        tp8.setTextColor(getResources().getColor(R.color.graybtns));
                        tp9.setTextColor(getResources().getColor(R.color.graybtns));
                        tp10.setTextColor(getResources().getColor(R.color.graybtns));
                        tp11.setTextColor(getResources().getColor(R.color.graybtns));
                        tp12.setTextColor(getResources().getColor(R.color.graybtns));
                        tp13.setTextColor(getResources().getColor(R.color.graybtns));
                        tp14.setTextColor(getResources().getColor(R.color.graybtns));
                        Toast.makeText(context, selectedTime, Toast.LENGTH_SHORT).show();

                        break;
                    }
                    case R.id.tp4:
                    {
                        selectedTime="11:30 - 12 pm";
                        tp1.setTextColor(getResources().getColor(R.color.graybtns));
                        tp2.setTextColor(getResources().getColor(R.color.graybtns));
                        tp3.setTextColor(getResources().getColor(R.color.graybtns));
                        tp4.setTextColor(getResources().getColor(R.color.app_color_2));
                        tp5.setTextColor(getResources().getColor(R.color.graybtns));
                        tp6.setTextColor(getResources().getColor(R.color.graybtns));
                        tp7.setTextColor(getResources().getColor(R.color.graybtns));
                        tp8.setTextColor(getResources().getColor(R.color.graybtns));
                        tp9.setTextColor(getResources().getColor(R.color.graybtns));
                        tp10.setTextColor(getResources().getColor(R.color.graybtns));
                        tp11.setTextColor(getResources().getColor(R.color.graybtns));
                        tp12.setTextColor(getResources().getColor(R.color.graybtns));
                        tp13.setTextColor(getResources().getColor(R.color.graybtns));
                        tp14.setTextColor(getResources().getColor(R.color.graybtns));
                        Toast.makeText(context, selectedTime, Toast.LENGTH_SHORT).show();

                        break;
                    }

                    case R.id.tp5:
                    {
                        selectedTime="12 - 12:30 pm";
                        tp1.setTextColor(getResources().getColor(R.color.graybtns));
                        tp2.setTextColor(getResources().getColor(R.color.graybtns));
                        tp3.setTextColor(getResources().getColor(R.color.graybtns));
                        tp4.setTextColor(getResources().getColor(R.color.graybtns));
                        tp5.setTextColor(getResources().getColor(R.color.app_color_2));
                        tp6.setTextColor(getResources().getColor(R.color.graybtns));
                        tp7.setTextColor(getResources().getColor(R.color.graybtns));
                        tp8.setTextColor(getResources().getColor(R.color.graybtns));
                        tp9.setTextColor(getResources().getColor(R.color.graybtns));
                        tp10.setTextColor(getResources().getColor(R.color.graybtns));
                        tp11.setTextColor(getResources().getColor(R.color.graybtns));
                        tp12.setTextColor(getResources().getColor(R.color.graybtns));
                        tp13.setTextColor(getResources().getColor(R.color.graybtns));
                        tp14.setTextColor(getResources().getColor(R.color.graybtns));
                        Toast.makeText(context, selectedTime, Toast.LENGTH_SHORT).show();

                        break;
                    }
                    case R.id.tp6:
                    {
                        selectedTime="12:30 - 1 pm";
                        tp1.setTextColor(getResources().getColor(R.color.graybtns));
                        tp2.setTextColor(getResources().getColor(R.color.graybtns));
                        tp3.setTextColor(getResources().getColor(R.color.graybtns));
                        tp4.setTextColor(getResources().getColor(R.color.graybtns));
                        tp5.setTextColor(getResources().getColor(R.color.graybtns));
                        tp6.setTextColor(getResources().getColor(R.color.app_color_2));
                        tp7.setTextColor(getResources().getColor(R.color.graybtns));
                        tp8.setTextColor(getResources().getColor(R.color.graybtns));
                        tp9.setTextColor(getResources().getColor(R.color.graybtns));
                        tp10.setTextColor(getResources().getColor(R.color.graybtns));
                        tp11.setTextColor(getResources().getColor(R.color.graybtns));
                        tp12.setTextColor(getResources().getColor(R.color.graybtns));
                        tp13.setTextColor(getResources().getColor(R.color.graybtns));
                        tp14.setTextColor(getResources().getColor(R.color.graybtns));
                        Toast.makeText(context, selectedTime, Toast.LENGTH_SHORT).show();

                        break;
                    }
                    case R.id.tp7:
                    {
                        selectedTime="1 - 1:30 pm";
                        tp1.setTextColor(getResources().getColor(R.color.graybtns));
                        tp2.setTextColor(getResources().getColor(R.color.graybtns));
                        tp3.setTextColor(getResources().getColor(R.color.graybtns));
                        tp4.setTextColor(getResources().getColor(R.color.graybtns));
                        tp5.setTextColor(getResources().getColor(R.color.graybtns));
                        tp6.setTextColor(getResources().getColor(R.color.graybtns));
                        tp7.setTextColor(getResources().getColor(R.color.app_color_2));
                        tp8.setTextColor(getResources().getColor(R.color.graybtns));
                        tp9.setTextColor(getResources().getColor(R.color.graybtns));
                        tp10.setTextColor(getResources().getColor(R.color.graybtns));
                        tp11.setTextColor(getResources().getColor(R.color.graybtns));
                        tp12.setTextColor(getResources().getColor(R.color.graybtns));
                        tp13.setTextColor(getResources().getColor(R.color.graybtns));
                        tp14.setTextColor(getResources().getColor(R.color.graybtns));
                        Toast.makeText(context, selectedTime, Toast.LENGTH_SHORT).show();

                        break;
                    }

                    case R.id.tp8:
                    {
                        selectedTime="1:30 - 2 pm";
                        tp1.setTextColor(getResources().getColor(R.color.graybtns));
                        tp2.setTextColor(getResources().getColor(R.color.graybtns));
                        tp3.setTextColor(getResources().getColor(R.color.graybtns));
                        tp4.setTextColor(getResources().getColor(R.color.graybtns));
                        tp5.setTextColor(getResources().getColor(R.color.graybtns));
                        tp6.setTextColor(getResources().getColor(R.color.graybtns));
                        tp7.setTextColor(getResources().getColor(R.color.graybtns));
                        tp8.setTextColor(getResources().getColor(R.color.app_color_2));
                        tp9.setTextColor(getResources().getColor(R.color.graybtns));
                        tp10.setTextColor(getResources().getColor(R.color.graybtns));
                        tp11.setTextColor(getResources().getColor(R.color.graybtns));
                        tp12.setTextColor(getResources().getColor(R.color.graybtns));
                        tp13.setTextColor(getResources().getColor(R.color.graybtns));
                        tp14.setTextColor(getResources().getColor(R.color.graybtns));
                        Toast.makeText(context, selectedTime, Toast.LENGTH_SHORT).show();

                        break;
                    }
                    case R.id.tp9:
                    {
                        selectedTime="2 - 2:30 pm";
                        tp1.setTextColor(getResources().getColor(R.color.graybtns));
                        tp2.setTextColor(getResources().getColor(R.color.graybtns));
                        tp3.setTextColor(getResources().getColor(R.color.graybtns));
                        tp4.setTextColor(getResources().getColor(R.color.graybtns));
                        tp5.setTextColor(getResources().getColor(R.color.graybtns));
                        tp6.setTextColor(getResources().getColor(R.color.graybtns));
                        tp7.setTextColor(getResources().getColor(R.color.graybtns));
                        tp8.setTextColor(getResources().getColor(R.color.graybtns));
                        tp9.setTextColor(getResources().getColor(R.color.app_color_2));
                        tp10.setTextColor(getResources().getColor(R.color.graybtns));
                        tp11.setTextColor(getResources().getColor(R.color.graybtns));
                        tp12.setTextColor(getResources().getColor(R.color.graybtns));
                        tp13.setTextColor(getResources().getColor(R.color.graybtns));
                        tp14.setTextColor(getResources().getColor(R.color.graybtns));
                        Toast.makeText(context, selectedTime, Toast.LENGTH_SHORT).show();

                        break;
                    }
                    case R.id.tp10:
                    {
                        selectedTime="2:30 - 3 pm";
                        tp1.setTextColor(getResources().getColor(R.color.graybtns));
                        tp2.setTextColor(getResources().getColor(R.color.graybtns));
                        tp3.setTextColor(getResources().getColor(R.color.graybtns));
                        tp4.setTextColor(getResources().getColor(R.color.graybtns));
                        tp5.setTextColor(getResources().getColor(R.color.graybtns));
                        tp6.setTextColor(getResources().getColor(R.color.graybtns));
                        tp7.setTextColor(getResources().getColor(R.color.graybtns));
                        tp8.setTextColor(getResources().getColor(R.color.graybtns));
                        tp9.setTextColor(getResources().getColor(R.color.graybtns));
                        tp10.setTextColor(getResources().getColor(R.color.app_color_2));
                        tp11.setTextColor(getResources().getColor(R.color.graybtns));
                        tp12.setTextColor(getResources().getColor(R.color.graybtns));
                        tp13.setTextColor(getResources().getColor(R.color.graybtns));
                        tp14.setTextColor(getResources().getColor(R.color.graybtns));
                        Toast.makeText(context, selectedTime, Toast.LENGTH_SHORT).show();

                        break;
                    }

                    case R.id.tp11:
                    {
                        selectedTime="3 - 3:30 pm";
                        tp1.setTextColor(getResources().getColor(R.color.graybtns));
                        tp2.setTextColor(getResources().getColor(R.color.graybtns));
                        tp3.setTextColor(getResources().getColor(R.color.graybtns));
                        tp4.setTextColor(getResources().getColor(R.color.graybtns));
                        tp5.setTextColor(getResources().getColor(R.color.graybtns));
                        tp6.setTextColor(getResources().getColor(R.color.graybtns));
                        tp7.setTextColor(getResources().getColor(R.color.graybtns));
                        tp8.setTextColor(getResources().getColor(R.color.graybtns));
                        tp9.setTextColor(getResources().getColor(R.color.graybtns));
                        tp10.setTextColor(getResources().getColor(R.color.graybtns));
                        tp11.setTextColor(getResources().getColor(R.color.app_color_2));
                        tp12.setTextColor(getResources().getColor(R.color.graybtns));
                        tp13.setTextColor(getResources().getColor(R.color.graybtns));
                        tp14.setTextColor(getResources().getColor(R.color.graybtns));
                        Toast.makeText(context, selectedTime, Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.tp12:
                    {
                        selectedTime="3:30 - 4 pm";
                        tp1.setTextColor(getResources().getColor(R.color.graybtns));
                        tp2.setTextColor(getResources().getColor(R.color.graybtns));
                        tp3.setTextColor(getResources().getColor(R.color.graybtns));
                        tp4.setTextColor(getResources().getColor(R.color.graybtns));
                        tp5.setTextColor(getResources().getColor(R.color.graybtns));
                        tp6.setTextColor(getResources().getColor(R.color.graybtns));
                        tp7.setTextColor(getResources().getColor(R.color.graybtns));
                        tp8.setTextColor(getResources().getColor(R.color.graybtns));
                        tp9.setTextColor(getResources().getColor(R.color.graybtns));
                        tp10.setTextColor(getResources().getColor(R.color.graybtns));
                        tp11.setTextColor(getResources().getColor(R.color.graybtns));
                        tp12.setTextColor(getResources().getColor(R.color.app_color_2));
                        tp13.setTextColor(getResources().getColor(R.color.graybtns));
                        tp14.setTextColor(getResources().getColor(R.color.graybtns));
                        Toast.makeText(context, selectedTime, Toast.LENGTH_SHORT).show();

                        break;
                    }
                    case R.id.tp13:
                    {
                        selectedTime="4 - 4:30 pm";
                        tp1.setTextColor(getResources().getColor(R.color.graybtns));
                        tp2.setTextColor(getResources().getColor(R.color.graybtns));
                        tp3.setTextColor(getResources().getColor(R.color.graybtns));
                        tp4.setTextColor(getResources().getColor(R.color.graybtns));
                        tp5.setTextColor(getResources().getColor(R.color.graybtns));
                        tp6.setTextColor(getResources().getColor(R.color.graybtns));
                        tp7.setTextColor(getResources().getColor(R.color.graybtns));
                        tp8.setTextColor(getResources().getColor(R.color.graybtns));
                        tp9.setTextColor(getResources().getColor(R.color.graybtns));
                        tp10.setTextColor(getResources().getColor(R.color.graybtns));
                        tp11.setTextColor(getResources().getColor(R.color.graybtns));
                        tp12.setTextColor(getResources().getColor(R.color.graybtns));
                        tp13.setTextColor(getResources().getColor(R.color.app_color_2));
                        tp14.setTextColor(getResources().getColor(R.color.graybtns));
                        Toast.makeText(context, selectedTime, Toast.LENGTH_SHORT).show();

                        break;
                    }

                    case R.id.tp14:
                    {
                        selectedTime="4:30 - 5 pm";
                        tp1.setTextColor(getResources().getColor(R.color.graybtns));
                        tp2.setTextColor(getResources().getColor(R.color.graybtns));
                        tp3.setTextColor(getResources().getColor(R.color.graybtns));
                        tp4.setTextColor(getResources().getColor(R.color.graybtns));
                        tp5.setTextColor(getResources().getColor(R.color.graybtns));
                        tp6.setTextColor(getResources().getColor(R.color.graybtns));
                        tp7.setTextColor(getResources().getColor(R.color.graybtns));
                        tp8.setTextColor(getResources().getColor(R.color.graybtns));
                        tp9.setTextColor(getResources().getColor(R.color.graybtns));
                        tp10.setTextColor(getResources().getColor(R.color.graybtns));
                        tp11.setTextColor(getResources().getColor(R.color.graybtns));
                        tp12.setTextColor(getResources().getColor(R.color.graybtns));
                        tp13.setTextColor(getResources().getColor(R.color.graybtns));
                        tp14.setTextColor(getResources().getColor(R.color.app_color_2));
                        Toast.makeText(context, selectedTime, Toast.LENGTH_SHORT).show();

                        break;
                    }

                }
            }
        };
        tp1.setOnClickListener(onClickListener);
        tp2.setOnClickListener(onClickListener);
        tp3.setOnClickListener(onClickListener);
        tp4.setOnClickListener(onClickListener);
        tp5.setOnClickListener(onClickListener);
        tp6.setOnClickListener(onClickListener);
        tp7.setOnClickListener(onClickListener);
        tp8.setOnClickListener(onClickListener);
        tp9.setOnClickListener(onClickListener);
        tp10.setOnClickListener(onClickListener);
        tp11.setOnClickListener(onClickListener);
        tp12.setOnClickListener(onClickListener);
        tp13.setOnClickListener(onClickListener);
        tp14.setOnClickListener(onClickListener);


    }
    private void init(){

        editTextDate=findViewById(R.id.getDateEditext);
        context=getApplicationContext();
        //TimePickerTextViews
        tp1=findViewById(R.id.tp1);
        tp2=findViewById(R.id.tp2);
        tp3=findViewById(R.id.tp3);
        tp4=findViewById(R.id.tp4);
        tp5=findViewById(R.id.tp5);
        tp6=findViewById(R.id.tp6);
        tp7=findViewById(R.id.tp7);
        tp8=findViewById(R.id.tp8);
        tp9=findViewById(R.id.tp9);
        tp10=findViewById(R.id.tp10);
        tp11=findViewById(R.id.tp11);
        tp12=findViewById(R.id.tp12);
        tp13=findViewById(R.id.tp13);
        tp14=findViewById(R.id.tp14);
        dateSelectBtn=findViewById(R.id.dateSelectBtn);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            datePicker.getDayOfMonth();
            date=i2+"/"+i1+"/"+i;
            editTextDate.setText(date);
            SelectedDate=date;
    }
}

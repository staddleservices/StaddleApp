package staddle.com.staddle.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import staddle.com.staddle.R;
import staddle.com.staddle.fragment.ShoppingFragment;

public class SchedulingActivity extends AppCompatActivity implements  DatePickerDialog.OnDateSetListener {

    DatePicker datePicker;
    Context context;
    ImageView backbtn;

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

    TextView editTextDate;
    String date;
    int atHome=0;
    String crTime="";
    int currentTimeHour;
    int currentTimeMinutes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduling);
        init();
        Intent intent=getIntent();
        Nickname=intent.getStringExtra("nickname");
        Address=intent.getStringExtra("address");
        atHome=Integer.parseInt(intent.getStringExtra("atHome"));

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(SchedulingActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String _year = String.valueOf(year);
                        String _month = (month+1) < 10 ? "0" + (month+1) : String.valueOf(month+1);
                        String _date = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                        String _pickedDate = year + "-" + _month + "-" + _date;
                        Log.e("PickedDate: ", "Date: " + _pickedDate); //2019-02-12
                        editTextDate.setText(_pickedDate);
                        //editTextDate.setEnabled(false);
                        SelectedDate=_pickedDate;
                        Calendar c = Calendar.getInstance();

                        String sDate = c.get(Calendar.DAY_OF_MONTH)+"";
                        if(sDate.equals(_date)){
                            setEnabledZone();
                        }else{
                            setEnabledAll();
                        }

                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.MONTH));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();
            }
        });

        dateSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(SelectedDate.equals("") || selectedTime.equals("")){
                    Toast.makeText(context, "Please select proper date and time", Toast.LENGTH_SHORT).show();
                }else{
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
                        //Toast.makeText(context, selectedTime, Toast.LENGTH_SHORT).show();

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
                        //Toast.makeText(context, selectedTime, Toast.LENGTH_SHORT).show();

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
                        //Toast.makeText(context, selectedTime, Toast.LENGTH_SHORT).show();

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
                        //Toast.makeText(context, selectedTime, Toast.LENGTH_SHORT).show();

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
                        //Toast.makeText(context, selectedTime, Toast.LENGTH_SHORT).show();

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
                        //Toast.makeText(context, selectedTime, Toast.LENGTH_SHORT).show();

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
                        //Toast.makeText(context, selectedTime, Toast.LENGTH_SHORT).show();

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
                        //Toast.makeText(context, selectedTime, Toast.LENGTH_SHORT).show();

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
                        //Toast.makeText(context, selectedTime, Toast.LENGTH_SHORT).show();

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
                        //Toast.makeText(context, selectedTime, Toast.LENGTH_SHORT).show();

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
                        //Toast.makeText(context, selectedTime, Toast.LENGTH_SHORT).show();
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
                        //Toast.makeText(context, selectedTime, Toast.LENGTH_SHORT).show();

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
                        //Toast.makeText(context, selectedTime, Toast.LENGTH_SHORT).show();

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
                        //Toast.makeText(context, selectedTime, Toast.LENGTH_SHORT).show();

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
        backbtn = findViewById(R.id.backbtntiming);
        dateSelectBtn=findViewById(R.id.dateSelectBtn);
        setDisabledAll();



    }

    private void setDisabledAll() {
        tp1.setEnabled(false);
        tp2.setEnabled(false);
        tp3.setEnabled(false);
        tp4.setEnabled(false);
        tp5.setEnabled(false);
        tp6.setEnabled(false);
        tp7.setEnabled(false);
        tp8.setEnabled(false);
        tp9.setEnabled(false);
        tp10.setEnabled(false);
        tp11.setEnabled(false);
        tp12.setEnabled(false);
        tp13.setEnabled(false);
        tp14.setEnabled(false);
    }
//    public static String Datetime()
//    {
//        String formattedDate;
//        Calendar c = Calendar .getInstance().getTime();
//        System.out.println("Current time => "+c.getTime());
//        formattedDate=c.getTime()+"";
//        return formattedDate;
//    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            datePicker.getDayOfMonth();
            date=i2+"/"+i1+"/"+i;
            editTextDate.setText(date);
            //editTextDate.setEnabled(false);
            SelectedDate=date;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

    public void setEnabledZone(){
        Calendar calender = Calendar.getInstance();
        calender.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        Log.d("CRTIME",calender.get(Calendar.HOUR_OF_DAY) + ":" + calender.get(Calendar.MINUTE) +  ":" + calender.getActualMinimum(Calendar.SECOND));
        currentTimeHour = calender.get(Calendar.HOUR_OF_DAY);
        currentTimeMinutes = calender.get(Calendar.MINUTE);
        String crtm = currentTimeHour+"."+currentTimeMinutes;
        if(currentTimeHour==10)
        {
            if(currentTimeMinutes>30){
                tp1.setEnabled(false);
                tp2.setEnabled(true);
                tp3.setEnabled(true);
                tp4.setEnabled(true);
                tp5.setEnabled(true);
                tp6.setEnabled(true);
                tp7.setEnabled(true);
                tp8.setEnabled(true);
                tp9.setEnabled(true);
                tp10.setEnabled(true);
                tp11.setEnabled(true);
                tp12.setEnabled(true);
                tp13.setEnabled(true);
                tp14.setEnabled(true);

                Log.d("EX-TIME","10.30+");
            }else{
                tp1.setEnabled(true);
                tp2.setEnabled(true);
                tp3.setEnabled(true);
                tp4.setEnabled(true);
                tp5.setEnabled(true);
                tp6.setEnabled(true);
                tp7.setEnabled(true);
                tp8.setEnabled(true);
                tp9.setEnabled(true);
                tp10.setEnabled(true);
                tp11.setEnabled(true);
                tp12.setEnabled(true);
                tp13.setEnabled(true);
                tp14.setEnabled(true);
                Log.d("EX-TIME","10.00+");
            }
        } else if(currentTimeHour==11)
        {
            if(currentTimeMinutes>30){
                tp1.setEnabled(false);
                tp2.setEnabled(false);
                tp3.setEnabled(false);
                tp4.setEnabled(true);
                tp5.setEnabled(true);
                tp6.setEnabled(true);
                tp7.setEnabled(true);
                tp8.setEnabled(true);
                tp9.setEnabled(true);
                tp10.setEnabled(true);
                tp11.setEnabled(true);
                tp12.setEnabled(true);
                tp13.setEnabled(true);
                tp14.setEnabled(true);
                Log.d("EX-TIME","11.30+");
            }else{
                tp1.setEnabled(false);
                tp2.setEnabled(false);
                tp3.setEnabled(true);
                tp4.setEnabled(true);
                tp5.setEnabled(true);
                tp6.setEnabled(true);
                tp7.setEnabled(true);
                tp8.setEnabled(true);
                tp9.setEnabled(true);
                tp10.setEnabled(true);
                tp11.setEnabled(true);
                tp12.setEnabled(true);
                tp13.setEnabled(true);
                tp14.setEnabled(true);
                Log.d("EX-TIME","11.00+");
            }
        }else if(currentTimeHour==12)
        {
            if(currentTimeMinutes>30){
                tp1.setEnabled(false);
                tp2.setEnabled(false);
                tp3.setEnabled(false);
                tp4.setEnabled(false);
                tp5.setEnabled(false);
                tp6.setEnabled(true);
                tp7.setEnabled(true);
                tp8.setEnabled(true);
                tp9.setEnabled(true);
                tp10.setEnabled(true);
                tp11.setEnabled(true);
                tp12.setEnabled(true);
                tp13.setEnabled(true);
                tp14.setEnabled(true);
                Log.d("EX-TIME","12.30+");
            }else{
                tp1.setEnabled(false);
                tp2.setEnabled(false);
                tp3.setEnabled(false);
                tp4.setEnabled(false);
                tp5.setEnabled(true);
                tp6.setEnabled(true);
                tp7.setEnabled(true);
                tp8.setEnabled(true);
                tp9.setEnabled(true);
                tp10.setEnabled(true);
                tp11.setEnabled(true);
                tp12.setEnabled(true);
                tp13.setEnabled(true);
                tp14.setEnabled(true);
                Log.d("EX-TIME","12.00+");
            }
        }else if(currentTimeHour==13)
        {
            if(currentTimeMinutes>30){
                tp1.setEnabled(false);
                tp2.setEnabled(false);
                tp3.setEnabled(false);
                tp4.setEnabled(false);
                tp5.setEnabled(false);
                tp6.setEnabled(false);
                tp7.setEnabled(false);
                tp8.setEnabled(true);
                tp9.setEnabled(true);
                tp10.setEnabled(true);
                tp11.setEnabled(true);
                tp12.setEnabled(true);
                tp13.setEnabled(true);
                tp14.setEnabled(true);
                Log.d("EX-TIME","1.30+");
            }else{
                tp1.setEnabled(false);
                tp2.setEnabled(false);
                tp3.setEnabled(false);
                tp4.setEnabled(false);
                tp5.setEnabled(false);
                tp6.setEnabled(true);
                tp7.setEnabled(true);
                tp8.setEnabled(true);
                tp9.setEnabled(true);
                tp10.setEnabled(true);
                tp11.setEnabled(true);
                tp12.setEnabled(true);
                tp13.setEnabled(true);
                tp14.setEnabled(true);
                Log.d("EX-TIME","1.00+");
            }
        }else if(currentTimeHour==14)
        {
            if(currentTimeMinutes>30){
                tp1.setEnabled(false);
                tp2.setEnabled(false);
                tp3.setEnabled(false);
                tp4.setEnabled(false);
                tp5.setEnabled(false);
                tp6.setEnabled(false);
                tp7.setEnabled(false);
                tp8.setEnabled(false);
                tp9.setEnabled(false);
                tp10.setEnabled(true);
                tp11.setEnabled(true);
                tp12.setEnabled(true);
                tp13.setEnabled(true);
                tp14.setEnabled(true);
                Log.d("EX-TIME","2.30+");
            }else{
                tp1.setEnabled(false);
                tp2.setEnabled(false);
                tp3.setEnabled(false);
                tp4.setEnabled(false);
                tp5.setEnabled(false);
                tp6.setEnabled(false);
                tp7.setEnabled(true);
                tp8.setEnabled(true);
                tp9.setEnabled(true);
                tp10.setEnabled(true);
                tp11.setEnabled(true);
                tp12.setEnabled(true);
                tp13.setEnabled(true);
                tp14.setEnabled(true);
                Log.d("EX-TIME","2.00+");
            }
        }else if(currentTimeHour==15)
        {
            if(currentTimeMinutes>30){
                tp1.setEnabled(false);
                tp2.setEnabled(false);
                tp3.setEnabled(false);
                tp4.setEnabled(false);
                tp5.setEnabled(false);
                tp6.setEnabled(false);
                tp7.setEnabled(false);
                tp8.setEnabled(false);
                tp9.setEnabled(false);
                tp10.setEnabled(false);
                tp11.setEnabled(false);
                tp12.setEnabled(true);
                tp13.setEnabled(true);
                tp14.setEnabled(true);
                Log.d("EX-TIME","3.30+");
            }else{
                tp1.setEnabled(false);
                tp2.setEnabled(false);
                tp3.setEnabled(false);
                tp4.setEnabled(false);
                tp5.setEnabled(false);
                tp6.setEnabled(false);
                tp7.setEnabled(false);
                tp8.setEnabled(false);
                tp9.setEnabled(false);
                tp10.setEnabled(false);
                tp11.setEnabled(true);
                tp12.setEnabled(true);
                tp13.setEnabled(true);
                tp14.setEnabled(true);
                Log.d("EX-TIME","3.00+");
            }
        }else if(currentTimeHour==16)
        {
            if(currentTimeMinutes>30){
                tp1.setEnabled(false);
                tp2.setEnabled(false);
                tp3.setEnabled(false);
                tp4.setEnabled(false);
                tp5.setEnabled(false);
                tp6.setEnabled(false);
                tp7.setEnabled(false);
                tp8.setEnabled(false);
                tp9.setEnabled(false);
                tp10.setEnabled(false);
                tp11.setEnabled(false);
                tp12.setEnabled(false);
                tp13.setEnabled(false);
                tp14.setEnabled(true);
                Log.d("EX-TIME","11.30+");
            }else{
                tp1.setEnabled(false);
                tp2.setEnabled(false);
                tp3.setEnabled(false);
                tp4.setEnabled(false);
                tp5.setEnabled(false);
                tp6.setEnabled(false);
                tp7.setEnabled(false);
                tp8.setEnabled(false);
                tp9.setEnabled(false);
                tp10.setEnabled(false);
                tp11.setEnabled(false);
                tp12.setEnabled(false);
                tp13.setEnabled(true);
                tp14.setEnabled(true);
                Log.d("EX-TIME","11.00+");
            }
        }

    }
    private void setEnabledAll() {
        tp1.setEnabled(true);
        tp2.setEnabled(true);
        tp3.setEnabled(true);
        tp4.setEnabled(true);
        tp5.setEnabled(true);
        tp6.setEnabled(true);
        tp7.setEnabled(true);
        tp8.setEnabled(true);
        tp9.setEnabled(true);
        tp10.setEnabled(true);
        tp11.setEnabled(true);
        tp12.setEnabled(true);
        tp13.setEnabled(true);
        tp14.setEnabled(true);
    }
}

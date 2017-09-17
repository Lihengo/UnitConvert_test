package com.example.unitconvert_test;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
    private Context mContext;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;

    private DrawerLayout drawer_layout;
    private ListView rightListDrawer;

    private TextView topbar_name;
    private Button topbar_btn;

    private EditText scrn_input;
    private EditText scrn_output;
    private Button btn_1;
    private Button btn_2;
    private Button btn_3;
    private Button btn_4;
    private Button btn_5;
    private Button btn_6;
    private Button btn_7;
    private Button btn_8;
    private Button btn_9;
    private Button btn_0;
    private Button btn_clr;
    private Button btn_back;
    private Button btn_dot;
    private Button btn_scientific;
    private Button btn_negative;
    private Button btn_pi;

    private Spinner spin_in;
    private Spinner spin_out;

//    private AdapterView

    private MyAdapter<Unit> myAdapter_in = null;
    private MyAdapter<Unit> myAdapter_out = null;
    private MyAdapter<Unit> myAdapter_category = null;

    boolean inUnitSelected = false;
    boolean outUnitSelected = false;
    boolean CategorySelected = false;

    private ArrayList<Unit> inputUnitList = null;
    private ArrayList<Unit> outputUnitList = null;
    private ArrayList<Unit> categoryList = null;

    String input_txt;
    String output_txt;
    String new_txt;

    String[] unitName_stringArray = null;
    String[] unitValue_stringArray = null;
    String[] categoryName_stringArray = null;
    String[] categoryIndex_stringArray = null;


    BigDecimal input_num = new BigDecimal("0");
    BigDecimal test_num = new BigDecimal("1");
    BigDecimal denominator = new BigDecimal("1");
    BigDecimal numerator = new BigDecimal("1");

    BigDecimal ten = new BigDecimal("10");

    int counter_decimalDigit = 0;
    int counter_scientificDigit = 0;
    int counter_integerDigit = 0;

    int tenToPow = 0;
    int indexCategory = 1;

    boolean isDotLocked = false;
    boolean isScientificLocked = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = MainActivity.this;

        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        rightListDrawer = (ListView) findViewById(R.id.list_right_drawer);

        inputUnitList = new ArrayList<Unit>();
        outputUnitList = new ArrayList<Unit>();
        categoryList = new ArrayList<Unit>();

        topbar_btn = (Button) findViewById(R.id.topbar_btn);
        topbar_name = (TextView) findViewById(R.id.topbar_name);

        scrn_input = (EditText) findViewById(R.id.scrn_input);
        scrn_output = (EditText) findViewById(R.id.scrn_output);

        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_3 = (Button) findViewById(R.id.btn_3);
        btn_4 = (Button) findViewById(R.id.btn_4);
        btn_5 = (Button) findViewById(R.id.btn_5);
        btn_6 = (Button) findViewById(R.id.btn_6);
        btn_7 = (Button) findViewById(R.id.btn_7);
        btn_8 = (Button) findViewById(R.id.btn_8);
        btn_9 = (Button) findViewById(R.id.btn_9);
        btn_0 = (Button) findViewById(R.id.btn_0);
        btn_clr = (Button) findViewById(R.id.btn_clr);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_dot = (Button) findViewById(R.id.btn_dot);
        btn_scientific = (Button) findViewById(R.id.btn_scientific);
        btn_negative = (Button) findViewById(R.id.btn_negative);
        btn_pi = (Button) findViewById(R.id.btn_pi);

        spin_in = (Spinner) findViewById(R.id.spinner_inputUnit);
        spin_out = (Spinner) findViewById(R.id.spinner_outputUnit);

        topbar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_layout.openDrawer(Gravity.START);
            }
        });

        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
        btn_6.setOnClickListener(this);
        btn_7.setOnClickListener(this);
        btn_8.setOnClickListener(this);
        btn_9.setOnClickListener(this);
        btn_0.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_clr.setOnClickListener(this);
        btn_dot.setOnClickListener(this);
        btn_scientific.setOnClickListener(this);
        btn_negative.setOnClickListener(this);
        btn_pi.setOnClickListener(this);

        input_txt = "0";
        output_txt = "=0";

        Resources res = getResources();
        unitName_stringArray = res.getStringArray(R.array.length_unitName);
        unitValue_stringArray = res.getStringArray(R.array.length_unitValue);
        categoryName_stringArray = res.getStringArray(R.array.category_name);
        categoryIndex_stringArray = res.getStringArray(R.array.category_index);

        addUnitList(unitName_stringArray, unitValue_stringArray, inputUnitList);
        addUnitList(unitName_stringArray, unitValue_stringArray, outputUnitList);
        addUnitList(categoryName_stringArray, categoryIndex_stringArray, categoryList);

        myAdapter_in = new MyAdapter<Unit>(inputUnitList, R.layout.spinner_view) {
            @Override
            public void bindView(ViewHolder holder, Unit obj) {
                holder.setText(R.id.spin_txt, obj.getUnitName());
            }
        };

        myAdapter_out = new MyAdapter<Unit>(outputUnitList, R.layout.spinner_view) {
            @Override
            public void bindView(ViewHolder holder, Unit obj) {
                holder.setText(R.id.spin_txt, obj.getUnitName());
            }
        };

        myAdapter_category = new MyAdapter<Unit>(categoryList, R.layout.rightdrawer_listview) {
            @Override
            public void bindView(ViewHolder holder, Unit obj) {
                holder.setText(R.id.category_txt, obj.getUnitName());
            }
        };

        spin_in.setAdapter(myAdapter_in);
        spin_out.setAdapter(myAdapter_out);
        rightListDrawer.setAdapter(myAdapter_category);

        rightListDrawer.setOnItemClickListener(this);
        spin_in.setOnItemSelectedListener(this);
        spin_out.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Resources res_new = getResources();
        indexCategory = Integer.parseInt(categoryList.get(position).getUnitValue());
        topbar_name.setText(categoryList.get(position).getUnitName());
        unitName_stringArray = null;
        unitValue_stringArray = null;
        switch (indexCategory){
            case 1:
                unitName_stringArray = res_new.getStringArray(R.array.length_unitName);
                unitValue_stringArray = res_new.getStringArray(R.array.length_unitValue);
                break;
            case 2 :
                unitName_stringArray = res_new.getStringArray(R.array.mass_unitName);
                unitValue_stringArray = res_new.getStringArray(R.array.mass_unitValue);
                break;
            case 3 :
                unitName_stringArray = res_new.getStringArray(R.array.time_unitName);
                unitValue_stringArray = res_new.getStringArray(R.array.time_unitValue);
                break;
            case 4 :
                unitName_stringArray = res_new.getStringArray(R.array.current_unitName);
                unitValue_stringArray = res_new.getStringArray(R.array.current_unitValue);
                break;
            case 5 :
                unitName_stringArray = res_new.getStringArray(R.array.temperature_unitName);
                unitValue_stringArray = res_new.getStringArray(R.array.temperature_unitValue);
                break;
            default:
                unitName_stringArray = res_new.getStringArray(R.array.length_unitName);
                unitValue_stringArray = res_new.getStringArray(R.array.length_unitValue);
                break;
        }
        addUnitList(unitName_stringArray,unitValue_stringArray,inputUnitList);
        addUnitList(unitName_stringArray,unitValue_stringArray,outputUnitList);
        Toast.makeText(mContext,"index~："+indexCategory+" "+inputUnitList.size(),Toast.LENGTH_SHORT).show();
        spin_in.setAdapter(myAdapter_in);
        spin_out.setAdapter(myAdapter_out);
        clear_num();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){

            case R.id.spinner_inputUnit:
                if(inUnitSelected){
                    numerator = new BigDecimal(inputUnitList.get(position).getUnitValue());
                }
                else{
                    inUnitSelected = true;
                }
                break;
            case R.id.spinner_outputUnit:
                if(outUnitSelected){
                    denominator = new BigDecimal(outputUnitList.get(position).getUnitValue());
                }
                else{
                    outUnitSelected = true;
                }
                break;
        }
        test_num = numerator.divide(denominator, 10, RoundingMode.HALF_UP);
        test_num = new BigDecimal(myRounding(test_num));
//        Toast.makeText(mContext,"test_num~："+test_num.toPlainString(),Toast.LENGTH_SHORT).show();
        output_txt = output2txt(input_num, test_num, tenToPow, isScientificLocked);
        scrn_output.setText(output_txt);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @Override
    public void onClick(View view) {
        input_txt = scrn_input.getText().toString();
        output_txt = scrn_output.getText().toString();
        new_txt = "";
        // input_num = new BigDecimal(input_txt);  //maybe unnecessary
        switch(view.getId()){


            case R.id.btn_1:
            case R.id.btn_2:
            case R.id.btn_3:
            case R.id.btn_4:
            case R.id.btn_5:
            case R.id.btn_6:
            case R.id.btn_7:
            case R.id.btn_8:
            case R.id.btn_9:
            case R.id.btn_0:
                new_txt+=(((Button)view).getText());
                input_num = addNewNum(input_num, new_txt);
                break;
            case R.id.btn_clr:
                clear_num();
                break;
            case R.id.btn_back:
                input_num = refreshBackNum(input_num);
                break;
            case R.id.btn_negative:
                if(isScientificLocked == false){
                    input_num = input_num.negate();
                }
                else{
                    tenToPow = -tenToPow;
                }
                break;
            case R.id.btn_dot:
                setOnDecimalMode();
                break;
            case R.id.btn_scientific:
                setOnScientificMode();
                break;
            case R.id.btn_pi:
                break;
        }
        input_txt = num2txt(input_num);
        output_txt = output2txt(input_num, test_num, tenToPow, isScientificLocked);
        scrn_input.setText(input_txt);
        scrn_input.setSelection(input_txt.length());
        scrn_output.setText(output_txt);
    }



    //--------------------------------------------------------------------------
    public BigDecimal str2double(String in_str){          //08-28
        BigDecimal res_num;
        res_num = new BigDecimal(in_str);
        return res_num;
    }

    public BigDecimal addNewNum(BigDecimal in_num, String in_txt){
        BigDecimal res_num = new BigDecimal("0");
        BigDecimal newNum = new BigDecimal(in_txt);

        if(isScientificLocked == true){ //do not modify the in_num, just add the new num to the tenToPow
            if(tenToPow < 1000000) {
                counter_scientificDigit++;
                tenToPow = tenToPow * 10 + newNum.intValue();
            }
            else{
                alert = null;
                builder = new AlertDialog.Builder(mContext);
                alert = builder
                        .setMessage("Um...this is infinity, yeah, infinity.")
                        .setNegativeButton("BACK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).create();             //创建AlertDialog对象
                alert.show();                    //显示
            }
            res_num = input_num;
        }
        else if(isDotLocked == true){
            counter_decimalDigit++;
            newNum = newNum.movePointLeft(counter_decimalDigit);
            res_num = in_num.add(newNum);
        }
        else {
            if(input_num.doubleValue() != 0.0d){
                counter_integerDigit++;
            }
            res_num = (in_num.multiply(ten)).add(newNum);
        }
        return res_num;
    }

    public BigDecimal refreshBackNum(BigDecimal in_num){
        BigDecimal res_num = new BigDecimal("0");
        if(isScientificLocked == true){
            if(counter_scientificDigit <= 1){
                isScientificLocked = false;
                counter_scientificDigit = 0;
                tenToPow = 0;
                if(counter_decimalDigit == 0){
                    isDotLocked = false;
                }
            }
            else{
                counter_scientificDigit--;
                tenToPow /= 10;
            }
            res_num = in_num;
        }
        else if(isDotLocked == true){
            if(counter_decimalDigit <= 1){
                isDotLocked = false;
                counter_decimalDigit = 0;
                res_num = in_num.setScale(0, RoundingMode.DOWN);
            }
            else {
                counter_decimalDigit--;
                in_num = new BigDecimal(format_dot(in_num, counter_decimalDigit));
                res_num = in_num;
            }
        }
        else {
            if (in_num.abs().doubleValue() < 10d) {
                counter_integerDigit = 0;
                res_num = new BigDecimal("0");
            }
            else {
                counter_integerDigit--;
                res_num = in_num.divideToIntegralValue(BigDecimal.TEN).setScale(0, RoundingMode.DOWN);
            }
        }
        return res_num;
    }

    public  String num2txt(BigDecimal in_num){
        String res_str = "";
        if(isScientificLocked == true){
            res_str = in_num.toPlainString() + "E" + Integer.toString(tenToPow);
        }
        else if(isDotLocked == true){
            res_str = in_num.toString();
            if(counter_decimalDigit >= 7) {
                res_str = format_dot(in_num, counter_decimalDigit);
            }
        }
        else{
            res_str = in_num.toString();
        }
        return res_str;
    }

    private static String format_scientific(BigDecimal x, int scale){
        NumberFormat formatter = new DecimalFormat("###################0E0");
        formatter.setRoundingMode(RoundingMode.DOWN);
        formatter.setMinimumFractionDigits(scale);
        return formatter.format(x);
    }

    private static String format_integer(BigDecimal x, int scale){
        NumberFormat formatter = new DecimalFormat("#,###,###");
        formatter.setRoundingMode(RoundingMode.DOWN);
        formatter.setMinimumFractionDigits(scale);
        return formatter.format(x);
    }

    private static String format_dot(BigDecimal x, int scale){
        NumberFormat formatter = new DecimalFormat("#,###0.0");
        formatter.setRoundingMode(RoundingMode.DOWN);
        formatter.setMinimumFractionDigits(scale);
        return formatter.format(x);
    }

    private void clear_num(){
        input_txt="0";
        output_txt="0";
        input_num= new BigDecimal("0");
        counter_decimalDigit = 0;
        counter_integerDigit = 0;
        counter_scientificDigit = 0;
        tenToPow = 0;
        isDotLocked = false;
        isScientificLocked = false;
        scrn_input.setText(input_txt);
        scrn_output.setText(output_txt);
        return;
    }
    //--------------------------------------------------------8-31
    private void setOnDecimalMode() {
        if(isDotLocked == true){
            return;
        }
        else{
            isDotLocked = true;
            counter_decimalDigit = 0;
            input_num = input_num.setScale(1);
        }
    }

    private void setOnScientificMode(){
        if(isScientificLocked == true){
            return;
        }
        else{
            isScientificLocked = true;
            isDotLocked = true;
            tenToPow = 0;
        }
    }

    public String output2txt(BigDecimal in_num, BigDecimal test_num,int tenToPow, boolean isScientificLocked){
        String res_str = "";
        BigDecimal res_num = new BigDecimal("0");

        if(indexCategory == 5){
            double formula;
            BigDecimal one_eight = new BigDecimal("1.8");
            BigDecimal thirty_two = new BigDecimal("32");
            BigDecimal two_hun = new BigDecimal("273.15");
            BigDecimal four_hun = new BigDecimal("459.67");
            formula = test_num.doubleValue();
            res_str = in_num.toPlainString();
            if(formula == 1){
                res_str = in_num.toPlainString();
            }
            else if(formula == 2){ //from Fahrenheit  to  Celsius
                res_num = (in_num.subtract(thirty_two)).divide(one_eight, 2, RoundingMode.HALF_UP);
                res_str = format_dot(res_num, 2);
            }
            else if(formula == 0.2){//from Fahrenheit  to Kelvin
                res_num = (in_num.subtract(thirty_two)).divide(one_eight, 2, RoundingMode.HALF_UP).add(two_hun);
                res_str = format_dot(res_num, 2);
            }
            else if(formula == 0.5){//from Celsius  to Fahrenheit
                res_num = in_num.multiply(one_eight).add(thirty_two);
                res_str = format_dot(res_num, 2);
            }
            else if(formula == 0.1){//from Celsius  to Kelvin
                res_num = in_num.add(two_hun);
                res_str = format_dot(res_num, 2);
            }
            else if(formula == 10){//from Kelvin to Celsius
                res_num = in_num.subtract(two_hun);
                res_str = format_dot(res_num, 2);
            }
            else if(formula == 5){//from Kelvin to Fahrenheit
                res_num = in_num.subtract(two_hun).multiply(one_eight).add(thirty_two);
                res_str = format_dot(res_num, 2);
            }
        }
        else {
            if (isScientificLocked == true) {
                res_str = in_num.toPlainString() + "E" + Integer.toString(tenToPow);
                res_num = new BigDecimal(res_str);
                res_num = res_num.multiply(test_num);
                if (res_num.toString().length() > 12) {
                    res_str = "=" + format_scientific(res_num, 7);
                    if (res_str.length() > 12) {
                        res_str = "=" + format_scientific(res_num, 5);
                    }
                } else {
                    res_str = "=" + res_num.toString();
                }
            } else {
                res_num = in_num.multiply(test_num);
                if (res_num.toString().length() > 12) {
                    res_str = "=" + format_scientific(res_num, 7);
                } else {
                    res_str = "=" + res_num.toString();
                }
            }
        }
        return res_str;
    }
    //-------------------------------------------------------9-1

    public void addUnitList(String[] str_name,String[] str_value, ArrayList<Unit> unit_list ){
        unit_list.clear();
        for(int i = 0; i < str_name.length; i++){
            unit_list.add(new Unit(str_name[i], str_value[i]));
        }
    }

    public String myRounding(BigDecimal num){//-------------------bug
        String int_str = "";
        String decimal_str = "";
        int_str = num.toEngineeringString();
        double a = Double.parseDouble(int_str);
        int_str = Double.toString(a);
        return  int_str;

    }


}

package com.example.gaoyounan.calculator;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * textView_Input is used to display the inputting letters
     */
    private TextView textView_Input;

    /**
     * symbol is used to present the operator (+, -, *, /)
     */
    private String symbol = null;

    /**
     * text_to_show is the string to present in the screen
     * Set "0" as initialized
     */
    private String text_to_show = "0";

    /**
     * text_all is the string to storage all of the operands
     */
    private StringBuffer text_all = new StringBuffer("0");

    /**
     * This function is used to process number button
     * @param num
     */
    private void input_process_num_and_show(String num)
    {
        if(this.symbol == null || "=".equals(this.symbol))
        {
            if("=".equals(this.symbol))
            {
                text_to_show = num;
            }
            else if("0".equals(text_to_show))
            {
                //"0"1
                text_to_show = num;
            }
            else if("-0".equals(text_to_show))
            {
                //"-0"1
                text_to_show = "-" + num;
            }
            else
            {
                //"1"2
                text_to_show += num;
            }
            this.symbol = null;
            setTexttoInputView(true);
            text_all = new StringBuffer(text_to_show);
        }
        else
        {
            if(text_all.charAt(text_all.length()-1)>='0' && text_all.charAt(text_all.length()-1) <= '9' )
            {
                //"1+2"2;
                if("0".equals(text_to_show))
                {
                    //"1+0"2
                    text_to_show = num;
                }
                else if("-0".equals(text_to_show))
                {
                    //"1+-0"2;
                    text_to_show = "-" + num;
                }
                else
                {
                    text_to_show += num;
                }
            }
            else if(text_all.charAt(text_all.length()-1) == '.')
            {
                //"1+2."2;
                text_to_show += num;
            }
            else
            {
                //"1+"2
                text_to_show = num;
            }
            setTexttoInputView(true);
            text_all.append(num);
        }
    }

    /**
     * This function is used to analyse the string of text_all
     * @return operator 1 and operator 2
     * Exception case -7-8 and 7--8 has been covered
     */
    private String [] split_text_all()
    {
        String [] strArr = null;
        String regexStr = "\\" + symbol;

        if("-".equals(symbol) )
        {
            if(text_all.indexOf("--") > -1)
            {
                //-7--8 or 7--8
                strArr = text_all.toString().split(regexStr+regexStr);
                strArr[1] = "-" + strArr[1];

            }
            else if(text_all.charAt(0) == '-')
            {
                //-7-8
                strArr = text_all.substring(1,text_all.length()).split(regexStr);
                strArr[0] = "-" + strArr[0];
            }
            else
            {
                //7-8
                strArr = text_all.toString().split(regexStr);
            }

        }
        else
        {
            strArr = text_all.toString().split(regexStr);
        }

        return strArr;
    }

    /**
     * To calculate the result by +,-,*,/
     * @return
     */
    public double getResult()
    {
        String [] strArr = split_text_all();
        System.out.println(text_all);
        double variableLeft = Double.valueOf(strArr[0]);
        double variableRight = Double.valueOf(strArr[1]);
        double result = 0d;

        if("+".equals(symbol))
        {
            result = variableLeft + variableRight;
            button_plus.setBackgroundColor(Color.parseColor("#FFA500"));
        }
        else if("-".equals(symbol))
        {
            result = variableLeft - variableRight;
            button_minus.setBackgroundColor(Color.parseColor("#FFA500"));
        }
        else if("/".equals(symbol))
        {
            result = variableLeft / variableRight;
            button_divided.setBackgroundColor(Color.parseColor("#FFA500"));
        }
        else
        {
            result = variableLeft * variableRight;
            button_times.setBackgroundColor(Color.parseColor("#FFA500"));
        }

        result =  (result == -0.0) ? 0: result;

        return result;
    }

    /**
     * This function is used to process the simple symbol which only includes "+,-,*,/"
     * @param _symbol
     */
    private void input_process_simple_symbol_and_show(char _symbol)
    {
        if(symbol == null || "=".equals(symbol))
        {
            //"123"+; "0"+; "-0"+
            symbol = String.valueOf(_symbol);
            text_all.append(symbol);

        }
        else if(symbol.equals(String.valueOf(_symbol)))
        {
            if(text_all.charAt(text_all.length() - 1) == _symbol)
            {
                //eg:"15+"+
                return;
            }
            else
            {
                //eg:"15+8"+; "15+8."+;
                double result = getResult();
                this.text_to_show = String.valueOf(result);
                setTexttoInputView(false);
                symbol = String.valueOf(_symbol);
                this.text_all = new StringBuffer(text_to_show);
                this.text_all.append(_symbol);
            }

        }
        else
        {
            char lastChar = text_all.charAt(text_all.length()-1);
            if(lastChar >= '0' && lastChar <= '9' )
            {
                //"15+6"/
                double result = getResult();
                this.text_to_show = String.valueOf(result);
                setTexttoInputView(false);
                symbol = String.valueOf(_symbol);
                this.text_all = new StringBuffer(text_to_show);
                this.text_all.append(_symbol);
            }
            else if(lastChar == '.')
            {
                //"15+."-
                this.text_all.append('0');
                double result = getResult();
                this.text_to_show = String.valueOf(result);
                setTexttoInputView(false);
                symbol = String.valueOf(_symbol);
                this.text_all = new StringBuffer(text_to_show);
                this.text_all.append(_symbol);
            }
            else
            {
                //eg:"15+"-
                symbol = String.valueOf(_symbol);
                this.text_all.setCharAt(this.text_all.length()-1, _symbol);
            }
        }

    }

    /**
     * Reset the button to orignial color which is orange
     */
    private void resetButtonColor()
    {
        button_plus.setBackgroundColor(Color.parseColor("#FFA500"));
        button_divided.setBackgroundColor(Color.parseColor("#FFA500"));
        button_times.setBackgroundColor(Color.parseColor("#FFA500"));
        button_minus.setBackgroundColor(Color.parseColor("#FFA500"));
    }

    /**
     * Click handler function, which includes all of the 20 buttons
     * @param view
     */
    @Override
    public void onClick(View view)
    {
        int buttonId = view.getId();

        if(buttonId == R.id.btn_ac)
        {
            text_all = new StringBuffer("0");
            text_to_show = "0";
            symbol = null;
            setTexttoInputView(false);

            resetButtonColor();
        }
        else if(buttonId == R.id.btn_C)
        {
            if(symbol == null || "=".equals(symbol))
            {
                symbol = null;
                if("0".equals(text_to_show))
                {
                    //"0"C
                    return ;
                }
                else if("-0".equals(text_to_show))
                {
                    //"-0"C
                    text_all = new StringBuffer("0");
                    text_to_show = "0";
                }
                else
                {
                    //"5"C
                    text_all = new StringBuffer("0");
                    text_to_show = "0";
                }
            }
            else if(text_all.charAt(text_all.length()-1) == '+'
                        || text_all.charAt(text_all.length()-1) == '-'
                        || text_all.charAt(text_all.length()-1) == '*'
                        || text_all.charAt(text_all.length()-1) == '/')
            {
                //"15+"
                symbol = null;
                text_all = new StringBuffer(text_all.substring(0, text_all.length()-1));
                this.resetButtonColor();
                text_to_show = text_all.toString();

            }
            else
            {
                //"15+-0" "15+0." "15+18" "15+0"
                String strArr [] = split_text_all();
                text_all = new StringBuffer(strArr[0]);
                text_all.append(symbol);
                text_to_show = "0";
            }

            setTexttoInputView(false);
        }
        else if(buttonId == R.id.btn_plus)
        {
            input_process_simple_symbol_and_show('+');
            resetButtonColor();
            this.button_plus.setBackgroundColor(Color.parseColor("#FFC0CB"));
        }
        else if(buttonId == R.id.btn_minus)
        {
            input_process_simple_symbol_and_show('-');
            resetButtonColor();
            this.button_minus.setBackgroundColor(Color.parseColor("#FFC0CB"));
        }
        else if(buttonId == R.id.btn_X)
        {
            input_process_simple_symbol_and_show('*');
            resetButtonColor();
            this.button_times.setBackgroundColor(Color.parseColor("#FFC0CB"));
        }
        else if(buttonId == R.id.btn_divided)
        {
            input_process_simple_symbol_and_show('/');
            resetButtonColor();
            this.button_divided.setBackgroundColor(Color.parseColor("#FFC0CB"));
        }
        else if(buttonId == R.id.btn_eq)
        {
            if(this.symbol == null || "=".equals(symbol))
            {
                //"0"= or "15"=
                symbol = "=";
                this.text_all = new StringBuffer(text_to_show);

            }
            else if(this.text_all.charAt( this.text_all.length() - 1) >= '0' && this.text_all.charAt( this.text_all.length() - 1) <= '9')
            {
                //"15+16"=
                double result = getResult();
                this.text_to_show = String.valueOf(result);
                setTexttoInputView(false);
                symbol = "=";
                this.text_all = new StringBuffer(text_to_show);

            }
            else
            {
                //"15+"= ; -> "15+."=
                text_all = text_all.append(0);
                Double result = getResult();
                text_to_show = String.valueOf(result);
                this.setTexttoInputView(false);
                text_all = new StringBuffer(text_to_show);
                symbol = "=";
            }

        }
        else if(buttonId == R.id.btn_sqrt)
        {
            double result = 0d;
            if(symbol == null || "=".equals(symbol))
            {
                //"0"，"16" sqrt
                result = Double.valueOf(this.text_to_show);

                if(result < 0)
                {
                    showNormalDialog();
                    return;
                }
                result = Math.sqrt(result);
                this.text_to_show = String.valueOf(result);
                setTexttoInputView(false);
                this.text_all = new StringBuffer(text_to_show);
                symbol = null;
            }
            else if(text_all.charAt(text_all.length()-1) >='0' && text_all.charAt(text_all.length()-1) <= '9')
            {
                //"18+16"  sqrt
                double rightVariable = Double.valueOf(text_to_show);
                if(rightVariable < 0)
                {
                    showNormalDialog();
                    return;
                }
                else
                {
                    result = Math.sqrt(rightVariable);
                    this.text_to_show = String.valueOf(result);
                    setTexttoInputView(false);

                    String []strArr = split_text_all();
                    this.text_all = new StringBuffer(strArr[0]);
                    text_all.append(symbol).append(text_to_show);
                }

            }
            else
            {
                //"15+"  sqrt
                result = Double.valueOf(this.text_to_show);
                if(result < 0)
                {
                    showNormalDialog();
                    return;
                }
                result = Math.sqrt(result);
                this.text_to_show = String.valueOf(result);
                setTexttoInputView(false);
                this.text_all = new StringBuffer(text_to_show);
                this.text_all.append(symbol);
            }

        }
        else if(buttonId == R.id.btn_p_n)
        {

           if(symbol == null || "=".equals(symbol))
            {
                if("0".equals(text_to_show))
                {
                    //-"0"
                    text_to_show = "-0";
                    text_all = new StringBuffer("-0");
                    setTexttoInputView(true);
                }
                else if("-0".equals(text_to_show)) {
                    //-"-0"
                    text_to_show = "0";
                    text_all = new StringBuffer("0");
                    setTexttoInputView(true);
                }
                else
                {
                    //"5"-
                    char firstChar = text_to_show.charAt(0);
                    if(firstChar == '-')
                    {
                        text_to_show = text_to_show.substring(1);
                    }
                    else
                    {
                        text_to_show = "-" + text_to_show;
                    }

                    text_all = new StringBuffer(text_to_show);
                    setTexttoInputView(true);
                }

                symbol = null;

            }
            else if(text_all.charAt(text_all.length()-1)>='0' && text_all.charAt(text_all.length()-1) <='9')
            {
                if("-0".equals(text_to_show))
                {
                    //"16+-0"-;
                    text_to_show = "0";

                }
                else if("0".equals(text_to_show))
                {
                    //"16+0"-;
                    text_to_show = "-0";
                }
                else
                {
                    //"16+15"-
                    char firstChar = text_to_show.charAt(0);
                    if(firstChar == '-')
                    {
                        text_to_show = text_to_show.substring(1);
                    }
                    else
                    {
                        text_to_show = "-" + text_to_show;
                    }

                }
                String [] strArr = this.split_text_all();
                text_all = new StringBuffer(strArr[0]);
                text_all.append(symbol).append(text_to_show);
                setTexttoInputView(true);
            }
            else if(text_all.charAt(text_all.length()-1)=='.')
            {

                char firstChar = text_to_show.charAt(0);
                if(firstChar == '-')
                {
                    //"2+-0."-
                    text_to_show = text_to_show.substring(1);
                }
                else
                {
                    //"2+0."-
                    text_to_show = "-" + text_to_show;
                }
                String [] strArr = this.split_text_all();
                text_all = new StringBuffer(strArr[0]);
                text_all.append(symbol).append(text_to_show);
                setTexttoInputView(true);
            }
            else
            {
                //"16+"-;
                text_to_show = "-0";
                setTexttoInputView(true);
                text_all.append("-0");
            }
        }
        else if(buttonId == R.id.btn_dot)
        {
            if(null == this.symbol)
            {
                if(text_to_show.indexOf('.') > -1)
                {
                    //"2.".
                    return;
                }
                else
                {
                    //"2".
                    text_to_show += ".";
                    text_all.append('.');
                    setTexttoInputView(true);
                }
            }
            else if("=".equals(this.symbol))
            {
                // symbol is null and "56".
                text_to_show = "0.";
                text_all = new StringBuffer(text_to_show);
                setTexttoInputView(true);
                symbol = null;
            }
            else if(text_all.charAt(text_all.length()-1)>='0' && text_all.charAt(text_all.length()-1) <='9')
            {
                if(text_to_show.indexOf('.') > -1)
                {
                    //"4.1+2.9".
                    return;
                }
                else
                {
                    //"4.1+9".
                    text_to_show += ".";
                    text_all.append('.');
                    setTexttoInputView(true);
                }
            }
            else if(text_all.charAt(text_all.length()-1) == '.')
            {
                //"2+3.".
                return;
            }
            else
            {
                //"2+".
                text_to_show = "0.";
                text_all.append(text_to_show);
                setTexttoInputView(true);
            }

        }
        else if(buttonId == R.id.btn_00)
        {
            if("0".equals(text_to_show))
            {
                return;
            }
            else
            {
                input_process_num_and_show("0");
            }
        }
        else if(buttonId == R.id.btn_1)
        {
            input_process_num_and_show("1");
        }
        else if(buttonId == R.id.btn_2)
        {
            input_process_num_and_show("2");
        }
        else if(buttonId == R.id.btn_3)
        {
            input_process_num_and_show("3");
        }
        else if(buttonId == R.id.btn_4)
        {
            input_process_num_and_show("4");
        }
        else if(buttonId == R.id.btn_5)
        {
            input_process_num_and_show("5");
        }
        else if(buttonId == R.id.btn_6)
        {
            input_process_num_and_show("6");
        }
        else if(buttonId == R.id.btn_7)
        {
            input_process_num_and_show("7");
        }
        else if(buttonId == R.id.btn_8)
        {
            input_process_num_and_show("8");
        }
        else if(buttonId == R.id.btn_9)
        {
            input_process_num_and_show("9");
        }

    }

    /**
     * Set text into the TextView for display
     * Exception:
     * 1. The limitation of input is 10 letters
     * 2. Simplified the result 2.1000 -> 2.1
     *
     * @param inputing
     * When inputing is true， means the user is inputting, it allows the user to input 2.1000
     * When inputing is false, means the result is calculated, then it simplified the result.
     */
    private void setTexttoInputView(boolean inputing )
    {
        if(this.text_to_show.length() > 10)
        {
            //10.00000000000000001
            text_to_show = this.text_to_show.substring(0,10);
        }

        if(!inputing)
        {
            if(text_to_show.indexOf(".") > 0)
            {
                text_to_show = text_to_show.replaceAll("0+?$", "");
                text_to_show = text_to_show.replaceAll("[.]$", "");
            }
        }

        this.textView_Input.setText(text_to_show);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView_Input = findViewById(R.id.text_input);
        setTexttoInputView(true);

        button_0 = findViewById(R.id.btn_00);
        button_1 = findViewById(R.id.btn_1);
        button_2 = findViewById(R.id.btn_2);
        button_3 = findViewById(R.id.btn_3);
        button_4 = findViewById(R.id.btn_4);
        button_5 = findViewById(R.id.btn_5);
        button_6 = findViewById(R.id.btn_6);
        button_7 = findViewById(R.id.btn_7);
        button_8 = findViewById(R.id.btn_8);
        button_9 = findViewById(R.id.btn_9);
        button_AC = findViewById(R.id.btn_ac);
        button_C = findViewById(R.id.btn_C);
        button_eq = findViewById(R.id.btn_eq);
        button_plus = findViewById(R.id.btn_plus);
        button_minus = findViewById(R.id.btn_minus);
        button_times = findViewById(R.id.btn_X);
        button_divided = findViewById(R.id.btn_divided);
        button_dot = findViewById(R.id.btn_dot);
        button_sqrt = findViewById(R.id.btn_sqrt);
        button_p_n = findViewById(R.id.btn_p_n);

        button_0.setOnClickListener(this);
        button_1.setOnClickListener(this);
        button_2.setOnClickListener(this);
        button_3.setOnClickListener(this);
        button_4.setOnClickListener(this);
        button_5.setOnClickListener(this);
        button_6.setOnClickListener(this);
        button_7.setOnClickListener(this);
        button_8.setOnClickListener(this);
        button_9.setOnClickListener(this);
        button_AC.setOnClickListener(this);
        button_C.setOnClickListener(this);
        button_eq.setOnClickListener(this);
        button_plus.setOnClickListener(this);
        button_minus.setOnClickListener(this);
        button_times.setOnClickListener(this);
        button_divided.setOnClickListener(this);
        button_dot.setOnClickListener(this);
        button_sqrt.setOnClickListener(this);
        button_p_n.setOnClickListener(this);

    }

    private Button button_0;
    private Button button_1;
    private Button button_2;
    private Button button_3;
    private Button button_4;
    private Button button_5;
    private Button button_6;
    private Button button_7;
    private Button button_8;
    private Button button_9;
    private Button button_AC;
    private Button button_C;
    private Button button_eq;
    private Button button_plus;
    private Button button_minus;
    private Button button_times;
    private Button button_divided;
    private Button button_dot;
    private Button button_sqrt;
    private Button button_p_n;

    /**
     * When error happens, it shows a simple dialogue
     */
    private void showNormalDialog(){

        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(MainActivity.this);

        normalDialog.setTitle("Error");
        normalDialog.setMessage("The operand has to be >= 0");

        normalDialog.setNegativeButton("Close",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        normalDialog.show();
    }



}

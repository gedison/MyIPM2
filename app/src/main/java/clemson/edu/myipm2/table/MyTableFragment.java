package clemson.edu.myipm2.table;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import clemson.edu.myipm2.R;
import clemson.edu.myipm2.database.dao.TableEntry;
import clemson.edu.myipm2.table.utility.HorizontalScrollViewListener;
import clemson.edu.myipm2.table.utility.MyScrollView;
import clemson.edu.myipm2.table.utility.MyScrollViewHorizontal;
import clemson.edu.myipm2.table.utility.ScrollViewListener;
import clemson.edu.myipm2.utility.SharedPreferencesHelper;

public class MyTableFragment extends Fragment implements
        ScrollViewListener, HorizontalScrollViewListener, View.OnClickListener {

    //Tables for the ridiculousness
    protected TableLayout topLeft;
    protected TableLayout topRight;
    protected TableLayout bottomLeft;
    protected TableLayout bottomRight;


    List<TableRow> rowsToAdd_TopLeft = new ArrayList<>();
    List<TableRow> rowsToAdd_TopRight= new ArrayList<>();
    List<TableRow> rowsToAdd_BottomLeft = new ArrayList<>();
    List<TableRow> rowsToAdd_BottomRight= new ArrayList<>();

    //Scroll Views for same
    MyScrollView verticalScroll1;
    MyScrollView verticalScroll2;
    MyScrollViewHorizontal horizontalScroll1;
    MyScrollViewHorizontal horizontalScroll2;

    //Table Sizes
    protected int headerHeight;
    protected int columnHeight;
    protected int colorHeight;

    protected float header = 40;
    protected float column = 30;
    protected float color  = 20;

    //data
    protected String[] rowNames;
    protected TableEntry[] data=null;

    //Pulse
    protected String pulseRow="";
    protected boolean pulse=true;
    protected ValueAnimator colorAnimation;

    //sort index
    protected int sortBy=0;
    protected boolean doubleSort=false;

    private String[] colors = {"#FF0000", "#000000", "#2B2B2B", "#474646", "#656565", "#868686", "#A1A1A1", "#BCBCBC", "#D8D8D8"};

    private int widthModifier = 2;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            widthModifier = 3;
        }else{
            widthModifier = 2;
        }

        View tableParent = inflater.inflate(R.layout.table_fragment, container, false);

        //Tables
        topLeft = (TableLayout)tableParent.findViewById(R.id.top_left2);
        topRight = (TableLayout)tableParent.findViewById(R.id.sideHeader2);
        bottomLeft = (TableLayout)tableParent.findViewById(R.id.mainColumn);
        bottomRight = (TableLayout)tableParent.findViewById(R.id.sideColumn);

        //Scroll Views
        verticalScroll1 = (MyScrollView)tableParent.findViewById(R.id.bottom_left);
        verticalScroll1.setVerticalScrollBarEnabled(false);
        verticalScroll1.setScrollViewListener(this);
        verticalScroll2 = (MyScrollView)tableParent.findViewById(R.id.bottom_right);
        verticalScroll2.setScrollViewListener(this);
        horizontalScroll2 = (MyScrollViewHorizontal)tableParent.findViewById(R.id.bottom_right_hor);
        horizontalScroll2.setScrollViewListener(this);
        horizontalScroll1 = (MyScrollViewHorizontal)tableParent.findViewById(R.id.top_right);
        horizontalScroll1.setScrollViewListener(this);
        horizontalScroll1.setHorizontalScrollBarEnabled(false);

        //Layout Stuff
        headerHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, header, getResources().getDisplayMetrics());
        colorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, color, getResources().getDisplayMetrics());
        columnHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, column, getResources().getDisplayMetrics());

        //Get Data
        if(this.getArguments()!=null){
            data = (TableEntry[]) this.getArguments().getSerializable("data");
            rowNames = this.getArguments().getStringArray("rownames");
            pulseRow = this.getArguments().getString("pulseRow");
            this.getArguments().remove("pulseRow");
        }return tableParent;
    }

    int pulseIndex=0;
    public void pulseRow(){
        if(pulse && pulseRow!=null && !pulseRow.isEmpty()) {
            pulse=false;
            for(int i=0; i<bottomLeft.getChildCount(); i++){
                TableRow pulseChild = (TableRow) bottomLeft.getChildAt(i);
                TextView cell = (TextView)pulseChild.getChildAt(0);
                if(cell.getTag()!=null && pulseRow.equals(cell.getTag().toString())){
                    pulseIndex=i;
                }
            }


            colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), Color.WHITE, Color.RED);
            colorAnimation.setDuration(800); // milliseconds

            colorAnimation.setRepeatCount(5);
            colorAnimation.setRepeatMode(ValueAnimator.REVERSE);

            colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {

                    TableRow pulseChild = (TableRow) bottomRight.getChildAt(pulseIndex);
                    for (int j = 0; j < pulseChild.getChildCount(); j++)
                        pulseChild.getChildAt(j).setBackgroundColor((Integer) animator.getAnimatedValue());
                    pulseChild = (TableRow) bottomLeft.getChildAt(pulseIndex);
                    for (int j = 0; j < pulseChild.getChildCount(); j++)
                        pulseChild.getChildAt(j).setBackgroundColor((Integer) animator.getAnimatedValue());

                }

            });
            colorAnimation.start();
        }
    }

    private TextView setColor(String text, String color, boolean offRow){
        TextView columnText =  this.setTableCell(text, offRow);

        if(!color.contains(":")){
            columnText.setTextColor(Color.parseColor('#' + color));
            return columnText;
        }else{
            String[] colors = color.split(":");
            String[] fracs = text.split(";");
            String splitText="";
            for(int fracNum=0; fracNum<fracs.length; fracNum++){
                if(fracNum!=0)splitText+=";";
                splitText+="<FONT COLOR=#"+colors[fracNum]+">"+fracs[fracNum]+"</FONT>";
            }
            columnText.setText(Html.fromHtml(splitText));
        }return columnText;
    }


    //Set Default Header Cell
    protected TextView setHeaderCell(String text){
        TextView columnText = this.setTableCell(" "+text+" ", false);
        columnText.setTextSize(16);
        columnText.setGravity(Gravity.CENTER);

        int width = this.getResources().getDisplayMetrics().widthPixels;
        columnText.setHeight(headerHeight);
        if(text.length() < 15 || text.contains("\n"))columnText.setWidth((width/4)-5);
        columnText.setTypeface(null, Typeface.BOLD);

        return columnText;
    }

    //Set Default Header Cell with Tag
    protected TextView setHeaderCellWithListener(String text, int id){
        TextView columnText = this.setHeaderCell(text);
        columnText.setTag(id);
        columnText.setOnClickListener(this);
        return columnText;
    }

    protected TextView setSearchHeader(int column, TextView columnText, int width){
        if(sortBy==column){
            if(doubleSort)columnText.setBackgroundResource(R.color.pwd_blue);
            else columnText.setBackgroundResource(R.color.search_blue);
            columnText.setTextColor(Color.WHITE);
        }return columnText;
    }

    //Set Default Table Cell
    protected TextView setTableCell(String text, boolean offRow){
        TextView columnText = new TextView(getActivity());
        columnText.setText(text);

        columnText.setTextSize(14);
        if(columnText.getText().length()>45)columnText.setTextSize(8);
        else if(columnText.getText().length()>30)columnText.setTextSize(10);
        else if(columnText.getText().length()>15)columnText.setTextSize(12);

        if(offRow) columnText.setHeight(colorHeight);
        else columnText.setHeight(columnHeight);

        columnText.setGravity(Gravity.CENTER);
        columnText.setBackgroundColor(Color.WHITE);

        TableRow.LayoutParams llp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        llp.setMargins(0, 0, 10, 0);
        columnText.setLayoutParams(llp);
        return columnText;
    }

    //Set default Table Row
    protected TableRow setTableRow(){
        TableRow tempRow = new TableRow(getActivity());
        tempRow.setBackgroundResource(R.color.lgtGrey);
        tempRow.setPadding(0, 0, 0, 5);
        tempRow.setGravity(Gravity.CENTER);
        return tempRow;
    }

    private void setTopLeftTable(int width){
        TableRow tempRow = this.setTableRow();
        TableRow dummyRow = this.setTableRow();

        String text = rowNames[0];
        TextView columnText = this.setHeaderCellWithListener(text, 0);
        columnText = setSearchHeader(0, columnText, width);
        columnText.setWidth(width/widthModifier);

        TextView dummyText = this.setHeaderCell(text);
        dummyText.setHeight(0);
        dummyText.setWidth(width/widthModifier);

        tempRow.addView(columnText);
        dummyRow.addView(dummyText);
        topLeft.addView(tempRow);
        bottomLeft.addView(dummyRow);
    }

    private void setTopRightTable(){
        TableRow tempRow = this.setTableRow();
        TableRow dummyRow = this.setTableRow();
        for(int column=1; column<rowNames.length; column++){
            String text = rowNames[column];
            TextView columnText = this.setHeaderCellWithListener(text, column);
            columnText = setSearchHeader(column, columnText, 0);

            TextView dummyText = this.setHeaderCell(text);
            dummyText.setHeight(0);
            tempRow.addView(columnText);
            dummyRow.addView(dummyText);
        }topRight.addView(tempRow);
        bottomRight.addView(dummyRow);
    }

    public void setBottomLeftTable(int width){
        TableRow tempRow = this.setTableRow();
        TextView columnText = this.setTableCell("", true);
        columnText.setBackgroundColor(Color.parseColor(colors[0]));
        tempRow.addView(columnText);

        bottomLeft.addView(tempRow);

        //Data Rows
        for(int row=0; row<data.length; row++){
            tempRow = this.setTableRow();
            TableRow dummyRow = this.setTableRow();
            String text = data[row].getDisplayAtIndex(0);

            columnText = this.setColor(text, data[row].getColorAtIndex(0), false);

            //Set Active Ingredient Frac Code Color
            if(rowNames[0].equals(getResources().getString(R.string.ai_column1))){
                columnText = this.setColor(text, data[row].getColorAtIndex(0), false);
                columnText.setTag(Integer.parseInt(data[row].getId()));
                columnText.setWidth(width/widthModifier);
                columnText.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        ((TableActivity)getActivity()).createBabyTable((Integer)v.getTag());
                    }
                });
            }else{
                columnText.setWidth(width/widthModifier);
                columnText.setTag(Integer.parseInt(data[row].getOtherId()));

                columnText.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        ((TableActivity)getActivity()).createBabyTableActive((Integer)v.getTag());
                    }
                });

            }

            tempRow.addView(columnText);
            bottomLeft.addView(tempRow);

            TextView dummyText = this.setTableCell(text, false);
            dummyText.setHeight(0);
            dummyText.setWidth(width/widthModifier);
            dummyRow.addView(dummyText);
            dummyRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, 0));
            dummyRow.setPadding(0, 0, 0, 0);

            topLeft.addView(dummyRow);
        }

        //Add Fill Content Row
        tempRow = this.setTableRow();
        TextView dummyText = this.setTableCell("", false);
        dummyText.setWidth(width/widthModifier);
        TableRow.LayoutParams llp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
        llp.setMargins(0, 0, 10, 0);
        dummyText.setLayoutParams(llp);
        tempRow.addView(dummyText);
        tempRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f));
        bottomLeft.addView(tempRow);
    }

    public void setBottomRightTable(){
        TableRow tempRow = this.setTableRow();
        for(int column=1; column < rowNames.length; column++){
            TextView columnText = this.setTableCell("", true);
            String color = "#D8D8D8";
            if(column < colors.length)color = colors[column];
            columnText.setBackgroundColor(Color.parseColor(color));
            tempRow.addView(columnText);
        }

        bottomRight.addView(tempRow);

        //Data Rows
        for(int row=0; row<data.length; row++){
            tempRow = this.setTableRow();
            TableRow dummyRow = this.setTableRow();
            for(int column=1; column < rowNames.length; column++){

                //Get String
                String text = data[row].getDisplayAtIndex(column);



                TextView columnText = this.setColor(text, ((data[row].getColorAtIndex(column)).equals("ffffff") ? "000000" : data[row].getColorAtIndex(column)), false);
                TextView dummyText = this.setTableCell(text, false);
                if(column>=10)columnText.setBackgroundColor(Color.parseColor('#' + data[row].getColorAtIndex(column)));
                dummyText.setHeight(0);

                tempRow.addView(columnText);
                dummyRow.addView(dummyText);

            }

            bottomRight.addView(tempRow);

            dummyRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, 0));
            dummyRow.setPadding(0, 0, 0, 0);

            topRight.addView(dummyRow);
        }

        //Fill Content Row
        tempRow = this.setTableRow();
        for(int column=1; column < rowNames.length; column++){
            TextView dummyText = this.setTableCell("", false);
            TableRow.LayoutParams llp =  new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
            llp.setMargins(0, 0, 10, 0);
            dummyText.setLayoutParams(llp);
            tempRow.addView(dummyText);
        }
        tempRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f));
        bottomRight.addView(tempRow);
    }


    private class MyRunnable implements Runnable{
        int width;

        MyRunnable(int width){
            this.width = width;
        }

        public void run() {
            System.out.println("Starting");
            setBottomLeftTable(width);
            setBottomRightTable();
            pulseRow();
            System.out.println("Done");
        }
    };

    private Handler mHandler = new Handler();
    private void loadDataIntoTable(){
        int width = this.getResources().getDisplayMetrics().widthPixels;

        setTopLeftTable(width);
        setTopRightTable();

        mHandler.postDelayed(new MyRunnable(width), 1);

    }

    public void setTableData(TableEntry[] data){
        if(!this.data.equals(data)) {
            this.data = data;
            if (colorAnimation != null && colorAnimation.isRunning()) colorAnimation.end();
            resetTable();
        }
    }

    //remove all views from table and reload
    public void resetTable(){
        topLeft.removeAllViews();
        topRight.removeAllViews();
        bottomLeft.removeAllViews();
        bottomRight.removeAllViews();
        loadDataIntoTable();
    }

    private void sortData() {
        Arrays.sort(this.data, new Comparator<TableEntry>() {
            public int compare(TableEntry a, TableEntry b) {
                return a.compareTo(b, sortBy, doubleSort);
            }
        });
    }

    public void onClick(View v) {
        int id = Integer.parseInt(v.getTag().toString());
        doubleSort = sortBy == id && !doubleSort;
        sortBy=id;

        this.sortData();
        this.resetTable();
    }

    public void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy) {
        if(scrollView==verticalScroll1){
            verticalScroll2.scrollTo(verticalScroll2.getScrollX(),y);
        }else if(scrollView==verticalScroll2){
            verticalScroll1.scrollTo(verticalScroll1.getScrollX(), y);
        }
    }

    public void onScrollChanged(MyScrollViewHorizontal scrollView, int x,int y, int oldx, int oldy) {
        if(scrollView==horizontalScroll1){
            horizontalScroll2.scrollTo(x, horizontalScroll2.getScrollY());
        }else if(scrollView==horizontalScroll2){
            horizontalScroll1.scrollTo(x, horizontalScroll1.getScrollY());
        }
    }

}

package com.example.rpm;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.rpm.controllers.ApiService;
import com.example.rpm.modelsDB.Food;
import com.example.rpm.modelsJSON.CountForm;

import java.util.List;

public class FoodListAdapter extends ArrayAdapter<Food> {

    private LayoutInflater inflater;
    private Context thisContext;
    private int layout;
    private List<Food> foods;
    private ApiService apiService = new ApiService();
    private CountForm countform = new CountForm();
    boolean show = false;
    public FoodListAdapter(@NonNull Context context, int resource, @NonNull List<Food> objects) {
        super(context, resource, objects);
        this.foods = objects;
        this.layout = resource;
        this.thisContext = context;
        this.inflater = LayoutInflater.from(context);
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(this.layout, parent, false);

        TextView nameView = view.findViewById(R.id.std_first_name_text_view);
        TextView widthView= view.findViewById(R.id.std_second_name_text_view);
        ImageButton button = view.findViewById(R.id.imageButton2);
        setViewListeners(nameView, widthView, button,position,view);

        TextView price = (TextView) view.findViewById(R.id.std_first_name_text_view1);
        TextView inStock = (TextView) view.findViewById(R.id.std_second_name_text_view1);
        LinearLayout info = (LinearLayout) view.findViewById(R.id.std_first_name_text_view142);
        Food food = this.foods.get(position);

        nameView.setText(food.name);
        widthView.setText(food.width);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    countform = apiService.selectCount(food.name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        sleep(1000);
       if(countform == null){
            price.setText("-");
           inStock.setText("-");}
            else { price.setText(String.valueOf(countform.price));
            if(countform.inStock==1)
                inStock.setText("Доступен");
            else  inStock.setText("Недоступен");}
            countform= new CountForm();

        info.setVisibility(View.INVISIBLE);
        return view;
    }    private void sleep(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void setViewListeners(TextView nameView,TextView nameWidth,ImageButton button,int position, View view){
        //Long Click
        nameView.setOnLongClickListener(v->{
            ((FoodListActivity)thisContext).sortByColumn(0);
            return true;
        });
        nameWidth.setOnLongClickListener(v->{
            ((FoodListActivity)thisContext).sortByColumn(1);
            return true;
        });
        button.setOnClickListener(v->{

            if(!show){
                view.findViewById(R.id.std_first_name_text_view142).setVisibility(View.VISIBLE);
                show=true;
            }else{

                view.findViewById(R.id.std_first_name_text_view142).setVisibility(View.INVISIBLE);

                show=false;
            }

        });
        //Short Click
        nameView.setOnClickListener(v->{
            ((FoodListActivity)thisContext).simpleClick(position);
        });

        nameWidth.setOnClickListener(v->{
            ((FoodListActivity)thisContext).simpleClick(position);
        });
    }


    public interface ISortByColumn{
        void sortByColumn(int columnId);
        void simpleClick(int position);
        void showDet(int position,boolean show);
    }
}

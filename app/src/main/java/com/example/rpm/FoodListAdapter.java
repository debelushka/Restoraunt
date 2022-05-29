package com.example.rpm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.rpm.modelsDB.Food;

import java.util.List;

public class FoodListAdapter extends ArrayAdapter<Food> {

    private LayoutInflater inflater;
    private Context thisContext;
    private int layout;
    private List<Food> foods;

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
        setViewListeners(nameView, widthView, button,position);

        Food food = this.foods.get(position);

        nameView.setText(food.name);
        widthView.setText(food.width);

        return view;
    }
    private void setViewListeners(TextView nameView,TextView nameWidth,ImageButton button,int position){
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
            ((FoodListActivity)thisContext).showDet(position);
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
        void showDet(int position);
    }
}

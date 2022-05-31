package com.example.rpm;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rpm.controllers.ApiService;
import com.example.rpm.modelsDB.DataHandler;
import com.example.rpm.modelsDB.Food;
import com.example.rpm.modelsJSON.CountForm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FoodListActivity extends AppCompatActivity implements FoodListAdapter.ISortByColumn {

    private Menu foodMenu;
    private ListView listView;

    private DataHandler dataHandler = new DataHandler();

    private int foodPod;
    private String foodPodC;
    private FoodListAdapter empAdapter;
    private ArrayList<Food> foodList = new ArrayList<>();
    private ApiService apiService = new ApiService();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        initializeValues();
        if(foodPod != -1){
            getDatabaseInfo();
        }
        else{
            Toast.makeText(getApplicationContext(), "Не удалось получить данные", Toast.LENGTH_SHORT)
                    .show();
        }
    }



    private void initializeValues(){
        listView = findViewById(R.id.food_list_view);
        Intent intent = getIntent();
        Bundle args = intent.getExtras();
        foodPod = args.getInt("categoriesId");
        foodPodC = args.getString("title");

        setTitle(foodPodC);
        dataHandler.createOrConnectToDB(getApplicationContext());
    }
    private void getDatabaseInfo(){
        GetFood getFood = new GetFood();
        getFood.execute();
    }
    private void setListView(){


        empAdapter = new FoodListAdapter(this, R.layout.restoran_element_listview , foodList);
        listView.setAdapter(empAdapter);
    }

    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.food_list_menu, menu);
        foodMenu = menu;
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.add_new_food:{
                createOrChangeFood(true, null);
                return true;
            }
            case R.id.change_foodPodC_name:{
                AlertDialog inputDialog = new AlertDialog.Builder(FoodListActivity.this).create();
                View vv = (LinearLayout) getLayoutInflater().inflate(R.layout.input_food_p, null);
                inputDialog.setView(vv);
                inputDialog.setCancelable(true);
                ((EditText) vv.findViewById(R.id.input_direction_name)).setText(foodPodC);

                ((Button) vv.findViewById(R.id.add_direction_accept)).setText("Изменить");
                ((Button) vv.findViewById(R.id.add_direction_accept)).setOnClickListener(v->{


                    String newName = ((EditText) vv.findViewById(R.id.input_direction_name)).getText().toString();
                    newName = newName.trim();

                    if(newName.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Недопустимое направление", Toast.LENGTH_SHORT)
                                .show();
                        inputDialog.cancel();
                        return;
                    }


                    setTitle(((EditText) vv.findViewById(R.id.input_direction_name)).getText().toString());

                    dataHandler.updateDirection(foodPod, ((EditText) vv.findViewById(R.id.input_direction_name)).getText().toString());

                    inputDialog.cancel();
                });
                ((Button) vv.findViewById(R.id.add_direction_decline)).setOnClickListener(v->{
                    inputDialog.cancel();
                });
                inputDialog.show();
                return true;
            }
            case R.id.delete_FoodPodC:{
                dataHandler.deleteDirection(foodPod);
                Intent intent = new Intent(FoodListActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }
    private void showDetails(Food food,boolean show){


        dataHandler.updateFood(food);
    };

    private void createOrChangeFood(boolean createNew, Food food){
        AlertDialog inputDialog = new AlertDialog.Builder(FoodListActivity.this).create();
        View vv = (LinearLayout) getLayoutInflater().inflate(R.layout.input_food, null);
        inputDialog.setView(vv);
        inputDialog.setCancelable(true);

        EditText firstNameTextView = (EditText) vv.findViewById(R.id.input_std_first_name);
        EditText secondNameTextView = (EditText) vv.findViewById(R.id.input_std_second_name);

        Button acceptButton = (Button) vv.findViewById(R.id.add_std_accept);


        if(!createNew){
            firstNameTextView.setText(food.name);
            secondNameTextView.setText(food.width);

            acceptButton.setText("Изменить");
            ((Button) vv.findViewById(R.id.add_std_delete)).setVisibility(View.VISIBLE);
        }else{
            acceptButton.setText("Добавить");
            ((Button) vv.findViewById(R.id.add_std_delete)).setVisibility(View.INVISIBLE);
        }

        acceptButton.setOnClickListener(v->{
            if(createNew){
                Food newFood = new Food();
                newFood.name = ((EditText) vv.findViewById(R.id.input_std_first_name)).getText().toString();
                newFood.width = ((EditText) vv.findViewById(R.id.input_std_second_name)).getText().toString();

                newFood.podCid = foodPod;
                dataHandler.addFood(newFood);



            }
            else{
                food.name = firstNameTextView.getText().toString();
                food.width = secondNameTextView.getText().toString();

                dataHandler.updateFood(food);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            GetFood getFood = new GetFood();
            getFood.execute();
            inputDialog.cancel();
        });
        ((Button) vv.findViewById(R.id.add_std_decline)).setOnClickListener(v->{
            inputDialog.cancel();
        });

        ((Button) vv.findViewById(R.id.add_std_delete)).setOnClickListener(v->{
            dataHandler.deleteFood(food);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            GetFood getFood = new GetFood();
            getFood.execute();
            inputDialog.cancel();
        });
        inputDialog.show();
    }

    @Override
    public void sortByColumn(int columnId) {
        if(columnId == 0){
            Comparator<Food> comparator = (o1, o2) -> o1.name.compareTo(o2.name);
            Collections.sort(foodList, comparator);
            setListView();
            Toast.makeText(getApplicationContext(), "Вы нажали на столбец с именем", Toast.LENGTH_SHORT)
                    .show();
        }
        if(columnId == 1){
            Comparator<Food> comparator = (o1, o2) -> o1.width.compareTo(o2.width);
            Collections.sort(foodList, comparator);
            setListView();
            Toast.makeText(getApplicationContext(), "Вы нажали на столбец с фамилией", Toast.LENGTH_SHORT)
                    .show();
        }

    }

    @Override
    public void simpleClick(int position) {
        createOrChangeFood(false, foodList.get(position));
    }
    @Override
    public void showDet(int position,boolean show) {
        showDetails(foodList.get(position),show);
    }

    class GetFood extends AsyncTask<Void, Void, ArrayList<Food>> {
        @Override
        protected ArrayList<Food> doInBackground(Void... unused) {
            return (ArrayList<Food>) dataHandler
                    .getDB()
                    .foodDao()
                    .getPodCFood(foodPod);
        }
        @Override
        protected void onPostExecute(ArrayList<Food> foodArrayList) {
            foodList = foodArrayList;
            if(foodList.size() < 1){
                Toast.makeText(getApplicationContext(), "Пока нет студентов", Toast.LENGTH_SHORT)
                        .show();
            }else{
                Toast.makeText(getApplicationContext(), "Студенты загружены (" + String.valueOf(foodList.size()) + ")", Toast.LENGTH_SHORT)
                        .show();
            }
            setListView();

        }

    }
}
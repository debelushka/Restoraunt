package com.example.rpm;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.rpm.controllers.ApiService;
import com.example.rpm.modelsDB.DataHandler;
import com.example.rpm.modelsDB.FoodPodC;
import com.example.rpm.modelsDB.Food;
import com.example.rpm.modelsDB.Restaurant;
import com.example.rpm.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.P)
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ActivityMainBinding binding;

    private NavigationView navigationView;
    private ListView FoddPath;
    private Menu mainMenu;

    private DataHandler dataHandler = new DataHandler();
    private int currentFacultyId;

    private ArrayList<Restaurant> allRestaurant;
    private ArrayList<FoodPodC> allFoodPodCS;
    private ArrayList<FoodPodC> currentFacultyFoodPodCS;
    private ArrayList<Food> allFood;
    private ApiService apiService = new ApiService();

    //Initializing
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeValues();

        getDatabaseInfo();

        createNavigationMenu();

    }

    private void initializeValues(){
        currentFacultyId = -1;
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        FoddPath = (ListView) findViewById(R.id.directionsListView);
        FoddPath.setOnItemClickListener((parent, view, position, id)->{
            Intent intent = new Intent(MainActivity.this, FoodListActivity.class);
            int dirId = currentFacultyFoodPodCS.get(position).id;
            String title = currentFacultyFoodPodCS.get(position).name;

            intent.putExtra("categoriesId", dirId);
            intent.putExtra("title", title);
            startActivity(intent);
        });

        dataHandler.createOrConnectToDB(getApplicationContext());
    }

    private void setListViewAdapter(){
        ArrayList<String> str = new ArrayList<>();
        for(FoodPodC foodPodC : currentFacultyFoodPodCS){
            int amount = 0;
            for(Food food: allFood){
                if(food.podCid == foodPodC.id) amount++;
            }
            str.add(foodPodC.name);
        }
        FoddPath.setAdapter(
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1,
                        str)
        );
    }

    private void getDatabaseInfo(){
        GetRestaurant getRestaurant = new GetRestaurant();
        getRestaurant.execute();
        GetFoodP getFoodP = new GetFoodP();
        getFoodP.execute();
        GetFood getFood = new GetFood();
        getFood.execute();
    }

    @Override public void onBackPressed() {
        DrawerLayout dl = (DrawerLayout)findViewById(R.id.drawer_layout);
        if(dl.isDrawerOpen(GravityCompat.START)){
            dl.closeDrawer(GravityCompat.START);
            return;
        }
        if(currentFacultyId != -1){
            currentFacultyId = -1;
            FoddPath.setAdapter(
                    new ArrayAdapter<String>(this,
                            android.R.layout.simple_list_item_1)
            );
            changeMenuOptions(false);
            setTitle("Restaurant");
        }
    }

    //Menu
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        mainMenu = menu;
        return true;
    }

    @Override public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.add_faculty:{
                addOrChangeFaculty(true);
                return true;
            }
            case R.id.change_faculty_name:{
                addOrChangeFaculty(false);
                return true;
            }
            case R.id.add_direction:{
                AlertDialog inputDialog = new AlertDialog.Builder(MainActivity.this).create();
                View vv = (LinearLayout) getLayoutInflater().inflate(R.layout.input_food_p, null);
                inputDialog.setView(vv);
                inputDialog.setCancelable(true);

                ((Button) vv.findViewById(R.id.add_direction_accept)).setOnClickListener(v->{


                    String newName = ((EditText) vv.findViewById(R.id.input_direction_name)).getText().toString();
                    newName = newName.trim();

                    if(newName.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Недопустимый вид еды", Toast.LENGTH_SHORT)
                                .show();
                        inputDialog.cancel();
                        return;
                    }


                    FoodPodC foodPodC = new FoodPodC();
                    foodPodC.name = ((EditText) vv.findViewById(R.id.input_direction_name)).getText().toString();
                    foodPodC.categoriesId = currentFacultyId;

                    dataHandler.addDirection(foodPodC);
                    sleep(500);
                    GetFoodP getFoodP = new GetFoodP();
                    getFoodP.execute();

                    inputDialog.cancel();
                });
                ((Button) vv.findViewById(R.id.add_direction_decline)).setOnClickListener(v->{
                    inputDialog.cancel();
                });
                inputDialog.show();
                return true;
            }
            case R.id.delete_faculty:{
                changeMenuOptions(false);
                setTitle("Restaurant");
                dataHandler.deleteFaculty(Restaurant.findFacultyById(allRestaurant, currentFacultyId));
                currentFacultyId = -1;
                sleep(500);
                getDatabaseInfo();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeMenuOptions(boolean visible){
        MenuItem addDepItem = mainMenu.findItem(R.id.add_direction);
        MenuItem change = mainMenu.findItem(R.id.change_faculty_name);
        MenuItem deleteFaculty = mainMenu.findItem(R.id.delete_faculty);

        addDepItem.setVisible(visible);
        change.setVisible(visible);
        deleteFaculty.setVisible(visible);
    }

    private void addOrChangeFaculty(boolean createNew){
        AlertDialog inputDialog = new AlertDialog.Builder(MainActivity.this).create();
        View vv = (LinearLayout) getLayoutInflater().inflate(R.layout.input_new_catecoria_layout, null);
        inputDialog.setView(vv);
        inputDialog.setCancelable(true);
        EditText editFacultyName = (EditText) vv.findViewById(R.id.editFacultyName);
        Button accept = (Button) vv.findViewById(R.id.addFacultyAccept);

        if(!createNew){
            accept.setText("Изменить");
            editFacultyName.setText(Restaurant.findFacultyById(allRestaurant, currentFacultyId).name);
        }
        else accept.setText("Добавить");
        accept.setOnClickListener(v->{

            String newName = editFacultyName.getText().toString();
            newName = newName.trim();
            if(newName.isEmpty()){
                Toast.makeText(getApplicationContext(), "Недопустимое имя факультета", Toast.LENGTH_SHORT)
                        .show();
                inputDialog.cancel();
                return;
            }

            if(createNew){
                dataHandler.addFaculty(editFacultyName.getText().toString());

            }
            else{
                setTitle(editFacultyName.getText().toString());
                Restaurant restaurant = Restaurant.findFacultyById(allRestaurant, currentFacultyId);
                restaurant.name = editFacultyName.getText().toString();
                dataHandler.updateFaculty(restaurant);
            }
            sleep(500);
            GetRestaurant getRestaurant = new GetRestaurant();
            getRestaurant.execute();
            inputDialog.cancel();
        });
        ((Button) vv.findViewById(R.id.addFacultyDecline)).setOnClickListener(v->{
            inputDialog.cancel();
        });
        inputDialog.show();
    }

    //Navigation Drawer

    private void createNavigationMenu() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, myToolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void updateNavigationMenuValues(){
        navigationView.getMenu().clear();

        if(allRestaurant.size() > 0){
            for(Restaurant restaurant : allRestaurant){
                navigationView.getMenu().add(Menu.NONE, restaurant.id, Menu.NONE, restaurant.name);
            }
        }
        else navigationView.getMenu().add(Menu.NONE, 0, Menu.NONE, "Еще нет факультетов");


        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        changeMenuOptions(true);
        currentFacultyId = item.getItemId();
        setTitle(Restaurant.findFacultyById(allRestaurant, currentFacultyId).name);
        getDatabaseInfo();
        if(allFoodPodCS.size() > 0){
            searchForFacultyDirections();
            setListViewAdapter();
        }

        DrawerLayout dl = (DrawerLayout)findViewById(R.id.drawer_layout);

        if(dl.isDrawerOpen(GravityCompat.START)){
            dl.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    private void sleep(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void searchForFacultyDirections(){
        currentFacultyFoodPodCS = new ArrayList<>();
        for(FoodPodC foodPodC : allFoodPodCS){
            if(foodPodC.categoriesId == currentFacultyId) currentFacultyFoodPodCS.add(foodPodC);
        }
    }

    class GetRestaurant extends AsyncTask<Void, Void, ArrayList<Restaurant>> {
        @Override
        protected ArrayList<Restaurant> doInBackground(Void... unused) {
            return (ArrayList<Restaurant>) dataHandler.getDB().restaurantDao().getAll();
        }
        @Override
        protected void onPostExecute(ArrayList<Restaurant> restaurant) {
            allRestaurant = restaurant;
            Toast.makeText(getApplicationContext(), "Ресторан загружен(" + String.valueOf(allRestaurant.size()) + ")", Toast.LENGTH_SHORT)
                    .show();
            updateNavigationMenuValues();
        }
    }

    class GetFoodP extends AsyncTask<Void, Void, ArrayList<FoodPodC>> {
        @Override
        protected ArrayList<FoodPodC> doInBackground(Void... unused) {
            return (ArrayList<FoodPodC>) dataHandler.getDB().foodPodCDao().getAll();
        }
        @Override
        protected void onPostExecute(ArrayList<FoodPodC> foodPodCArrayList) {
            allFoodPodCS = foodPodCArrayList;
            if(currentFacultyId != -1){
                searchForFacultyDirections();
                setListViewAdapter();
                //((ArrayAdapter) departmentsList.getAdapter()).notifyDataSetChanged();
            }
            Toast.makeText(getApplicationContext(), "Виды блюд загружены(" + String.valueOf(allFoodPodCS.size()) + ")", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    class GetFood extends AsyncTask<Void, Void, ArrayList<Food>> {
        @Override
        protected ArrayList<Food> doInBackground(Void... unused) {
            return (ArrayList<Food>) dataHandler.getDB().foodDao().getAll();
        }
        @Override
        protected void onPostExecute(ArrayList<Food> foodArrayList) {
            allFood = foodArrayList;
            Toast.makeText(getApplicationContext(), "Блюда загружена (" + String.valueOf(allFood.size()) + ")", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
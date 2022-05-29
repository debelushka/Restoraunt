package com.example.rpm.modelsDB;

import android.content.Context;

import java.util.List;

public class DataHandler {

    private AppDatabase db;
    private RestaurantDao facultyDao;
    private FoodPodCDao foodPodCDao;
    private FoodDao foodDao;

    public void createOrConnectToDB(Context context){
        db = AppDatabase.getInstance(context);

        facultyDao = db.restaurantDao();
        foodPodCDao = db.foodPodCDao();
        foodDao = db.foodDao();
    }
    public AppDatabase getDB(){
        return db;
    }


    //Cate
    public void addFaculty(String name){
        Restaurant restaurant = new Restaurant();
        restaurant.name = name;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                facultyDao.insertAll(restaurant);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    public void addFaculty(int id, String name){
        Restaurant restaurant = new Restaurant();
        restaurant.id = id;
        restaurant.name = name;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                facultyDao.insertAll(restaurant);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    public void deleteFaculty(Restaurant restaurant){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                facultyDao.delete(restaurant);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    public void updateFaculty(int id, String newName){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Restaurant restaurant = facultyDao.getOneById(id);
                restaurant.name = newName;
                facultyDao.update(restaurant);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    public  void updateFaculty(Restaurant restaurant){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                facultyDao.update(restaurant);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }


    //FoodPodC

    public void addDirection(FoodPodC foodPodC){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                foodPodCDao.insertAll(foodPodC);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    public void addDirection(int id, String name, int facultyId){
        FoodPodC dir = new FoodPodC();
        dir.id = id;
        dir.name = name;
        dir.categoriesId = facultyId;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                foodPodCDao.insertAll(dir);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    public void addDirection(String name, int facultyId){
        FoodPodC dir = new FoodPodC();
        dir.name = name;
        dir.categoriesId = facultyId;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                List<FoodPodC> direcs = foodPodCDao.getAll();
                dir.id = direcs.size();
                foodPodCDao.insertAll(dir);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    public void deleteDirection(int id){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                FoodPodC dir = foodPodCDao.getOneById(id);
                foodPodCDao.delete(dir);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    public void updateDirection(int id, String newName){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                FoodPodC dir = foodPodCDao.getOneById(id);
                dir.name = newName;
                foodPodCDao.update(dir);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }


    //Food
    public void addFood(Food food){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                foodDao.insertAll(food);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    public void updateFood(Food food){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                foodDao.update(food);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    public void deleteFood(Food food){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                foodDao.delete(food);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
}

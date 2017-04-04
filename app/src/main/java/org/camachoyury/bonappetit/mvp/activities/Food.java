package org.camachoyury.bonappetit.mvp.activities;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by yury on 4/4/17.
 */

public class Food {
    private String urlImage;
    private String foodName;
    private String Restaurant;
    private FirebaseUser user;

    public Food() {
    }

    public Food(String urlImage, String foodName, String restaurant, FirebaseUser user) {
        this.urlImage = urlImage;
        this.foodName = foodName;
        Restaurant = restaurant;
        this.user = user;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getRestaurant() {
        return Restaurant;
    }

    public void setRestaurant(String restaurant) {
        Restaurant = restaurant;
    }

    public FirebaseUser getUser() {
        return user;
    }

    public void setUser(FirebaseUser user) {
        this.user = user;
    }
}

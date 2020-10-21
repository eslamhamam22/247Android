package amaz.objects.TwentyfourSeven.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telecom.Call;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.data.models.BlockedArea;
import amaz.objects.TwentyfourSeven.data.models.Category;
import amaz.objects.TwentyfourSeven.data.models.User;

public class LocalSettings {

    private SharedPreferences preferences;
    private String LOCALE = "locale";
    private static final String TOKEN = "token";
    private static final String REFRESH_TOKEN = "refresh_token";
    private static final String USER = "user";
    private static final String IS_HOW_TO_USE_SHOWN = "is_how_to_use_shown";
    private static final String NOTIFICATIONTOKEN = "notification_token";
    private static final String CATEGORIES = "categories";
    private static final String DEFAULT_CATEGORY = "default_category";
    private static final String IS_Has_UN_SEEN_NOT = "is_das_un_seen_not";
    private static final String BLOCKEDAREAS = "blocked_areas";
    private static final String MINMINUSAMOUNT = "min_minus_amount";
    private static final String APP_SHARE_LINK = "app_share_link";
    private static final String FIREBASE_TOKEN = "firebase_token";
    private static final String IS_ORDER_DETAILS_OPENED = "isOrderDetailsOpened";

    public LocalSettings(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }
    public void setRegisteredToken(String token) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(NOTIFICATIONTOKEN, token);
        editor.apply();
    }
    public String getRegisteredToken() {
        return preferences.getString(NOTIFICATIONTOKEN,"");

    }
    public void setLocale(String language) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LOCALE,language);
        editor.apply();
    }

    public String getLocale(){
        return preferences.getString(LOCALE,null);
    }

    public void setToken(String token){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(TOKEN,token);
        editor.apply();
    }

    public String getToken() {
        return preferences.getString(TOKEN,null);
    }

    public void removeToken(){
        SharedPreferences.Editor editor= preferences.edit();
        editor.remove(TOKEN);
        editor.apply();
    }

    public void setFirebaseToken(String firebaseToken){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(FIREBASE_TOKEN, firebaseToken);
        editor.apply();
    }

    public String getFirebaseToken() {
        return preferences.getString(FIREBASE_TOKEN,null);
    }

    public void removeFirebaseToken(){
        SharedPreferences.Editor editor= preferences.edit();
        editor.remove(FIREBASE_TOKEN);
        editor.apply();
    }

    public void setRefreshToken(String refreshToken){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(REFRESH_TOKEN,refreshToken);
        editor.apply();
    }

    public String getRefreshToken() {
        return preferences.getString(REFRESH_TOKEN,null);
    }

    public void removeRefreshToken(){
        SharedPreferences.Editor editor= preferences.edit();
        editor.remove(REFRESH_TOKEN);
        editor.apply();
    }

    public void setUser(User user){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER,new Gson().toJson(user));
        editor.apply();
    }

    public User getUser() {
        return new Gson().fromJson(preferences.getString(USER,null),User.class);
    }

    public void removeUser(){
        SharedPreferences.Editor editor= preferences.edit();
        editor.remove(USER);
        editor.apply();
    }

    public void setIsHowToUseShown(boolean isHowToUseShown){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(IS_HOW_TO_USE_SHOWN,isHowToUseShown);
        editor.apply();
    }

    public boolean getIsHowToUseShown() {
        return preferences.getBoolean(IS_HOW_TO_USE_SHOWN,false);
    }

    public void removeIsHowToUseShown(){
        SharedPreferences.Editor editor= preferences.edit();
        editor.remove(IS_HOW_TO_USE_SHOWN);
        editor.apply();
    }

    public void setCategories(ArrayList<Category> categories){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(CATEGORIES,new Gson().toJson(categories));
        editor.apply();
    }

    public ArrayList<Category> getCategories() {
        return new Gson().fromJson(preferences.getString(CATEGORIES,null),new TypeToken<ArrayList<Category>>(){}.getType());
    }

    public void setDefaultCategory(Category category){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DEFAULT_CATEGORY,new Gson().toJson(category));
        editor.apply();
    }

    public Category getDefaultCategory() {
        return new Gson().fromJson(preferences.getString(DEFAULT_CATEGORY,null),Category.class);
    }
    public void setIsHAsNotificationUnSeen(boolean isHowToUseShown){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(IS_Has_UN_SEEN_NOT,isHowToUseShown);
        editor.apply();
    }

    public boolean getIsHAsNotificationUnSeen() {
        return preferences.getBoolean(IS_Has_UN_SEEN_NOT,false);
    }

    public void setBlockedAreas(ArrayList<BlockedArea> blockedAreas) {
        SharedPreferences.Editor editor = preferences.edit();
        String json = new Gson().toJson(blockedAreas);
        editor.putString(BLOCKEDAREAS,json);
        editor.commit();
    }

    public BlockedArea[]  getBlockedAreas() {

        return new Gson().fromJson(preferences.getString(BLOCKEDAREAS,""),BlockedArea[].class);
    }

    public float getMaxMinusAmount() {
        return preferences.getFloat(MINMINUSAMOUNT,0);
    }

    public void setMaxMinusAmount(Float min_amount) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(MINMINUSAMOUNT,min_amount);
        editor.apply();
    }

    public String getAppShareLink() {

        return preferences.getString(APP_SHARE_LINK,null);
    }

    public void setAppShareLink(String appShareLink) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(APP_SHARE_LINK,appShareLink);
        editor.apply();
    }

    public boolean isOrderDetailsOpened() {
        return preferences.getBoolean(IS_ORDER_DETAILS_OPENED,false);
    }

    public void setIsOrderDetailsOpened(boolean isOrderDetailsOpened) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(IS_ORDER_DETAILS_OPENED, isOrderDetailsOpened);
        editor.apply();
    }


}

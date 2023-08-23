package com.great_systems.sysdb.entity;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import java.util.UUID;

/**
 * Класс хранения флагов загрузки
 */

public class LauncherEntity {

    // если требуется регистрация, то переходим на форагмент ввода фирмы
    // boolean needRegistration;

    // если треубется ввести логин, то идет переход на строницу логирования
    // при потере сессии, если флаг needLogin == false идет автоматичекое перелогинивание с
    // получением новой сесии
    // boolean needLogin;

    // текущая сессия
    // String session;

    private SharedPreferences sp;
    public LauncherEntity(Context context) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean isNeedRegistration() {
        return sp.getBoolean(IPref.NEED_REGISTRATION_PREF_KEY, true);
    }

    public void setNeedRegistration(boolean needRegistration) {
        sp.edit().putBoolean(IPref.NEED_REGISTRATION_PREF_KEY, needRegistration).apply();
    }

    public boolean isNeedLogin() {
       return sp.getBoolean(IPref.NEED_LOGIN_PREF_KEY, true);
    }

    public void setNeedLogin(boolean needLogin) {
        sp.edit().putBoolean(IPref.NEED_LOGIN_PREF_KEY, needLogin).apply();
    }

    public boolean isAutoLogin() {
       return sp.getBoolean(IPref.NEED_AUTOLOGIN_PREF_KEY, false);
    }

    public void setAutoLogin(boolean autoLogin) {
        sp.edit().putBoolean(IPref.NEED_AUTOLOGIN_PREF_KEY, autoLogin).apply();
    }

    public UUID getSession() {
       UUID val = new UUID(0L, 0L);
       try {
           val = UUID.fromString(sp.getString(IPref.NEED_SESSION_PREF_KEY, ""));
       } catch (Exception ignore) {
       }

       return val;
    }

    public void setSession(String session) {
       sp.edit().putString(IPref.NEED_SESSION_PREF_KEY, session).apply();
    }

    public void setSession(UUID session) {
       sp.edit().putString(IPref.NEED_SESSION_PREF_KEY, session.toString()).apply();
    }

    public void setLogin(String login) {
        sp.edit().putString(IPref.LOGIN_SESSION_PREF_KEY, login).apply();
    }

    public String getLogin() {
        return sp.getString(IPref.LOGIN_SESSION_PREF_KEY, "");
    }

    public void setPassword(String password) {
        sp.edit().putString(IPref.PASSWORD_SESSION_PREF_KEY, password).apply();
    }

    public String getPassword() {
        return sp.getString(IPref.PASSWORD_SESSION_PREF_KEY, "");
    }


    public void setDataServerURL(String server) {
        sp.edit().putString(IPref.DATA_SERVER_URL_PREF_KEY, server).apply();
    }

    public String getDataServerURL() {
        return sp.getString(IPref.DATA_SERVER_URL_PREF_KEY, "");
    }


    public void setCurrentUser(UUID uuid) {
        sp.edit().putString(IPref.DATA_USER_PREF_KEY, uuid.toString()).apply();
    }

    public UUID getCurrentUser() {
        UUID val = new UUID(0L, 0L);
        try {
            val = UUID.fromString(sp.getString(IPref.DATA_USER_PREF_KEY, ""));
        } catch (Exception ignore) {
        }

        return val;
    }

    public void setCurrentCompany(UUID uuid) {
        sp.edit().putString(IPref.DATA_COMPANY_PREF_KEY, uuid.toString()).apply();
    }

    public UUID getCurrentCompany() {
        UUID val = new UUID(0L, 0L);
        try {
            val = UUID.fromString(sp.getString(IPref.DATA_COMPANY_PREF_KEY, ""));
        } catch (Exception ignore) {
        }

        return val;
    }

}

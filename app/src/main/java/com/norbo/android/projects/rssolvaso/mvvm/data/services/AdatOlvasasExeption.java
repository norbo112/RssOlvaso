package com.norbo.android.projects.rssolvaso.mvvm.data.services;

/**
 * Ha XMLel kapcsolatos hibák jönnek
 */
public class AdatOlvasasExeption extends Exception {

    public AdatOlvasasExeption(String message) {
        super(message);
    }

    public AdatOlvasasExeption(String message, Throwable cause) {
        super(message, cause);
    }
}

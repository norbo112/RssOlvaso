package com.norbo.android.projects.rssolvaso.mvvm.data.services;

/**
 * Ha XMLel kapcsolatos hibák jönnek
 */
public class XMLExeption extends Exception {

    public XMLExeption(String message) {
        super(message);
    }

    public XMLExeption(String message, Throwable cause) {
        super(message, cause);
    }
}

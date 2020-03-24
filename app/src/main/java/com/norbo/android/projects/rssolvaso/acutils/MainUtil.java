package com.norbo.android.projects.rssolvaso.acutils;

import android.view.Menu;

import com.norbo.android.projects.rssolvaso.R;

public class MainUtil {
    /**
     * Főoldalon lévő menüpontok nem aktuálisak a mellékoldalakra,
     * ezért eltávolítom őket
     * @param menu
     */
    public static void removeunnecessaryMenuItems(Menu menu) {
        menu.removeItem(R.id.menu_export);
        menu.removeItem(R.id.menu_import);
        menu.removeItem(R.id.menu_ujhir);
    }
}

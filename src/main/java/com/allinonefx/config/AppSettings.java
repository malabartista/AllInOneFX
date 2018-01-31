/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.allinonefx.config;

import com.allinonefx.controllers.I18NController;
import java.util.Locale;
import javafx.geometry.NodeOrientation;

public final class AppSettings {

    private static final class Themes {

        public static final String theme_BLUE = "blue";
        public static final String theme_RED = "red";
    }

    private static final class Locales {

        //public static final Locale es_ES_LOCALE = new Locale.Builder().setLanguageTag("es-ES").build(); // nu is for numbers
        public static final Locale ES_LOCALE = new Locale("es", "ES");
        public static final Locale EN_LOCALE = new Locale("en", "EN");
    }

    public static enum Language implements I18NController.I18NLanguage {
        SPANISH(Locales.ES_LOCALE, NodeOrientation.LEFT_TO_RIGHT),
        ENGLISH(Locales.EN_LOCALE, NodeOrientation.LEFT_TO_RIGHT);

        private Locale locale;
        private NodeOrientation nodeOrientation;

        Language(Locale locale, NodeOrientation nodeOrientation) {
            this.locale = locale;
            this.nodeOrientation = nodeOrientation;
        }

        public Locale getLocale() {
            return locale;
        }

        public NodeOrientation getNodeOrientation() {
            return nodeOrientation;
        }
    }
}

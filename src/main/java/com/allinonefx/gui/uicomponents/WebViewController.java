/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.allinonefx.gui.uicomponents;

import com.allinonefx.controllers.MainController;
import io.datafx.controller.ViewController;
import java.io.IOException;
import java.net.URL;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.annotation.PostConstruct;
import org.w3c.dom.Document;

@ViewController(value = "/fxml/ui/WebView.fxml", title = "Javascript Example")
public class WebViewController {

    @FXML
    private StackPane root;

    /**
     * init fxml when loaded.
     */
    @PostConstruct
    public void init() throws IOException, Exception {
        MainController.lblTitle.setText("WebView");

        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        WebView webView = new WebView();
        webView.setContextMenuEnabled(true);

        // local error console
        com.sun.javafx.webkit.WebConsoleListener.setDefaultListener(
                (webview, message, lineNumber, sourceId) -> System.out
                        .println("Console: [" + sourceId + ":" + lineNumber + "] " + message));

        final WebEngine engine = webView.getEngine();

        engine.setOnError(event -> System.out.println(event.getMessage()));
        engine.setOnAlert(event -> System.out.println(event.getData()));

        //load url
        URL url = this.getClass().getResource("/index.html");
        engine.load(url.toString());
//        engine.load("https://html5test.com/index.html");
//        engine.load("https://stackoverflow.com/questions/14029964/execute-a-javascript-function-for-a-webview-from-a-javafx-program");
//        engine.load("https://www.tradingview.com/chart/bKsZf5LY/");
        engine.documentProperty().addListener(new ChangeListener<Document>() {
            @Override
            public void changed(ObservableValue<? extends Document> prop,
                    Document oldDoc, Document newDoc) {
                enableFirebug(engine);
            }
        });

//        engine.getLoadWorker().stateProperty().addListener(
//                new ChangeListener<State>() {
//            @Override
//            public void changed(ObservableValue ov, State oldState, State newState) {
//                if (newState == State.SUCCEEDED) {
//                    engine.executeScript(
//                            "function highlightWord(root,word){"
//                            + "  textNodesUnder(root).forEach(highlightWords);"
//                            + ""
//                            + "  function textNodesUnder(root){"
//                            + "    var n,a=[],w=document.createTreeWalker(root,NodeFilter.SHOW_TEXT,null,false);"
//                            + "    while(n=w.nextNode()) a.push(n);"
//                            + "    return a;"
//                            + "  }"
//                            + ""
//                            + "  function highlightWords(n){"
//                            + "    for (var i; (i=n.nodeValue.indexOf(word,i)) > -1; n=after){"
//                            + "      var after = n.splitText(i+word.length);"
//                            + "      var highlighted = n.splitText(i);"
//                            + "      var span = document.createElement('span');"
//                            + "      span.style.backgroundColor='#f00';"
//                            + "      span.appendChild(highlighted);"
//                            + "      after.parentNode.insertBefore(span,after);"
//                            + "    }"
//                            + "  }"
//                            + "}"
//                            + "\n"
//                            + "highlightWord(document.body,'function');");
//                }
//            }
//        });
        root.getChildren().add(webView);
    }

    /**
     * Enables Firebug Lite for debugging a engine.
     *
     * @param engine the engine for which debugging is to be enabled.
     */
    private static void enableFirebug(final WebEngine engine) {
        engine.executeScript("if (!document.getElementById('FirebugLite')){E = document['createElement' + 'NS'] && document.documentElement.namespaceURI;E = E ? document['createElement' + 'NS'](E, 'script') : document['createElement']('script');E['setAttribute']('id', 'FirebugLite');E['setAttribute']('src', 'https://getfirebug.com/' + 'firebug-lite.js' + '#startOpened');E['setAttribute']('FirebugLite', '4');(document['getElementsByTagName']('head')[0] || document['getElementsByTagName']('body')[0]).appendChild(E);E = new Image;E['setAttribute']('src', 'https://getfirebug.com/' + '#startOpened');}");
    }
}

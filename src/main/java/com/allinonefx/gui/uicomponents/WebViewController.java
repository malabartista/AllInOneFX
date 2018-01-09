/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.allinonefx.gui.uicomponents;

import io.datafx.controller.ViewController;
import java.io.IOException;
import java.net.URL;
import javafx.beans.value.*;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.annotation.PostConstruct;

@ViewController(value = "/fxml/ui/WebView.fxml", title = "Javascript Example")
public class WebViewController {

    @FXML
    private StackPane root;

    /**
     * init fxml when loaded.
     */
    @PostConstruct
    public void init() throws IOException, Exception {
        WebView webView = new WebView();
        final WebEngine engine = webView.getEngine();
//        engine.load("https://stackoverflow.com/questions/14029964/execute-a-javascript-function-for-a-webview-from-a-javafx-program");
        URL url = this.getClass().getResource("/index.html");
        

        engine.getLoadWorker().stateProperty().addListener(
                new ChangeListener<State>() {
            @Override
            public void changed(ObservableValue ov, State oldState, State newState) {
                if (newState == State.SUCCEEDED) {
                    engine.executeScript(
                            "function highlightWord(root,word){"
                            + "  textNodesUnder(root).forEach(highlightWords);"
                            + ""
                            + "  function textNodesUnder(root){"
                            + "    var n,a=[],w=document.createTreeWalker(root,NodeFilter.SHOW_TEXT,null,false);"
                            + "    while(n=w.nextNode()) a.push(n);"
                            + "    return a;"
                            + "  }"
                            + ""
                            + "  function highlightWords(n){"
                            + "    for (var i; (i=n.nodeValue.indexOf(word,i)) > -1; n=after){"
                            + "      var after = n.splitText(i+word.length);"
                            + "      var highlighted = n.splitText(i);"
                            + "      var span = document.createElement('span');"
                            + "      span.style.backgroundColor='#f00';"
                            + "      span.appendChild(highlighted);"
                            + "      after.parentNode.insertBefore(span,after);"
                            + "    }"
                            + "  }"
                            + "}"
                            + "\n"
                            + "highlightWord(document.body,'webEngine');");
                }
            }
        });
        engine.load(url.toString());
        root.getChildren().add(webView);
    }
}

/**
 * Copyright (c) 2016 Jens Deters http://www.jensd.de
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 *
 */
package com.allinonefx.gui.uicomponents;

import com.allinonefx.controllers.MainController;
import com.allinonefx.glyphs.browser.GlyphsBrowser;
import com.allinonefx.glyphs.browser.GlyphsBrowserAppModel;
import io.datafx.controller.ViewController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javax.annotation.PostConstruct;

@ViewController(value = "/fxml/ui/GlyphsBrowser.fxml", title = "GlyphsBrowser")
public class GlyphsBrowserController {
    @FXML
    private StackPane root;
    
    public final static String TTF_PATH = "/ttf/Panton.otf";

    static {
        try {
            Font.loadFont(GlyphsBrowserController.class.getResource(TTF_PATH).openStream(), 10.0d);
        } catch (IOException ex) {
            Logger.getLogger(GlyphsBrowserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   /**
     * init fxml when loaded.
     */
    @PostConstruct
    public void init() throws IOException {
        MainController.lblTitle.setText("Glyphs Browser");
        GlyphsBrowserAppModel model = new GlyphsBrowserAppModel();
        GlyphsBrowser iconsBrowser = new GlyphsBrowser(model);
        root.getChildren().addAll(iconsBrowser);
    }

}

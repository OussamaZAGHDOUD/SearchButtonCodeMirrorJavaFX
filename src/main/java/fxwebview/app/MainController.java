package fxwebview.app;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;

public class MainController implements Initializable {

    @FXML
    private WebView webView;
    private WebEngine webEngine;
    private String code = 
            "Bonjour Java\\r" +
            "    hello java\\r" +
            "    hello world,\\r" +
            "   hello JavaFX -51%\\r" +
            "let let let let let";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        webEngine = webView.getEngine();
        webEngine.setOnAlert(this::alertWorker);
        webEngine.load(this.getClass().getResource("/codemirror/public_html/index.html").toExternalForm());
        webEngine.getLoadWorker().stateProperty().addListener(this::stateChangeListener);
    }

    /**
     * Executes JavaScript code that sets the code in the editor after the WebView index html is loaded
     *
     * @param observable
     * @param oldValue
     * @param newValue
     */
    private void stateChangeListener(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
        if (newValue == Worker.State.SUCCEEDED) {
              setEditorCode(code);
              enableFirebug(false);
        }
    }

    /**
     * Sets the code in the editor.
     * @param newCode
     */
    public void setEditorCode(String newCode) {
        webEngine.executeScript("editor.setValue('" + newCode + "' );");
    }

     /**
     * Gets the code in the editor.
     * @return 
     */
    public String getEditorCode() {
        this.code = (String) webEngine.executeScript("editor.getValue();");
        return code;
    }

    /**
     * Prints webView alerts to the console
     */
    private void alertWorker(WebEvent<String> alert) {
        System.out.println("Alert Event - Message: " + alert.getData());
    }
    
    /**
     * Quick one-liner that delimits using the end of file character and returns
     * the whole input stream as a String. Use for small files.
     *
     * @param inputStream byte input stream.
     * @return String a file or JSON text
     */
    private static String streamToString(InputStream inputStream) {
        try (Scanner scanner = new Scanner(inputStream, "UTF-8")) {
            return scanner.useDelimiter("\\Z").next();
        }
    }
    
   /**
     * Enables Firebug for debugging a webEngine.
     * @param engine the webEngine for which debugging is to be enabled.
     */
    private void enableFirebug(boolean enabled) {
        if (enabled) {
            InputStream file = this.getClass().getResourceAsStream("/firebug/firebug-script.js");
            final String script = streamToString(file);
            webEngine.executeScript(script);

        }
    }
}
    

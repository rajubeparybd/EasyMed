package com.easymed.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;

/**
 * FXMLScene class is used to load and switch between different FXML scenes and transfer data between them.
 *
 * @author Raju Bepary
 * @since 1.0.0
 */
public class FXMLScene {
    private Parent root = null;
    private Object controller = null;

    /**
     * load method is used to load an FXML file.
     *
     * @param fxmlpath FXML file path
     *
     * @return FXMLScene object
     */
    public static FXMLScene load(String fxmlpath) {
        FXMLScene fxmlScene = new FXMLScene();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(fxmlScene.getClass().getResource(fxmlpath));
            fxmlScene.root = fxmlLoader.load();
            fxmlScene.controller = fxmlLoader.getController();
        } catch (Exception e) {
            System.out.println("Error loading FXML file: " + e.getMessage());
        }

        return fxmlScene;
    }

    /**
     * switchScene method is used to switch between different FXML scenes.
     *
     * @param fxmlPath FXML file path
     * @param node     Node object
     */
    public static void switchScene(String fxmlPath, Node node) {
        FXMLScene fxmlScene = FXMLScene.load(fxmlPath);
        Scene scene = new Scene(fxmlScene.getRoot());
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }

    /**
     * switchScene method is used to switch between different FXML scenes.
     *
     * @param fxmlScene FXMLScene object
     * @param node      Node object
     */
    public static void switchScene(FXMLScene fxmlScene, Node node) {
        Scene scene = new Scene(fxmlScene.getRoot());
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * switchScene method is used to switch between different FXML scenes and transfer data between them.
     *
     * @param fxmlPath FXML file path
     * @param node     Node object
     * @param data     Data to be transferred
     */
    public static void switchScene(String fxmlPath, Node node, HashMap<String, String> data) {
        FXMLScene fxmlScene = FXMLScene.load(fxmlPath);
        try {
            fxmlScene.getController().getClass().getMethod("setData", HashMap.class).invoke(fxmlScene.getController(), data);
        } catch (Exception e) {
            System.out.println("Error setting data to controller: " + e.getMessage());
        }
        Scene scene = new Scene(fxmlScene.getRoot());
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * getRoot method is used to get the root node of the FXML scene.
     *
     * @return Parent root
     */
    public Parent getRoot() {
        return root;
    }

    /**
     * getController method is used to get the controller of the FXML scene.
     *
     * @return Object controller
     */

    public Object getController() {
        return controller;
    }
}

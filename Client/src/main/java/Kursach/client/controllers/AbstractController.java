package Kursach.client.controllers;

import javafx.scene.Scene;

public abstract class AbstractController {

    protected Scene scene;
    public final void setSceneReference(Scene scene) {
        this.scene = scene;
    }

}

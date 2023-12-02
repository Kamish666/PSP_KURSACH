package Kursach.client.controllers;

import Kursach.client.Polzovatel;
import javafx.scene.Scene;

public abstract class AbstractController {

    protected Scene scene;
    public final void setSceneReference(Scene scene) {
        this.scene = scene;
    }

    protected Polzovatel polzovatel;

    public AbstractController() {
        polzovatel = Polzovatel.getInstance();
    }

}

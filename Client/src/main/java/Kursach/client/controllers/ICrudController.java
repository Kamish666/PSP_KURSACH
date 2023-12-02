package Kursach.client.controllers;

public interface ICrudController {

    void onAdd();
    void onEdit();
    void onDelete();

    public void getAll();

    void enableButtons(boolean enable);

    void showFields(boolean visible);
}

package com.sgpublic.jxproject.core;

import javafx.stage.Stage;

public class JxController extends JxContext {
    final void setup(JxCompatActivity context){
        this.context = context;
        setStage(context.getStage());
    }

    public void init(){

    }

    @Override
    public final void start(Stage primaryStage) {}
}

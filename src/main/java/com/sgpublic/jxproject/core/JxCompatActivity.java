package com.sgpublic.jxproject.core;

import com.sgpublic.jxproject.JxLauncher;
import com.sgpublic.jxproject.core.ui.DropShadowMaker;
import com.sgpublic.jxproject.exception.JxMessage;
import com.sgpublic.jxproject.exception.JxRuntimeException;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.lang.reflect.Field;

public class JxCompatActivity extends JxContext {
    private JxController controller;

    private Node shadow;
    private DropShadow onFocusShadow;
    private DropShadow outOfFocusShadow;

    @Override
    public final void start(Stage primaryStage) {
        super.start(primaryStage);
        primaryStage.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != newValue){
                if (newValue){
                    if (shadow != null && onFocusShadow != null){
                        shadow.setEffect(onFocusShadow);
                    }
                    onFocus();
                } else {
                    if (shadow != null && outOfFocusShadow != null){
                        shadow.setEffect(outOfFocusShadow);
                    }
                    onBlur();
                }
            }
        });
        primaryStage.maximizedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != newValue){
                if (newValue){
                    onMaximized();
                } else {
                    onReduction();
                }
            }
        });
        primaryStage.iconifiedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != newValue){
                if (newValue){
                    onMinimized();
                }
            }
        });
        if (JxManifest.getActivityIcon(this) != null){
            primaryStage.getIcons().add(new Image(
                    JxManifest.getActivityIcon(this)
            ));
        }
        primaryStage.setResizable(JxManifest.isActivityResizable(this));
        String stageStyle = JxManifest.getStageStyleOfActivity(this);
        if (stageStyle.equals("BORDERLESS") || stageStyle.equals("TRANSPARENT")){
            primaryStage.initStyle(StageStyle.TRANSPARENT);
        } else {
            switch (stageStyle){
                case "UNDECORATED":
                    primaryStage.initStyle(StageStyle.UNDECORATED);
                    break;
                case "UTILITY":
                    primaryStage.initStyle(StageStyle.UTILITY);
                    break;
                case "UNIFIED":
                    primaryStage.initStyle(StageStyle.UNIFIED);
                    break;
            }
        }
        onCreate(primaryStage);
        getStage().show();
    }

    @Override
    public final void stop() throws Exception {
        onDestroy();
        JxIntent.removeIntent(this);
        super.stop();
    }

    protected void onCreate(Stage primaryStage) {

    }

    protected void onDestroy() {

    }

    protected void onMaximized() {

    }

    protected void onReduction(){

    }

    protected void onMinimized() {

    }

    protected void onBlur() {

    }

    protected void onFocus() {

    }

    protected final void setContentView(String layoutName) {
        setContentView(layoutName, 600.0, 400.0);
    }

    protected final void setContentView(String layoutName, double windowWidth, double windowHeight) {
        DropShadowMaker dropshadow = null;
        if (JxManifest.getStageStyleOfActivity(this).equals("BORDERLESS")){
            Color color = Color.rgb(0, 0, 0, 0.3);
            dropshadow = new DropShadowMaker(color);
        }
        setContentView(layoutName, windowWidth, windowHeight, 16, dropshadow);
    }

    protected final void setContentView(
            String layoutName, double windowWidth, double windowHeight,
            double radius, DropShadowMaker dropShadow){
        setContentView(layoutName, windowWidth, windowHeight, radius, dropShadow, dropShadow);
    }

    protected final void setContentView(
            String layoutName, double windowWidth, double windowHeight,
            double radius, DropShadowMaker onFocusShadow, DropShadowMaker outOfFocusShadow) {
        this.onFocusShadow = onFocusShadow.build(radius);
        this.outOfFocusShadow = outOfFocusShadow.build(radius);
        try {
            if (JxLauncher.isResourceExist("/layout/" + layoutName + ".fxml")){
                FXMLLoader loader = new FXMLLoader(JxLauncher
                        .getResource("/layout/" + layoutName + ".fxml"));

                Scene scene;
                if (JxManifest.getStageStyleOfActivity(this).equals("BORDERLESS")){
                    shadow = loader.load();
                    shadow.setEffect(this.onFocusShadow);
                    shadow.setStyle("-fx-background-color: #F2F2F2");

                    double padding = this.onFocusShadow.getRadius();
                    StackPane content = new StackPane();
                    content.setBackground(Background.EMPTY);
                    content.setAlignment(Pos.CENTER);
                    content.setPadding(new Insets(padding));
                    content.getChildren().add(shadow);
                    scene = new Scene(content, padding * 2 + windowWidth,
                            padding * 2 + windowHeight, Color.TRANSPARENT);
                } else {
                    scene = new Scene(loader.load(), windowWidth, windowHeight);
                }
                if (loader.getController() instanceof JxController){
                    controller = loader.getController();
                    controller.setup(this);
                    controller.init();
                }
                setContentView(() -> scene);
            } else {
                throw new JxRuntimeException(JxMessage.LAYOUT_NOT_FIND, layoutName);
            }
        } catch (IOException e) {
            throw new JxRuntimeException(JxMessage.LAYOUT_NOT_FIND, layoutName);
        }
    }

    protected final void setContentView(SceneBuilder builder){
        getStage().setTitle(JxManifest.getLabelOfActivity(this));
        Scene scene = builder.onSceneBuild();
        JxManifest.getStyleSheetOfActivity(this, stylesheet -> {
            String resName = "/stylesheet";
            String userResName = resName + "-" + JxLauncher.getColorProfile();
            if (JxLauncher.isResourceExist(userResName + "/" + stylesheet +".css")){
                resName = userResName;
            }
            scene.getStylesheets().add(
                    JxLauncher.getResource(resName + "/" + stylesheet + ".css").toExternalForm()
            );
        });
        getStage().setScene(scene);
    }

    protected final String getString(String resName){
        return JxRecourse.getString(resName);
    }

    protected final String getColor(String resName){
        return JxRecourse.getColor(resName);
    }

    @SuppressWarnings("unchecked")
    public <T extends JxController>T getController() {
        return (T) controller;
    }

    @SuppressWarnings("unchecked")
    public <T extends Node>T findViewById(String id){
        Class<? extends JxController> clazz = controller.getClass();
        try {
            Field field = clazz.getDeclaredField(id);
            if (Node.class.isAssignableFrom(field.getType())){
                return (T) field.get(controller);
            }
        } catch (NoSuchFieldException | IllegalAccessException ignore) {}
        return null;
    }

    public interface SceneBuilder {
        Scene onSceneBuild();
    }
}

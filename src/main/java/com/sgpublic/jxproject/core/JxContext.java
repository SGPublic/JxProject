package com.sgpublic.jxproject.core;

import com.sgpublic.jxproject.exception.JxMessage;
import com.sgpublic.jxproject.exception.JxRuntimeException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class JxContext extends Application {
    protected String TAG = getClass().getSimpleName();
    protected JxCompatActivity context;
    private Stage stage;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
    }

    public void startActivity(JxIntent intent) {
        Class<?> clazz = intent.getTargetActivity();
        if (JxManifest.findActivity(clazz)){
            JxIntent.putIntent(clazz, intent);
            try {
                Method method = clazz.getMethod("start", Stage.class);
                method.setAccessible(true);
                method.invoke(clazz.getDeclaredConstructor().newInstance(), new Stage());
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
                throw new JxRuntimeException(JxMessage.ACTIVITY_LAUNCH_FAILURE);
            }
        } else {
            throw new JxRuntimeException(JxMessage.ACTIVITY_EXPORT);
        }
    }

    public Stage getStage() {
        return stage;
    }

    public void maximized(){
        if (stage != null){
            stage.setMaximized(!stage.isMaximized());
        } else {
            throw new JxRuntimeException(JxMessage.ACTIVITY_MAXIMIZED_FAILURE);
        }
    }

    public void maximized(boolean b){
        if (stage != null){
            stage.setMaximized(b);
        } else {
            throw new JxRuntimeException(JxMessage.ACTIVITY_MAXIMIZED_FAILURE);
        }
    }

    public void iconified(){
        if (stage != null){
            stage.setIconified(!stage.isIconified());
        } else {
            throw new JxRuntimeException(JxMessage.ACTIVITY_ICONIFIED_FAILURE);
        }
    }

    public void iconified(boolean b){
        if (stage != null){
            stage.setIconified(b);
        } else {
            throw new JxRuntimeException(JxMessage.ACTIVITY_ICONIFIED_FAILURE);
        }
    }

    public final void finish(){
        if (stage != null){
            stage.close();
        } else {
            throw new JxRuntimeException(JxMessage.ACTIVITY_CLOSE_FAILURE);
        }
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }

    protected final JxIntent getIntent(){
        return JxIntent.getIntent(this);
    }

    protected final void registerReceiver(JxBroadcastReceiver receiver, String intentFilter){

    }

    public final void runOnUiThread(Runnable action){
        Platform.runLater(action);
    }

    public void addDragNode(List<Node> nodeList){
        nodeList.forEach(node -> new DragListener(stage).enableDrag(node));
    }

    public void addDragNode(Node root) {
        new DragListener(stage).enableDrag(root);
    }

    public void removeDragNode(List<Node> nodeList){
        nodeList.forEach(node -> new DragListener(stage).disableDrag(node));
    }

    public void removeDragNode(Node root) {
        new DragListener(stage).disableDrag(root);
    }

    static class DragListener implements EventHandler<MouseEvent> {
        private double xOffset = 0;
        private double yOffset = 0;
        private final Stage stage;

        public DragListener(Stage stage) {
            this.stage = stage;
        }

        @Override
        public void handle(MouseEvent event) {
            event.consume();
            if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            } else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                stage.setX(event.getScreenX() - xOffset);
                if(event.getScreenY() - yOffset < 0) {
                    stage.setY(0);
                }else {
                    stage.setY(event.getScreenY() - yOffset);
                }
            }
        }

        public void enableDrag(Node node) {
            node.setOnMousePressed(this);
            node.setOnMouseDragged(this);
        }

        public void disableDrag(Node node) {
            node.setOnMouseDragged(null);
            node.setOnMouseDragged(null);
        }
    }
}

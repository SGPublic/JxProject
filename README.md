# JavaFX Extra Project

## 中文 | [English](https://github.com/SGPublic/JxProject/tree/master/README_EN.md)

这是一个开源的JavaFX工程管理框架，能帮助你更清晰的管理你的JavaFX工程，并扩展支持了更多特性。

**_注意！_** `JavaFX Extra Project` 尚处于开发阶段，如果因为此项目而导致的一切问题本人概不负责。

## 前言

`JavaFX Extra Project` 设计初衷是为了模仿 Android 开发模式，使得 JavaFX 开发项目结构更为清晰，并支持了更多特性，让使用者能更轻松的开发 JavaFX 软件。

### TODO

下表列出了已完成和计划完成的所有功能，欢迎在 issue 里提出好点子，加入更多特性的支持。

|功能|是否完成|备注|
|---|---|---|
|Manifest 快速设置常见属性|是|更多特性待扩展|
|Intent 启动窗口和传参|是||
|自适应资源调用|是|更多特性待扩展|
|CSS 样式资源调用|是||
|SharedPreference 保存配置|是||
|Log 调试输出工具|是||
|**_未完待续_**|

## 使用

将 `JxProject` 添加到项目依赖：

Gradle
```groovy
implementation 'io.github.sgpublic:JxProject:0.2.5'
```

maven
```xml
<dependency>
  <groupId>io.github.sgpublic</groupId>
  <artifactId>JxProject</artifactId>
  <version>0.2.5</version>
</dependency>
```

### 创建启动器

创建一个 class 作为启动器，在其入口函数中添加代码 `new JxLauncher(xxx.class);` ，例如：

```java
class ProjectLauncher {
    public static void main(String[] args) {
        new JxLauncher(ProjectLauncher.class);
    }
}
```

添加 `CONFIGURATION` ，类型为 `Application` ，主类选择刚刚创建的启动器。

### 创建 Activity

创建一个 class ，让其继承自 `JxCompatActivity` 。例如：

```java
import com.sgpublic.jxproject.core.JxCompatActivity;

public class MainActivity extends JxCompatActivity {

}
```

### 创建并关联布局

在项目的 resources 目录中新建子目录并命名为 `layout` ，在其中新建后缀为 `.fxml` 的布局文件。

在创建好的 Activity 中实现方法 `onCreate()` ，并在其中调用方法 `setContentView(String layoutBame)` 即可加载布局文件。例如：

MainActivity.java
```java
import com.sgpublic.jxproject.core.JxCompatActivity;

public class MainActivity extends JxCompatActivity {
    @Override
    protected void onCreate(Stage primaryStage) {
        setContentView("layout_main");
    }
}
```
layout_main.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.control.*?>

<AnchorPane xmlns="http://javafx.com/javafx"
            xmlns:fx="http://javafx.com/fxml"
            fx:controller="com.sgpublic.bilicheers.contollers.MainController">
    
</AnchorPane>
```

### 在 Manifest 中注册

在项目的 resources 目录中新建文件并命名为 `JxManifest.xml` 。文件样本如下：
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<manifest
    package="com.sgpublic.bilicheers">
    <application
        label="@string/app_name"
        icon="@mipmap/ic_launcher"
        theme="@style/material">
        <activity
            name=".MainActivity"
            lable="@string/app_name">
            <intent-filter>
                <action name="JxProject.JxIntent.action.ACTION_MAIN" />
            </intent-filter>
        </activity>
        <activity name=".BangumiPlayer" />
        <activity name=".LivePlayer" />
    </application>
</manifest>
```

其中节点 `intent-filter` 中, `action` 节点中的 `name` 参数指定为 `JxProject.JxIntent.action.ACTION_MAIN` 即可将此 Activity 指定为默认 Activity。

### 启动你的 JavaFX 项目

此时点击 IntelliJ IDEA 上方工具栏的运行按钮，即可启动你的 JavaFX 项目。

## 更多

此项目由我一时兴起而发起，欢迎所有愿意一起完成的朋友共同努力，将 JxProject 变得更加完善。
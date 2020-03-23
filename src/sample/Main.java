package sample;

import javafx.application.Application;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("WikiWallpaper");

        Button b1 = new Button("Wallpaper changer 1");
        Button b2 = new Button("Wallpaper changer 2");
        Button b3 = new Button("URL Image Set");

        GridPane root = new GridPane();

        root.add(b1, 0,0);
        root.add(b2, 1,0);
        root.add(b3, 2,0);
        root.setPadding(new Insets(40));
        root.setVgap(10);
        root.setHgap(10);

        try(InputStream in = new URL("https://upload.wikimedia.org/wikipedia/commons/4/46/Vaxholmsleden_February_2013_02_%28crop%29.jpg").openStream()){
            Files.copy(in, Paths.get("downloadImage.jpg"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        b1.setOnAction(e -> {
            System.out.println("b1 has been pressed");

            String command = "powershell.exe .\\Set-Wallpaper.ps1 -Image \"C:\\Users\\Ethan\\IdeaProjects\\WikiWallpaper\\test1.jpg\"";
            Process powerShellProcess = null;
            try {
                powerShellProcess = Runtime.getRuntime().exec(command);
                powerShellProcess.getOutputStream().close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        b2.setOnAction(e -> {
            String command = "powershell.exe .\\Set-Wallpaper.ps1 -Image \"C:\\Users\\Ethan\\IdeaProjects\\WikiWallpaper\\test2.jpg\"";
            Process powerShellProcess = null;
            try {
                powerShellProcess = Runtime.getRuntime().exec(command);
                powerShellProcess.getOutputStream().close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        b3.setOnAction(e -> {
            String command = "powershell.exe .\\Set-Wallpaper.ps1 -Image \"C:\\Users\\Ethan\\IdeaProjects\\WikiWallpaper\\downloadImage.jpg\"";
            Process powerShellProcess = null;
            try {
                powerShellProcess = Runtime.getRuntime().exec(command);
                powerShellProcess.getOutputStream().close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        Scene home = new Scene(root);

        primaryStage.setScene(home);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

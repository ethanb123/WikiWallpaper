package sample;

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.MongoClient;

import javafx.application.Application;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import com.mongodb.BasicDBObject;
import javafx.stage.WindowEvent;
import org.bson.Document;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicReference;

import javafx.scene.control.Label;
import java.util.prefs.*;
import javafx.application.Platform;

import javax.imageio.ImageIO;


public class Main extends Application {

    // Variable setup
    Label currentDate = new Label();
    private Preferences prefs;
    String ID1 = "Vertical Photo Filter";
    String ID2 = "History";
    String ID3 = "Landscapes";
    String ID4 = "Architecture";
    String ID5 = "Wildlife & Nature";
    String ID6 = "Science";
    String ID7 = "People";
    String articleURL;
    String imageURL;
    String date = "No Wallpaper Selected";
    StringProperty dateTest;

    // MongoDB Connection settings (read-only)
    ConnectionString connString = new ConnectionString(
            "mongodb+srv://WikiWallpaperRead:AZNxFDdALL6BcH7xb3hKc6di3QAtnd@wikiwallpapercluster-c805q.mongodb.net/test"
    );
    MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(connString)
            .retryWrites(true)
            .build();
    MongoClient mongoClient = MongoClients.create(settings);

    // Selects the database and collection from MongoDB
    MongoDatabase db = mongoClient.getDatabase("WikiWallpaper");
    MongoCollection<Document> myCollection = db.getCollection("Main");


    @Override // Start method of JavaFX Application
    public void start(Stage primaryStage) throws Exception{

        // Set up user preferences for this class
        prefs = Preferences.userRoot().node(this.getClass().getName());

        // JavaFX Scene Setup
        primaryStage.setTitle("WikiWallpaper");

        Button newWallpaperButton = new Button("Random Wallpaper");
        Button article = new Button("Link to Article");

        CheckBox cb1 = new CheckBox("Landscape");
        CheckBox cb2 = new CheckBox("History");
        CheckBox cb3 = new CheckBox("Landscapes");
        CheckBox cb4 = new CheckBox("Architecture");
        CheckBox cb5 = new CheckBox("Wildlife & Nature");
        CheckBox cb6 = new CheckBox("Science");
        CheckBox cb7 = new CheckBox("People");

        Label checkboxLabel = new Label("User Preferences");
        Label intervalLabel = new Label("Change on Interval");

        // Makes the labels bold
        checkboxLabel.setStyle("-fx-font-weight: bold");
        intervalLabel.setStyle("-fx-font-weight: bold");

        // Options for the Combo Box
        String comboOptions[] = { "None", "15 seconds", "1 Minute", "15 Minutes", "1 Hour", "1 Day" };

        // Creates the combo box
        ComboBox combo_box = new ComboBox(FXCollections.observableArrayList(comboOptions));
        combo_box.setValue("None");

        // Default User Settings - Check boxs
        cb1.setSelected(true);
        cb2.setSelected(true);
        cb3.setSelected(true);
        cb4.setSelected(true);
        cb5.setSelected(true);
        cb6.setSelected(true);
        cb7.setSelected(true);

        // Default User Settings - Saved Preferences
        prefs.putBoolean(ID1, true);
        prefs.putBoolean(ID2, true);
        prefs.putBoolean(ID3, true);
        prefs.putBoolean(ID4, true);
        prefs.putBoolean(ID5, true);
        prefs.putBoolean(ID6, true);
        prefs.putBoolean(ID7, true);

        // JavaFX Scene Setup
        GridPane root = new GridPane();
        primaryStage.setScene(new Scene(root));

        // JavaFX Column constraints to stop the WikiWallpaper logo from effecting other UI elements
        ColumnConstraints col1Constraints = new ColumnConstraints();
        ColumnConstraints col2Constraints = new ColumnConstraints();
        col1Constraints.setPercentWidth(50);
        col2Constraints.setPercentWidth(50);
        root.getColumnConstraints().addAll(col1Constraints, col2Constraints);

        // JavaFX Window Size
        primaryStage.setWidth(370);
        primaryStage.setHeight(500);

        // Adding elements to the scene
        root.add(currentDate,0,1);
        root.add(newWallpaperButton, 0,2);

        root.add(checkboxLabel, 0,3);
        root.add(cb1, 0,4);
        root.add(cb2, 0,5);
        root.add(cb3, 0,6);
        root.add(cb4, 0,7);
        root.add(cb5, 0,8);
        root.add(cb6, 0,9);
        root.add(cb7, 0,10);

        root.add(article, 1,2);
        root.add(intervalLabel, 1,3);
        root.add(combo_box, 1,4);

        // Sets Grid Sizing and Padding
        root.setPadding(new Insets(30));
        root.setVgap(6);
        root.setHgap(10);

        // Sets min height of checkboxes to prevent other columns from affecting size
        int checkboxMinHeight = 20;
        cb1.setMinHeight(checkboxMinHeight);
        cb2.setMinHeight(checkboxMinHeight);
        cb3.setMinHeight(checkboxMinHeight);
        cb4.setMinHeight(checkboxMinHeight);
        cb5.setMinHeight(checkboxMinHeight);
        cb6.setMinHeight(checkboxMinHeight);
        cb7.setMinHeight(checkboxMinHeight);

        // Adds Icon
        primaryStage.getIcons().add(new Image("https://i.imgur.com/Mmi62YC.png"));
        ImageView bigIcon = new ImageView("https://i.imgur.com/9AP5mo8.png");
        bigIcon.setFitHeight(75);
        bigIcon.setFitWidth(250);
        root.add(bigIcon, 0, 0);


        //File powershellScriptFile = new File("Set-WallPaper.ps1");
        //String pathOfScript = powershellScriptFile.getAbsolutePath();
        //System.out.println("!!SCRIPT!! "+pathOfScript);




        // 'New Wallpaper' Button actions
        newWallpaperButton.setOnAction(e -> {
            System.out.println("b1 has been pressed");
            newWallpaper();
            Platform.runLater(() -> currentDate.setText("POTD: "+date));
        });

        // 'Link to Article' Button actions
        article.setOnAction(e -> {
            try {
                Desktop.getDesktop().browse(new URL(articleURL).toURI());
            } catch (IOException es) {
                es.printStackTrace();
            } catch (URISyntaxException es) {
                es.printStackTrace();
            }
        });

        // Check Box Actions
        cb1.setOnAction(e ->{
            if(cb1.isSelected()) { prefs.putBoolean(ID1, true);
            }else{ prefs.putBoolean(ID1, false);
            }});
        cb2.setOnAction(e ->{
            if(cb2.isSelected()) { prefs.putBoolean(ID2, true);
            }else{ prefs.putBoolean(ID2, false);
            }});
        cb3.setOnAction(e ->{
            if(cb3.isSelected()) { prefs.putBoolean(ID3, true);
            }else{ prefs.putBoolean(ID3, false);
            }});
        cb4.setOnAction(e ->{
            if(cb4.isSelected()) { prefs.putBoolean(ID4, true);
            }else{ prefs.putBoolean(ID4, false);
            }});
        cb5.setOnAction(e ->{
            if(cb5.isSelected()) { prefs.putBoolean(ID5, true);
            }else{ prefs.putBoolean(ID5, false);
            }});
        cb6.setOnAction(e ->{
            if(cb6.isSelected()) { prefs.putBoolean(ID6, true);
            }else{ prefs.putBoolean(ID6, false);
            }});
        cb7.setOnAction(e ->{
            if(cb7.isSelected()) { prefs.putBoolean(ID7, true);
            }else{ prefs.putBoolean(ID7, false);
            }});

        // Timer for Interval wallpaper changes
        AtomicReference<Timer> timer = new AtomicReference<>(new Timer());

        // Interval Changer Combo Box Action
        combo_box.setOnAction(e -> {

            // Repeat Wallpaper Change in the timer loop, update the label each time
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    newWallpaper();
                    Platform.runLater(() -> currentDate.setText("POTD: "+date));
                }
            };

            switch(combo_box.getValue().toString()) {
                case "None":
                    System.out.println("selected None");
                    timer.get().cancel();
                    break;
                case "15 seconds":
                    System.out.println("selected 15 seconds");
                    timer.get().cancel();
                    timer.set(new Timer());
                    timer.get().schedule(task, 0, 15000);
                    break;
                case "1 Minute":
                    System.out.println("selected 1 Minute");
                    timer.get().cancel();
                    timer.set(new Timer());
                    timer.get().schedule(task, 0, 60000);
                    break;
                case "15 Minutes":
                    System.out.println("selected 15 Minutes");
                    timer.get().cancel();
                    timer.set(new Timer());
                    timer.get().schedule(task, 0, 900000);
                    break;
                case "1 Hour":
                    System.out.println("selected 1 Hour");
                    timer.get().cancel();
                    timer.set(new Timer());
                    timer.get().schedule(task, 0, 3600000);
                    break;
                case "1 Day":
                    System.out.println("selected 1 Day");
                    timer.get().cancel();
                    timer.set(new Timer());
                    timer.get().schedule(task, 0, 86400000);
                    break;
            }

        });
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public void newWallpaper(){

        // Arraylists of User settings build the query to search the MongoDB collection
        BasicDBObject andQuery = new BasicDBObject();
        List<BasicDBObject> vert = new ArrayList<BasicDBObject>();
        List<BasicDBObject> tags = new ArrayList<BasicDBObject>();

        // Prints out all user preferences
        System.out.println("ID1: "+prefs.getBoolean(ID1, true));
        System.out.println("ID2: "+prefs.getBoolean(ID2, true));
        System.out.println("ID3: "+prefs.getBoolean(ID3, true));
        System.out.println("ID4: "+prefs.getBoolean(ID4, true));
        System.out.println("ID5: "+prefs.getBoolean(ID5, true));
        System.out.println("ID6: "+prefs.getBoolean(ID6, true));
        System.out.println("ID7: "+prefs.getBoolean(ID7, true));

        // Sets vertical database object for building the query
        if(prefs.getBoolean(ID1, true)){
            vert.add(new BasicDBObject("Vertical", "0"));
        }else{
            vert.add(new BasicDBObject("Vertical", "1"));
        }

        // Sets the image tags based off user preferences for building the query
        if(prefs.getBoolean(ID2, true)){
            tags.add(new BasicDBObject("History", "1"));
        }if(prefs.getBoolean(ID3, true)){
            tags.add(new BasicDBObject("Landscapes", "1"));
        }if(prefs.getBoolean(ID4, true)){
            tags.add(new BasicDBObject("Architecture", "1"));
        }if(prefs.getBoolean(ID5, true)){
            tags.add(new BasicDBObject("Wildlife/nature", "1"));
        }if(prefs.getBoolean(ID6, true)){
            tags.add(new BasicDBObject("Science", "1"));
        }if(prefs.getBoolean(ID7, true)){
            tags.add(new BasicDBObject("People", "1"));
        }

        // Logic for building the database query
        andQuery.put("$and", vert);
        andQuery.put("$or", tags);
        System.out.println(andQuery.toString());

        // Calculates a random index from the query results
        int randomIndex = (int) Math.round(myCollection.countDocuments(andQuery)*Math.random());
        int total = (int) myCollection.countDocuments(andQuery);
        System.out.println("Total: " + total + " RandomIndex: " + randomIndex);

        // Iterates through queried results to find the POTD at the random index
        MongoCursor<Document> currentPOTD = myCollection.find(andQuery).iterator();
        int e = 0;
        while (e<randomIndex-1) {
            e++;
            currentPOTD.next();
        }
        Document currentEntry = currentPOTD.next();

        // Pulls the Image and Article URL from the MongoDB Entry
        articleURL = currentEntry.get("Article").toString();
        imageURL = currentEntry.get("URL").toString();
        date = currentEntry.get("Date").toString();

        System.out.println("Date: " + date);
        System.out.println("URL: " + imageURL);
        System.out.println("Article: " + articleURL);

        // Downloads the Image from the URL stored in the current database entry
        try(InputStream in = new URL(imageURL).openStream()){
            Files.deleteIfExists(Paths.get("downloadImage.jpg"));
            Files.copy(in, Paths.get("downloadImage.jpg"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // calls the Wallpaper changer PowerShell script with the downloaded POTD image
        File downloadedImageFile = new File("downloadImage.jpg");
        //*File powershellScriptFile = new File("Set-WallPaper.ps1");
        String pathOfImageFile = downloadedImageFile.getAbsolutePath();
        //*String pathOfScript = powershellScriptFile.getAbsolutePath();
        //System.out.println("path of image: "+pathOfImageFile);
        //System.out.println("path of script: "+pathOfScript);

        URL is = this.getClass().getClassLoader().getResource("Set-WallPaper.ps1");
        //String scriptPath = is.getPath().;
        //System.out.println("!!ScriptPath!! "+scriptPath.substring(6));
        //String scriptPath = "C:\Users\ethan\Documents\GitHub\WikiWallpaper\out\artifacts\WikiWallpaper_jar\WikiWallpaper.jar!\Set-WallPaper.ps1";
        try(InputStream in = new URL(is.toString()).openStream()){
            Files.deleteIfExists(Paths.get("Set-Wallpaper.ps1"));
            Files.copy(in, Paths.get("Set-Wallpaper.ps1"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        /*if(Files.exists(Paths.get("/javafx-sdk-11.0.2"))){
            System.out.println("JAVAFX EXISTS");
        }else{
            System.out.println("does not exist");
        }*/

        //Path ScrPath = Paths.("Set-Wallpaper.ps1");
        File powershellScriptFile = new File("Set-WallPaper.ps1");
        System.out.println("PATH IS: "+powershellScriptFile.getAbsolutePath());

        String command = "powershell.exe "+powershellScriptFile.getAbsolutePath()+" -Image " + pathOfImageFile;
        System.out.println("PowerShell Command: "+command);
        Process powerShellProcess = null;
        try {
            System.out.println("Trying Powershell command, wish me luck");
            powerShellProcess = Runtime.getRuntime().exec(command);
            powerShellProcess.getOutputStream().close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

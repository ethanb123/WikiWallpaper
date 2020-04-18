package sample;


import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.MongoClient;


import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import com.mongodb.BasicDBObject;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.geoWithinCenter;

//import java.awt.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicReference;

import javafx.scene.control.Label;
import java.util.prefs.*;



public class Main extends Application {

    // declare my variable at the top of my Java class
    private Preferences prefs;
    String ID1 = "Vertical Photo Filter";
    String ID2 = "History";
    String ID3 = "Landscapes";
    String ID4 = "Architecture";
    String ID5 = "Wildlife & Nature";
    String ID6 = "Science";
    String ID7 = "People";


    //Button article = new Button("Empty Article URL");
    String articleURL;
    String imageURL;
    String date = "None Selected";

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





   // Timer timer = new Timer();
/*
    public void changeOnInterval(long interval) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            int j = 0;
            @Override
            public void run() {
                System.out.println("Running: " + j);
                j++;
                newWallpaper();
            }

        }, 0, interval);
        timer.purge();
    }*/

    @Override
    public void start(Stage primaryStage) throws Exception{

        prefs = Preferences.userRoot().node(this.getClass().getName());

        // JavaFX Scene Setup
        primaryStage.setTitle("WikiWallpaper");

        Button newWallpaperButton = new Button("Random Wallpaper");
        Button article = new Button("Link to Article");

        CheckBox cb1 = new CheckBox("Horizontal Photos Only");
        CheckBox cb2 = new CheckBox("History");
        CheckBox cb3 = new CheckBox("Landscapes");
        CheckBox cb4 = new CheckBox("Architecture");
        CheckBox cb5 = new CheckBox("Wildlife & Nature");
        CheckBox cb6 = new CheckBox("Science");
        CheckBox cb7 = new CheckBox("People");

        Label checkboxLabel = new Label("User Preferences");
        Label intervalLabel = new Label("Change on Interval");
        checkboxLabel.setStyle("-fx-font-weight: bold");
        intervalLabel.setStyle("-fx-font-weight: bold");

        cb1.setOnAction(e ->{
            if(cb1.isSelected()) {
                prefs.putBoolean(ID1, true);
                //System.out.println(prefs.getBoolean(ID1, true));
            }else{
                prefs.putBoolean(ID1, false);
            }
        });

        cb2.setOnAction(e ->{
            if(cb2.isSelected()) {
                prefs.putBoolean(ID2, true);
            }else{
                prefs.putBoolean(ID2, false);
            }
        });

        cb3.setOnAction(e ->{
            if(cb3.isSelected()) {
                prefs.putBoolean(ID3, true);
            }else{
                prefs.putBoolean(ID3, false);
            }
        });

        cb4.setOnAction(e ->{
            if(cb4.isSelected()) {
                prefs.putBoolean(ID4, true);
            }else{
                prefs.putBoolean(ID4, false);
            }
        });

        cb5.setOnAction(e ->{
            if(cb5.isSelected()) {
                prefs.putBoolean(ID5, true);
            }else{
                prefs.putBoolean(ID5, false);
            }
        });

        cb6.setOnAction(e ->{
            if(cb6.isSelected()) {
                prefs.putBoolean(ID6, true);
            }else{
                prefs.putBoolean(ID6, false);
            }
        });

        cb7.setOnAction(e ->{
            if(cb7.isSelected()) {
                prefs.putBoolean(ID7, true);
            }else{
                prefs.putBoolean(ID7, false);
            }
        });

        cb1.setSelected(true);
        cb2.setSelected(true);
        cb3.setSelected(true);
        cb4.setSelected(true);
        cb5.setSelected(true);
        cb6.setSelected(true);
        cb7.setSelected(true);

        prefs.putBoolean(ID1, true);
        prefs.putBoolean(ID2, true);
        prefs.putBoolean(ID3, true);
        prefs.putBoolean(ID4, true);
        prefs.putBoolean(ID5, true);
        prefs.putBoolean(ID6, true);
        prefs.putBoolean(ID7, true);


        // Weekdays
        String week_days[] =
                { "None", "15 seconds", "1 Minute", "15 Minutes",
                        "1 Hour", "1 Day" };

        // Create a combo box
        ComboBox combo_box =
                new ComboBox(FXCollections
                        .observableArrayList(week_days));

        combo_box.setValue("None");

        GridPane root = new GridPane();
        primaryStage.setScene(new Scene(root));

        root.add(checkboxLabel, 0,2);
        root.add(cb1, 0,3);
        root.add(cb2, 0,4);
        root.add(cb3, 0,5);
        root.add(cb4, 0,6);
        root.add(cb5, 0,7);
        root.add(cb6, 0,8);
        root.add(cb7, 0,9);

        int checkboxMinHeight = 20;
        cb1.setMinHeight(checkboxMinHeight);
        cb2.setMinHeight(checkboxMinHeight);
        cb3.setMinHeight(checkboxMinHeight);
        cb4.setMinHeight(checkboxMinHeight);
        cb5.setMinHeight(checkboxMinHeight);
        cb6.setMinHeight(checkboxMinHeight);
        cb7.setMinHeight(checkboxMinHeight);

        root.add(newWallpaperButton, 0,0);
        root.add(article, 1,0);
        root.add(intervalLabel, 1,2);
        root.add(combo_box, 1,3);

        root.setPadding(new Insets(40));
        root.setVgap(6);
        root.setHgap(10);

        root.setMinSize(10,100);


        primaryStage.getIcons().add(new Image("file:WikiWallpaperLogo.png"));

        FileInputStream imageStream = new FileInputStream("WikiWallpaperLogo.png");
        Image image = new Image(imageStream);
        ImageView image4 = new ImageView(image);
        image4.setFitHeight(100);
        image4.setFitWidth(100);
        root.add(image4, 1, 10);

        // 'New Wallpaper' Button actions
        newWallpaperButton.setOnAction(e -> {
            System.out.println("b1 has been pressed");
            newWallpaper();
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

        AtomicReference<Timer> timer = new AtomicReference<>(new Timer());


        combo_box.setOnAction(e -> {



            TimerTask test = new TimerTask() {
                int j = 0;

                @Override
                public void run() {
                    System.out.println("Running: " + j);
                    j++;
                    newWallpaper();
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
                    timer.get().schedule(test, 0, 15000);
                    break;
                case "1 Minute":
                    System.out.println("selected 1 Minute");
                    timer.get().cancel();
                    timer.set(new Timer());
                    timer.get().schedule(test, 0, 60000);
                    break;
                case "15 Minutes":
                    System.out.println("selected 15 Minutes");
                    timer.get().cancel();
                    timer.set(new Timer());
                    timer.get().schedule(test, 0, 900000);
                    break;
                case "1 Hour":
                    System.out.println("selected 1 Hour");
                    timer.get().cancel();
                    timer.set(new Timer());
                    timer.get().schedule(test, 0, 3600000);
                    break;
                case "1 Day":
                    System.out.println("selected 1 Day");
                    System.out.println("selected 1 Hour");
                    timer.get().cancel();
                    timer.set(new Timer());
                    timer.get().schedule(test, 0, 86400000);
                    break;
            }


        });

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public void newWallpaper(){

        // Builds the query to search the MongoDB collection
        BasicDBObject andQuery = new BasicDBObject();
        List<BasicDBObject> vert = new ArrayList<BasicDBObject>();
        List<BasicDBObject> tags = new ArrayList<BasicDBObject>();

        //Prints out all user preferences
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
        }

        if(prefs.getBoolean(ID3, true)){
            tags.add(new BasicDBObject("Landscapes", "1"));
        }

        if(prefs.getBoolean(ID4, true)){
            tags.add(new BasicDBObject("Architecture", "1"));
        }

        if(prefs.getBoolean(ID5, true)){
            tags.add(new BasicDBObject("Wildlife/nature", "1"));
        }

        if(prefs.getBoolean(ID6, true)){
            tags.add(new BasicDBObject("Science", "1"));
        }

        if(prefs.getBoolean(ID7, true)){
            tags.add(new BasicDBObject("People", "1"));
        }

        // Logic for building the query
        andQuery.put("$and", vert);
        andQuery.put("$or", tags);

        System.out.println(andQuery.toString());

        // Calculates a random index from the queried results
        int randomIndex = (int) Math.round(myCollection.countDocuments(andQuery)*Math.random());
        int total = (int) myCollection.countDocuments(andQuery);
        System.out.println("Total: " + total + " randomIndex: " + randomIndex);

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
        String path2 = Paths.get("downloadImage.jpg").toString();
        String command = "powershell.exe .\\Set-Wallpaper.ps1 -Image C:\\Users\\ethan\\Documents\\GitHub\\WikiWallpaper\\downloadImage.jpg";
        Process powerShellProcess = null;
        try {
            powerShellProcess = Runtime.getRuntime().exec(command);
            powerShellProcess.getOutputStream().close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}

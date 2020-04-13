package sample;


import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.MongoClient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import com.mongodb.BasicDBObject;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;


public class Main extends Application {

    //Button article = new Button("Empty Article URL");
    String articleURL;
    String imageURL;

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


    public void newWallpaper(){

        // Builds the query to search the MongoDB collection
        BasicDBObject andQuery = new BasicDBObject();
        List<BasicDBObject> vert = new ArrayList<BasicDBObject>();
        List<BasicDBObject> tags = new ArrayList<BasicDBObject>();
        vert.add(new BasicDBObject("Vertical", "0"));
        tags.add(new BasicDBObject("History", "1"));
        tags.add(new BasicDBObject("Landscapes", "1"));
        tags.add(new BasicDBObject("Architecture", "1"));
        tags.add(new BasicDBObject("Wildlife/nature", "1"));
        tags.add(new BasicDBObject("Science", "1"));
        tags.add(new BasicDBObject("People", "1"));

        andQuery.put("$and", vert);
        andQuery.put("$or", tags);

        // Calculates a random index from the queried results
        int randomIndex = (int) Math.round(myCollection.count(andQuery)*Math.random());
        int total = (int) myCollection.count(andQuery);
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
    @Override
    public void start(Stage primaryStage) throws Exception{

        // JavaFX Scene Setup
        primaryStage.setTitle("WikiWallpaper");

        Button newWallpaperButton = new Button("Random Wallpaper");
        Button article = new Button("Link to Article");

        GridPane root = new GridPane();
        primaryStage.setScene(new Scene(root, 300, 275));
        root.add(newWallpaperButton, 0,0);
        root.add(article, 0,1);
        root.setPadding(new Insets(40));
        root.setVgap(10);
        root.setHgap(10);

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

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

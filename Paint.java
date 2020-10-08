/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

/**
 *
 * @author Daniel
 */
public class Paint extends Application {

    
    
    private FileChooser fileChooser = new FileChooser();
    private File file;
    private final File helpFile = new File("C:\\Users\\Daniel\\Documents\\cs250\\paint.txt\\release_notes.txt");
    private ImageView overallImage;
    private boolean hasSaved;
    private ColorPicker cp = new ColorPicker();
    private boolean visibleCP = false;
    private Canvas canvasRef = new Canvas();
    private GraphicsContext graphicContext = canvasRef.getGraphicsContext2D();
    private Image image;
    private WritableImage wim;
    private Line line;
    private MouseEvent getMousePosition;
    private double rectX;
    private double rectY;
    private double startingPosX;
    private double startingPosY;
    private Stack<WritableImage> undoStack = new Stack<WritableImage>();
    private Stack<WritableImage> redoStack = new Stack<WritableImage>();
    private double[] triangleX = new double[3];
    private double[] triangleY = new double[3];
    private int increment;
    private int checkItem = 0;
    private boolean showTimer = false;
    private GraphicsContext selectGC;
    private double moveImageStartX;
    private double moveImageStartY;
    private double moveImageEndX;
    private double moveImageEndY;
    private MouseEvent eve;
    private MouseEvent ev;
    private GraphicsContext pasteGC;
    private int imageWidth;
    private int imageHeight;
    private Timer saveTimer = new Timer();
    private Label timer = new Label();
    private BorderPane newRoot;
    private int checkViewItem = 0;
    private boolean checkSecret = true;
    private Alert secretItem = new Alert(AlertType.INFORMATION);
    private int seconds = 10;
    private Timer logTimer = new Timer();
    private IOException exec;
    private int numberOfSides;
    private TabPane tab = new TabPane();
    private static final Integer STARTTIME = 10;
    private Timeline timeline;
    private Label timerLabel = new Label();
    private IntegerProperty timeSeconds = new SimpleIntegerProperty(STARTTIME);
    private String tool = "No tool";
    private String fileName = "unsaved";
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Paint");
        
        
        
        Tooltip itemOne = new Tooltip("This opens a image");
        Tooltip itemThree = new Tooltip("Save image as different file type");
        Tooltip itemFive = new Tooltip("How to use Paint");
        Tooltip itemFour = new Tooltip("Closes Paint");
        Tooltip itemTwo = new Tooltip("Saves the image");
        
        
        
        Menu menu = new Menu("File");
        Menu menu_two = new Menu("Draw");
        Menu menu_three = new Menu("View");
        Menu menu_four = new Menu("Secret");
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(menu, menu_two, menu_three);
        CustomMenuItem menuItem1 = new CustomMenuItem(new Label("Open Image"));
        Tooltip.install(menuItem1.getContent(), itemOne);
        CustomMenuItem menuItem2 = new CustomMenuItem(new Label("Save Image"));
        Tooltip.install(menuItem2.getContent(), itemTwo);
        CustomMenuItem menuItem3 = new CustomMenuItem(new Label("Save Image As"));
        Tooltip.install(menuItem3.getContent(), itemThree);
        CustomMenuItem menuItem4 = new CustomMenuItem(new Label("Close"));
        Tooltip.install(menuItem4.getContent(), itemFour);
        CustomMenuItem menuItem5 = new CustomMenuItem(new Label("Help"));
        Tooltip.install(menuItem5.getContent(), itemFive);
        MenuItem menuItem6 = new MenuItem("Zoom");
        MenuItem menuItem7 = new MenuItem("Undo");
        MenuItem menuItem8 = new MenuItem("Redo");
        MenuItem menuItem9 = new MenuItem("New Tab");
        menu.getItems().addAll(menuItem1, menuItem2, menuItem3, menuItem4, menuItem5, menuItem6, menuItem7, menuItem8, menuItem9);
        RadioMenuItem drawItem1 = new RadioMenuItem("Pencil");
        MenuItem drawItem2 = new MenuItem("Choose Color");
        MenuItem drawItem3 = new MenuItem("Set Line Width");
        RadioMenuItem drawItem4 = new RadioMenuItem("Line");
        RadioMenuItem drawItem5 = new RadioMenuItem("Get Color");
        RadioMenuItem drawItem6 = new RadioMenuItem("Square");
        RadioMenuItem drawItem7 = new RadioMenuItem("Rectangle");
        RadioMenuItem drawItem8 = new RadioMenuItem("Ellipse");
        RadioMenuItem drawItem9 = new RadioMenuItem("Circle");
        MenuItem drawItem10 = new MenuItem("Resize");
        RadioMenuItem drawItem11 = new RadioMenuItem("Text");
        RadioMenuItem drawItem12 = new RadioMenuItem("Eraser");
        RadioMenuItem drawItem13 = new RadioMenuItem("Triangle");
        RadioMenuItem drawItem14 = new RadioMenuItem("Polygon");
        RadioMenuItem drawItem15 = new RadioMenuItem("Move Image");
        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(drawItem1, drawItem4, drawItem6, drawItem7, drawItem8, drawItem9, drawItem11, drawItem14, drawItem13, drawItem12, drawItem5, drawItem15);
        menu_two.getItems().addAll(drawItem1, drawItem4, drawItem6, drawItem7, drawItem8, drawItem9, drawItem11, drawItem14, drawItem13, drawItem2, drawItem3, drawItem5, drawItem10, drawItem12, drawItem15);
        RadioMenuItem viewItem1 = new RadioMenuItem("Save Timer");
        MenuItem viewItem2 = new MenuItem("Secret Mode");
        ToggleGroup viewToggleGroup = new ToggleGroup();
        viewToggleGroup.getToggles().add(viewItem1);
        menu_three.getItems().addAll(viewItem1, viewItem2);
        MenuItem secretItem1 = new MenuItem("Ada Lovelace");
        MenuItem secretItem2 = new MenuItem("Grace Hopper");
        MenuItem secretItem3 = new MenuItem("James Gosling");
        MenuItem secretItem4 = new MenuItem("Frances Allen");
        MenuItem secretItem5 = new MenuItem("Donald Knuth");
        MenuItem secretItem6 = new MenuItem("Alan Turing");
        MenuItem secretItem7 = new MenuItem("George Boole");
        MenuItem secretItem8 = new MenuItem("John von Neumann");
        MenuItem secretItem9 = new MenuItem("Margaret Hamilton");
        MenuItem secretItem10 = new MenuItem("Barbara Liskov");
        MenuItem secretItem11 = new MenuItem("Seymour Cray");
        menu_four.getItems().addAll(secretItem1, secretItem2, secretItem3, secretItem4, secretItem5, secretItem6, secretItem7, secretItem8, secretItem9, secretItem10, secretItem11);
        
        VBox root = new VBox(menuBar);
        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToHeight(true);
        newRoot = new BorderPane(scrollPane);
        newRoot.setPadding(new Insets(10));
        newRoot.setTop(menuBar);
        
        
        Button btn = new Button("Choose Image");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER);
        hbBtn.getChildren().add(btn);
        root.getChildren().add(hbBtn);
        VBox tabBox = new VBox(tab);
        newRoot.setBottom(tabBox);
        
        //the starting choose image option
        btn.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent e){
                file = fileChooser.showOpenDialog(primaryStage);
                if(file != null)
                {
                    image = new Image(file.toURI().toString());
                    canvasRef.setHeight(image.getHeight());
                    canvasRef.setWidth(image.getWidth());
                    graphicContext.drawImage(image, 0, 0, image.getWidth(), image.getHeight());
                    root.getChildren().add(canvasRef);
                    btn.setVisible(false);
                    hasSaved = false;
                    callTimer();
                    fixTimer();
                    logging();
                }
            }
        });
        
        
        //open image menu option
        menuItem1.setOnAction(e -> {
            file = fileChooser.showOpenDialog(primaryStage);
            if(file != null)
            {
                root.getChildren().remove(canvasRef);
                image = new Image(file.toURI().toString());
                canvasRef.setHeight(image.getHeight());
                canvasRef.setWidth(image.getWidth());
                graphicContext.drawImage(image, 0, 0, image.getWidth(), image.getHeight());
                root.getChildren().add(canvasRef);
                hasSaved = false;
                seconds = 10;
            }
        });
        menuItem1.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        
        
        //save menu option
        menuItem2.setOnAction(e -> {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Save?");
            alert.setContentText("Are you sure you would like to save?");
            Optional<ButtonType> result = alert.showAndWait();
            if((result.isPresent()) && (result.get() == ButtonType.OK))
            {
            if(file != null)
            {
                wim = new WritableImage((int)image.getWidth(), (int)image.getHeight());
                canvasRef.snapshot(null, wim);
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
                } catch (IOException ex) {
                    Logger.getLogger(Paint.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            hasSaved = true;
            seconds = 10;
            }
        });
        menuItem2.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        
        
        //the save as menu option
        menuItem3.setOnAction(e -> {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Save?");
            alert.setContentText("Are you sure you would like to save? (Note if the image is saved as a different file type some data may be lost)");
            Optional<ButtonType> result = alert.showAndWait();
            if((result.isPresent()) && (result.get() == ButtonType.OK))
            {
            file = fileChooser.showSaveDialog(primaryStage);
            if(file != null)
            {
                wim = new WritableImage((int)image.getWidth(), (int)image.getHeight());
                canvasRef.snapshot(null, wim);
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
                } catch (IOException ex) {
                    Logger.getLogger(Paint.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            hasSaved = true;
            seconds = 10;
            }
        });
        
        
        
        //the close menu option
        //e -> == eventhandler stuff
        menuItem4.setOnAction(e -> {
            if(hasSaved == true){
            Platform.exit();
            System.exit(0);
            }
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Not Saved");
            String s = "The Image has not been saved. Would you like to save?";
            alert.setContentText(s);
            
            Optional<ButtonType> result = alert.showAndWait();
            
            if((result.isPresent()) && (result.get() == ButtonType.OK))
            {
                //saves the image
                if(file != null)
                {
                    wim = new WritableImage((int)image.getWidth(), (int)image.getHeight());
                    canvasRef.snapshot(null, wim);
                    try {
                        ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
                    } catch (IOException ex) {
                        Logger.getLogger(Paint.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                //exits program
                Platform.exit();
                System.exit(0);
            }
            //if cancel button is clicked
            else
            {
                //exits program
                Platform.exit();
                System.exit(0);
            }
        });
        menuItem4.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.ALT_DOWN));
        
        //The help popup
        menuItem5.setOnAction(e -> {
            Alert helpAlert = new Alert(AlertType.INFORMATION);
            
            helpAlert.setTitle("Help");
            String h = "Please open a new image and then choose a color and draw on image!";
            helpAlert.setHeaderText("Help With Paint");
            helpAlert.setContentText(h);
            Desktop desktop = Desktop.getDesktop();
            try {
                    desktop.open(helpFile);
            } catch (IOException ex) {
                    Logger.getLogger(Paint.class.getName()).log(Level.SEVERE, null, ex);
            }
            helpAlert.showAndWait();
        });
        menuItem5.setAccelerator(new KeyCodeCombination(KeyCode.H, KeyCombination.CONTROL_DOWN));
        
        //Zoom
        menuItem6.setOnAction(e -> {
            TextInputDialog zoom = new TextInputDialog("Zoom");
            zoom.setHeaderText("Please enter how much you would like to zoom in or out:");
            zoom.showAndWait();
            String w = zoom.getEditor().getText();
            int zoomFactor = Integer.parseInt(w);
            canvasRef.setScaleX(zoomFactor);
            canvasRef.setScaleY(zoomFactor);
        });
        menuItem6.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN));
        
        //Undo
        menuItem7.setOnAction(e -> {
            undo();
        });
        menuItem7.setAccelerator(new KeyCodeCombination(KeyCode.U, KeyCombination.CONTROL_DOWN));
        
        //Redo
        menuItem8.setOnAction(e -> {
           redo(); 
        });
        menuItem8.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN));
        
        menuItem9.setOnAction(e -> {
           addTab(); 
        });
        
        //The Color Picker
        drawItem2.setOnAction(e -> {
            if(visibleCP == false){
            newRoot.getChildren().add(cp);
            newRoot.setLeft(cp);
            visibleCP = true;
            }
            else
            {
                newRoot.getChildren().remove(cp);
                visibleCP = false;
            }
        });
        
        //Draw Pencil
        drawItem1.setOnAction(e -> {
            saveToStack();
            if(checkItem == 1)
            {
                drawItem1.setSelected(false);
            }
            whichDrawing("pencil");
            checkItem = 1;
            seconds = 10;
            tool = "Pencil";
        });
        
        //Line
        drawItem4.setOnAction(e -> {
            saveToStack();
            if(checkItem == 2)
            {
                drawItem4.setSelected(false);
            }
            whichDrawing("line");
            checkItem = 2;
            seconds = 10;
            tool = "Line";
        });
        
        //Set Line Width
        drawItem3.setOnAction(e -> {
            TextInputDialog td = new TextInputDialog("enter any number");
            td.setHeaderText("Please enter the width of the line");
            td.showAndWait();
            String w = td.getEditor().getText();
            int width = Integer.parseInt(w);
            graphicContext.setLineWidth(width);
        });
        
        //Color Grabber
        drawItem5.setOnAction(e -> {
            if(checkItem == 3)
            {
                drawItem5.setSelected(false);
            }
            canvasRef.addEventHandler(MouseEvent.MOUSE_CLICKED, 
                    new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event)
                        {
                            WritableImage wim = new WritableImage((int)canvasRef.getWidth(), (int)canvasRef.getHeight());
                            SnapshotParameters sp = new SnapshotParameters();
                            sp.setFill(Color.TRANSPARENT);
                            WritableImage snapshot = canvasRef.snapshot(sp, wim);
                            PixelReader pr = snapshot.getPixelReader();
                            cp.setValue(pr.getColor((int)event.getX(), (int)event.getY()));
                        }
                    });
            checkItem = 3;
            tool = "Color Grabber";
        });
        
        //Square
        drawItem6.setOnAction(e -> {
            saveToStack();
            if(checkItem == 4)
            {
                drawItem6.setSelected(false);
            }
            graphicContext.setStroke(cp.getValue());
            graphicContext.setFill(cp.getValue());
            drawSquare();
            whichDrawing("square");
            checkItem = 4;
            seconds = 10;
            tool = "Square";
        });
        
        //Rectangle
        drawItem7.setOnAction(e -> {
            saveToStack();
            if(checkItem == 5)
            {
                drawItem7.setSelected(false);
            }
            whichDrawing("rectangle");
            checkItem = 5;
            seconds = 10;
            tool = "Rectangle";
        });
        
        //Ellipse
        drawItem8.setOnAction(e -> {
            saveToStack();
            if(checkItem == 6)
            {
                drawItem8.setSelected(false);
            }
            whichDrawing("ellipse");
            checkItem = 6;
            seconds = 10;
            tool = "Ellipse";
        });
        
        //Circle
        drawItem9.setOnAction(e -> {
            saveToStack();
            if(checkItem == 7)
            {
                drawItem9.setSelected(false);
            }
            whichDrawing("circle");
            checkItem = 7;
            seconds = 10;
            tool = "Circle";
        });
        
        //Resize
        drawItem10.setOnAction(e -> {
            TextInputDialog width = new TextInputDialog("enter any number");
            width.setHeaderText("Please enter the width of the image");
            width.showAndWait();
            String w = width.getEditor().getText();
            TextInputDialog height = new TextInputDialog("enter any number");
            height.setHeaderText("Please enter the height of the image");
            height.showAndWait();
            String h = height.getEditor().getText();
            canvasRef.setHeight(Integer.parseInt(h));
            canvasRef.setWidth(Integer.parseInt(w));
            ImageView resizeImage = new ImageView();
            resizeImage.setImage(image);
            resizeImage.setFitWidth(Integer.parseInt(w));
            resizeImage.setPreserveRatio(true);
            resizeImage.setSmooth(true);
            resizeImage.setCache(true);
        });
        
        //Text
        drawItem11.setOnAction(e -> {
            saveToStack();
            if(checkItem == 8)
            {
                drawItem11.setSelected(false);
            }
            whichDrawing("text");
            checkItem = 8;
            seconds = 10;
            tool = "Text";
        });
        
        //Eraser
        drawItem12.setOnAction(e -> {
            saveToStack();
            if(checkItem == 9)
            {
                drawItem12.setSelected(false);
            }
            whichDrawing("eraser");
            checkItem = 9;
            seconds = 10;
            tool = "Eraser";
        });
        
        //triangle
        drawItem13.setOnAction(e -> {
            saveToStack();
            if(checkItem == 10)
            {
                drawItem13.setSelected(false);
            }
            whichDrawing("triangle");
            checkItem = 10;
            seconds = 10;
            tool = "Triangle";
        });
        
        //Polygon
        drawItem14.setOnAction(e -> {
           saveToStack();
           if(checkItem == 11)
           {
               drawItem14.setSelected(false);
           }
           TextInputDialog points = new TextInputDialog("enter number");
           points.setHeaderText("Please enter how many sides of the polygon");
           points.showAndWait();
           String p = points.getEditor().getText();
           numberOfSides = Integer.parseInt(p);
           whichDrawing("polygon");
           checkItem = 14;
           seconds = 10;
           tool = "Polygon";
        });
        
        //Copy And Paste
        drawItem15.setOnAction(e -> {
           if(checkItem == 12)
           {
               drawItem15.setSelected(false);
           }
           copyTool();
           seconds = 10;
        });
        
        viewItem1.setOnAction(e -> {
           if(checkViewItem == 1)
           {
               viewItem1.setSelected(false);
               hideTimer();
               checkViewItem = 0;
           }
           else
           {
               showTimer();
               newRoot.setLeft(timerLabel);
               checkViewItem = 1;
           }
        });
        
        viewItem2.setOnAction(e -> {
            if(checkSecret)
            {
                checkSecret = false;
                menuBar.getMenus().add(menu_four);
            }
            else
            {
                checkSecret = true;
                menuBar.getMenus().remove(menu_four);
            }
        });
        
        
        secretItem1.setOnAction(e -> {
           secretItem.setTitle("Ada Lovelace");
           secretItem.setHeaderText("The Enchantress of Number");
           secretItem.setContentText("Ada Lovelace was a mathematician and known as the first computer programmer. "
                   + "Her work on the Babbage Engine is what inspired the U.S Department of Defence to name a computer language after her. "
                   + "This computer language is known as Ada");
           secretItem.showAndWait();
        });
        
        secretItem2.setOnAction(e -> {
           secretItem.setTitle("Grace Hopper");
           secretItem.setHeaderText("Grandma COBOL");
           secretItem.setContentText("Grace Hopper was a mathematician and a rear admiral in the U.S Navy. "
                   + "Grace pioneered development in computer technology, helping design UNIVAC I and COBOL. "
                   + "Her accomplishments led to a Navy destroyer being named the USS Hopper in her honor");
           secretItem.showAndWait();
        });
        
        secretItem3.setOnAction(e -> {
           secretItem.setTitle("James Gosling");
           secretItem.setHeaderText("Dr. Java");
           secretItem.setContentText("James Gosling is a computer scientist. He is the founder and lead designer"
                   + " of the Java Programming Language. He is currently an advisor at the company Lightbend, "
                   + "indpendent director at Jelastic, a strategic advisor for Eucalyptus, and is "
                   + "a board member of DIRTT Environmental Solutions");
           secretItem.showAndWait();
        });
        
        secretItem4.setOnAction(e -> {
           secretItem.setTitle("Frances Allen");
           secretItem.setHeaderText("The First Woman To Win The Turing Award");
           secretItem.setContentText("Frances Allen was a computer scientist and pioneer in the field of optimizing compilers. "
                   + "During her career, Frances Allen helped to improve the efficiency of machine code translated from "
                   + "high-level languages. Because of her work she is the first woman to become an IBM Fellow and the first "
                   + "woman to win the Turing Award");
           secretItem.showAndWait();
        });
        
        secretItem5.setOnAction(e -> {
            secretItem.setTitle("Donald Knuth");
            secretItem.setHeaderText("The Father Of The Analysis Of Algorithms");
            secretItem.setContentText("Donald Knuth is a computer scientist and mathematician. Donald Knuth is best "
                    + "known for his book The Art Of Computer Programming, which made the analysis of algorithms "
                    + "into its own academic subject. Because of his work he was awarded the first ACM Grace "
                    + "Murray Hopper Award. He has also recieved other prestigous awards, such as the Turing award");
            secretItem.showAndWait();
        });
        
        secretItem6.setOnAction(e -> {
           secretItem.setTitle("Alan Turing");
           secretItem.setHeaderText("Prof");
           secretItem.setContentText("Alan Turing was a mathematician, computer scientist, logician, cryptanalyst, philospher, and theoretical biologist. "
                   + "Alan Turing's work as a codebreaker helped to end the WWII and saved over 14 million lives. "
                   + "Because of his work the prestigous Turing award was named after him");
           secretItem.showAndWait();
        });
        
        secretItem7.setOnAction(e -> {
           secretItem.setTitle("George Boole");
           secretItem.setHeaderText("Mr. Boolean");
           secretItem.setContentText("George Boole was a mathematician, philospher, and logician. He is best known "
                   + "for creating Boolean algebra, which is credited for laying the foundations for the information age. "
                   + "For his contributions the Boolean data type is named after him");
           secretItem.showAndWait();
        });
        
        secretItem8.setOnAction(e -> {
            secretItem.setTitle("John von Neumann");
            secretItem.setHeaderText("The Last Representative Of The Great Mathematicians");
            secretItem.setContentText("John von Neumann was a mathematician and computer scientist among other things. "
                    + "John von Neumann made many major contributions to many fields but was best known for working on "
                    + "the Manhattan Project by developing the mathematical models behind the explosive lenses used in "
                    + "the implosion-type nuclear weapon. During this time he coined the word kiloton to measure explosive force");
            secretItem.showAndWait();
        });
        
        secretItem9.setOnAction(e -> {
           secretItem.setTitle("Margaret Hamilton"); 
           secretItem.setHeaderText("Director Of Software Engineering Of MIT");
           secretItem.setContentText("Margaret Hamilton was a computer scientist, systems engineer, and business owner. "
                   + "Her work with NASA was the foundation for ultra-reliable software design. She also is one of three"
                   + " people to coin the term software engineering. Her work led to her recieving the Presidential Medal "
                   + "Of Freedom from President Barack Obama");
           secretItem.showAndWait();
        });
        
        secretItem10.setOnAction(e -> {
            secretItem.setTitle("Barbara Liskov");
            secretItem.setHeaderText("One Of The First Woman To Gain A Doctorate In Computer Science");
            secretItem.setContentText("Barbar Liskov is an Institute Professor at MIT and Ford Professor Of Engineering. "
                    + "Barbara Liskov is best known for developing the Liskov substitution principle, which allows for better code"
                    + " reusability. For her work, Barbara Liskov was listed in Discover magazine as one of the 50 most important"
                    + " women in science");
            secretItem.showAndWait();
        });
        
        secretItem11.setOnAction(e -> {
            secretItem.setTitle("Seymour Cray");
            secretItem.setHeaderText("The Father Of Supercomputing");
            secretItem.setContentText("Seymour Cray was an electrical engineer and supercomputer arcitect. He is best"
                    + " known for being the preeminent designer of supercomputers. He is also infamously known for digging"
                    + " a tunnel underneath his home where elves would visit him and solve his problems");
            secretItem.showAndWait();
        });
        
        Scene scene = new Scene(newRoot, 300, 275);
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * This makes a writableimage of
     * a snapshot of the canvas
     * That writableimage will then be added
     * to the undo and redo stacks
     * When the undo button is called 
     * it will push out the first writableimage
     * saved to it, undoing the action
     * When the redo button is called
     * it will push out the writableimage
     * at the top of the stack
     * undoing the undo button
     */
    public void saveToStack(){
        SnapshotParameters sp = new SnapshotParameters();
        wim = new WritableImage((int)canvasRef.getWidth(), (int)canvasRef.getHeight());
        undoStack.push(canvasRef.snapshot(sp, wim));
    }
    
    /**
     * This enacts code that will push out
     * the writableimage that is at the top
     * of the undo stack
     */
    public void undo()
    {
        if(undoStack.empty())
        {
            System.out.println("Is Empty");
        }
        else
        {
            SnapshotParameters sp = new SnapshotParameters();
            wim = new WritableImage((int)canvasRef.getWidth(), (int)canvasRef.getHeight());
            redoStack.push(canvasRef.snapshot(sp, wim));
            graphicContext.drawImage((undoStack.pop()), 0, 0, canvasRef.getWidth(), canvasRef.getHeight());
        }
    }
    
    /**
     * This enacts code that will push out
     * the writableimage that is at the top
     * of the redo stack
     */
    public void redo()
    {
        if(redoStack.empty())
        {
            System.out.println("Is Empty");
        }
        else
        {
            graphicContext.drawImage((redoStack.pop()), 0, 0, canvasRef.getWidth(), canvasRef.getHeight());
        }
    }
    
    /**
     * This method is meant to test the junits
     * @param a1 just a random number
     * @param a2 just a random number
     * @return 4 to make sure the tests work
     */
    public int test(String a1, String a2){
        int s1 = Integer.parseInt(a1);
        int s2 = Integer.parseInt(a2);
        return s1+s2;
    }
    
    public void copyTool()
    {
        selectGC = canvasRef.getGraphicsContext2D();
        canvasRef.setOnMousePressed(eve -> {
            moveImageStartX = eve.getX();
            moveImageStartY = eve.getY();
            canvasRef.setOnMousePressed(ev ->{});
        });
        
        canvasRef.setOnMouseReleased(eve -> {
           moveImageEndX = eve.getX();
           moveImageEndY = eve.getY();
           imageWidth = (int)(moveImageEndX-moveImageStartX); 
           imageHeight = (int)(moveImageStartY-moveImageEndY);
           
           if(imageWidth < 0)
           {
               imageWidth = imageWidth * -1;
           }
           if(imageHeight < 0)
           {
               imageHeight = imageHeight * -1;
           }
           
           
           WritableImage temp = new WritableImage((int)canvasRef.getWidth(), (int)canvasRef.getHeight());
           canvasRef.snapshot(null, temp);
           selectGC.clearRect(moveImageStartX, moveImageStartY, imageWidth, imageHeight);
           WritableImage cropSnap = new WritableImage(temp.getPixelReader(), (int)moveImageStartX, (int)moveImageStartY, imageWidth, imageHeight);
           canvasRef.setOnMouseReleased(ev ->{});
           pasteTool(cropSnap);
           
        });
    }
    public void pasteTool(WritableImage snapshot)
    {
        pasteGC = canvasRef.getGraphicsContext2D();
        canvasRef.setOnMousePressed(eve -> {
           moveImageStartX = eve.getX();
           moveImageStartY = eve.getY();
           canvasRef.setOnMousePressed(ev -> {});
        });
        canvasRef.setOnMouseReleased(eve -> {
            pasteGC.drawImage(snapshot, moveImageStartX, moveImageStartY);
            pasteGC.stroke();
            canvasRef.setOnMouseReleased(ev -> {});
        });
    }
    
    public void drawSquare()
    {
        canvasRef.setOnMousePressed(eve -> {
                            graphicContext.beginPath();
                            rectX = eve.getX();
                            rectY = eve.getY();
        });
            canvasRef.setOnMouseDragged(eve -> {
                graphicContext.rect(rectX, rectY, eve.getX() - rectX, eve.getY() - rectY);
                graphicContext.fillRect(rectX, rectY, eve.getX() - rectX, eve.getY() - rectY);
                    });
            canvasRef.setOnMouseReleased(eve -> {
                            graphicContext.rect(rectX, rectY, eve.getX() - rectX, eve.getY() - rectY);
                            graphicContext.fillRect(rectX, rectY, eve.getX() - rectX, eve.getY() - rectY);
                            graphicContext.closePath();
                    });
    }
    
    public String testTwo(String h, String w)
    {
        String both  = h+w;
        return both;
    }
    
    public double testThree(double one, double two)
    {
        double three = two/one;
        return three;
    }
    
    private void callTimer()
    {
        
        Thread taskThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i<10; i++){
                    try{
                        Thread.sleep(1000);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    countdown();
                    
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            
                        }
                    });
                }
            }
        });
    }
    
    public void fixTimer()
    {
       timerLabel.textProperty().bind(timeSeconds.asString());
        timerLabel.setTextFill(Color.RED);
        timerLabel.setStyle("-fx-font-size: 4em;");
        timeSeconds.set(STARTTIME);
                timeline = new Timeline();
                timeline.getKeyFrames().add(
                        new KeyFrame(Duration.seconds(STARTTIME+1),
                        new KeyValue(timeSeconds, 0)));
                timeline.playFromStart(); 
    }
    
    public void showTimer()
    {
        newRoot.getChildren().add(timerLabel);
        
    }
    
    public void hideTimer()
    {
        newRoot.getChildren().remove(timerLabel);
    }
    
    public void saveTheImage()
    {
        if(file != null)
        {
            wim = new WritableImage((int)image.getWidth(), (int)image.getHeight());
            canvasRef.snapshot(null, wim);
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
            } catch(IOException ex) {
                Logger.getLogger(Paint.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void countdown()
    {
        if(seconds == 0)
        {
            saveTheImage();
            seconds = 10;
            fixTimer();
        }
        else
        {
            seconds--;
        }
    }
    
    public void logging()
    {
        logTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Logger.getLogger(Paint.class.getName()).log(Level.SEVERE, tool);
                Logger.getLogger(Paint.class.getName()).log(Level.SEVERE, fileName);
            }
        }, 0, 60000);
    }
    /**
     * create countdown function
     * call during run method and pass that to showTimer
     * For Tabs use arraylist
     * For Preview use stacks
     */
    
    public void whichDrawing(String draw)
    {
        switch(draw)
        {
            case "line":
                drawLine();
            case "pencil":
                drawPencil();
            case "square":
                drawSquare();
            case "rectangle":
                drawRectangle();
            case "eraser":
                drawEraser();
            case "ellipse":
                drawEllipse();
            case "circle":
                drawCircle();
            case "triangle":
                drawTriangle();
            case "polygon":
                drawPolygon();
            case "text":
                drawText();
        }
    }
    
    public void drawLine()
    {
        graphicContext.setStroke(cp.getValue());
            canvasRef.setOnMousePressed(eve -> {
                           graphicContext.beginPath(); 
                           graphicContext.moveTo(eve.getX(), eve.getY());
                        
                    });
            canvasRef.setOnMouseDragged(eve -> {
                            graphicContext.lineTo(eve.getX(), eve.getY());
                    });
            canvasRef.setOnMouseReleased(eve -> {
                            graphicContext.lineTo(eve.getX(), eve.getY());
                            graphicContext.stroke();
                            graphicContext.closePath();
                    });
    }
    public void drawPencil()
    {
        graphicContext.setStroke(cp.getValue());
            canvasRef.setOnMousePressed(eve -> {
                            graphicContext.beginPath();
                            graphicContext.moveTo(eve.getX(), eve.getY());
                            graphicContext.stroke();
                    });
            canvasRef.setOnMouseDragged(eve -> {
                            graphicContext.lineTo(eve.getX(), eve.getY());
                            graphicContext.stroke();
                    });
            canvasRef.setOnMouseReleased(eve -> {
                            graphicContext.closePath();
                    });
    }
    
    public void drawRectangle()
    {
        graphicContext.setStroke(cp.getValue());
            graphicContext.setFill(cp.getValue());
            canvasRef.setOnMousePressed(eve -> {
                            graphicContext.beginPath();
                            rectX = eve.getX();
                            rectY = eve.getY();
                    });
            canvasRef.setOnMouseDragged(eve -> {
                graphicContext.rect(rectX, rectY, eve.getX() - rectX, eve.getY() - rectY);
                graphicContext.fillRect(rectX, rectY, eve.getX() - rectX, eve.getY() - rectY);
                    });
            canvasRef.setOnMouseReleased(eve -> {
                            graphicContext.rect(rectX, rectY, eve.getX() - rectX, eve.getY() - rectY);
                            graphicContext.fillRect(rectX, rectY, eve.getX() - rectX, eve.getY() - rectY);
                            graphicContext.closePath();
                    });
    }
    
    public void drawEllipse()
    {
        canvasRef.setOnMousePressed(eve -> {
                           graphicContext.beginPath();
                           startingPosX = eve.getX();
                           startingPosY = eve.getY();
                    });
        canvasRef.setOnMouseDragged(eve -> {
            graphicContext.strokeOval(Math.min(startingPosX, eve.getX()), Math.min(startingPosY, eve.getY()), Math.abs(eve.getX() - startingPosX), Math.abs(eve.getY() - startingPosY));
            graphicContext.fillOval(Math.min(startingPosX, eve.getX()), Math.min(startingPosY, eve.getY()), Math.abs(eve.getX() - startingPosX), Math.abs(eve.getY() - startingPosY));
        });
            canvasRef.setOnMouseReleased(eve -> {
                            graphicContext.setStroke(cp.getValue());
                            graphicContext.setFill(cp.getValue());
                            graphicContext.strokeOval(Math.min(startingPosX, eve.getX()), Math.min(startingPosY, eve.getY()), Math.abs(eve.getX() - startingPosX), Math.abs(eve.getY() - startingPosY));
                            graphicContext.fillOval(Math.min(startingPosX, eve.getX()), Math.min(startingPosY, eve.getY()), Math.abs(eve.getX() - startingPosX), Math.abs(eve.getY() - startingPosY));
                            graphicContext.closePath();
                    });
    }
    
    public void drawCircle()
    {
        graphicContext.setStroke(cp.getValue());
        graphicContext.setFill(cp.getValue());
        canvasRef.setOnMousePressed(eve -> {
                           graphicContext.beginPath();
                           startingPosX = eve.getX();
                           startingPosY = eve.getY();
                    });
        canvasRef.setOnMouseDragged(eve ->{
            graphicContext.strokeOval(Math.min(startingPosX, eve.getX()), Math.min(startingPosY, eve.getY()), Math.abs(eve.getX() - startingPosX), Math.abs(eve.getY() - startingPosY));
            graphicContext.fillOval(Math.min(startingPosX, eve.getX()), Math.min(startingPosY, eve.getY()), Math.abs(eve.getX() - startingPosX), Math.abs(eve.getY() - startingPosY));
        });
            canvasRef.setOnMouseReleased(eve -> {
                            
                            graphicContext.strokeOval(Math.min(startingPosX, eve.getX()), Math.min(startingPosY, eve.getY()), Math.abs(eve.getX() - startingPosX), Math.abs(eve.getY() - startingPosY));
                            graphicContext.fillOval(Math.min(startingPosX, eve.getX()), Math.min(startingPosY, eve.getY()), Math.abs(eve.getX() - startingPosX), Math.abs(eve.getY() - startingPosY));
                            graphicContext.closePath();
                    });
    }
    
    public void drawTriangle()
    {
        canvasRef.setOnMouseClicked(eve -> {
                            for(increment=0; increment<3; increment++){
                                triangleX[increment] = eve.getX();
                                triangleY[increment] = eve.getY();
                            }                                
                    });
            graphicContext.strokePolygon(triangleX, triangleY, 3);
            graphicContext.fillPolygon(triangleX, triangleY, 3);
            triangleX = new double[triangleX.length];
            triangleY = new double[triangleY.length];
            Alert preview = new Alert(AlertType.CONFIRMATION);
            preview.setTitle("Preview");
            preview.setContentText("Are you sure you would like to draw the triangle?");
            Optional<ButtonType> result = preview.showAndWait();
            if(result.get() != ButtonType.OK)
            {
                undo();
            }
    }
    
    public void drawPolygon()
    {
        double[] polygonX = new double[numberOfSides];
        double[] polygonY = new double[numberOfSides];
           canvasRef.setOnMouseClicked(eve -> {
                          for(increment=0; increment<numberOfSides; increment++)
                          {
                              polygonX[increment] = eve.getX();
                              polygonY[increment] = eve.getY();
                          }
                   });
           graphicContext.strokePolygon(polygonX, polygonY, polygonX.length);
           graphicContext.fillPolygon(polygonX, polygonY, polygonX.length);
           Alert preview = new Alert(AlertType.CONFIRMATION);
           preview.setTitle("Preview");
           preview.setContentText("Are you sure you would like to draw the Polygon?");
           Optional<ButtonType> result = preview.showAndWait();
           if(result.get() != ButtonType.OK)
           {
               undo();
           }
           
    }
    
    public void drawText()
    {
        canvasRef.setOnMouseClicked(eve -> {
                            TextInputDialog text = new TextInputDialog("enter text");
                            text.setHeaderText("Please enter the text you would like on the image");
                            text.showAndWait();
                            String t = text.getEditor().getText();
                            graphicContext.strokeText(t, eve.getX(), eve.getY());
                            graphicContext.fillText(t, eve.getX(), eve.getY());
                    });
    }
    
    public void drawEraser()
    {
        canvasRef.setOnMousePressed(eve -> {
                           graphicContext.clearRect(eve.getX(), eve.getY(), graphicContext.getLineWidth(), graphicContext.getLineWidth());
                    });
            canvasRef.setOnMouseDragged(eve -> {
                            graphicContext.clearRect(eve.getX(), eve.getY(), graphicContext.getLineWidth(), graphicContext.getLineWidth());
                    });
    }
    
    public void addTab()
    {
        int numTabs = tab.getTabs().size();
        Tab tabs = new Tab("Tab "+(numTabs+1));
        tabs.setContent(canvasRef);
        tab.getTabs().add(tabs);
    }
}

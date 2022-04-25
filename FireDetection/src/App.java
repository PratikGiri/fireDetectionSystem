

import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    Map buldingMap = new Map();
    Label resultLabel = new Label();
    ArrayList<Button> btnList = new ArrayList<>();
    public static void main(String[] args) {
      launch(args);
    }

  public void setIcon(String path, Button btn){
    Image img = new Image(path);
    ImageView view = new ImageView(img);
    view.setPreserveRatio(true);
    btn.setGraphic(view);
  }
  public Button createCell(String text, int count){
    Button btn = new Button();
    int id = count;
    App that = this;

    if(text.equals("#")){
      setIcon("wall.png", btn);
    }
    if(text.equals("_")){
      setIcon("road.png", btn);
    }
    if(text.equals("[]")){ //room
      setIcon("room.png", btn);
    }
    if(text.equals("@")){ // roomgate
      setIcon("roomgate.png", btn);
    }
    if(text.equals("Ex")){ // roomgate
      setIcon("fire-exit.png", btn);
    }
    if(text.equals("E")){ // roomgate
      setIcon("gate.png", btn);
    }

   // btn.setText(text);
    btn.setTooltip(new Tooltip(that.buldingMap.idNodeMap.get(id).numberOfPeople+""));
    btn.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            that.buldingMap.idNodeMap.get(id).setSmokeLevel(2.0);
            that.buldingMap.idNodeMap.get(id).setTemperature(100.0);
            Image img = new Image("fire.png");
            ImageView view = new ImageView(img);
            view.setPreserveRatio(true);
            btn.setGraphic(view);
      
           // System.out.println(text);
            //Simulator.simulate
        }
    });

    btn.setMinWidth(80);
    btnList.add(btn);
    return btn;
  }

  public GridPane createGrid(){
    GridPane gridPane = new GridPane();
    int count =1;
    for(int i=0;i<Math.sqrt(buldingMap.numberOfNodes);i++){
        for(int j=0;j<Math.sqrt(buldingMap.numberOfNodes);j++){
            if(buldingMap.idNodeMap.get(count).type.equals("MainGate")){
              gridPane.add(createCell("E", count), j, i, 1, 1);
            } else if(buldingMap.idNodeMap.get(count).type.equals("Room")){
              gridPane.add(createCell("[]", count), j, i, 1, 1);
            } else if(buldingMap.idNodeMap.get(count).type.equals("Room Gate")){
              gridPane.add(createCell("@", count), j, i, 1, 1);
            } else if (buldingMap.idNodeMap.get(count).type.equals("Road")){
              gridPane.add(createCell("_", count), j, i, 1, 1);
            } else if(buldingMap.idNodeMap.get(count).type.equals("Building Exit")){
              gridPane.add(createCell("Ex", count), j, i, 1, 1);
            }else if(buldingMap.idNodeMap.get(count).type.equals("cell")){
              gridPane.add(createCell("#", count), j, i, 1, 1);
            }
            count++;
        }
    }
    //gridPane.setHgap(10);
    return gridPane;
  }

  public Pane simulatorResult(){
    Pane pane = new Pane();
    resultLabel.setText("No Fire Detected......");
    pane.getChildren().addAll(resultLabel);
    return pane;
  }

  public void startSimulation(){
    Simulation simulator = new Simulation(resultLabel, buldingMap, btnList);
    simulator.start();
  }


  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setTitle("Building Bluprint");
    VBox verticalBox = new VBox();
    // This button is added just to normalize the indexes of button and path finder.
    btnList.add(new Button("Random"));
    verticalBox.getChildren().addAll(createGrid(),simulatorResult());
    Scene scene = new Scene(verticalBox, 600, 400);
    startSimulation();
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}

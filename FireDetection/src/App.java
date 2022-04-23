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
    public static void main(String[] args) {
      launch(args);
    }

  public Button createCell(String text, int count){
    Button btn = new Button();
    int id = count;
    App that = this;
    btn.setText(text);
    btn.setTooltip(new Tooltip(that.buldingMap.idNodeMap.get(id).numberOfPeople+""));
    btn.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            that.buldingMap.idNodeMap.get(id).setSmokeLevel(2.0);
            that.buldingMap.idNodeMap.get(id).setTemperature(100.0);
           /* Image img = new Image("https://w7.pngwing.com/pngs/206/654/png-transparent-burning-fire-combustion-raging-fire-flames.png");
            ImageView view = new ImageView(img);
            view.setPreserveRatio(true);
            btn.setGraphic(view);*/
      
            System.out.println(text);
            //Simulator.simulate
        }
    });
    return btn;
  }

  public GridPane createGrid(){
    GridPane gridPane = new GridPane();
    int count =1;
    for(int i=0;i<5;i++){
        for(int j=0;j<4;j++){
            if(buldingMap.idNodeMap.get(count).type.equals("MainGate")){
              gridPane.add(createCell("()", count), j, i, 1, 1);
            } else if(buldingMap.idNodeMap.get(count).type.equals("Room")){
              gridPane.add(createCell("+", count), j, i, 1, 1);
            } else if(buldingMap.idNodeMap.get(count).type.equals("Room Gate")){
              gridPane.add(createCell("@", count), j, i, 1, 1);
            } else if (buldingMap.idNodeMap.get(count).type.equals("Road")){
              gridPane.add(createCell("_", count), j, i, 1, 1);
            } else if(buldingMap.idNodeMap.get(count).type.equals("Building Exit")){
              gridPane.add(createCell("(.)", count), j, i, 1, 1);
            }else if(buldingMap.idNodeMap.get(count).type.equals("cell")){
              gridPane.add(createCell("#", count), j, i, 1, 1);
            }
            count++;
        }
    }
    return gridPane;
  }

  public Pane simulatorResult(){
    Pane pane = new Pane();
    resultLabel.setText("No Fire Detected......");
    pane.getChildren().addAll(resultLabel);
    return pane;
  }

  public void startSimulation(){
    Simulation simulator = new Simulation(resultLabel, buldingMap);
    simulator.start();
  }


  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setTitle("Building Bluprint");
    VBox verticalBox = new VBox();
    verticalBox.getChildren().addAll(createGrid(),simulatorResult());
    Scene scene = new Scene(verticalBox, 400, 300);
    startSimulation();
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}

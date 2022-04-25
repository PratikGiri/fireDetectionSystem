
import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class Simulation extends Thread {
    Label result;
    String message;
    int round;
    ArrayList<Button> btnList;

    Map buildingMap;
    Simulation(Label label, Map map, ArrayList<Button> buttons){
        this.result = label;
        this.buildingMap = map;
        this.round = 0;
        this.btnList = buttons;
    }

    public void run(){
        Runnable uiUpdater = new Runnable() {
            public void run(){
                result.setText(message);
            }
        };
        while(true){
            this.round++;
            System.out.println("Round: "+round);
            System.out.println("----------------------------");
            message = "---------------------------Round "+ round +" ---------------------------------\n";

            Firedectector fireDetector=  new Firedectector(buildingMap);
            Thread fireDetectionThread = new Thread(fireDetector);
            fireDetectionThread.start();

            Peopledetecter peopleDetector=  new Peopledetecter(buildingMap);
            Thread peopleDetectorThread = new Thread(peopleDetector);
            peopleDetectorThread.start();
            try {
                // Fire Detection Work;
                fireDetectionThread.join();
                ArrayList<Integer> nodesOnFire = fireDetector.getAllNodesOnFire();
                message += "Nodes on Fire";
                for(Integer i: nodesOnFire){
                    message = message +" "+ i +"|";
                }
                message += "\n";
                
                // People Detection Work
                peopleDetectorThread.join();
                HashMap<Integer,Integer> peopleInRoom = peopleDetector.getPeopleRoomMap();
                for (HashMap.Entry<Integer, Integer> entry : peopleInRoom.entrySet()) {
                    Integer gateNumber = entry.getKey();
                    Integer numberOfPeopleInsideRoom = entry.getValue();
                    message += "Room With Gate Number : "+ gateNumber + " has " + numberOfPeopleInsideRoom + " left \n"; 
                }

                //Path Finder
                PathFinder pathFinder = new PathFinder(buildingMap, nodesOnFire, 6);
                /*Call the path finder. Pass the node number from where we need to find the nearest exit.
                The node number can be calculated from where the people are present
                 */
                ArrayList<Integer> path = pathFinder.findRoute(56);
                System.out.println("Path: " + path);
                for(Button btn: btnList){
                    btn.setStyle(null);
                }
                for(int i: path){
                    btnList.get(i+1).setStyle("-fx-background-color: #98FB98; ");
                }
               
                // Updating the UI.
                Platform.runLater(uiUpdater);

                if(nodesOnFire.size()>0 ){
                    for (HashMap.Entry<Integer, Node> entry : buildingMap.roomGate.entrySet()) {
                        Integer roomId = entry.getKey();
                        if(buildingMap.idNodeMap.get(roomId).numberOfPeople >0){
                            buildingMap.idNodeMap.get(roomId).numberOfPeople -= 2;
                        } 
                        if(buildingMap.idNodeMap.get(roomId).numberOfPeople <0){
                            buildingMap.idNodeMap.get(roomId).numberOfPeople = 0;
                        }
                        
                    }
                }

                


            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
           
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
    }
}

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

    public void handleRealTimePathDetections(ArrayList<Integer> nodesOnFire) throws InterruptedException{
         //Path Finder Work
         PathFinder pathFinder = new PathFinder(buildingMap, nodesOnFire, 6);
         Thread pathFinderThread = new Thread(pathFinder);
         pathFinderThread.start();
        
         pathFinderThread.join();
         ArrayList<Integer> path = pathFinder.getPath();
         
         // Simulation for People walking.
         int pathIndex = 0;
         while(pathIndex<path.size()){
            if(buildingMap.idNodeMap.get(path.get(pathIndex)).numberOfPeople == 0){
                buildingMap.idNodeMap.get(path.get(pathIndex)).numberOfPeople = 5;
                break;
            }
            pathIndex++;
         }
        
         // Highlighting path.
         for(Button btn: btnList){
             btn.setStyle(null);
         }
         for(int i: path){
             btnList.get(i).setStyle("-fx-background-color: #98FB98; ");
         }
         pathFinderThread.interrupt();;
    }

    public void run(){
        Runnable uiUpdater = new Runnable() {
            public void run(){
                result.setText(message);
            }
        };
        while(true){
            int peopleLeft=65 ;
            this.round++;
            System.out.println();
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
                    message += "Room With Gate Number : "+ gateNumber + " has following number of people " + numberOfPeopleInsideRoom + " left \n"; 
                }

                if(nodesOnFire.size() >0 && peopleLeft > 0){
                    handleRealTimePathDetections(nodesOnFire);
                }

                
                // Updating the UI.
                Platform.runLater(uiUpdater);

                if(nodesOnFire.size()>0 ){
                    for (HashMap.Entry<Integer, Node> entry : buildingMap.roomGate.entrySet()) {
                        Integer roomId = entry.getKey();
                        if(buildingMap.idNodeMap.get(roomId).numberOfPeople >0){
                            buildingMap.idNodeMap.get(roomId).numberOfPeople -= 5;
                        } 
                        if(buildingMap.idNodeMap.get(roomId).numberOfPeople <0){
                            buildingMap.idNodeMap.get(roomId).numberOfPeople = 0;
                        }
                        peopleLeft =  buildingMap.idNodeMap.get(roomId).numberOfPeople;
                    }
                    if(peopleLeft>0)
                        System.out.println("Status :: People Evacuating.");
                    else   
                        System.out.println("Status :: All People Evacuated !!");
                }

                


            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
           
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            fireDetectionThread.interrupt();;
            peopleDetectorThread.interrupt();
        }
        
    }
}

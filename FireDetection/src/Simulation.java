import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class Simulation extends Thread {
    Label result;
    String message;
    int round;

    Map buildingMap;
    Simulation(Label label, Map map){
        this.result = label;
        this.buildingMap = map;
        this.round = 0;
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
                    message += "Room With Gate Number : "+ gateNumber + "has " + numberOfPeopleInsideRoom + " left \n"; 
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

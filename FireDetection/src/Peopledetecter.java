import java.util.HashMap;

public class Peopledetecter implements Runnable{

    HashMap<Integer,Integer> numberOfPeopleinRoomMap = new HashMap<>();
    Map buildingMap;
    Peopledetecter(Map map){
        this.buildingMap = map;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        for(int i=1;i<buildingMap.numberOfNodes;i++){
            if(buildingMap.idNodeMap.get(i).type.equals("Room")){
                numberOfPeopleinRoomMap.putIfAbsent(buildingMap.roomGate.get(i).getNodeId(), buildingMap.idNodeMap.get(i).numberOfPeople);
            }
        }
    }

    public HashMap<Integer,Integer> getPeopleRoomMap(){
        return numberOfPeopleinRoomMap;
    }
    
}

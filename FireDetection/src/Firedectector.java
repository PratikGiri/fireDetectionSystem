
import java.util.ArrayList;

public class Firedectector implements Runnable {
    ArrayList<Integer> nodesOnFire = new ArrayList<>();
    Map buildingMap;
    Firedectector(Map map){
        this.buildingMap = map;
    }
    @Override
    public void run() {
        for(int i=1;i<=buildingMap.numberOfNodes;i++){
            if(buildingMap.idNodeMap.get(i).getTemperature() > 70.0 && buildingMap.idNodeMap.get(i).getSmoke() > 2.6){
               nodesOnFire.add(i);
            }
        }
    }

    public ArrayList<Integer> getAllNodesOnFire(){
        System.out.println("Number of Nodes on Fire: " + nodesOnFire.size());
        return nodesOnFire;
    
    }
    
}

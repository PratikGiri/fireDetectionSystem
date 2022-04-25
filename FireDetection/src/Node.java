
import java.util.ArrayList;

public class Node {
    boolean state = false;  // burning state of the node.
    int id = 0;
    String type ="";
    ArrayList<Node> neighbour = new ArrayList<>();
    double temperature = 0.5, smokeLevel = 0.5;
    int ccTVID;
    int capacity;
    int numberOfPeople;


    public Node(int node_id, String nodeType, int capacity, int numberOfPeople){
        this.id = node_id;
        this.type = nodeType; 
        this.capacity = capacity;
        this.numberOfPeople = numberOfPeople;
    }

    public void addCctv(int cctvID){
        this.ccTVID = cctvID;
    }
    
    public void addNeighbours(ArrayList<Node> newNeighbours){
        for(int i=0;i<newNeighbours.size();i++){
            neighbour.add(newNeighbours.get(i));
        }
    }

    public void addNeighbour(Node newNeighbour){
        neighbour.add(newNeighbour);
    }

    public boolean isBurning(){
        return state;
    }

    public int getNodeId(){
        return id;
    }

    public String getNodeType(){
        return type;
    }

    public void setState(boolean shallBurnNode){
        this.state = shallBurnNode;
    }

    public void setSmokeLevel(double level){
        smokeLevel = level;
    }

    public void setTemperature(double temperature){
        this.temperature = temperature;
    }

    public double getTemperature(){
        return temperature;
    }

    public double getSmoke(){
        return smokeLevel;
    }


}

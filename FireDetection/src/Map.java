import java.util.ArrayList;
import java.util.HashMap;

public class Map {
    Node buildinEntyr;
    int numberOfNodes = 20;
    HashMap<Integer,Node> idNodeMap;
    HashMap<Integer, Node> roomGate;
    HashMap<Integer, String> nodeTypeMap;
    public Map(){
        buildinEntyr = new Node(1, "MainGate",5,0);
        idNodeMap = new HashMap<>();
        roomGate = new HashMap<>();
        idNodeMap.put(1, buildinEntyr);
        nodeTypeMap = new HashMap<>();
        mapInit();
    }

    public void mapInit(){
        nodeTypeMap.put(2, "Room");
        nodeTypeMap.put(3, "Room");
        nodeTypeMap.put(4, "Room");
        nodeTypeMap.put(5, "Room");
        nodeTypeMap.put(6, "Room Gate");
        nodeTypeMap.put(11, "Road");
        nodeTypeMap.put(12, "Road");
        nodeTypeMap.put(16, "Road");
        nodeTypeMap.put(20, "Building Exit");
        for(int i=2;i<=numberOfNodes;i++){
            addNodeToMap(i);
           // idNodeMap.put(i, new Node(i,nodeTypeMap.getOrDefault(i, "cell")));
        }
        connectNodesInMap(1,6);
        connectNodesInMap(6,11);
        connectNodesInMap(11,12);
        connectNodesInMap(12,16);
        connectNodesInMap(16,20);
        /*idNodeMap.get(1).addNeighbour(idNodeMap.get(6));
        idNodeMap.get(6).addNeighbour(idNodeMap.get(11));
        idNodeMap.get(11).addNeighbour(idNodeMap.get(12));
        idNodeMap.get(12).addNeighbour(idNodeMap.get(16));
        idNodeMap.get(16).addNeighbour(idNodeMap.get(20));*/
        roomGate.put(2, idNodeMap.get(6));
        roomGate.put(3, idNodeMap.get(6));
        roomGate.put(4, idNodeMap.get(6));
        roomGate.put(5, idNodeMap.get(6));
    }

    public boolean addNodeToMap(int node){
        String type = this.nodeTypeMap.getOrDefault(node, "cell");
        int capacity=0, numberOfPeople=0;
        if(type.equals("cells")){
            capacity =0;
            numberOfPeople =0;
        } else if(type.equals("MainGate")){
            capacity =10;
            numberOfPeople =0;
        } else if(type.equals("Room")){
            capacity =30;
            numberOfPeople =19;
        } else if(type.equals("Room Gate")){
            capacity =5;
            numberOfPeople =0;
        } else if(type.equals("Road")){
            capacity =5;
            numberOfPeople =0;
        } else if(type.equals("Building Exit")){
            capacity =5;
            numberOfPeople =0;
        }
        idNodeMap.put(node, new Node(node,this.nodeTypeMap.getOrDefault(node, "cell"), capacity, numberOfPeople));
        return true;
    }

    public boolean connectNodesInMap(int node1, int node2){
        idNodeMap.get(node1).addNeighbour(idNodeMap.get(node2));
        return true;
    }

    public boolean removeNodesFromMap(int nodeid){
        return false;
    }

    
}

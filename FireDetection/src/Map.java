package FireDetection.src;

import java.util.ArrayList;
import java.util.HashMap;

public class Map {
    Node buildinEntyr;
    int numberOfNodes = 100;
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
        nodeTypeMap.put(55, "Room");
        nodeTypeMap.put(56, "Room");
        nodeTypeMap.put(45, "Room");
        nodeTypeMap.put(46, "Room");
        nodeTypeMap.put(57, "Room Gate");
        nodeTypeMap.put(2, "Road");
        nodeTypeMap.put(3, "Road");
        nodeTypeMap.put(4, "Road");
        nodeTypeMap.put(5, "Road");
        nodeTypeMap.put(6, "Road");
        nodeTypeMap.put(7, "Road");
        nodeTypeMap.put(8, "Road");
        nodeTypeMap.put(9, "Road");
        nodeTypeMap.put(12, "Road");
        nodeTypeMap.put(19, "Road");
        nodeTypeMap.put(22, "Road");
        nodeTypeMap.put(29, "Road");
        nodeTypeMap.put(32, "Road");
        nodeTypeMap.put(39, "Road");
        nodeTypeMap.put(42, "Road");
        nodeTypeMap.put(49, "Road");
        nodeTypeMap.put(52, "Road");
        nodeTypeMap.put(59, "Road");
        nodeTypeMap.put(62, "Road");
        nodeTypeMap.put(63, "Road");
        nodeTypeMap.put(64, "Road");
        nodeTypeMap.put(65, "Road");
        nodeTypeMap.put(66, "Road");
        nodeTypeMap.put(67, "Road");
        nodeTypeMap.put(68, "Road");
        nodeTypeMap.put(69, "Road");
        nodeTypeMap.put(72, "Road");
        nodeTypeMap.put(79, "Road");
        nodeTypeMap.put(82, "Road");
        nodeTypeMap.put(89, "Road");
        nodeTypeMap.put(92, "Road");
        nodeTypeMap.put(99, "Road");
        nodeTypeMap.put(93, "Road");
        nodeTypeMap.put(94, "Road");
        nodeTypeMap.put(95, "Road");
        nodeTypeMap.put(96, "Road");
        nodeTypeMap.put(97, "Road");
        nodeTypeMap.put(98, "Road");
        nodeTypeMap.put(10, "Building Exit");
        nodeTypeMap.put(51, "Building Exit");
        nodeTypeMap.put(60, "Building Exit");
        nodeTypeMap.put(91, "Building Exit");
        nodeTypeMap.put(100, "Building Exit");
        for(int i=2;i<=numberOfNodes;i++){
            addNodeToMap(i);
           // idNodeMap.put(i, new Node(i,nodeTypeMap.getOrDefault(i, "cell")));
        }
        connectNodesInMap(1,2);
        connectNodesInMap(9,10);
        connectNodesInMap(2,3);
        connectNodesInMap(3,4);
        connectNodesInMap(4,5);
        connectNodesInMap(5,6);
        connectNodesInMap(6,7);
        connectNodesInMap(7,8);
        connectNodesInMap(8,9);
        connectNodesInMap(62,63);
        connectNodesInMap(63,64);
        connectNodesInMap(64,65);
        connectNodesInMap(65,66);
        connectNodesInMap(66,67);
        connectNodesInMap(67,68);
        connectNodesInMap(68,69);
        connectNodesInMap(92,93);
        connectNodesInMap(93,94);
        connectNodesInMap(94,95);
        connectNodesInMap(95,96);
        connectNodesInMap(96,97);
        connectNodesInMap(97,98);
        connectNodesInMap(98,99);
        connectNodesInMap(60,61);
        connectNodesInMap(69,70);
        connectNodesInMap(90,91);
        connectNodesInMap(99,100);
        connectNodesInMap(2,12);
        connectNodesInMap(12,22);
        connectNodesInMap(22,32);
        connectNodesInMap(32,42);
        connectNodesInMap(42,52);
        connectNodesInMap(52,62);
        connectNodesInMap(62,72);
        connectNodesInMap(72,82);
        connectNodesInMap(82,92);
        connectNodesInMap(9,19);
        connectNodesInMap(19,29);
        connectNodesInMap(29,39);
        connectNodesInMap(39,49);
        connectNodesInMap(49,59);
        connectNodesInMap(59,69);
        connectNodesInMap(69,79);
        connectNodesInMap(79,89);
        connectNodesInMap(89,99);
        //connectNodesInMap(6,11);
        //connectNodesInMap(11,12);
        //connectNodesInMap(12,16);
        //connectNodesInMap(16,20);
        /*idNodeMap.get(1).addNeighbour(idNodeMap.get(6));
        idNodeMap.get(6).addNeighbour(idNodeMap.get(11));
        idNodeMap.get(11).addNeighbour(idNodeMap.get(12));
        idNodeMap.get(12).addNeighbour(idNodeMap.get(16));
        idNodeMap.get(16).addNeighbour(idNodeMap.get(20));*/
        //roomGate.put(2, idNodeMap.get(6));
        //roomGate.put(3, idNodeMap.get(6));
        //roomGate.put(4, idNodeMap.get(6));
        //roomGate.put(5, idNodeMap.get(6));
        roomGate.put(55, idNodeMap.get(57));
        roomGate.put(56, idNodeMap.get(57));
        roomGate.put(45, idNodeMap.get(57));
        roomGate.put(46, idNodeMap.get(57));
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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class PathFinder implements Runnable {
	int count = 0;
	int [][]dir = {{1,0},{0,1},{-1,0},{0,-1}};
	Queue<Integer> q = new LinkedList<>();
//	private int [][] NewMap;
	private int [][]Mark;

	private static int[]exits;
	private static int cols = 10;
	private static int rows = 10;

	Map buildingMap;

	private ArrayList<Integer> route = new ArrayList<>();

	public PathFinder(Map map, ArrayList<Integer> nodesOnFire, int numberOfEntryAndExits ) {
		exits = new int[numberOfEntryAndExits];
		this.Mark = buildMark(map, nodesOnFire);
		this.buildingMap = map;
	}
//
	private static int[][] buildMark(Map map, ArrayList<Integer> nodesOnFire){
		int [][] newMap = new int[rows][cols];
		int count = 0;
		for(int i = 0; i< rows; i++) {
			for(int j = 0; j< cols; j++) {

				if(map.idNodeMap.get(i*cols+j+1).type.equals("cell") || map.idNodeMap.get(i*cols+j+1).type.equals("Room")){
					newMap[i][j] = -1;   // -1 -> A blocked cell like fire or wall
				}else if(map.idNodeMap.get(i*cols+j+1).type.equals("MainGate") || map.idNodeMap.get(i*cols+j+1).type.equals("Building Exit")){
					exits[count] = i*cols+j;
					count++;
				}
			}
		}

		for(int i=0; i< nodesOnFire.size(); i++){
			int temp = nodesOnFire.get(i)-1;
			int r = temp/cols;
			int c = temp%cols;

			newMap[r][c] = -1;
		}
		return newMap;
	}

	public boolean inBound(int i, int j) {	//judge if out of bound
		return(i>=0 && j>=0 && i<rows && j<cols);
	}

	public boolean findPoint(int a, int[] Source) {
		boolean found = false;
		for(int i = 0; i < Source.length; i++) {
			if(Source[i] == a)	found = true;
		}
		return found;
	}
	public int bfs(int start, int[] targets) {
		q.add(start);							//initialize queue with Target coordinate
		Mark[start/cols][start%cols] = 1;
		count = 0;								//initialize count to 1
		while(q.peek()!=null) {					//while the queue is not empty(has coordinate ready to be searched
			int i = q.peek()/cols;					//transfer coordinate to i,j
			int j = q.peek()%cols;
			int tempi, tempj;
			for(int t= 0;  t< 4; t++) {			//find adjacent, not out of bound coordinate
				tempi = i + dir[t][0];
				tempj = j + dir[t][1];
				if(inBound(tempi,tempj) && Mark[tempi][tempj] == 0) {

					Mark[tempi][tempj] = Mark[i][j]+1;	//mark with distance
					int exit = tempi*cols+tempj;
					if(findPoint(exit, targets))	return exit;
					q.add(exit);				//add coordinate to queue
				}
			}
			q.remove();
		}
		return -1;
	}

	public void findRoute(int start){
		start = start -1;
		int nearestExit = bfs(start, exits);
		if(nearestExit == -1) route = new ArrayList<>();
		int step = Mark[nearestExit/cols][nearestExit%cols];
		route.add(nearestExit+1);
		int curi = nearestExit/cols, curj = nearestExit%cols;
		int tempi, tempj;
		while(--step>0) {
			for(int t= 0;  t< 4; t++) {			//find adjacent, not out of bound coordinate
				tempi = curi + dir[t][0];
				tempj = curj + dir[t][1];
				if(inBound(tempi,tempj) && Mark[tempi][tempj] == step) {
					int newCor = tempi *cols +tempj;
					route.add(newCor+1);
					curi = tempi;
					curj = tempj;
					break;
				}
			}
		}
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		findRoute(57);
		boolean isReRouteNeeded = true;
		for(int i: route){
			if(!(buildingMap.idNodeMap.get(i).capacity == buildingMap.idNodeMap.get(i).numberOfPeople)){
				isReRouteNeeded = false;
			}
		}

		if(isReRouteNeeded){
			System.out.println("Rerouting ***");
			int index = route.get(0);
			buildingMap.idNodeMap.get(index).type = "cell";
			findRoute(57);
		}
	} 

	public ArrayList<Integer> getPath(){
		return route;
	}
	
}

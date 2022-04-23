import java.util.ArrayList;
import java.util.List;

public class Main implements Runnable{

    private static final int DEFAULT_SIZE = 512;
    private static final double ZOOM_CONSTANT = 2.0;
    private static final double EDGE = 0.5;

    private static final int EMPTY_VALUE = 0;
    private static final int BLOCKED_VALUE = 1;
    private static final int EXIT_VALUE = 2;
    private static final int PATH_VALUE = 3;
    private static final int BOTTON_VALUE_ADD_EXIT_MODE_ON = 8; // Deprecated
    private static final int BOTTON_VALUE_ADD_EXIT_MODE_OFF = 9; // Deprecated
    private static final int FIRE_VALUE = 4;
    private static final int SAFE_VALUE = 5;

    private static final int NUMBER_OF_EXIT = 3;
    private static final double EXIT_RADIUS = 1;

    private static final int EXTEND_SIZE = 10;

    private static boolean addExitMode = true;
    private static boolean fireMode = false;
    private static final int FIRE_RADIUS = 5;
    public static int[][] matrix;
    private Thread t;
    private String threadName;

    private static List<Node> nodes = new ArrayList<Node>();

    private static Node currentStarting;

    public Main(String name) {
        this.threadName = name;
    }
    private static void fillInCoordinates(int row, int col, int M, char shape) {
        if(shape == 's')
            Draw.filledSquare(col + 1 - EDGE, M - row -1 + EDGE, 0.5);
        else if(shape == 'c')
            Draw.filledCircle(col + 1 - EDGE, M - row -1 + EDGE, 0.4);
    }
    private static void initializeMatrix(int M, int N, int[][] matrix) {
        Draw.clear();
        Draw.setPenColor(Draw.GRAY);
        Draw.rectangle(N/2.0, M/2.0, N/2.0,M/2.0);
        for (int row = 0; row < M; row++) {
            for (int col = 0; col < N; col++) {
                if (matrix[row][col]==EMPTY_VALUE) {
                    // Empty
                    Draw.setPenColor(Draw.WHITE);
                    fillInCoordinates(row,col,M,'s');
                }
                else if (matrix[row][col]==BLOCKED_VALUE) {
                    // Barrier
                    Draw.setPenColor(Draw.BLACK);
                    fillInCoordinates(row,col,M,'s');
                }
                else if (matrix[row][col]==EXIT_VALUE || matrix[row][col]==BOTTON_VALUE_ADD_EXIT_MODE_ON){
                    // Exit
                    Draw.setPenColor(Draw.GREEN);
                    fillInCoordinates(row,col,M,'c');
                }
                else if(matrix[row][col]==BOTTON_VALUE_ADD_EXIT_MODE_OFF || matrix[row][col]==PATH_VALUE) {
                    Draw.setPenColor(Draw.ORANGE);
                    fillInCoordinates(row,col,M,'s');
                }
                else if(matrix[row][col]==FIRE_VALUE) {
                    Draw.setPenColor(Draw.RED);
                    fillInCoordinates(row,col,M,'s');
                }
                else if(matrix[row][col]==SAFE_VALUE) {
                    Draw.setPenColor(Draw.GREEN);
                    fillInCoordinates(row,col,M,'s');
                }
            }
        }
        for(Node each: nodes) {
            Draw.setPenColor(Draw.GREEN);
//			StdDraw.filledCircle(each.x + 1 - EDGE, M - each.y -1 + EDGE, EXIT_RADIUS);
            Draw.filledCircle(each.y + 1 - EDGE, M - each.x -1 + EDGE, EXIT_RADIUS);
        }
    }
    private static int[][] updateMatrixWithFire(int[][] matrix, int col, int row){
        for(int i=col-FIRE_RADIUS/2; i < col+FIRE_RADIUS/2; i++) {
            for(int j=row-FIRE_RADIUS/2; j < row+FIRE_RADIUS/2; j++)
                if(matrix[i][j]!=BLOCKED_VALUE) {
                    matrix[i][j] = FIRE_VALUE;
                }
        }
        return matrix;
    }

    private static int[][] removeAllPath(int[][] matrix){
        for(int i=0; i<matrix.length; i++) {
            for(int j=0; j<matrix[0].length; j++) {
                if(matrix[i][j]==PATH_VALUE)
                    matrix[i][j]=EMPTY_VALUE;
            }
        }
        return matrix;
    }
    private static int[][] updateMatrixWithBFS(int[][] matrix, int pressedCol,int pressedRow){
        Route bFS = new Route(matrix);
        Node targetExit = bFS.bfs(new Node(pressedCol,pressedRow), nodes.toArray(new Node[0]));
        if(targetExit!=null) {
            List<Node> path = bFS.findRoute(new Node(pressedCol,pressedRow), targetExit, bFS.Mark);
            for(Node each:path) {
                matrix[each.x][each.y] = PATH_VALUE;
            }
        }
        else
            System.out.println("Dead!");
        return matrix;
    }

    private static int[][] extendMatrix(int[][] matrix) {
        int length = matrix.length;
        int width = matrix[0].length;
        int[][] extendedMatrix = new int[length+EXTEND_SIZE][width+EXTEND_SIZE];

        // Copy original matrix
        for(int i=0; i<length; i++) {
            for(int j=0; j<width; j++) {
                extendedMatrix[i][j] = matrix[i][j];
            }
        }

        // Initialize new column and row with BOTTON_VALUE
        for(int i=0; i<length; i++){
            for(int j=width; j<width+EXTEND_SIZE;j++)
                extendedMatrix[i][j] = BLOCKED_VALUE;
        }
        for(int j=0; j<width; j++) {
            for(int i=length; i<length+EXTEND_SIZE;i++)
                extendedMatrix[i][j] = BLOCKED_VALUE;
        }
        for(int i=length; i<length+EXTEND_SIZE;i++) {
            for(int j=width; j<width+EXTEND_SIZE;j++) {
                extendedMatrix[i][j] = SAFE_VALUE;
            }
        }




        return extendedMatrix;

    }

    private static int[] getMouseCoordinates(int M, int N) {
        // screen coordinates
        double x = Draw.mouseX();
        double y = Draw.mouseY();
        //System.out.printf("Coordinate: (%f, %f)\n",x,y);

        // convert to row i, column j
        int i = (int) (M - Math.floor(y)-1);
        int j = (int) (Math.floor(x));


        return new int[] {i,j};
    }

    public static void visualize(int[][] matrix) {
        int M = matrix.length;
        int N = matrix[0].length;

        matrix = extendMatrix(matrix);
        M = matrix.length;
        N = matrix[0].length;

        Draw.show(0);
        Draw.setCanvasSize((int)(ZOOM_CONSTANT * DEFAULT_SIZE * N / Math.max(M,N)),
                (int)(ZOOM_CONSTANT * DEFAULT_SIZE * M / Math.max(M,N)));
        Draw.setXscale(-EDGE, N+EDGE);
        Draw.setYscale(-EDGE, M+EDGE);

        initializeMatrix(M,N,matrix);

        while(true) {
            if(Draw.mousePressed()) {
                initializeMatrix(M,N,matrix);
                int pressedCol = getMouseCoordinates(M,N)[0];
                int pressedRow = getMouseCoordinates(M,N)[1];

                if(pressedRow > N - EXTEND_SIZE && pressedCol > M - EXTEND_SIZE) {
                    if(fireMode) {
                        fireMode = false;
                        int length = matrix.length;
                        int width = matrix[0].length;
                        for(int i=length - EXTEND_SIZE; i<length;i++) {
                            for(int j=width - EXTEND_SIZE; j<width;j++) {
                                matrix[i][j] = SAFE_VALUE;
                            }
                        }
                    }
                    else {
                        fireMode = true;
                        int length = matrix.length;
                        int width = matrix[0].length;
                        for(int i=length - EXTEND_SIZE; i<length;i++) {
                            for(int j=width - EXTEND_SIZE; j<width;j++) {
                                matrix[i][j] = FIRE_VALUE;
                            }
                        }
                    }
                    initializeMatrix(M,N,matrix);
                }
                else {
                    try{
                        //Press exits
                        if(matrix[pressedCol][pressedRow]==EMPTY_VALUE || matrix[pressedCol][pressedRow]==PATH_VALUE) {
                            if(addExitMode) {
                                matrix[pressedCol][pressedRow] = EXIT_VALUE;
                                nodes.add(new Node(pressedCol,pressedRow));
                                if(nodes.size()>=NUMBER_OF_EXIT)
                                    addExitMode = false;
                            }
                            else if(fireMode){
                                matrix = updateMatrixWithFire(matrix, pressedCol, pressedRow);
                                matrix = removeAllPath(matrix);
                                matrix = updateMatrixWithBFS(matrix,currentStarting.x,currentStarting.y);
                                initializeMatrix(M,N,matrix);
                            }
                            else{
                                matrix = removeAllPath(matrix);
                                matrix = updateMatrixWithBFS(matrix,pressedCol,pressedRow);
                                currentStarting = new Node(pressedCol,pressedRow);
                            }
                        }
                    }
                    catch(IndexOutOfBoundsException e) {

                    }
                }
            }
            Draw.show(20);
        }
    }


    public static void main(String[] args) {

        int[][] matrix = {
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,1,0,0,1,0,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,0,1,0,0,1,0,0,1,0,0,1,0,0,0,1},
                {1,1,1,1,1,1,1,1,1,1,1,0,0,0,1,0,0,1,0,0,1,0,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,0,1,0,0,1,0,0,1,0,0,1,0,0,0,1},
                {1,1,1,1,1,1,1,1,1,1,1,0,0,0,1,0,0,1,0,0,1,0,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,0,1,0,0,1,0,0,1,0,0,1,0,0,0,1},
                {1,1,1,1,1,1,1,0,0,0,0,0,1,0,1,1,0,0,0,1,1,1,1,0,1,0,1,1,0,0,1,0,0,1,1,0,1,0,1,1,1,0,1,0,1,1,1,1,0,1,1,1,1,1,0,1,0,1,0,1},
                {1,1,1,1,1,1,1,0,0,0,0,0,1,1,1,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,0,0,0,1,1,1,1,1,1,1,1,1,0,1,1,1,1,1,1,0,1,1,1,1,1,0,1,1,1,1,1,1,0,1,1,1,1,1,0,1,1,1,1,0,1,0,0,0,1},
                {1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1},
                {1,1,1,1,1,1,1,0,0,0,0,1,1,0,1,0,1,0,0,1,0,1,1,1,0,1,1,1,1,1,1,0,1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1,1,0,1,0,1,0,0,1,0,0,0,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,1,1,1,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,1,0,1,0,0,1,0,0,1,0,0,1,0,1,0,1,0,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,0,0,0,1,0,0,1,1,0,1,1,1,0,0,1,0,1,0,1,1,1,1,0,0,0,0,1,0,1,0,1,1,1,1,1,1,0,0,1,0,1,0,0,1,0,1,0,1,0,0,0,0,1},
                {1,1,1,1,1,1,1,0,0,0,0,0,1,1,0,0,1,1,1,0,0,1,0,1,0,0,0,1,0,0,1,0,1,1,1,0,0,1,0,0,0,0,1,0,0,0,0,0,0,0,1,0,1,1,1,0,0,0,0,1},
                {1,1,1,1,1,1,1,1,1,1,0,0,0,1,0,0,1,1,1,0,0,1,0,1,0,0,0,1,0,0,1,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,0,0,0,0,0,1,1,1,1},
                {1,1,1,1,1,1,1,0,0,0,0,0,0,1,1,0,1,1,1,0,0,1,0,1,0,0,0,1,0,0,1,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,1,1,0,0,1,0,0,0,0,1},
                {1,1,1,1,1,1,1,0,0,0,0,1,0,1,1,0,1,1,1,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,1,0,0,1,0,0,0,0,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,1,1,1,1,1,1},
                {1,1,1,1,1,1,0,0,0,0,1,1,0,1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,1,0,0,0,0,0,0,1},
                {1,1,1,1,1,1,0,0,0,0,1,1,0,1,1,1,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,1,0,0,0,1,0,0,0,1},
                {1,1,1,1,1,1,0,0,1,0,0,1,0,1,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,1,1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,0,0,0,1,0,1,0,1,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,0,0,0,1,1,1,0,1,1,1,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,0,0,0,1,0,1,0,1,0,1,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,0,1,0,1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,0,0,0,0,0,1,0,1,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,0,0,0,1,1,1,0,1,0,1,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,0,0,0,1,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,0,0,0,1,1,1,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,0,0,0,0,0,0,0,0,0,0,0,1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,0,0,0,1,1,1,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,0,1,1,0,0,1,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,0,0,0,0,0,0,1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,0,0,1,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,0,1,0,0,1,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,0,0,0,1,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
        };
        visualize(matrix);
    }

    @Override
    public void run() {
        visualize(matrix);
    }

}


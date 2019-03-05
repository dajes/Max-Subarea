import java.util.Random;

public class GridWorld {
    public String [][]matrix;
    public int [][] computing;
    int M, N;
    String obstacleTree = "t", obstacleBuilding = "b", freeCell = "_", selectedCell = "s";
    int treeFreq = 10, buildFreq = 10, freeFreq = 80;
    private boolean vertical;
    GridWorld(int M, int N, int treeFreq, int buildFreq, int freeFreq){
        //initializing frequencies of appearance each type of cell
        this.treeFreq = treeFreq;
        this.buildFreq = buildFreq;
        this.freeFreq = freeFreq;


        //initializing matrix with random values
        Random r = new Random();
        this.matrix = new String[M][N];
        //on the fly creating 0 and 1 matrix
        computing = new int[M][N];
        this.M = M;
        this.N = N;
        for(int i = 0; i < M; i++){
            for(int j = 0; j < N; j++){
                int rand = r.nextInt(treeFreq + buildFreq + freeFreq);
                if(rand < treeFreq){
                    matrix[i][j] = obstacleTree;
                } else if(rand - treeFreq < buildFreq){
                    matrix[i][j] = obstacleBuilding;
                } else {
                    matrix[i][j] = freeCell;
                }
                computing[i][j] = matrix[i][j].equals(freeCell) ? 1 : 0;

            }
        }
        //marking cells that touch 0 but are not 0 as 2
        for(int i = 0; i < M; i++){
            for(int j = 0; j < N; j++){
                if(computing[i][j]==0){
                    try{if(computing[i][j+1] != 0)computing[i][j+1] = 2;} catch (Exception e){};
                    try{if(computing[i+1][j] != 0)computing[i+1][j] = 2;} catch (Exception e){};
                    try{if(computing[i][j-1] != 0)computing[i][j-1] = 2;} catch (Exception e){};
                    try{if(computing[i-1][j] != 0)computing[i-1][j] = 2;} catch (Exception e){};
                }
            }
        }
        //replacing 2 with 0
        for(int i = 0; i < M; i++){
            for(int j = 0; j < N; j++){
                if(computing[i][j]==2){
                    computing[i][j]=0;
                }
            }
        }
        this.vertical = N <= M;
    }
    //method to mark our area with a symbol of selected cell
    private void markArea(int i1, int j1, int i2, int j2){
        for(int i = i1; i<=i2; i++){
            for(int j = j1; j<=j2; j++){
                matrix[i][j] = selectedCell;
            }
        }
    }
    public String toString(){
        StringBuilder s = new StringBuilder();
        for(int i = 0; i<M; i++){
            for(int j = 0; j<N; j++){
                s.append(matrix[i][j]);
            }
            s.append("\n");
        }
        return s.toString();
    }
    //method for finding the answer
    public int calculate(){
        //initial values are impossibly small
        int maxArea = -1;
        int i1 = -1, j1 = -1, i2 = -1, j2 = -1;
        //choose which is more optimal: columns or rows
        if(vertical){
            //column counter
            int[] temp = new int[N];
            //init with zeros
            for(int i = 0; i < N; i++) temp[i] = 0;
            //go through all the rows
            for(int i = 0; i < M; i++){
                //go through each element of the row
                for(int j = 0; j < N; j++){
                    //if it is 1, then increment the element in the counter, else make it 0
                    temp[j] = computing[i][j] == 1 ? temp[j]+1 : 0;
                }
                //find the max area in our histogram and horizontal indexes of it's start and finish (j1, j2)
                int[] response = new MaxHistogram(temp).maxArea();
                int area = response[0], start = response[1], end = response[2];

                //if result become better
                if(area > maxArea){
                    maxArea = area;
                    j1 = start;
                    //calculate height of the area which is equal to the min value of subhistogram which we have
                    //marked with horizontal borders
                    int min = temp[end];
                    for(int k = start; k < end; k++){
                        if(min > temp[k]) min = temp[k];
                    }
                    i1 = i-min + 1;
                    j2 = end;
                    i2 = i;
                }
            }
        } else {
            //row counter
            int[] temp = new int[M];
            //init with zeros
            for(int i = 0; i < M; i++) temp[i] = 0;
            //go through all the columns
            for(int i = 0; i < N; i++){

                //go through each element of the column
                for(int j = 0; j < M; j++){
                    //if it is 1, then increment the element in the counter, else make it 0
                    temp[j] = computing[j][i] == 1 ? temp[j]+1 : 0;
                }
                //find the max area in our histogram and vertical indexes of it's start and finish (i1, i2)
                int[] response = new MaxHistogram(temp).maxArea();
                int area = response[0], start = response[1], end = response[2];

                //if result become better
                if(area > maxArea){
                    maxArea = area;
                    i1 = start;
                    //calculate height of the area which is equal to the min value of subhistogram which we have
                    //marked with vertical borders
                    int min = temp[end];
                    for(int k = start; k < end; k++){
                        if(min > temp[k]) min = temp[k];
                    }
                    j1 = i-min + 1;
                    i2 = end;
                    j2 = i;
                }
            }
        }
        //paint our selected rectangle on the matrix
        markArea(i1, j1, i2, j2);
        return maxArea;
    }
}

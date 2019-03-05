import java.util.Stack;

public class MaxHistogram {
    //histogram represented in an array
    private int[] array;
    //stack of indexes
    private Stack<Integer> stack;
    MaxHistogram(int [] array){
        stack = new Stack<>();
        this.array = array;
    }
    //if with no parameters set the parameter to true
    public int[] maxArea(){
        return maxArea(true);
    }
    //calculate maxArea and it's borders on histogram
    public int[] maxArea(boolean silent){
        //impossibly small init values
        int max = -1;
        int start = -1, end = -1;
        //for each element of histogram
        for(int i = 0; i < array.length; i++){
            // if stack has indexes
            if(!stack.isEmpty()){
                // if top index element less or equal than the current element
                if(array[stack.peek()] <= array[i]){
                    // add current index to the stack
                    stack.push(i);
                } else {
                    // until stack is not empty or top index element is less or equal than the current one
                    while (!stack.isEmpty() && array[stack.peek()] > array[i]){
                        int top = stack.pop();
                        // length of rectangle
                        // if stack is empty, length is whole histogram until the current point
                        // else length is difference between current point and previous one which is in stack
                        int length = stack.isEmpty() ? i : (i - 1 - stack.peek());
                        // area of rect = height * width
                        int area = array[top] * length;
                        if(area > max){
                            max = area;
                            start = !stack.isEmpty() ? stack.peek() + 1 : 0;
                            end = i-1;
                        }
                    }
                    // add the current index to stack
                    stack.push(i);
                }
            }
            // if stack has not indexes
            else {
                // push one in here
                stack.push(i);
            }
        }
        // empty the stack of indexes
        while (!stack.isEmpty()){
            int top = stack.pop();
            // length of rectangle
            // if stack is empty, length is whole histogram's length
            // else length is difference between current point and previous one which is in stack
            int length = stack.isEmpty() ? array.length : (array.length - 1 - stack.peek());
            int area = array[top] * length;
            if(area > max){
                max = area;
                start = stack.isEmpty() ? 0 : stack.peek() + 1;
                end = array.length-1;
            }
        }
        //if chosen not to be silent, result indexes is printed to the console
        if(!silent)
            System.out.println("[" + start + ":" + end + "]");
        // return array of values: max area, it's start and end on the histogram
        return new int[]{max, start, end};
    }
}

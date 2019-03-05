# Max-Subarea
Project with visualisation about finding the biggest rectangle avoiding obstacles on a given matrix MxN.

You are given a matrix with obstacles: trees (green) and buildings (grey). The task is to find a place where you can build another building (cyan) so that it is of the maximal area but do not touches the obstacles.

Algorithm's time complexity is O(N * M) and space complexity O(min(N, M)).

Algorithm:
1) We are creating a matrix of 0 and 1 from our initial matrix with different types of obstacles in which 0 is an obstacle and 1 is not.
2) We are marking the area that touches 0 elements as 2 and then replace all the 2 with 0, so we can "touch" obstacles with our area. Now are solving the usual problem of finding the biggest ones submatrix.
3) For the sake of decreasing space complexity, we choose what is more effective to store: column or row.
4) Depending on what we have chosen on step 3, we create an array of length of either a column or a row and put zeros in it.
5) We go through all of our columns/rows and do the following:
If an element of a column/row is 1, we increment the corresponding element of our array.
Else if it is 0, we make the corresponding element of our array equals zero.
6) With this method, we get a bunch of histograms while proceeding through the matrix's rows/columns which indicate the possibility of each element to expand up. So now all we have to do is check maximal possible rectangle area through all of the histograms and then find the biggest value of this local maximums.
7) As we are replacing the current max with the new one, we also calculate which 2 points form this area so after checking all of the histograms we have not only our max area but 2 points which form that rectangle.

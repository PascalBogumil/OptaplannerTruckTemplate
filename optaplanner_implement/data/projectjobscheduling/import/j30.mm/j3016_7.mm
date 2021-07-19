************************************************************************
file with basedata            : mf16_.bas
initial value random generator: 1433523421
************************************************************************
projects                      :  1
jobs (incl. supersource/sink ):  32
horizon                       :  227
RESOURCES
  - renewable                 :  2   R
  - nonrenewable              :  2   N
  - doubly constrained        :  0   D
************************************************************************
PROJECT INFORMATION:
pronr.  #jobs rel.date duedate tardcost  MPM-Time
    1     30      0       22       11       22
************************************************************************
PRECEDENCE RELATIONS:
jobnr.    #modes  #successors   successors
   1        1          3           2   3   4
   2        3          3           5  16  22
   3        3          3           7   8  10
   4        3          2           6   7
   5        3          1          19
   6        3          3          20  28  29
   7        3          1          18
   8        3          3           9  13  17
   9        3          2          12  14
  10        3          3          11  24  29
  11        3          2          17  28
  12        3          1          15
  13        3          3          20  22  27
  14        3          1          24
  15        3          1          21
  16        3          2          23  27
  17        3          2          20  23
  18        3          2          19  21
  19        3          2          24  25
  20        3          1          26
  21        3          3          23  25  31
  22        3          2          25  28
  23        3          1          26
  24        3          2          27  30
  25        3          2          26  29
  26        3          1          30
  27        3          1          31
  28        3          2          30  31
  29        3          1          32
  30        3          1          32
  31        3          1          32
  32        1          0        
************************************************************************
REQUESTS/DURATIONS:
jobnr. mode duration  R 1  R 2  N 1  N 2
------------------------------------------------------------------------
  1      1     0       0    0    0    0
  2      1     4       8   10    0   10
         2     6       8   10    0    5
         3     8       6    9    9    0
  3      1     2       2    4    6    0
         2     7       2    3    5    0
         3     7       2    3    0    7
  4      1     1       8    4    3    0
         2     3       4    3    3    0
         3     3       5    4    0    8
  5      1     1       2    3    0    5
         2     4       1    3    0    3
         3    10       1    3    4    0
  6      1     7       6    3    4    0
         2     9       5    3    3    0
         3    10       3    2    2    0
  7      1     2       9    5    0    7
         2     8       8    5    0    5
         3     9       8    5    9    0
  8      1     1       6    3    3    0
         2     4       4    2    0    4
         3     4       3    3    3    0
  9      1     1       2    9    4    0
         2     6       2    8    0    5
         3     8       2    6    4    0
 10      1     1       8    7   10    0
         2     5       7    7    8    0
         3     6       6    6    8    0
 11      1     2       8    5    6    0
         2     6       6    3    0    8
         3     8       4    3    3    0
 12      1     2      10    6    0    6
         2     5       9    6   10    0
         3     6       9    2    7    0
 13      1     1       5    5    0    6
         2     2       4    5    0    6
         3     6       4    4    0    6
 14      1     3       9    4    0    5
         2     6       8    2    9    0
         3     8       8    1    0    4
 15      1     1       6    4    3    0
         2     1       6    4    0    8
         3     8       3    3    0    6
 16      1     3       6   10    7    0
         2     4       5    9    0    7
         3     8       5    8    0    7
 17      1     6       6    7    6    0
         2     9       5    7    0    7
         3    10       4    4    0    5
 18      1     2       7    5    6    0
         2     3       4    3    0   10
         3     6       2    3    5    0
 19      1     6       5    5    0    5
         2     7       5    4    8    0
         3     9       5    3    0    2
 20      1     2      10    8    0    8
         2     3       9    6    0    7
         3     8       8    2    4    0
 21      1     1       5    5    7    0
         2     6       3    2    0   10
         3     6       4    3    6    0
 22      1     6       5   10    5    0
         2     7       3    9    0    8
         3     9       1    9    0    8
 23      1     2       9    7    5    0
         2     3       9    7    0    3
         3     6       9    5    3    0
 24      1     2       5    7    0    9
         2     7       3    6    0    5
         3    10       2    2    8    0
 25      1     2       9    5    6    0
         2     4       8    4    0   10
         3     9       7    2    0    2
 26      1     1       8    5    6    0
         2     2       5    2    4    0
         3     5       3    1    3    0
 27      1     1       9    4    8    0
         2     6       8    3    0    3
         3     7       6    3    8    0
 28      1     4       7    6    0    1
         2     5       5    6    4    0
         3     9       3    5    2    0
 29      1     4      10    5    2    0
         2     5      10    4    2    0
         3    10       9    4    0    9
 30      1     7       7    9    0   10
         2     7       8    9    5    0
         3    10       5    8    0   10
 31      1     1       9    6    0    9
         2     2       9    6    0    7
         3     4       9    5    6    0
 32      1     0       0    0    0    0
************************************************************************
RESOURCEAVAILABILITIES:
  R 1  R 2  N 1  N 2
   53   46   93   97
************************************************************************
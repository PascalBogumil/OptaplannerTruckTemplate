************************************************************************
file with basedata            : mf50_.bas
initial value random generator: 1554693730
************************************************************************
projects                      :  1
jobs (incl. supersource/sink ):  32
horizon                       :  255
RESOURCES
  - renewable                 :  2   R
  - nonrenewable              :  2   N
  - doubly constrained        :  0   D
************************************************************************
PROJECT INFORMATION:
pronr.  #jobs rel.date duedate tardcost  MPM-Time
    1     30      0       34       23       34
************************************************************************
PRECEDENCE RELATIONS:
jobnr.    #modes  #successors   successors
   1        1          3           2   3   4
   2        3          3           5   7  16
   3        3          3           6  17  18
   4        3          3           7  14  21
   5        3          3           8   9  10
   6        3          2          11  21
   7        3          1          15
   8        3          2          12  22
   9        3          2          11  14
  10        3          2          27  31
  11        3          2          13  28
  12        3          3          25  28  31
  13        3          3          19  23  30
  14        3          2          19  27
  15        3          2          19  22
  16        3          1          20
  17        3          3          20  24  30
  18        3          1          26
  19        3          2          20  24
  20        3          1          25
  21        3          2          23  28
  22        3          2          26  27
  23        3          1          24
  24        3          2          25  26
  25        3          1          29
  26        3          1          31
  27        3          1          29
  28        3          1          30
  29        3          1          32
  30        3          1          32
  31        3          1          32
  32        1          0        
************************************************************************
REQUESTS/DURATIONS:
jobnr. mode duration  R 1  R 2  N 1  N 2
------------------------------------------------------------------------
  1      1     0       0    0    0    0
  2      1     8       8    0    8    6
         2     9       0    8    6    5
         3    10       0    8    5    3
  3      1     5       2    0    6    3
         2     5       0    6    6    3
         3    10       2    0    5    3
  4      1     4       0    6    7    5
         2     4       5    0    7    5
         3     9       0    7    7    4
  5      1     6       0    5    8    5
         2     9       9    0    8    2
         3     9       0    5    7    3
  6      1     3       0    3    4    9
         2     6       0    2    3    8
         3     7       0    2    1    7
  7      1     7       0    8    5    4
         2     8       0    2    4    4
         3     8       8    0    3    4
  8      1     4       0   10    3    7
         2     7       0   10    2    7
         3     9       1    0    2    6
  9      1     4       0    2    8   10
         2     9       6    0    3   10
         3     9       6    0    4    9
 10      1     4       0    5    3    2
         2     6       5    0    3    2
         3     8       3    0    3    1
 11      1     2       9    0    5    9
         2     2       0    6    4    9
         3     5       9    0    4    9
 12      1     1       9    0    3    9
         2     5       0    8    2    7
         3     7       0    6    2    5
 13      1     2       7    0   10   10
         2     7       0    3    9   10
         3    10       3    0    7   10
 14      1     4       0    5    7   10
         2     5       0    1    6    8
         3    10       7    0    3    8
 15      1     2       1    0    9    6
         2     3       0    6    9    6
         3     5       0    6    8    6
 16      1     3       0   10    9    5
         2     4       0    9    6    4
         3    10       6    0    5    2
 17      1     8       0    6    6    9
         2     9       0    5    6    6
         3    10       0    3    6    4
 18      1     5       0    8    4    9
         2     7       0    5    3    9
         3    10       0    3    2    9
 19      1     3       9    0    9    7
         2     5       5    0    9    4
         3    10       0    7    8    4
 20      1     3       2    0    8    8
         2     5       2    0    6    7
         3     7       0    3    4    5
 21      1     2       0    6    5    7
         2     6       0    5    5    6
         3     9       0    4    5    5
 22      1     3       8    0    6    3
         2     7       5    0    3    2
         3     9       4    0    2    2
 23      1     2       3    0    6   10
         2     3       2    0    5   10
         3     7       0    7    5   10
 24      1     2       7    0   10    8
         2     3       6    0    9    4
         3     9       3    0    7    2
 25      1     4       6    0    4    9
         2    10       3    0    4    7
         3    10       1    0    4    8
 26      1     2       8    0    6    5
         2     2       0    3    7    5
         3     6       0    2    6    2
 27      1     1       0    9    5    5
         2     7       3    0    3    4
         3     7       0    9    3    4
 28      1     4       0    3    8    7
         2     6       7    0    4    6
         3     7       5    0    1    6
 29      1     2       7    0    6    8
         2     6       6    0    6    8
         3    10       0    1    5    8
 30      1     3       0    6    8    3
         2     7       8    0    4    2
         3     9       0    2    2    2
 31      1     5       0    5    4   10
         2     9       5    0    4    8
         3     9       4    0    4    9
 32      1     0       0    0    0    0
************************************************************************
RESOURCEAVAILABILITIES:
  R 1  R 2  N 1  N 2
   19   24  176  195
************************************************************************

************************************************************************
file with basedata            : md329_.bas
initial value random generator: 1795401344
************************************************************************
projects                      :  1
jobs (incl. supersource/sink ):  22
horizon                       :  159
RESOURCES
  - renewable                 :  2   R
  - nonrenewable              :  2   N
  - doubly constrained        :  0   D
************************************************************************
PROJECT INFORMATION:
pronr.  #jobs rel.date duedate tardcost  MPM-Time
    1     20      0       27        0       27
************************************************************************
PRECEDENCE RELATIONS:
jobnr.    #modes  #successors   successors
   1        1          3           2   3   4
   2        3          3           5   9  10
   3        3          2           7  12
   4        3          3           6  10  17
   5        3          2          17  19
   6        3          3           7  11  13
   7        3          2           8   9
   8        3          3          14  15  20
   9        3          1          16
  10        3          2          11  16
  11        3          1          14
  12        3          3          15  18  20
  13        3          1          14
  14        3          2          18  21
  15        3          2          19  21
  16        3          2          18  21
  17        3          1          20
  18        3          1          19
  19        3          1          22
  20        3          1          22
  21        3          1          22
  22        1          0        
************************************************************************
REQUESTS/DURATIONS:
jobnr. mode duration  R 1  R 2  N 1  N 2
------------------------------------------------------------------------
  1      1     0       0    0    0    0
  2      1     1       0    6    0    5
         2     1       1    0    0    5
         3     6       0    6    2    0
  3      1     3       6    0    0    9
         2     6       0   10    0    4
         3     8       0    7    5    0
  4      1     1       9    0    0    7
         2     2       0    8    0    6
         3     9       8    0    0    5
  5      1     1       1    0    8    0
         2     7       0    4    0    3
         3     8       0    3    4    0
  6      1     2      10    0    7    0
         2     5       6    0    0    7
         3     5       6    0    7    0
  7      1     4       5    0    9    0
         2     7       5    0    8    0
         3     8       0    4    8    0
  8      1     6       5    0    0    3
         2     8       5    0    4    0
         3     9       2    0    3    0
  9      1     1       0    7    9    0
         2     1       7    0    0    6
         3     8       7    0    9    0
 10      1     3       0    8    8    0
         2     5       1    0    0    2
         3     5       0    4    0    5
 11      1     7       5    0    5    0
         2     8       0    8    0    7
         3    10       3    0    2    0
 12      1     3       8    0    4    0
         2     7       7    0    4    0
         3     9       7    0    3    0
 13      1     6      10    0    0    4
         2     8       6    0    9    0
         3    10       3    0    9    0
 14      1     4       0    4   10    0
         2     4       0    5    0    6
         3     5       5    0    0    2
 15      1     1       3    0    7    0
         2     4       3    0    0    2
         3     9       0    9    6    0
 16      1     2       3    0    6    0
         2     4       0    2    0    6
         3    10       3    0    0    5
 17      1     2       0    8    9    0
         2     5       0    2    0    4
         3     8       3    0    0    3
 18      1     6       0    6    0    7
         2     7      10    0    0    6
         3     9       8    0    0    4
 19      1     4       7    0    0    7
         2     7       7    0    0    6
         3     8       2    0    0    2
 20      1     4       0    6    6    0
         2     5       0    2    0    3
         3     9       9    0    0    1
 21      1     1       0    6    5    0
         2     3       4    0    0    4
         3     6       0    3    0    4
 22      1     0       0    0    0    0
************************************************************************
RESOURCEAVAILABILITIES:
  R 1  R 2  N 1  N 2
   13    6   62   53
************************************************************************

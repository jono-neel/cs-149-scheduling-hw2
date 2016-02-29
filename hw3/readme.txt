HW3 README

TODO REQUIREMENTS
-(Matthew)medium-ticket seats need to go 5, 6, 4, 7, 3, 8, 2, 9, 1
  notes to Matthew: uncomment/comment 25/26
-optional: clean up code
-EC: customer leaves after 10 seconds

RUNNING PROGRAM
-Need to have C++ compiler installed on computer.
-Suggested to compile and run on Linux via terminal since pthread library is included.
-If not on Linux, need to figure out how to include pthread library.
  Notes: Code works on ubuntu, but on MAC it may run unexpectedly.

INSTALLING GNU C/C++ COMPILER ON LINUX
https://help.ubuntu.com/community/InstallingCompilers

RUN ON LINUX VIA TERMINAL
compile: g++ -pthreads main.cpp -w (-w will ignore warnings)
run: ./a.out N (N = number of customers)

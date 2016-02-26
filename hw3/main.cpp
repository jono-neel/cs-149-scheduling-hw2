#include <iostream>
#include <ctime>
#include <stdlib.h>
#include <queue>
#include <pthread.h>

#define CHART_SIZE 10
#define N_CUSTOMER 10
#define MIN_H_RANGE 1
#define MAX_H_RANGE 2
#define MAX_M_RANGE 4
#define MAX_L_RANGE 7

using namespace std;
enum SellerType { H = 72, M = 77, L = 76 };

class Seller;
//class Customer;
class Seat;

class Customer
{
    public:
        string name;
        Seller *seller;

    public:
        Customer()
        {
            name = "";
            seller = NULL;
        }
        Customer(Seller *s)
        {
            name = "";
            seller = s;
        }
        string getName()
        {
            if(name == "")
                return "N/A";
            else
                return name;
        }

        string getSellerInfo()
        {
            if(!seller)
              return "N/A";
            else
               return "";
        }
};

class Seat
{
    private:
        bool isAvailable;
        Customer *customer;

    public:
        Seat()
        {
            isAvailable = false;
            customer = NULL;
        };

        void setAvailable(){};
        void getAvailable(){};
        void print()
        {
            if (!customer)
                cout << "---";
            else
                cout << customer->getSellerInfo();
        }
};

class Seller
{
    private:
        string name;
        SellerType type;
        queue<Customer> customerQueue;

    public:
        Seller()
        {
            name = "";
        }
        Seller(int n, SellerType t)
        {
            name = (char)t;
            name += (char)(n + 48);
            type = t;
        }

        int generateWaitTime()
        {
            switch(type)
            {
                case H : return (rand() % (MAX_H_RANGE - MIN_H_RANGE + 1) + MIN_H_RANGE);
                //(rand() % (max - min + 1) + min) should give a value in range of max - min
                case M : return (rand() % (MAX_M_RANGE - MAX_H_RANGE + 1) + MAX_H_RANGE);
                case L : return (rand() % (MAX_L_RANGE - MAX_M_RANGE + 1) + MAX_M_RANGE);
            };
        };

        string getInfo()
        {
            return name;
        }
};

// main() is where program execution begins.
int main()
{
    //HARDCODE TESTING
    Seller s1(1, H);
    Seller s2(1, L);
    Seller s3(1, M);


    pthread_t thread1, thread2;

    Customer cusArr[10];
    cusArr[0].name = "A";
    cusArr[1].name = "B";
    cusArr[2].name = "C";
    cusArr[3].name = "D";
    cusArr[4].name = "E";

    // set the seed
    srand((unsigned)time(NULL));


    // create 10 by 10 seat chart
    Seat seatChart[CHART_SIZE][CHART_SIZE];
    for (int i = 0; i < CHART_SIZE; i++)
    {
        for (int j = 0; j < CHART_SIZE; j++)
        {
            seatChart[i][j].print();
            cout << " | ";
        }

        cout << endl;
    }


    return 0;
}

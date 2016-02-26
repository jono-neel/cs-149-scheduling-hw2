#include <iostream>
#include <ctime>
#include <stdlib.h>
#include <vector>
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
class Seat;

class Customer
{
    private:
        string name;
        Seller *seller;

    public:
        Customer();
        Customer(Seller *s);
        
        string getName();
        string getSellerInfo();
};


class Seat
{
    private:
        bool isAvailable;
        Customer *customer;

    public:
        Seat();

        void setAvailable();
        bool getAvailable();
        void setCustomer(Customer* pCus);
        Customer* getCustomer();
        void print();
};

class Seller
{
    private:
        string name;
        SellerType type;
        vector<Customer> customerQueue;

    public:
        Seller();
        Seller(int n, SellerType t);

        int generateWaitTime();
        string getInfo();
};

Customer::Customer()
{
    name = "";
    seller = NULL;
}

Customer::Customer(Seller *s)
{
    name = "";
    seller = s;
}

string Customer::getName()
{
    if(name == "")
        return "N/A";
    else
        return name;
}

string Customer::getSellerInfo()
{
    if(!seller)
      return "N/A";
    else
       return seller->getInfo();
}

Seat::Seat()
{
    isAvailable = false;
    customer = NULL;
};

void Seat::setAvailable()
{
    isAvailable = false;
};

bool Seat::getAvailable(){
    return isAvailable;
};

void Seat::setCustomer(Customer* pCus)
{
    customer = pCus;
}

Customer* Seat::getCustomer()
{
    return customer;
}

void Seat::print()
{
    if (!customer)
        cout << "---";
    else
        cout << customer->getSellerInfo();
}


Seller::Seller()
{
    name = "";
}

Seller::Seller(int n, SellerType t)
{
    name = (char)t;
    name += (char)(n + 48);
    type = t;
}

int Seller::generateWaitTime()
{
    switch(type)
    {
        //(rand() % (max - min + 1) + min) should give a value in range of max - min
        case H : return (rand() % (MAX_H_RANGE - MIN_H_RANGE + 1) + MIN_H_RANGE);
        case M : return (rand() % (MAX_M_RANGE - MAX_H_RANGE + 1) + MAX_H_RANGE);
        case L : return (rand() % (MAX_L_RANGE - MAX_M_RANGE + 1) + MAX_M_RANGE);
    };
};

string Seller::getInfo()
{
    return name;
}


// main() is where program execution begins.
int main()
{
    //HARDCODE TESTING
    Seller s1(1, H);
    Seller s2(1, L);
    Seller s3(1, M);

    pthread_t thread1, thread2;

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

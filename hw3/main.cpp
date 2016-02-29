#include <iostream>
#include <ctime>
#include <stdlib.h>
#include <vector>
#include <cstdlib>
#include <pthread.h>
#include <unistd.h>
#include <sstream> // for stringstream
// GLOBAL CONSTANTS
#define SIM_TIME 15
#define CHART_SIZE 100
#define NUM_SELLERS 10
#define MIN_H_RANGE 1
#define MAX_H_RANGE 2
#define MAX_M_RANGE 4
#define MAX_L_RANGE 7
#define SIM_PROCESSING_TIME 20 // real time multiplier
// NAMESPACE
using namespace std;
// GLOBAL VARIABLES
enum SellerType { H = 72, M = 77, L = 76 }; // ascii code
int times = 0;
bool SHOULD_EXIST =  true;
pthread_mutex_t mutexx;
//SellerType types[10] = {H, M, M, M, L, L, L, L, L, L};
SellerType types[10] = {H, H, H, H, L, L, L, L, L, L};
int ids[10] = {1, 1, 2, 3, 1, 2, 3, 4, 5, 6};
int num_customers = 0;
// FORWARD DECLARATIONS
class Seller;
class Seat;
class SeatManager;
// CLASS DECLARATIONS BEGIN------------------------------------------
class Customer
{
    private:
        string name;
        Seller *seller;
        SellerType type;
        int seatLocation;
    public:
        Customer();
        Customer(Seller *s);
        
        string getName();
        string getSellerInfo();
        void setSeller(Seller *s);
        void setName(string s);
        SellerType getWantedTicket();
        void setSeatLocation(int n);
};

class Seat
{
    private:
        bool isAvailable;
        Customer *customer;
    public:
        Seat();

        void book(Customer *pCus);
        bool getAvailable();
        void setCustomer(Customer* pCus);
        Customer* getCustomer();
        void print();
};

class Seller
{
    private:
        string name;
        int id;
        SellerType type;
        vector<Customer *> customerQueue;
        SeatManager *sm;
        int total_customers;
        bool closed;
    public:
        Seller();
        Seller(int n, SellerType t, SeatManager *s);

        int generateWaitTime();
        string getInfo();
        void setID(int id);
        void setSellerType(SellerType st);
        void setSeatManager(SeatManager *s);
        bool process();
        void enqueue(Customer *c);
        SellerType getSellerType();
        bool isClosed();
        bool isFull();
        bool isSeatManagerFull();
};

class SeatManager
{
    private:    
        Seat seatChart[CHART_SIZE];
        int availableSeats = CHART_SIZE;
        int tracker_H = 0;
        int tracker_M = CHART_SIZE / 2;
        int tracker_L = CHART_SIZE - 1;
    public:
        SeatManager();
        bool bookTicket(Customer *cPtr);
        void print();
        bool isFull();
};
// CLASS DECLARATIONS END--------------------------------------------
// CLASS DEFINITIONS BEGIN-------------------------------------------
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
    string temp = seller->getInfo();
    int printLocation = seatLocation % 10;
    temp += "0";
    temp += (char)(printLocation + 48);
    return temp;
}

string Customer::getSellerInfo()
{
    if(!seller)
      return "N/A";
    else
       return seller->getInfo();
}

void Customer::setSeller(Seller *s)
{
    seller = s;
}

void Customer::setName(string s)
{
    name = s;
}

SellerType Customer::getWantedTicket()
{
    return seller->getSellerType();
}

void Customer::setSeatLocation(int n)
{
    seatLocation = n;
}

Seat::Seat()
{
    isAvailable = true;
    customer = NULL;
};

void Seat::book(Customer *pCus)
{
    isAvailable = !isAvailable;
    customer = pCus;
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
    //cout << "Noooo\n" ;
    if (isAvailable)
        cout << "----";
    else
        cout << customer->getName();
}


Seller::Seller()
{
    name = "";
    closed = false;
}

Seller::Seller(int n, SellerType t, SeatManager *s)
{
    name = (char)t;
    name += (char)(n + 48);
    type = t;
    sm = s;
    closed = false;
}

SellerType Seller::getSellerType()
{
    return type;
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
    char c1 = (char)type;
    char c2 =  (char)(id + 48);
    string temp = "TT";
    temp[0] = c1;
    temp[1] = c2;
    //cout << "Testing inside Sller::getInfo() : " << c1 << "," << c2 << " - "<< temp << endl;
    return temp;
}

void Seller::setID(int id)
{
    this->id = id;
}

void Seller::setSellerType(SellerType st)
{
    type = st;
    name = (char)st;
    name += (char)(id + 48);
}

void Seller::setSeatManager(SeatManager *s)
{
    sm = s;
}

bool Seller::process()
{
    if (customerQueue.empty())
    {
        return false;
    }

    sleep(generateWaitTime() / (float)(SIM_PROCESSING_TIME));
    // Wait on the "emptySlots" semaphore.
    //sem_wait(&emptySlots);

    // Acquire the mutex lock to protect the buffer.
    pthread_mutex_lock(&mutexx);
    if (sm->isFull())
    {
        cout << "Seller " << this->getInfo() << " told customer concert is full." << endl;
        pthread_mutex_unlock(&mutexx);
        return false;
    }
    else
    {
        // Critical region: Insert an item into the buffer.
        sm->bookTicket(customerQueue.front());
        customerQueue.erase(customerQueue.begin());

        cout << "Seller " << this->getInfo() << " assigned seat to customer." << endl;
        // Release the mutex lock.
        pthread_mutex_unlock(&mutexx);
        return true;
    }
    
    // Signal the "filledSlots" semaphore.
    //sem_post(&filledSlots);  // signal
}

void Seller::enqueue(Customer *c)
{
    customerQueue.push_back(c);
    //total_customers++;
}

bool Seller::isClosed()
{
    return closed;
}

bool Seller::isFull()
{
    return customerQueue.size() >= num_customers;
}

bool Seller::isSeatManagerFull()
{
    return sm->isFull();
}

SeatManager::SeatManager()
{
    // create 100 seats
    for (int i = 0; i < CHART_SIZE; i++)
    {
        if (i % 10 == 0) // creates matrix
        {
            cout << endl;
        }
        seatChart[i].print();
        cout << " | ";
        
    }

    cout << endl;
}

bool SeatManager::bookTicket(Customer *cPtr)
{
    if (availableSeats <= 0)
    {
        cout << "=============================================PPP " << availableSeats << endl;
        return false;
    }
    cout << "=============================================SSS " << availableSeats << endl;

    if (cPtr->getWantedTicket() == H)
    {
        // if (tracker_H >= CHART_SIZE / 2)
        // {
        //     return false;
        // }
        // else
        // {
        int i = 0;
        for (; i < CHART_SIZE; i++)
        {
            if (seatChart[i].getAvailable())
            {
                cout << "SUP ?" << endl;
                cPtr->setSeatLocation(i);
                seatChart[i].book(cPtr);
                availableSeats--;
                cout << "HHH+++++++++++++++++++++++++++++++++++++++HHH  " << i << endl;
                return true;
            }
        }

        

        
        //tracker_H++;
    }
    /*else if (cPtr->getWantedTicket() == M)
    {
        if (tracker_M > tracker_L)
        {
            return false;
        }
        else
        {
            cPtr->setSeatLocation(tracker_M);
            seatChart[tracker_M].book(cPtr);
            tracker_M++;

        }
    }*/
    else if(cPtr->getWantedTicket() == L)//st == L
    {
        int i = CHART_SIZE - 1;
        for (; i >= 0; i--)
        {
            if (seatChart[i].getAvailable())
            {
                cout << "SUP ?" << endl;
                cPtr->setSeatLocation(i);
                seatChart[i].book(cPtr);
                availableSeats--;
                cout << "LLL+++++++++++++++++++++++++++++++++++++++LLL  " << i << endl;
                return true;
            }
        }

        
        // if (tracker_L < tracker_M)
        // {
        //     return false;
        // }
        // else
        // {
        //     cPtr->setSeatLocation(tracker_L);
        //     seatChart[tracker_L].book(cPtr);
        //     tracker_L--;

        // }

    }

    return false;
    
}

bool SeatManager::isFull()
{
    return availableSeats == 0;
}

void SeatManager::print()
{
    // create 100 seats
    for (int i = 0; i < CHART_SIZE; i++)
    {
        if (i % 10 == 0) // creates matrix
        {
            cout << endl;
        }
        //cout << i << "       ";
        seatChart[i].print();
        cout << " | ";
        
    }

    cout << endl;
}
// CLASS DEFINITIONS END---------------------------------------------
// run function for sellers to sell tickets
void *sellerRun(void *t)
{
    Seller* seller = (Seller *) t;
    while (times < SIM_TIME)
    {
        //cout << ":::::::::::::::::::::::::::::::::::::: " << times << endl;
        seller->process();
    }
    
    //cout << "Sleeping in thread " << endl;
    cout << "+++++++Thread with seller id : " << seller->getInfo() << "  ...exiting " << endl;
    pthread_exit(NULL);
}

// run function for customers to wake up, line up, and order a ticket
void *customerRun(void *t)
{
    Seller* seller = (Seller *) t;
    int rNum = 0;
    if(times < SIM_TIME){
    // sleep for random time 0-59 seconds
        sleep((rand() % 5 + 1) / (float)SIM_PROCESSING_TIME);
        rNum = rand() % 10;
        pthread_mutex_lock(&mutexx);
        if(!seller[rNum].isFull() && !seller[rNum].isSeatManagerFull())
        {
            //cout << "Cus created " << endl;
            Customer *c = new Customer();    
            c->setSeller(&(seller[rNum]));
            seller[rNum].enqueue(c);
        }

        if(seller[rNum].isSeatManagerFull())
        {
            cout << seller[rNum].getInfo() << " TOLD CUSTOMER THAT CONCERT IS FULL DUDE " << endl;
        }
        pthread_mutex_unlock(&mutexx);
    }
    cout << "Customer arrived at Seller " << seller[rNum].getInfo() << "'s queue." << endl;
    //cout << "Customer left at " << times << endl;
    pthread_exit(NULL);
}

// main() is where program execution begins.
int main(int argc, char *argv[])
{

    // converts to int and gets number of customers
    stringstream convert(argv[1]);
    convert >> num_customers;

    // set the seed
    srand((unsigned)time(NULL));

    SeatManager sm;
    pthread_t _customers[num_customers];
    pthread_t _sellers[NUM_SELLERS];
    int thread_check; // 0 means successfully created
    Seller sellers[NUM_SELLERS];
    int customers_created = 0;
    
    // create sellers with id and type
    for(int i = 0; i < NUM_SELLERS; i++ )
    {
        sellers[i].setID(ids[i]);
        sellers[i].setSellerType(types[i]);
        sellers[i].setSeatManager(&sm);
    }

    //cout << clock() << endl;
    //create seller threads
    for(int i = 0; i < NUM_SELLERS; i++ )
    {
        cout << "main() : creating seller thread, " << i << endl;

        thread_check = pthread_create(&_sellers[i], NULL, sellerRun, (void *)&sellers[i] );
        if (thread_check)
        {
            cout << "Error:unable to create thread," << thread_check << endl;
            exit(-1);
        }
    }
    //cout << "HELOOO ?";

    // run until timer runs out or sold out
    while(SHOULD_EXIST)
    {
        if (times == SIM_TIME)
        {
            cout << "STOP PUSH CUSTOMERS " << endl;
            SHOULD_EXIST = false;
            //cout << "STOP PUSH CUSTOMERS " << endl;
            break;
        }
        
        // create customer threads

        for(int i = 0; i < NUM_SELLERS; i++)
        {
            cout << "main() : creating customer thread, " << customers_created << endl;
            thread_check = pthread_create(&_customers[i], NULL, customerRun, (void *)sellers);
            if (thread_check)
            {
                cout << "Error:unable to create thread," << thread_check << endl;
                exit(-1);
            }
            customers_created++;
        }
        sleep(1);
        cout << times++ << "************************" << endl;

        //cout << "STOP PUSH CUSTOMERS " << endl;
    }


//    free attribute and wait for the other threads
   for( int i = 0; i < NUM_SELLERS; i++ )
   {
        cout << "Main: completed seller thread IDDDDDDD :" << i << endl;
        pthread_join(_sellers[i], NULL);
        
        //cout << "  exiting with status :" << endl;
    }

    sm.print(); // print seating chart

    return 0;
}

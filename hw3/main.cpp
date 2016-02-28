#include <iostream>
#include <ctime>
#include <stdlib.h>
#include <vector>
#include <cstdlib>
#include <pthread.h>
#include <unistd.h>
#include <sstream> // for stringstream
// GLOBAL CONSTANTS
#define CHART_SIZE 100
#define NUM_SELLERS 10
#define MIN_H_RANGE 1
#define MAX_H_RANGE 2
#define MAX_M_RANGE 4
#define MAX_L_RANGE 7
// NAMESPACE
using namespace std;
// GLOBAL VARIABLES
enum SellerType { H = 72, M = 77, L = 76 }; // ascii code
int times = 0;
bool SHOULD_EXIST =  true;
pthread_mutex_t mutexx;
SellerType types[10] = {H, M, M, M, L, L, L, L, L, L};
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
    public:
        Seller();
        Seller(int n, SellerType t, SeatManager *s);

        int generateWaitTime();
        string getInfo();
        bool isSeatAvailable();
        void setID(int id);
        void setSellerType(SellerType st);
        void setSeatManager(SeatManager *s);
        bool process();
        void enqueue(Customer *c);
        SellerType getSellerType();
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
    isAvailable = false;
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
    if (!isAvailable)
        cout << "----";
    else
        cout << customer->getName();
}


Seller::Seller()
{
    name = "";
}

Seller::Seller(int n, SellerType t, SeatManager *s)
{
    name = (char)t;
    name += (char)(n + 48);
    type = t;
    sm = s;
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
    char c2 =  (char)(id + 49);
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
    // Wait on the "emptySlots" semaphore.
    //sem_wait(&emptySlots);

    // Acquire the mutex lock to protect the buffer.
    pthread_mutex_lock(&mutexx);
    
    // Critical region: Insert an item into the buffer.
    sm->bookTicket(customerQueue.front());
    customerQueue.erase(customerQueue.begin());

    cout << "Customer processed." << endl;

    // Release the mutex lock.
    pthread_mutex_unlock(&mutexx);
    
    // Signal the "filledSlots" semaphore.
    //sem_post(&filledSlots);  // signal
}

void Seller::enqueue(Customer *c)
{
    customerQueue.push_back(c);
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
    // mutex lock
    if (availableSeats <= 0)
    {
        // mutex unlock
        return false;
    }

    if (cPtr->getWantedTicket() == H)
    {
        if (tracker_H >= CHART_SIZE / 2)
        {
            // release lock
            return false;
        }
        else
        {
            cPtr->setSeatLocation(tracker_H);
            seatChart[tracker_H].book(cPtr);
            tracker_H++;
        }
    }
    else if (cPtr->getWantedTicket() == M)
    {
        if (tracker_M > tracker_L)
        {
            // release lock
            return false;
        }
        else
        {
            cPtr->setSeatLocation(tracker_M);
            seatChart[tracker_M].book(cPtr);
            tracker_M++;

        }
    }
    else //st == L
    {
        if (tracker_L < tracker_M)
        {
            // release lock
            return false;
        }
        else
        {
            cPtr->setSeatLocation(tracker_L);
            seatChart[tracker_L].book(cPtr);
            tracker_L--;

        }
    }
    // release lock
    return true;
}

bool SeatManager::isFull()
{
    return !availableSeats;
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
    while (SHOULD_EXIST)
    {
        seller->process();
        sleep(0.1);
    }
    
    //cout << "Sleeping in thread " << endl;
    cout << "Thread with seller id : " << seller->getInfo() << "  ...exiting " << endl;
    pthread_exit(NULL);
}

// run function for customers to wake up, line up, and order a ticket
void *customerRun(void *t)
{
    Seller* seller = (Seller *) t;

    // sleep for random time 0-59 seconds
    sleep((rand() % 5 + 1) / 10.0);
    
    Customer *c = new Customer();
    int rNum = rand() % 10;
    c->setSeller(&(seller[rNum]));
    //c->setName(ids[rNum]);
    //c->setWantedTicket[seller[rNum].getSellerType()];
    seller[rNum].enqueue(c);
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

    // create seller threads
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

    // run until timer runs out or sold out
    while(SHOULD_EXIST && !sm.isFull())
    {
        if (times == 5)
        {
            SHOULD_EXIST = false;
        }
        // create customer threads
        while(customers_created < num_customers)
        {
            cout << "main() : creating customer thread, " << customers_created << endl;
            thread_check = pthread_create(&_customers[customers_created], NULL, customerRun, (void *)sellers );
            if (thread_check)
            {
                cout << "Error:unable to create thread," << thread_check << endl;
                exit(-1);
            }
            customers_created++;
        }
        sleep(1);
        times++;
    }

    // free attribute and wait for the other threads
   for( int i=0; i < NUM_SELLERS; i++ ){
        pthread_join(_sellers[i], NULL);
        cout << "Main: completed seller thread id :" << i << endl;
        //cout << "  exiting with status :" << endl;
    }

    sm.print(); // print seating chart
    return 0;
}

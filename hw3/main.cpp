#include <iostream>
#include <ctime>
#include <stdlib.h>
#include <vector>
#include <cstdlib>
#include <pthread.h>
#include <unistd.h>

#define CHART_SIZE 100
#define NUM_CUSTOMERS 10
#define NUM_SELLERS 10
#define MIN_H_RANGE 1
#define MAX_H_RANGE 2
#define MAX_M_RANGE 4
#define MAX_L_RANGE 7

using namespace std;
enum SellerType { H = 72, M = 77, L = 76 };
int times = 0;
bool SHOULD_EXIST =  true;
pthread_mutex_t mutex;

class Seller;
class Seat;
class SeatManager;

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

        void book();
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
        vector<SellerType> customerQueue;
        SeatManager *sm;

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
        bool bookTicket(SellerType st);
        void print();
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

void Seat::book()
{
    isAvailable = !isAvailable;
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
        cout << "---";
    else
        cout << "+++";
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
    //pthread_mutex_lock(&mutex);
    
    // Critical region: Insert an item into the buffer.
    sm->bookTicket(type);
    customerQueue.erase(customerQueue.begin());

    cout << "Customer processed." << endl;

    // Release the mutex lock.
    //pthread_mutex_unlock(&mutex);
    
    // Signal the "filledSlots" semaphore.
    //sem_post(&filledSlots);  // signal
}

void Seller::enqueue(Customer *c)
{
    customerQueue.push_back(this->type);
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

bool SeatManager::bookTicket(SellerType st)
{
    // mutex lock
    if (availableSeats <= 0)
    {
        // mutex unlock
        return false;
    }

    if (st == H)
    {
        if (tracker_H >= CHART_SIZE / 2)
        {
            // release lock
            return false;
        }
        else
        {
            
            seatChart[tracker_H].book();
            cout << "kdsjflksdjlkfdjflkdsjlkfsdjlkdsjfs         " << tracker_H++;
        }
    }
    else if (st == M)
    {
        if (tracker_M > tracker_L)
        {
            // release lock
            return false;
        }
        else
        {
            //cout << "kdsjflksdjlkfdjflkdsjlkfsdjlkdsjfs";
            seatChart[tracker_M].book();
            cout << "kdsjflksdjlkfdjflkdsjlkfsdjlkdsjfs         " << tracker_M++;

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
            //cout << "kdsjflksdjlkfdjflkdsjlkfsdjlkdsjfs";
            seatChart[tracker_L].book();
            //cout << "kdsjflksdjlkfdjflkdsjlkfsdjlkdsjfs         " << tracker_L--;

        }
    }
    // release lock
    return true;
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

// run function to sell tickets
void *sellerRun(void *t)
{
    Seller* seller = (Seller *) t;
    while (SHOULD_EXIST)
    {
        seller->process();
        sleep(0.1);
    }
    
    cout << "Sleeping in thread " << endl;
    cout << "Thread with seller : " << seller->getInfo() << "  ...exiting " << endl;
    pthread_exit(NULL);
}

// run function to wake up customers
void *customerRun(void *t)
{
    Seller* seller = (Seller *) t;

    // sleep for random time 0-59 seconds
    sleep((rand() % 5 + 1) / 10.0);
    
    Customer *c = new Customer();
    seller[rand() % 10].enqueue(c);

    cout << "Customer left at " << times << endl;
    pthread_exit(NULL);
}

// main() is where program execution begins.
int main()
{
    // set the seed
    srand((unsigned)time(NULL));

    SeatManager sm;
    pthread_t _customers[NUM_CUSTOMERS];
    pthread_t _sellers[NUM_SELLERS];
    int thread_check; // 0 means successfully created
    Seller sellers[NUM_SELLERS];
    SellerType types[10] = {H, M, M, M, L, L, L, L, L, L};
    int ids[10] = {1, 1, 2, 3, 1, 2, 3, 4, 5, 6};

    // create sellers
    for(int i = 0; i < NUM_SELLERS; i++ )
    {
        sellers[i].setID(ids[i]);
        sellers[i].setSellerType(types[i]);
        sellers[i].setSeatManager(&sm);
    }

    // create customer threads
    for(int i = 0; i < NUM_CUSTOMERS; i++ )
    {
        cout << "main() : creating customer thread, " << i << endl;
        thread_check = pthread_create(&_customers[i], NULL, customerRun, (void *)sellers );
        if (thread_check)
        {
            cout << "Error:unable to create thread," << thread_check << endl;
            exit(-1);
        }
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

    while(SHOULD_EXIST)
    {
        if (times == 5)
        {
            SHOULD_EXIST = false;
        }
        sleep(1);
        times++;
    }

 // free attribute and wait for the other threads
   for( int i=0; i < NUM_SELLERS; i++ ){
      pthread_join(_sellers[i], NULL);
      cout << "Main: completed thread id :" << i ;
      cout << "  exiting with status :" << endl;
   }

    sm.print();

    return 0;
}

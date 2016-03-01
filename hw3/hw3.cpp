#include <iostream>
#include <ctime>
#include <stdlib.h>
#include <vector>
#include <cstdlib>
#include <pthread.h>
#include <unistd.h>
#include <iterator>
#include <sstream> // for stringstream
// GLOBAL CONSTANTS
#define SIM_TIME 60
#define CHART_SIZE 100
#define NUM_SELLERS 10
#define MIN_H_RANGE 1
#define MAX_H_RANGE 2
#define MAX_M_RANGE 4
#define MAX_L_RANGE 7
#define SIM_PROCESSING_TIME 1
#define CUSTOMER_WAITING_TIME 10
// NAMESPACE
using namespace std;
// GLOBAL VARIABLES
enum SellerType { H = 72, M = 77, L = 76 }; // ascii code
int times = 0;
bool SHOULD_EXIST =  true;
pthread_mutex_t mutexx;
SellerType types[10] = {H, M, M, M, L, L, L, L, L, L};
int ids[10] = {1, 1, 2, 3, 1, 2, 3, 4, 5, 6};
int max_seller_queue_size = 0;
int turned_away_customers = 0;
int seated_customers[3] = {0, 0, 0};
// FORWARD DECLARATIONS
class Seller;
class Seat;
class SeatManager;
// CLASS DECLARATIONS BEGIN------------------------------------------
class Customer
{
    public:
        string name;
        Seller *seller;
        SellerType type;
        int seatLocation;
        pthread_t waitingThread;

    public:
        Customer();
        Customer(Seller *s);
        
        string getName();
        string getSellerInfo();
        void setSeller(Seller *s);
        void setName(string s);
        SellerType getWantedTicket();
        void setSeatLocation(int n);
        void startWaitingInQueue();
        void dequeue();
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
        bool dequeue(Customer *c);
        SellerType getSellerType();
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
        int tracker_M_helper = 0;
        int tracker_L = CHART_SIZE - 1;
    public:
        SeatManager();

        bool bookTicket(Customer *cPtr);
        void print();
        bool isFull();
        int getAvailableSeats();
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

void Customer::dequeue()
{
    if(seller != NULL)
        seller->dequeue(this);
}

void* customerWaitRun(void *t)
{   
    sleep(CUSTOMER_WAITING_TIME);
    // critical region, customer leaves after 10 seconds
    pthread_mutex_lock(&mutexx);
    Customer *cPtr = (Customer *)t;
    if(cPtr != NULL)
    {
        cPtr->dequeue(), cPtr = NULL;
    }
    pthread_mutex_unlock(&mutexx);

    pthread_exit(NULL);
}

void Customer::startWaitingInQueue()
{
    pthread_create(&waitingThread, NULL, customerWaitRun, (void *)this);
}

Seat::Seat()
{
    isAvailable = true;
    customer = NULL;
}

void Seat::book(Customer *pCus)
{
    isAvailable = !isAvailable;
    customer = pCus;
}

bool Seat::getAvailable(){
    return isAvailable;
}

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
    if (isAvailable)
    {
        cout << "----";
    }
    else
    {
        cout << customer->getName();
    }
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
    char c2 =  (char)(id + 48);
    string temp = "TT";
    temp[0] = c1;
    temp[1] = c2;
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
    sleep(generateWaitTime() / (float)(SIM_PROCESSING_TIME));

    // Acquire the mutex lock to protect the buffer.
    pthread_mutex_lock(&mutexx);

    if (customerQueue.empty())
    {
        pthread_mutex_unlock(&mutexx);
        return false;
    }

    if (sm->isFull())
    {
        cout << "Seller " << this->getInfo() << " told customer concert is full." << endl;
        pthread_mutex_unlock(&mutexx);
        return false;
    }
    else
    {
        // Critical region: Insert an item into the buffer.
        if(customerQueue.front() == NULL)
        {
            pthread_mutex_unlock(&mutexx);
            return false;
        }
        sm->bookTicket(customerQueue.front());
        if(customerQueue.size() != 0)
            customerQueue.erase(customerQueue.begin());
        cout << "Seller " << this->getInfo() << " assigned seat to customer." << endl;
        // Release the mutex lock.
        pthread_mutex_unlock(&mutexx);
        return true;
    }
    
}

void Seller::enqueue(Customer *c)
{
    customerQueue.push_back(c);
    c->startWaitingInQueue();
}

bool Seller::dequeue(Customer *c)
{
    if(customerQueue.size() != 0)
    {
        vector<Customer *>::iterator iPtr = customerQueue.begin();

        for(int i = 0; i < customerQueue.size(); i++)
        {
            if(customerQueue[i] != NULL)
            {
                if(customerQueue[i] == c)
                {
                    customerQueue.erase(iPtr);
                    turned_away_customers++;
                    cout << "I WAITED FOR TOO LONG ALREADY NOW I'M LEAVING SELLER : " << this->getInfo() << endl;
                    break;
                }
                else
                {    
                    advance(iPtr, 1);
                }
            }
            else
            {
                break;
            }
        }
    }   
}

bool Seller::isFull()
{
    return customerQueue.size() >= max_seller_queue_size;
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
        return false;
    }
    // sell high priced ticket
    if (cPtr->getWantedTicket() == H)
    {
        int i = 0;
        for (; i < CHART_SIZE; i++)
        {
            if (seatChart[i].getAvailable())
            {
                cPtr->setSeatLocation(i);
                seatChart[i].book(cPtr);
                availableSeats--;
                seated_customers[0]++;
                return true;
            }
        }
    }
    // sell medium priced ticket
    else if (cPtr->getWantedTicket() == M)
    {

        int multiplier = 10;
        for(int i = 0; i < CHART_SIZE && !isFull(); i++, tracker_M_helper++)
        {
            if(tracker_M_helper >= 10)
            {    
                tracker_M_helper = 0;
                tracker_M += multiplier;
                multiplier += 10;
                multiplier *= -1;
            }
            if (seatChart[tracker_M + tracker_M_helper].getAvailable())
            {
                cPtr->setSeatLocation(i);
                seatChart[tracker_M + tracker_M_helper].book(cPtr);
                availableSeats--;
                seated_customers[1]++;
                return true;
            }
        }
            
    }    
    // sell low priced ticked    
    else
    {
        int i = CHART_SIZE - 1;
        for (; i >= 0; i--)
        {
            if (seatChart[i].getAvailable())
            {
                cPtr->setSeatLocation(i);
                seatChart[i].book(cPtr);
                availableSeats--;
                seated_customers[2]++;
                return true;
            }
        }
    }

    return false;
}

bool SeatManager::isFull()
{
    return availableSeats == 0;
}

int SeatManager::getAvailableSeats()
{
    return availableSeats;
}

void SeatManager::print()
{
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
// CLASS DEFINITIONS END---------------------------------------------
// run function for sellers to sell tickets
void *sellerRun(void *t)
{
    Seller* seller = (Seller *) t;
    while (times < SIM_TIME)
    {
        if(seller->isSeatManagerFull()){
            cout << "Concert is full, seller stops selling tickets : " << seller->getInfo() << endl;
            pthread_exit(NULL);
        }
        if(seller != NULL);
            seller->process();
    }
    
    cout << "Thread with seller id : " << seller->getInfo() << "  ...exiting " << endl;
    pthread_exit(NULL);
}

// run function for customers to wake up, line up, and order a ticket
void *customerRun(void *t)
{
    Seller* seller = (Seller *) t;
    int rNum = 0;
    if(times < SIM_TIME){
    // sleep for random time 0-59 seconds
        sleep((rand() % 60) / (float)SIM_PROCESSING_TIME);
        rNum = rand() % 10; // random seller
        pthread_mutex_lock(&mutexx);
        if(seller != NULL)
        {
            if(!seller[rNum].isFull() && !seller[rNum].isSeatManagerFull())
            {
                Customer *c = new Customer();    
                c->setSeller(&(seller[rNum]));
                seller[rNum].enqueue(c);
                cout << "Customer arrived at Seller " << seller[rNum].getInfo() << "'s queue." << endl;
            }

            if(seller[rNum].isSeatManagerFull())
            {
                turned_away_customers++;
                cout << seller[rNum].getInfo() << " TOLD CUSTOMER THAT CONCERT IS FULL" << endl;
            }
        }
        pthread_mutex_unlock(&mutexx);
    }
    pthread_exit(NULL);
}

// main() is where program execution begins.
int main(int argc, char *argv[])
{

    // converts to int and gets number of customers
    stringstream convert(argv[1]);
    convert >> max_seller_queue_size;

    // set the seed
    srand((unsigned)time(NULL));

    SeatManager sm;
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

    //create seller threads
    for(int i = 0; i < NUM_SELLERS; i++ )
    {
        //cout << "main() : creating seller thread, " << i << endl;

        thread_check = pthread_create(&_sellers[i], NULL, sellerRun, (void *)&sellers[i] );
        if (thread_check)
        {
            cout << "Error:unable to create thread," << thread_check << endl;
            exit(-1);
        }
    }

    // run until timer runs out
    int rNum = 0;
    while(SHOULD_EXIST)
    {
        // stops loop when timer runs out
        if (times == SIM_TIME)
        {
            SHOULD_EXIST = false;
            break;
        }

        // create customer threads
        rNum = rand() % (CHART_SIZE / 5 + 1);
        pthread_t _customers[rNum];
        for(int i = 0; i < rNum; i++)
        {
            //cout << "main() : creating customer thread, " << customers_created << endl;
            thread_check = pthread_create(&_customers[i], NULL, customerRun, (void *)sellers);
            if (thread_check)
            {
                cout << "Error:unable to create thread," << thread_check << endl;
                exit(-1);
            }
            customers_created++;
        }
        sleep(1);
        cout << "\nEVENTS HAPPENED AT TIMESTAMP " << times++ << endl << endl;
    }


   // free attribute and wait for the other threads
   for( int i = 0; i < NUM_SELLERS; i++ )
   {
        cout << "Main: completed seller thread ID :" << i << endl;
        pthread_join(_sellers[i], NULL);
        
        //cout << "  exiting with status :" << endl;
    }

    sm.print(); // print seating chart

    cout << "\n" << seated_customers[0] << " high-priced customers seated." << endl;
    cout << seated_customers[1] << " medium-priced customers seated." << endl;
    cout << seated_customers[2] << " low-priced customers seated." << endl;
    cout << customers_created + sm.getAvailableSeats() - CHART_SIZE - 1 << " TURNED AWAY ~,~" << endl;
    return 0;
}

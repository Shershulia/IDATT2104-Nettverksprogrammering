#include"Workers.h"
#include <iostream>
using namespace std;

int main() {
    Workers worker_thread(4);
    Workers event_loop(1);

    worker_thread.start();
    event_loop.start();



    worker_thread.post([] {
        cout << "A" << " ";
    });
    worker_thread.post([] {
        cout << "B" << " ";

// Task B
// Might run in parallel with task A
    });

    event_loop.post([] {
        cout << "C" << " ";

// Task C
// Might run in parallel with task A and B
    });
    event_loop.post([] {
        cout << "D" << endl;

// Task D
// Will run after task C
// Might run in parallel with task A and B
    });


    for (int i = 0; i < 10; i++) {
        event_loop.post([i] {
            cout << i << endl;
        });
    }
    this_thread::sleep_for(2s);
    worker_thread.join_all();
    event_loop.join_all();


    return 0;
}
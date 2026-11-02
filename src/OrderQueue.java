-าpublic interface OrderQueue <T>{
    void enqueue(T item);
    T dequeue();
    T peek();
    boolean isEmpty();
    int size();
    void printQueue();
}

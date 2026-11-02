public class KitchenOrder<T> implements  OrderQueue<T> {

    private static class Node<T> {
        T data;
        Node<T> next;
        Node(T data) {this.data = data;}
    }

    private Node<T> front;
    private Node<T> rear;
    private int size;


    @Override
    public void enqueue(T item){
        Node<T> newNode = new Node<>(item);
        if (isEmpty()) {
            front = newNode;
            rear  = newNode;
        }else {
            rear.next = newNode;
            rear      = newNode;
        }
        size++;
    }

    /** ดึงรายการที่อยู่หัวคิวออกไปทำ -- O(1) */
    @Override
    public T dequeue() {
        if (isEmpty()) {
            return null;
        }
        T result = front.data;
        front = front.next;
        if (front == null) {
            rear = null; // คิวว่างสนิทแล้ว
        }
        size--;
        return result;
    }

    @Override
    public T peek(){
        return isEmpty() ? null : front.data;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size(){
        return size;
    }

    @Override
    public void printQueue(){
        if (isEmpty()) {
            System.out.println("ไม่มีรายการค้างอยู่ในคิว");
            return;
        }
        int position = 1;
        Node<T> current = front;
        while (current != null){
            System.out.println("คิวที่ " + position + ":" + current.data);
            current = current.next;
            position++;
        }
    }
}

public class ArrayOrder<T> implements OrderQueue<T> {

    private Object[] data;
    private int count;

    public ArrayOrder() {
        data = new Object[16];
        count = 0;
    }

    @Override
    public void enqueue(T item) {
        if (count == data.length) {
            resize();
        }
        data[count] = item;
        count++;
    }

    private void  resize(){
        Object[] newData = new Object[data.length * 2];
        System.arraycopy(data, 0, newData, 0, count);
        data = newData;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T dequeue(){
        if (isEmpty()) {
            return null;
        }
        T result = (T) data[0];
        for (int i = 0; i < count - 1; i++){
            data[i] = data[i + 1];
        }
        data[count - 1] = null;
        count--;
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T peek(){
        return isEmpty() ? null : (T) data[0];
    }


    @Override
    public boolean isEmpty(){
        return count == 0 ;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public void printQueue(){
        if (isEmpty()){
            System.out.println("ไม่มีรายการค้างอยู่ในคิว");
            return;
        }
        for (int i = 0; i < count; i++){
            System.out.println("คิวที่ " + (i + 1) + ": " + data[i]);
        }
    }
}

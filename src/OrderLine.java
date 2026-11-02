public class OrderLine {
    private final MenuItem item;
    private int quantity;

    public OrderLine(MenuItem item, int quantity){
        this.item = item;
        this.quantity = quantity;
    }

    public MenuItem getItem(){
        return item;
    }

    public int getQuantity(){
        return quantity;
    }

    public void addQuantity(int amout) {
        this.quantity += amout;
    }

    public double getSubtotal() {
        return item.getPrice() * quantity;
    }

    @Override
    public String toString(){
        return String.format("%-20s x%-3d %10.2f บาท", item.getName(), quantity, getSubtotal());
    }
}

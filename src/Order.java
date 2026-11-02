import java.util.List;

public class Order {
    private static int nextOrderId = 1001;

    private final int orderId;
    private final int tableNumber;
    private final List<OrderLine> lines;
    private String status;

    public Order(int tableNumber, List<OrderLine> lines) {
        this.orderId = nextOrderId++;
        this.tableNumber = tableNumber;
        this.lines = lines;
        this.status = "รอครัวรับออเดอร์";
    }

    public int getOrderId() {
        return orderId;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public List<OrderLine> getLines() {
        return lines;
    }

    public String getStatus() {
        return status;
    }

    public void markServed() {
        this.status = "เสิร์ฟแล้ว";
    }

    public double getTotal() {
        double total = 0;
        for (OrderLine line : lines) {
            total += line.getSubtotal();
        }
        return total;
    }

    public void printReceipt() {
        System.out.println("-----------------------------------");
        System.out.println("ใบเสร็จออเดอร์ #" + orderId + "  โต๊ะ " + tableNumber);
        System.out.println("-----------------------------------");
        for (OrderLine line : lines) {
            System.out.println(" " + line);
        }
        System.out.printf("รวมทั้งสิ้น: %.2f บาท%n", getTotal());
        System.out.println("สถานะ: " + status);
        System.out.println("-----------------------------------");
    }

    @Override
    public String toString() {
        return String.format("ออเดอร์ #%d | โต๊ะ %d | %d รายการ | %.2f บาท | สถานะ: %s",
                orderId, tableNumber, lines.size(), getTotal(), status);
    }
}
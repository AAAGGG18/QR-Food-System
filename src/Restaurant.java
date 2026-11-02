import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Restaurant {
    private final List<MenuItem> menu;
    private final MenuFinder menuFinder; // ค้นหาเมนูผ่าน interface
    private final OrderQueue<Order> kitchenQueue;
    private final List<Order> ordersHistory;
    private final Scanner scanner;

    public Restaurant() {
        this.menu = buildMenu();
        this.menuFinder = new HashMenuFinder(menu);
        this.kitchenQueue = new KitchenOrder<>();
        this.ordersHistory = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    private List<MenuItem> buildMenu() {
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem( 1, "ข้าวผัดทะเล",        399, "อาหารจานหลัก"));
        items.add(new MenuItem( 2, "ต้มยำกุ้ง",           290, "อาหารจานหลัก"));
        items.add(new MenuItem( 3, "ผัดไทกุ้งสด",         160, "อาหารจานหลัก"));
        items.add(new MenuItem( 4, "ปลากระพงทอด",      520, "อาหารจานหลัก"));
        items.add(new MenuItem( 5, "ไก่ย่างสมุนไพร(ตัว)",  285, "อาหารจานหลัก"));
        items.add(new MenuItem( 6, "กุ้งเผา(1kg.)",     1140, "อาหารจานหลัก"));
        items.add(new MenuItem( 7, "ข้าวเหนียวมะม่วง",    120, "ของหวาน"));
        items.add(new MenuItem( 8, "บัวลอยไข่หวาน",       85, "ของหวาน"));
        items.add(new MenuItem( 9, "ไอศกรีมนมฮอกไกโด",   50, "ของหวาน"));
        items.add(new MenuItem(10, "น้ำเปล่า",            20, "เครื่องดื่ม"));
        items.add(new MenuItem(11, "โค้ก",               25, "เครื่องดื่ม"));
        return items;
    }

    public void run(){
        boolean running = true;
        System.out.println("==========================================");
        System.out.println("\t     ยินดีต้อนรับเข้าสู่การสั่งอาหาร");
        System.out.println("==========================================");

        while (running){
            printMainMenu();
            int choice = readIntInRange("เลือกเมนู: ", 0, 6);
            switch (choice) {
                case 1 : startNewOrderFlow(); break;
                case 2 : viewKitchenQueue(); break;
                case 3 : processNextOrder(); break;
                case 4 : viewOrderHistory(); break;
                case 5 : comparePerformance(); break;
                case 6 : compareMenuSearch(); break;
                case 0 :
                    System.out.println("...ขอบคุณที่ใช้บริการค่ะ/ครับ...");
                    running = false;
                    break;
                default:
                    System.out.println("กรุณาเลือกให้ถูกต้อง");
                    break;

            }
        }
        scanner.close();
    }

    public void printMainMenu(){
        System.out.println("----------------- เมนูหลัก -----------------");
        System.out.println("1. เริ่มออเดอร์ (SCAN QR CODE)");
        System.out.println("2. ดูคิวออเดอร์ในครัว");
        System.out.println("3. [ครัว] ดำเนินการออเดอร์ถัดไป");
        System.out.println("4. ดูประวัติออเดอร์ทั้งหมด");
        System.out.println("5. เปรียบเทียบประสิทธิภาพคิว \n(Linked List vs Array) - Big-O Demo");
        System.out.println("6. เปรียบเทียบการค้นหาเมนู \n(Linear vs Binary vs Hash) - Big-O Demo");
        System.out.println("0. ออกจากโปรแกรม");
        System.out.println("------------------------------------------");
    }

    // -------------------- การสั่งอาหารของลูกค้า --------------------
    private void startNewOrderFlow() {
        int tableNumber = readIntInRange("สแกน QR สำเร็จ - กรุณาระบุหมายเลขโต๊ะ (1-20): ",
                1,
                20);
        List<OrderLine> cart = new ArrayList<>();

        boolean ordering = true;
        while (ordering) {
            printFullMenu();
            System.out.println("a.เพิ่มรายการอาหาร");
            System.out.println("b.ดูออเดอร์ปัจจุบัน");
            System.out.println("c.ยืนยันออเดอร์ (ส่งเข้าครัว)");
            System.out.println("d.ยกเลิกออเดอร์นี้");
            System.out.print("เลือก (a/b/c/d): ");
            String choice = scanner.nextLine().trim().toLowerCase();

            switch (choice) {
                case "a":
                    addItemToCart(cart);
                    break;
                case "b":
                    showCart(cart);
                    break;
                case "c":
                    if (cart.isEmpty()) {
                        System.out.println("ตะกร้ายังว่างอยู่ กรุณาเลือกอาหารก่อนยืนยันออเดอร์");
                    } else {
                        Order order = new Order(tableNumber, cart);
                        kitchenQueue.enqueue(order);
                        System.out.println("ส่งออเดอร์เข้าครัวเรียบร้อย!");
                        order.printReceipt();
                        ordering = false;
                    }
                    break;
                case "d":
                    System.out.println("ยกเลิกออเดอร์ของโต๊ะ " + tableNumber + " แล้ว");
                    ordering = false;
                    break;
                default:
                    System.out.println("กรุณาเลือก a, b, c หรือ d เท่านั้น");
                    break;
            }
        }
    }

    private void printFullMenu() {
        System.out.println();
        System.out.println("================ เมนูอาหาร ================");
        for (MenuItem item : menu) {
            System.out.println(item);
        }
        System.out.println("============================================");
    }

    private void addItemToCart(List<OrderLine> cart) {
        int id = readIntInRange("ระบุรหัสอาหารที่ต้องการ (0 = ยกเลิก): ", 0, 9999);
        if (id == 0) return;

        MenuItem selected = findMenuItemById(id);
        if (selected == null) {
            System.out.println("ไม่พบรหัสอาหารนี้ในเมนู กรุณาลองใหม่");
            return;
        }

        int quantity = readIntInRange("ระบุจำนวน: ", 1, 99);

        // ถ้ามีรายการนี้อยู่แล้วในตะกร้า ให้บวกจำนวนเพิ่มแทนการเพิ่มบรรทัดใหม่
        for (OrderLine line : cart) {
            if (line.getItem().getId() == selected.getId()) {
                line.addQuantity(quantity);
                System.out.println("เพิ่ม " + selected.getName() + " อีก " + quantity + " ที่แล้ว");
                return;
            }
        }
        cart.add(new OrderLine(selected, quantity));
        System.out.println("เพิ่ม " + selected.getName() + " x" + quantity + " ลงตะกร้าแล้ว");
    }

    private MenuItem findMenuItemById(int id) {
        return menuFinder.findById(id); // ใช้ HashMenuFinder -> O(1)
    }

    private void showCart(List<OrderLine> cart) {
        if (cart.isEmpty()) {
            System.out.println("ตะกร้าว่างอยู่");
            return;
        }
        double total = 0;
        System.out.println("----------- ตะกร้าปัจจุบัน -----------");
        for (OrderLine line : cart) {
            System.out.println("  " + line);
            total += line.getSubtotal();
        }
        System.out.printf("รวม: %.2f บาท%n", total);
        System.out.println("--------------------------------------");
    }

    // -------------------- 2) มุมมองของครัว --------------------
    private void viewKitchenQueue() {
        System.out.println();
        System.out.println("============ คิวออเดอร์ในครัว (" + kitchenQueue.size() + " ใบ) ============");
        kitchenQueue.printQueue();
        System.out.println("=============================================");
    }

    private void processNextOrder() {
        Order next = kitchenQueue.dequeue();
        if (next == null) {
            System.out.println("ไม่มีออเดอร์ค้างอยู่ในคิวครัว");
            return;
        }
        next.markServed();
        ordersHistory.add(next);
        System.out.println();
        System.out.println("ครัวดำเนินการและเสิร์ฟออเดอร์นี้เรียบร้อย:");
        next.printReceipt();
    }

    // -------------------- 3) ประวัติออเดอร์ --------------------
    private void viewOrderHistory() {
        System.out.println();
        if (ordersHistory.isEmpty()) {
            System.out.println("ยังไม่มีออเดอร์ที่เสิร์ฟเสร็จแล้ว");
            return;
        }
        System.out.println("================ ประวัติออเดอร์ที่เสิร์ฟแล้ว ================");
        double grandTotal = 0;
        for (Order order : ordersHistory) {
            System.out.println("  " + order);
            grandTotal += order.getTotal();
        }
        System.out.printf("ยอดขายรวมทั้งหมด: %.2f บาท%n", grandTotal);
        System.out.println("=============================================================");
    }

    // -------------------- 4) เปรียบเทียบ Big-O: Linked List vs Array --------------------
    /**
     * รัน enqueue/dequeue จำนวน n ครั้งบนคิวทั้งสองแบบ แล้ววัดเวลาจริงของ
     * dequeue ทั้งหมดเทียบกัน เพื่อโชว์ผลต่างระหว่าง O(1) (Linked List)
     * กับ O(n) (Array) แบบจับเวลาจริง ไม่ใช่แค่ทฤษฎี
     * ใช้ Integer แทน Order เพื่อไม่ให้กระทบเลขออเดอร์จริงของร้าน
     */
    private void comparePerformance() {
        int[] sizes = {2000, 6000, 12000};
        System.out.println();
        System.out.println("======== เปรียบเทียบประสิทธิภาพคิว: Linked List vs Array ========");
        System.out.printf("%-14s%-20s%-20s%n", "จำนวนรายการ", "Linked List (ms)", "Array (ms)");
        for (int n : sizes) {
            long linkedTimeNs = benchmarkDequeueAll(new KitchenOrder<>(), n);
            long arrayTimeNs = benchmarkDequeueAll(new ArrayOrder<>(), n);
            System.out.printf("%-14d%-20.3f%-20.3f%n",
                    n, linkedTimeNs / 1_000_000.0, arrayTimeNs / 1_000_000.0);
        }
        System.out.println();
        System.out.println("สังเกต: เวลาของ Array เพิ่มขึ้นเร็วกว่า Linked List ชัดเจนเมื่อ n โตขึ้น");
        System.out.println("เพราะ dequeue() ของ Array เป็น O(n) (ต้อง shift ทุกตัว) ในขณะที่");
        System.out.println("Linked List เป็น O(1) (ตัดโหนดแรกทิ้งตรง ๆ)");
        System.out.println("====================================================================");
    }

    /** เติมคิวด้วย n รายการ แล้ววัดเวลาที่ใช้ในการ dequeue จนคิวว่าง */
    private long benchmarkDequeueAll(OrderQueue<Integer> queue, int n) {
        for (int i = 0; i < n; i++) {
            queue.enqueue(i);
        }
        long start = System.nanoTime();
        while (!queue.isEmpty()) {
            queue.dequeue();
        }
        return System.nanoTime() - start;
    }

    // -------------------- 5) เปรียบเทียบ Big-O: การค้นหาเมนู --------------------
    /**
     * สร้างเมนูจำลอง n รายการ แล้วค้นหาทุก id (1 ถึง n) ด้วยวิธีค้นหาทั้ง 3 แบบ
     * จับเวลารวมเทียบกัน เพื่อโชว์ผลต่างระหว่าง O(n), O(log n) และ O(1)
     * (สร้างเมนูจำลองแยกต่างหาก ไม่กระทบเมนูจริงของร้าน)
     */
    private void compareMenuSearch() {
        int[] sizes = {1000, 5000, 20000};
        System.out.println();
        System.out.println("======== เปรียบเทียบการค้นหาเมนู: Linear vs Binary vs Hash ========");
        System.out.printf("%-14s%-18s%-18s%-18s%n",
                "จำนวนเมนู", "Linear (ms)", "Binary (ms)", "Hash (ms)");
        for (int n : sizes) {
            List<MenuItem> fake = new ArrayList<>();
            for (int i = 1; i <= n; i++) {
                fake.add(new MenuItem(i, "เมนู" + i, 100, "ทดสอบ"));
            }
            long linearNs = benchmarkSearchAll(new LinearMenuFinder(fake), n);
            long binaryNs = benchmarkSearchAll(new BinaryMenuFinder(fake), n);
            long hashNs = benchmarkSearchAll(new HashMenuFinder(fake), n);
            System.out.printf("%-14d%-18.3f%-18.3f%-18.3f%n",
                    n, linearNs / 1_000_000.0, binaryNs / 1_000_000.0, hashNs / 1_000_000.0);
        }
        System.out.println();
        System.out.println("สังเกต: Linear (O(n)) ช้าลงเร็วที่สุดเมื่อจำนวนเมนูเพิ่มขึ้น");
        System.out.println("Binary (O(log n)) ช้าลงน้อยมาก ส่วน Hash (O(1)) แทบไม่เปลี่ยนตามจำนวนเมนู");
        System.out.println("(ตัวเลขเวลาอาจแกว่งเล็กน้อยในแต่ละรอบ แต่แนวโน้มจะเหมือนเดิม)");
        System.out.println("====================================================================");
    }

    /** ค้นหาทุก id ตั้งแต่ 1 ถึง n แล้วคืนเวลารวมที่ใช้ (นาโนวินาที) */
    private long benchmarkSearchAll(MenuFinder finder, int n) {
        long start = System.nanoTime();
        int found = 0;
        for (int id = 1; id <= n; id++) {
            if (finder.findById(id) != null) {
                found++;
            }
        }
        long elapsed = System.nanoTime() - start;
        if (found != n) {
            System.out.println("คำเตือน: ค้นหาไม่ครบ (" + found + "/" + n + ")");
        }
        return elapsed;
    }

    // -------------------- Utility: อ่าน input แบบปลอดภัย ป้องกันบัค --------------------
    private int readIntInRange(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value < min || value > max) {
                    System.out.println("กรุณาระบุตัวเลขระหว่าง " + min + " ถึง " + max);
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("กรุณาระบุตัวเลขเท่านั้น");
            }
        }
    }
}
 

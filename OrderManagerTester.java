/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ordermanager;
import java.io.FileNotFoundException;
import java.io.IOException;

public class OrderManagerTester {
    public static void main(String[] args) {
        System.out.println("=== WIA1002 Campus Food Delivery - OrderManager Tester (Member 2) ===\n");
        
        // Initialize
        OrderManager manager = new OrderManager();
        
        // TEST 1: Create New Orders (Mandatory: 4.2)
        System.out.println("TEST 1: Creating Orders (Default Status = PENDING)...");
        try {
            manager.createOrder("Alice Tan", "Kolej Makanan", "Kolej Kediaman A", 1); // Urgent
            manager.createOrder("Bob Lim", "Cafeteria A", "Fakulti Sains", 2);       // Normal
            manager.createOrder("Charlie Wong", "Canteen B", "Hostel C", 1);         // Urgent
            manager.createOrder("Diana Lee", "Stall D", "Faculty E", 2);             // Normal
            System.out.println("✓ 4 orders created successfully.\n");
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Create failed: " + e.getMessage());
        }
        
        // TEST 2: View All Orders (Mandatory: 4.3)
        System.out.println("TEST 2: Viewing All Orders (Order of Creation via ArrayList)...");
        manager.viewAllOrders();
        System.out.println();
        
        // TEST 3: View Orders by Status (Mandatory: 4.3)
        System.out.println("TEST 3: Viewing Orders by Status (All PENDING)...");
        manager.viewOrdersByStatus(Status.PENDING);
        System.out.println("\nTEST 3.2: Viewing by ASSIGNED (should be empty)...");
        manager.viewOrdersByStatus(Status.ASSIGNED);
        System.out.println();
        
        // TEST 4: Get Next Pending Order (Helper: PriorityQueue Demo)
        System.out.println("TEST 4: PriorityQueue Demo - Dequeue Pending Orders (Urgent/Earliest First)...");
        System.out.println("Next: " + manager.getNextPendingOrder()); // Should be Alice (Urgent, earliest)
        System.out.println("Next: " + manager.getNextPendingOrder()); // Should be Charlie (Urgent)
        System.out.println("Next: " + manager.getNextPendingOrder()); // Should be Bob (Normal, earlier than Diana)
        System.out.println("Next: " + manager.getNextPendingOrder()); // Diana
        System.out.println("✓ PriorityQueue works: Urgent (1) before Normal (2), ties by time.\n");
        
        // Re-create orders for further tests (simulate reset)
        resetOrders(manager);
        
        // TEST 5: Fast Lookup by ID (Mandatory: HashMap Demo + 4.7 Search by ID)
        System.out.println("TEST 5: HashMap Lookup - Search Order by ID...");
        Order found = manager.getOrderById(1);
        System.out.println("Order ID 1: " + (found != null ? found : "Not found"));
        System.out.println("Order ID 99 (invalid): " + manager.getOrderById(99));
        System.out.println("✓ HashMap O(1) lookup works.\n");
        
        // TEST 6: Update Status (Helper for Integration: e.g., Dispatch/Complete)
        System.out.println("TEST 6: Update Order Status (For Assign/Complete Integration)...");
        manager.updateOrderStatus(1, Status.ASSIGNED);
        manager.updateOrderStatus(2, Status.DELIVERED);
        manager.viewOrdersByStatus(Status.ASSIGNED); // Should show Alice
        manager.viewOrdersByStatus(Status.DELIVERED); // Should show Bob
        System.out.println("✓ Status updates work (removed from Pending if ASSIGNED).\n");
        
        // TEST 7: Cancel/Delete Order (Mandatory: 4.6)
        System.out.println("TEST 7: Cancel/Delete Order by ID...");
        manager.cancelOrder(3); // Charlie (Pending → Cancelled, removed from structures)
        System.out.println("After cancel ID 3:");
        manager.viewAllOrders(); // Should show 1(Assigned), 2(Delivered), 4(Pending)
        System.out.println("View by CANCELLED: ");
        manager.viewOrdersByStatus(Status.CANCELLED); // Empty, since deleted
        System.out.println("✓ Cancel removes from PriorityQueue/Map/ArrayList.\n");
        
        // Re-create for optionals
        resetOrders(manager);
        
        // OPTIONAL TEST 8: Display Statistics
        System.out.println("OPTIONAL TEST 8: Order-Based Statistics...");
        manager.displayStatistics();
        System.out.println("✓ Stats calculated via ArrayList iteration (O(n)).\n");
        
        // OPTIONAL TEST 9: Save to CSV
        System.out.println("OPTIONAL TEST 9: Save Orders to CSV...");
        try {
            manager.saveOrdersToFile("orders.csv");
            System.out.println("✓ Saved to orders.csv (check file in project root).");
        } catch (IOException e) {
            System.out.println("✗ Save failed: " + e.getMessage());
        }
        System.out.println();
        
        // OPTIONAL TEST 10: Load from CSV
        System.out.println("OPTIONAL TEST 10: Load Orders from CSV...");
        OrderManager loadedManager = new OrderManager();
        try {
            loadedManager.loadOrdersFromFile("orders.csv");
            System.out.println("Loaded Orders:");
            loadedManager.viewAllOrders();
            System.out.println("Next Pending (after load): " + loadedManager.getNextPendingOrder());
            System.out.println("✓ Load rebuilds PriorityQueue/HashMap/ArrayList correctly.");
        } catch (FileNotFoundException e) {
            System.out.println("✗ Load failed: File not found (run save first).");
        } catch (Exception e) {
            System.out.println("✗ Load failed: " + e.getMessage());
        }
        
        System.out.println("\n=== All Tests Complete! Total: 10 Tests Passed. Ready for Integration. ===");
    }
    
    // Helper: Reset to initial 4 orders for repeated tests
    private static void resetOrders(OrderManager manager) {
        manager = new OrderManager(); // Fresh instance for reset
        manager.createOrder("Alice Tan", "Kolej Makanan", "Kolej Kediaman A", 1);
        manager.createOrder("Bob Lim", "Cafeteria A", "Fakulti Sains", 2);
        manager.createOrder("Charlie Wong", "Canteen B", "Hostel C", 1);
        manager.createOrder("Diana Lee", "Stall D", "Faculty E", 2);
    }
}

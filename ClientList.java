import java.io.*;
import java.util.*;

public class ClientList {
    private List<Client> clients;

    public ClientList() { clients = new LinkedList<>(); }

    public void addClient(Client c) { clients.add(c); }

    public Client findClientByID(String clientID) {
        for (Client c : clients) {
            if (c.getId().equals(clientID)) {
                return c;
            }
        }
        return null;
    }
    public List<Client> getAllClients(){ return clients; }

    public void displayAllClients() {
    System.out.println("=== Client List ===");
    if (clients.isEmpty()) {
        System.out.println("No clients found.");
    } else {
        for (Client c : clients) {
            System.out.println(c);
        }
    }
    System.out.println("-------------------");
    System.out.println("Total clients: " + clients.size());
}

public void displayWishLists(List<WishList> wishLists) {
    System.out.println("=== Wish Lists ===");
    for (Client c : clients) {
        boolean found = false;
        for (WishList wl : wishLists) {
            if (c.getId().equals(wl.getClientId())) {
                System.out.println("wishlist for client: " + c.getName());
                System.out.println(wl);
                found = true;
                break; // stop looking once we find their wishlist
            }
        }
        if (!found) {
            System.out.println("No wishlist for client: " + c.getName());
        }
    }
    System.out.println("--------------------");
}

        }

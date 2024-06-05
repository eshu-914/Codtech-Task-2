import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

class Item implements Serializable {
    String title;
    String authorOrDirector;
    String ISBN;
    boolean isAvailable;
    String type;

    public Item(String title, String authorOrDirector, String ISBN, String type) {
        this.title = title;
        this.authorOrDirector = authorOrDirector;
        this.ISBN = ISBN;
        this.isAvailable = true;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthorOrDirector() {
        return authorOrDirector;
    }

    public String getISBN() {
        return ISBN;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getType() {
        return type;
    }
}

class Library {
    private ArrayList<Item> items;
    private static final String FILE_NAME = "library.dat";

    public Library() {
        items = new ArrayList<>();
        loadItems();
    }

    public void addItem(Item item) {
        items.add(item);
        saveItems();
    }

    public void removeItem(String ISBN) {
        items.removeIf(item -> item.getISBN().equals(ISBN));
        saveItems();
    }

    public ArrayList<Item> getAllItems() {
        return items;
    }

    public ArrayList<Item> searchItems(String keyword) {
        ArrayList<Item> results = new ArrayList<>();
        for (Item item : items) {
            if (item.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                item.getAuthorOrDirector().toLowerCase().contains(keyword.toLowerCase()) ||
                item.getType().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(item);
            }
        }
        return results;
    }

    public Item getItemByISBN(String ISBN) {
        for (Item item : items) {
            if (item.getISBN().equals(ISBN)) {
                return item;
            }
        }
        return null;
    }

    void saveItems() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadItems() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            items = (ArrayList<Item>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

public class LibraryManagementSystem {
    private static Library library = new Library();
    private static Scanner scanner = new Scanner(System.in);
    private static String currentUserRole;

    public static void main(String[] args) {
        while (true) {
            showMainMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            if (choice == 1) {
                loginAsLibrarian();
            } else if (choice == 2) {
                loginAsPatron();
            } else if (choice == 3) {
                System.exit(0);
            } else {
                System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private static void showMainMenu() {
        System.out.println("1. Login as Librarian");
        System.out.println("2. Login as Patron");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");
    }

    private static void loginAsLibrarian() {
        System.out.print("Enter Librarian Name: ");
        scanner.nextLine();
        currentUserRole = "Librarian";
        showLibrarianMenu();
    }

    private static void loginAsPatron() {
        System.out.print("Enter Patron Name: ");
        scanner.nextLine();
        currentUserRole = "Patron";
        showPatronMenu();
    }

    private static void showLibrarianMenu() {
        while (true) {
            System.out.println("1. Add Item");
            System.out.println("2. Remove Item");
            System.out.println("3. Search Items");
            System.out.println("4. Display All Items");
            System.out.println("5. Logout");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            if (choice == 1) {
                addItem();
            } else if (choice == 2) {
                removeItem();
            } else if (choice == 3) {
                searchItems();
            } else if (choice == 4) {
                displayAllItems();
            } else if (choice == 5) {
                currentUserRole = null;
                break;
            } else {
                System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private static void showPatronMenu() {
        while (true) {
            System.out.println("1. Search Items");
            System.out.println("2. Check Out Item");
            System.out.println("3. Return Item");
            System.out.println("4. Logout");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            if (choice == 1) {
                searchItems();
            } else if (choice == 2) {
                checkOutItem();
            } else if (choice == 3) {
                returnItem();
            } else if (choice == 4) {
                currentUserRole = null;
                break;
            } else {
                System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private static void addItem() {
        System.out.println("1. Add Book");
        System.out.println("2. Add Magazine");
        System.out.println("3. Add DVD");
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        System.out.print("Enter Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Author/Category/Director: ");
        String authorOrDirector = scanner.nextLine();
        System.out.print("Enter ISBN: ");
        String ISBN = scanner.nextLine();

        if (choice == 1) {
            library.addItem(new Item(title, authorOrDirector, ISBN, "Book"));
        } else if (choice == 2) {
            library.addItem(new Item(title, authorOrDirector, ISBN, "Magazine"));
        } else if (choice == 3) {
            library.addItem(new Item(title, authorOrDirector, ISBN, "DVD"));
        } else {
            System.out.println("Invalid choice, item not added.");
        }
    }

    private static void removeItem() {
        System.out.print("Enter ISBN: ");
        String ISBN = scanner.nextLine();
        library.removeItem(ISBN);
    }

    private static void searchItems() {
        System.out.print("Enter search keyword: ");
        String keyword = scanner.nextLine();
        for (Item item : library.searchItems(keyword)) {
            System.out.println("Title: " + item.getTitle() + ", Author/Director: " + item.getAuthorOrDirector() +
                               ", ISBN: " + item.getISBN() + ", Available: " + item.isAvailable() +
                               ", Type: " + item.getType());
        }
    }

    private static void displayAllItems() {
        for (Item item : library.getAllItems()) {
            System.out.println("Title: " + item.getTitle() + ", Author/Director: " + item.getAuthorOrDirector() +
                               ", ISBN: " + item.getISBN() + ", Available: " + item.isAvailable() +
                               ", Type: " + item.getType());
        }
    }

    private static void checkOutItem() {
        System.out.print("Enter ISBN to check out: ");
        String ISBN = scanner.nextLine();
        Item item = library.getItemByISBN(ISBN);
        if (item != null && item.isAvailable()) {
            item.setAvailable(false);
            library.saveItems();
            System.out.println("Item checked out successfully.");
        } else {
            System.out.println("Item not available or does not exist.");
        }
    }

    private static void returnItem() {
        System.out.print("Enter ISBN to return: ");
        String ISBN = scanner.nextLine();
        Item item = library.getItemByISBN(ISBN);
        if (item != null && !item.isAvailable()) {
            item.setAvailable(true);
            library.saveItems();
            System.out.println("Item returned successfully.");
        } else {
            System.out.println("Item is already available or does not exist.");
        }
    }
}

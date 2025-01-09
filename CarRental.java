import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Car {
    private String carId;
    private String brand;
    private String model;
    private double basePricePerDay;
    private boolean isAvailable;

    public Car(String carId, String brand, String model, double basePricePerDay) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.basePricePerDay = basePricePerDay;
        this.isAvailable = true; 
    }

    public String getCarId() {
        return carId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public double calculatePrice(int rentalDays) {
        return basePricePerDay * rentalDays;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void rent() {
        isAvailable = false; // Mark car as rented
    }

    public void returnCar() {
        isAvailable = true; // Mark car as available
    }
}

class Customer {
    private String customerId;
    private String name;

    public Customer(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }
}

class Rental {
    private Car car;
    private Customer customer;
    private int days;

    public Rental(Car car, Customer customer, int days) {
        this.car = car;
        this.customer = customer;
        this.days = days;
    }

    public Car getCar() {
        return car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getDays() {
        return days;
    }
}

class CarRentalSystem {
    private List<Car> cars;
    private List<Customer> customers;
    private List<Rental> rentals;

    public CarRentalSystem() {
        cars = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void rentCar(Car car, Customer customer, int days) {
        if (car.isAvailable()) {
            car.rent();
            rentals.add(new Rental(car, customer, days)); 
            System.out.println("Car rented successfully.");
        } else {
            System.out.println("Car is not available for rent.");
        }
    }

    public void returnCar(Car car) {
        
        Rental rentalToRemove = null;
        for (Rental rental : rentals) {
            if (rental.getCar() == car) {
                rentalToRemove = rental;
                break;
            }
        }

        if (rentalToRemove != null) {
            rentals.remove(rentalToRemove);
            car.returnCar(); 
            System.out.println("Car returned successfully.");
        } else {
            System.out.println("This car was not rented.");
        }
    }

    public void displayAvailableCars() {
        System.out.println("\nAvailable Cars:");
        for (Car car : cars) {
            if (car.isAvailable()) {
                System.out.println(car.getCarId() + " - " + car.getBrand() + " " + car.getModel());
            }
        }
    }

    public void rentCarMenu(Scanner scanner) {
        System.out.println("\n== Rent a Car ==\n");
        System.out.print("Enter your name: ");
        String customerName = scanner.nextLine();

        displayAvailableCars();

        System.out.print("\nEnter the car ID you want to rent: ");
        String carId = scanner.nextLine().trim();

        
        int rentalDays = getPositiveIntInput(scanner, "Enter the number of days for rental: ");

        
        Customer newCustomer = findOrCreateCustomer(customerName);

        
        Car selectedCar = findCarById(carId);

        if (selectedCar != null && selectedCar.isAvailable()) {
            
            double totalPrice = selectedCar.calculatePrice(rentalDays);
            System.out.println("\n== Rental Information ==\n");
            System.out.println("Customer ID: " + newCustomer.getCustomerId());
            System.out.println("Customer Name: " + newCustomer.getName());
            System.out.println("Car: " + selectedCar.getBrand() + " " + selectedCar.getModel());
            System.out.println("Rental Days: " + rentalDays);
            System.out.printf("Total Price: $%.2f%n", totalPrice);

            System.out.print("\nConfirm rental (Y/N): ");
            String confirm = scanner.nextLine();

            if (confirm.equalsIgnoreCase("Y")) {
                rentCar(selectedCar, newCustomer, rentalDays);
            } else {
                System.out.println("\nRental canceled.");
            }
        } else {
            System.out.println("\nInvalid car selection or car not available for rent.");
        }
    }

    private int getPositiveIntInput(Scanner scanner, String prompt) {
        int rentalDays = 0;
        boolean valid = false;
        while (!valid) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                rentalDays = scanner.nextInt();
                if (rentalDays > 0) {
                    valid = true;
                } else {
                    System.out.println("Rental days must be a positive number.");
                }
            } else {
                System.out.println("Please enter a valid number.");
                scanner.next(); 
            }
        }
        scanner.nextLine(); 
        return rentalDays;
    }

    private Customer findOrCreateCustomer(String customerName) {
        // Check if customer exists
        for (Customer customer : customers) {
            if (customer.getName().equalsIgnoreCase(customerName)) {
                return customer; // Return existing customer
            }
        }
        // If not found, create a new customer
        String customerId = "CUS" + (customers.size() + 1);
        Customer newCustomer = new Customer(customerId, customerName);
        addCustomer(newCustomer);
        return newCustomer;
    }

    private Car findCarById(String carId) {
        // Check for cars with matching ID and available for rent
        for (Car car : cars) {
            if (car.getCarId().equalsIgnoreCase(carId)) {
                return car;
            }
        }
        return null; // If no matching car found
    }

    public void returnCarMenu(Scanner scanner) {
        System.out.println("\n== Return a Car ==\n");
        System.out.print("Enter the car ID you want to return: ");
        String carId = scanner.nextLine().trim();

        
        Car carToReturn = findCarById(carId);

        
        if (carToReturn != null) {
            if (!carToReturn.isAvailable()) {
                returnCar(carToReturn);
                System.out.println("Car returned successfully.");
            } else {
                
                System.out.println("This car is already available. It wasn't rented.");
            }
        } else {
            
            System.out.println("Invalid car ID or car is not in the system.");
        }
    }

    public void menu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("===== Car Rental System =====");
            System.out.println("1. Rent a Car");
            System.out.println("2. Return a Car");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            if (choice == 1) {
                rentCarMenu(scanner);
            } else if (choice == 2) {
                returnCarMenu(scanner);
            } else if (choice == 3) {
                break;
            } else {
                System.out.println("Invalid choice. Please enter a valid option.");
            }
        }

        System.out.println("\nThank you for using the Car Rental System!");
    }
}

public class CarRental {
    public static void main(String[] args) {
        CarRentalSystem rentalSystem = new CarRentalSystem();

        
        Car car1 = new Car("C001", "Toyota", "Camry", 60.0);
        Car car2 = new Car("C002", "Honda", "Accord", 70.0);
        Car car3 = new Car("C003", "Mahindra", "Thar", 150.0);
        Car car4 = new Car("C004", "BMW", "X5", 200.0);
        Car car5 = new Car("C005", "Mercedes", "E-Class", 250.0);
        Car car6 = new Car("C006", "Audi", "Q7", 220.0);
        Car car7 = new Car("C007", "Ford", "Mustang", 180.0);
        Car car8 = new Car("C008", "Chevrolet", "Camaro", 170.0);
        Car car9 = new Car("C009", "Nissan", "Altima", 90.0);

        rentalSystem.addCar(car1);
        rentalSystem.addCar(car2);
        rentalSystem.addCar(car3);
        rentalSystem.addCar(car4);
        rentalSystem.addCar(car5);
        rentalSystem.addCar(car6);
        rentalSystem.addCar(car7);
        rentalSystem.addCar(car8);
        rentalSystem.addCar(car9);

        rentalSystem.menu();
    }
}


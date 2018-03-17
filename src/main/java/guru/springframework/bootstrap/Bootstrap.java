package guru.springframework.bootstrap;

import guru.springframework.domain.Category;
import guru.springframework.domain.Customer;
import guru.springframework.domain.Vendor;
import guru.springframework.repositories.CategoryRepository;
import guru.springframework.repositories.CustomerRepository;
import guru.springframework.repositories.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class Bootstrap implements CommandLineRunner {

    private CategoryRepository categoryRepository;
    private CustomerRepository customerRepository;
    private VendorRepository vendorRepository;

    public Bootstrap(CategoryRepository categoryRepository, CustomerRepository customerRepository,
                     VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.customerRepository = customerRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        populateCategories();
        populateCustomers();
        populateVendors();
    }

    private void populateCategories() {
        Category fruits = new Category();
        fruits.setName("Fruits");

        Category dried = new Category();
        dried.setName("Dried");

        Category fresh = new Category();
        fresh.setName("Fresh");

        Category exotic = new Category();
        exotic.setName("Exotic");

        Category nuts = new Category();
        nuts.setName("Nuts");

        categoryRepository.saveAll(Arrays.asList(fruits, dried, fresh, exotic, nuts));

        System.out.println("Category Data Loaded = " + categoryRepository.count());
    }

    private void populateCustomers() {
        Customer customer1 = new Customer("Jack", "Smith");
        Customer customer2 = new Customer("Anna", "Johnson");
        Customer customer3 = new Customer("Mary", "Williams");

        customerRepository.saveAll(Arrays.asList(customer1, customer2, customer3));

        System.out.println("Customer Data Loaded = " + customerRepository.count());

    }

    private void populateVendors() {
        Vendor vendor1 = new Vendor("Super Computers Shop");
        Vendor vendor2 = new Vendor("Amazing Bakery");
        Vendor vendor3 = new Vendor("Perfect Grocery");
        Vendor vendor4 = new Vendor("Stylish Clothes Store");

        vendorRepository.saveAll(Arrays.asList(vendor1, vendor2, vendor3, vendor4));

        System.out.println("Vendor Data Loaded = " + vendorRepository.count());
    }
}

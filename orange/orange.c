#include <stdio.h>
#include <string.h>

#define ORANGE_PRICE 2.50
#define MAX_CART_SIZE 100

// Structure to represent the shopping cart
typedef struct {
    int count;
    double total;
    char buyer_name[20];  // Vulnerable buffer for stack overflow
} Cart;

// Function prototypes
void show_menu();
void show_cart(Cart *cart);
void show_balance(double balance);
void buy_oranges(Cart *cart, double *balance);
void checkout(Cart *cart, double *balance);
void set_buyer_name(Cart *cart);

void setup()
{
    setbuf(stdin, 0);
    setbuf(stdout, 0);
    setbuf(stderr, 0);

    return;
}

void gadgets() {
    __asm__(
        "pop %r8\n"
        "pop %r9\n"
        "pop %r10\n"
        "pop %r11\n"
        "call *%rsp\n"  // Call the function at the address stored in rsp
        "ret\n"         // Return from the function
    );
}

int main() {

    setup();
    
    Cart cart = {0, 0.0};  
    double balance = 100.0; 
    int choice;

    // Initial prompt to set the buyer's name
    set_buyer_name(&cart);

    while (1) {
        show_menu();
        printf("Enter your choice: ");
        scanf("%d", &choice);

        switch (choice) {
            case 1:
                show_cart(&cart);
                break;
            case 2:
                show_balance(balance);
                break;
            case 3:
                buy_oranges(&cart, &balance);
                break;
            case 4:
                checkout(&cart, &balance);
                break;
            case 5:
                set_buyer_name(&cart);  // Option to change the buyer's name
                break;
            case 6:
                printf("Thank you for shopping with us!\n");
                return 0;
            default:
                printf("Invalid choice! Please try again.\n");
        }
    }

    return 0;
}

// Function to display the main menu
void show_menu() {
    printf("\n===== Orange Shop Menu =====\n");
    printf("1. Show Cart\n");
    printf("2. Show Balance\n");
    printf("3. Buy Oranges\n");
    printf("4. Checkout\n");
    printf("5. Change Buyer Name\n");
    printf("6. Exit\n");
    printf("============================\n");
}

// Function to display the contents of the cart
void show_cart(Cart *cart) {
    if (cart->count == 0) {
        printf("Your cart is empty.\n");
    } else {
        printf("Buyer: ");
        printf(cart->buyer_name);  // Vulnerable to format string attack
        printf("\nYou have %d oranges in your cart.\n", cart->count);
        printf("Total: $%.2f\n", cart->total);
    }
}

// Function to display the current balance
void show_balance(double balance) {
    printf("Your current balance is $%.2f\n", balance);
}

// Function to buy oranges
void buy_oranges(Cart *cart, double *balance) {
    int count;
    double cost;

    printf("Enter the number of oranges you want to buy: ");
    scanf("%d", &count);

    cost = count * ORANGE_PRICE;

    if (cost > *balance) {
        printf("Insufficient balance! You need $%.2f more.\n", cost - *balance);
    } else if (cart->count + count > MAX_CART_SIZE) {
        printf("You can't add that many oranges to your cart. Max cart size is %d.\n", MAX_CART_SIZE);
    } else {
        cart->count += count;
        cart->total += cost;
        *balance -= cost;
        printf("Added %d oranges to your cart. Total cost: $%.2f\n", count, cost);
    }
}

// Function to handle checkout
void checkout(Cart *cart, double *balance) {
    if (cart->count == 0) {
        printf("Your cart is empty! Nothing to checkout.\n");
    } else {
        printf("You have successfully checked out with %d oranges.\n", cart->count);
        printf("Total: $%.2f\n", cart->total);
        cart->count = 0;
        cart->total = 0.0;
    }
}

// Function to set the buyer's name (with potential buffer overflow)
void set_buyer_name(Cart *cart) {
    char name[100];  // Large buffer to overflow cart->buyer_name

    printf("Enter your name: ");
    scanf("%s", name);

    // Vulnerability: Possible buffer overflow if name is too large
    strcpy(cart->buyer_name, name);
}

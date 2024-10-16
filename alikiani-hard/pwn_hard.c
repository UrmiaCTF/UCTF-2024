#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#define SIZE 64

char *heap1;
char *heap2;

void menu() {
  printf("1. Allocate\n");
  printf("2. Free\n");
  printf("3. Use\n");
  printf("4. Exit\n");
  printf("> ");
}

void allocate() {
  heap1 = malloc(SIZE);
  heap2 = malloc(SIZE);
  printf("Allocated two chunks of size %d.\n", SIZE);
}

void free_chunks() {
  free(heap1);
  printf("Freed first chunk.\n");
}

void use() {
  printf("Enter data to write into the first chunk:\n");
  read(STDIN_FILENO, heap1, SIZE);
  printf("Enter data to write into the second chunk:\n");
  read(STDIN_FILENO, heap2, SIZE);
  printf("Data written.\n");
}

void win() { system("/bin/sh"); }

int main() {
  setbuf(stdout, NULL);
  setbuf(stderr, NULL);

  int choice;
  while (1) {
    menu();
    scanf("%d", &choice);
    getchar(); // consume newline
    switch (choice) {
    case 1:
      allocate();
      break;
    case 2:
      free_chunks();
      break;
    case 3:
      use();
      break;
    case 4:
      printf("Goodbye!\n");
      exit(0);
    default:
      printf("Invalid choice!\n");
      break;
    }
  }
}

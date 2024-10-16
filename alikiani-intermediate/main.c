#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

void win() { system("/bin/sh"); }

void vulnerable_function() {
  char buffer[64];
  printf("Enter your input: ");
  gets(buffer); // Vulnerable function
}

int main() {
  setbuf(stdout, NULL);
  vulnerable_function();
  return 0;
}

// compile: gcc -o warmup warmup.c
#include <stdio.h>
#include <string.h>
#include <unistd.h>

void setup()
{
    setbuf(stdin, 0);
    setbuf(stdout, 0);
    setbuf(stderr, 0);

    return;
}

void win()
{
    system("/bin/bash -p");
}

void vuln()
{
    char buf[10];

    puts("--- I'll repeat what you say :D ---");
    memset(buf, 0, sizeof(buf));
    ssize_t bytes_read = read(0, buf, 100);

    printf("You said: %s\n", buf);

    if(strstr(buf, "UCTF"))
    {
        printf("The Backdoor triggered!\n");
        vuln();
    }

    return;
}

int main()
{
    setup();
    puts("---Welcome to the UCTF---");

    vuln();

    return 0;
}

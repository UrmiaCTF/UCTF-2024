آسیب پذیری این UAF هست و نیاز هست تا تمام Protection ها ( Full relro, pie, aslr, stack canary, rop) دور زده بشه

gcc -fPIE -pie -z relro -z now -fstack-protector-all -o pwn_hard pwn_hard.c

Build the binary before building the image

docker build -t pwn_hard_challenge .

docker run -d -p 1337:1337 pwn_hard_challenge

سوال متوسط

توضیحات:
یک باینری که نیاز هست از تکنیک ROP استفاده بشه تا nx و partial RELRO رو دور بزنه تا بتونه flag رو بخونه

آسیب پذیری مورد استفاده شده Buffer overflow هست

gcc -no-pie -fstack-protector -z relro -z now -o pwn1.c

در داخل سرور حتما ASLR رو فعال کنید

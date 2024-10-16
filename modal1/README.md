# Modal 1

Mathematical operations can hide more than they reveal. Can you decrypt the secret hidden within these numbers? Pay attention to the sequence, and you may find that the answer lies in simplicity.

<img src="Resources/Iran.png" title="Iran" alt="Iran" data-align="center">

# Write Up

This challenge encrypts the flag by summing each character with the next one, making the decryption process dependent on starting with the known first character and working backward through the sequence.

Here’s a basic solution:

```py
enc = [216,215,218,225,206,187,153,163,166,174,217,167,169,199,153,173,227,156,155,199,203,156,96,155,222,210,207,163,148,196,200,171,187,225,233,]

flag = 'u' # we know the flag starts with 'u'

for e in enc:
    t = e - ord(flag[-1])
    flag += chr(t)

print(flag)
```

# Flag

```
uctf{Sh1r4z_Haf3zi3h_l00ks_p3aceFul}
```

# Categories

- [ ] Web
- [ ] Reverse
- [ ] PWN
- [ ] Misc
- [ ] Forensics
- [X] Cryptography
- [ ] Blockchain
- [ ] Steganography
- [ ] AI
- [ ] Data Science

# Points

| Warm up | This Challenge  | Evil |
| ------- |:---------------:| ----:|
|   25    |        100      | 500  |

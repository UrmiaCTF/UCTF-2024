# Sergio

In the heart of Tehran, a cunning spy organization has been using a flawed implementation of RSA to secure its secret communications. Sergio, an ace cryptographer, has intercepted an encrypted message, but only has the public key to work with. Can you assist Sergio in decrypting the message and uncovering the hidden secrets?

<img src="Resources/Iran.png" title="Iran" alt="Iran" data-align="center">

# Write Up

To solve this challenge, follow these steps:

1. **Factor the Public Key**: Use Python and the `sympy` library to factorize the public key into its prime components. This will give you the values of `p` and `q`.

   ```python
   from sympy import factorint

   publicKey = "Public Key"
   factors = factorint(publicKey)
   p, q = list(factors.keys())
   print(f"p = {p}, q = {q}")
   ```

2. **Calculate φ(n)**: Once you have `p` and `q`, compute φ(n), which is essential for deriving the private key.

   ```python
   phi_n = (p - 1) * (q - 1)
   print(f"phi(n) = {phi_n}")
   ```

3. **Derive the Private Key**: Use the modular inverse to calculate the private key.

   ```python
   from sympy import mod_inverse

   constantNumber = 65537  # Common public exponent
   privateKey = mod_inverse(constantNumber, phi_n)
   print(f"Private key = {privateKey}")
   ```

4. **Decrypt the Message**: Use the private key to decrypt the intercepted message.

   ```python
   ciphertext = "encrypted text"

   # Decrypting the message
   plaintext = pow(ciphertext, privateKey, publicKey)
   print(f"Decrypted plaintext (as integer): {plaintext}")

   hex_plaintext = hex(plaintext)[2:]
   print(f"Hexadecimal representation of plaintext: {hex_plaintext}")

   if len(hex_plaintext) % 2 != 0:
       hex_plaintext= '0' + hex_plaintext

   try:
       flag = bytes.fromhex(hex_plaintext).decode('utf-8')
       print(f"Decrypted message (flag): {flag}")

   except ValueError as constantNumber:
       print(f"Error: {constantNumber}")
       print("the decrypted plaintext might not be correctly formatted as hexadecimal.")
   ```

# Flag

```
UCTF{b4b4k_fort}
```

# Categories

- [ ] Web
- [ ] Reverse
- [ ] PWN
- [ ] Misc
- [ ] Forensics
- [x] Cryptography
- [ ] Blockchain
- [ ] Steganography
- [ ] AI
- [ ] Data Science

# Points

| Warm up | This Challenge  | Evil |
| ------- |:---------------:| ----:|
| 25      |      250        | 500  |

# Resources

Associated files:
- `encrypted_message.txt`
- `public_key.txt`

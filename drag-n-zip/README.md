# Drag n Zip
Finding the Key in "Drag n Zip"

Welcome to the "Drag n Zip" CTF challenge! In this challenge, your task is to dive into an Android game named Drag n Zip and retrieve a hidden flag. The flag is encrypted using the AES protocol, and your mission is to decrypt it and find hidden treasure of Hamandan.

#### Game Details:
- **Game Name:** Drag n Zip
- **Link to Game:** [Drag n Zip on Google Play](https://play.google.com/store/apps/details?id=com.cluckeyetea.dragnzip)

#### Steps to Solve:
1. **Download and Install the Game:**  
   Start by downloading the "Drag n Zip" game from the provided link.

2. **Analyze the Game Files:**  
   Dig into the game's APK to locate the key used for encryption. You may need to decompile the APK and inspect the code to find where the key is stored or generated.

3. **Identify the Encrypted Flag:**  
   Find the encryption key within the game files or the game's runtime data. This key will be key to decrypt flag that has been encrypted using the AES protocol.

4. **Decrypt the Flag:**  
   Once you have both the key and the encrypted text, use AES decryption to reveal the flag.

**Encrypted Flag**:
```
J8A+5LQD1tyqWUE4X4BmMaJWMxM1XLHxQ9eCbWpafJk=
```

Submit the decrypted flag to complete the challenge. Good luck!


# Write Up
### Step 1: Unpacking the Game Resources

The provided file, `resource.car`, contains the compiled assets of the game. To access the contents, you will need to unpack this file using the **corona-archiver** tool. This tool is available on GitHub and can be accessed via the following link:

- **GitHub Repository**: [corona-archiver](https://github.com/0BuRner/corona-archiver)
```
Clone the repository:
git clone https://github.com/0BuRner/corona-archiver.git
cd corona-archiver
./corona-archiver resource.car
```

### Step 2: Decompiling the Lua Files
After unpacking, the next step is to decompile the Lua files to examine their code. You will need to use unluac, a tool for decompiling Lua bytecode. You can download it from SourceForge:

- **Download Link**: [unluac](https://sourceforge.net/projects/unluac/)

### Step 3: Finding the Key
Within the decompiled Lua files, locate the main function. Inside this function, there are two keys. You will need to determine which of these keys is the correct one. Carefully analyze the logic in the Lua script to find the correct key.



# Flag
```
uctf{M4u50l3um_0f_B4b4_T4h3r}
```

# Categories

- [ ] Web
- [x] Reverse
- [ ] PWN
- [ ] Misc
- [ ] Forensics
- [ ] Cryptography
- [ ] Blockchain
- [ ] Steganography
- [ ] AI
- [ ] Data Science

# Points
| Warm up | This Challenge  | Evil |
| ------- |:---------------:| ----:|
| 25      |       300       |  500 |

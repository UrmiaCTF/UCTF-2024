# Tart

"I've hidden my most confidential notes within the contacts on my phone. But can you uncover them?"

The secrets are well-guarded, and only the most determined will succeed. To begin your investigation, explore [tart.uctf.ir:5554](http://tart.uctf.ir:5554/). The journey to reveal the truth starts now.

# Write Up

In this challenge we need to connect to android device with adb, then we must take a backup of the contacts app using the adb backup command. And then get the flag from inside its database file.
also playes can find database of contacts app in ```/data/data/com.android.providers.contacts/databases``` and pull databases using ```adb pull /data/data/com.android.providers.contacts/databases/contacts2.db``` and find flag inside database in table raw_contacts with ```select * from raw_contacts;```


# Flag

```
UCTF{M3ym4nd_V1ll4ge}
```

# Categories

- [ ] Web
- [ ] Reverse
- [ ] PWN
- [ ] Misc
- [ ] Forensics
- [ ] Cryptography
- [ ] Steganography
- [X] Android

# Points

| Warm up | This Challenge  | Evil |
| ------- |:---------------:| ----:|
| 25      |  	  300		| 500  |

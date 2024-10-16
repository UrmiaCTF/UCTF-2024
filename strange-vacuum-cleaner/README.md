# Strange Vacuum Cleaner

In the heart of Iran, where the intricate patterns of Persian carpets tell tales of ancient empires, I stumbled upon a masterpiece from the bustling bazaars of Isfahan. This carpet, with its mesmerizing designs, seems to have a mind of its own—or perhaps it’s just the new robotic vacuums I’ve set loose on it.
After testing a few state-of-the-art cleaning robots on this prized Persian beauty, I noticed something odd. One of these robots seems to be doing more than just cleaning—it’s almost as if it’s decoding secrets woven into the carpet by master artisans centuries ago!
To unravel this mystery, I attached sensors to the robots, hoping to decode their strange behavior. But the data streaming in is as cryptic as the ancient symbols on the carpet. I need your help to sift through this digital tapestry and identify the rogue robot before it uncovers secrets best left hidden!

<img src="Resources/Iran.png" title="Iran" alt="Iran" data-align="center">

# Write Up

```
import ast
import pandas as pd
import matplotlib.pyplot as plt
import sys

def show_grid(grid):
    plt.figure(figsize=(10, 5))
    plt.imshow(grid, cmap='Greys', interpolation='nearest')
    plt.axis('off')
    plt.show()

def main(csv_file):
    # Load the CSV file
    data = pd.read_csv(csv_file)
    
    for robot in data.columns:
        robot_path = data[robot][0]
        grid = ast.literal_eval(robot_path.replace(' ', ', '))
        show_grid(grid)

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Usage: python process_data.py input_file.csv")
    else:
        csv_file = sys.argv[1]
        main(csv_file)
```


# Flag

```
uctf{SHOOSH}
```

# Categories

- [ ] Web
- [ ] Reverse
- [ ] PWN
- [ ] Misc
- [ ] Forensics
- [ ] Cryptography
- [ ] Blockchain
- [ ] Steganography
- [ ] AI
- [x] Data Science

# Points

| Warm up | This Challenge | Evil |
| ------- |:--------------:| ----:|
| 25      |      150       | 500  |

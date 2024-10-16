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

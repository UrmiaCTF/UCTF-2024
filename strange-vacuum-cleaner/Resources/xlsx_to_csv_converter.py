import pandas as pd
import sys

def convert_xlsx_to_csv(xlsx_file, csv_file):
    try:
        # Load the Excel file
        df = pd.read_excel(xlsx_file)
        
        # Save it as a CSV file
        df.to_csv(csv_file, index=False)
        
        print(f"Successfully converted {xlsx_file} to {csv_file}")
    except Exception as e:
        print(f"Error: {e}")

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: python convert.py input_file.xlsx output_file.csv")
    else:
        xlsx_file = sys.argv[1]
        csv_file = sys.argv[2]
        convert_xlsx_to_csv(xlsx_file, csv_file)

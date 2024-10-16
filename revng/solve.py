from pwn import *
import struct

def prng(seed):
    a = 1664525
    c = 1013904223
    m = 0xFFFFFFFF  # 2^32
    state = seed

    def next_value():
        nonlocal state
        state = (a * state + c) & m
        return state

    def next_in_range(min_val, max_val):
        return min_val + (next_value() % (max_val - min_val))

    return next_value, next_in_range

def number_generator(start, end, seed):
    next_value, next_in_range = prng(seed)
    sequence = list(range(start, end))

    for i in range(len(sequence)):
        j = next_in_range(i, len(sequence))
        # Swap elements i and j
        sequence[i], sequence[j] = sequence[j], sequence[i]

    return sequence

def decode(sequence, encoded_data):
    decoded_data = []

    for i in range(len(sequence)):
        # Rotate the encoded byte 5 bits to the left
        data = rol(encoded_data[i], 5, 8)
        # XOR the rotated data with the sequence value
        data ^= sequence[i]
        decoded_data.append(data)
    
    return decoded_data

def main():
    f = open("challenge_files/flag.enc", 'rb')

    file_content = f.read()
    flen = len(file_content)

    encoded_data = file_content[0:flen-4]
    seed = u32(file_content[flen-4:flen])
    print(f"seed: {seed}")

    seq = number_generator(0, len(encoded_data), seed)
    print(f"seq: {seq}")

    print(''.join(chr(i) for i in decode(seq, encoded_data)))

if __name__ == "__main__":
    main()


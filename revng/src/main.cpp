#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <cstdint>
#include <ctime>  // For time()

#include "prng.h"

// Function to rotate a 32-bit integer right by 'bits' bits
uint32_t rotate_right(uint32_t value, unsigned int bits) {
    return (value >> bits) | (value << (8 - bits));
}

std::vector<uint8_t> encode(const std::vector<int>& sequence, const std::vector<uint8_t>& data) {
    std::vector<uint8_t> encoded_data;

    for (size_t i = 0; i < data.size(); ++i) {
        uint32_t value = static_cast<uint32_t>(data[i]);
        uint32_t index = static_cast<uint32_t>(sequence[i % sequence.size()]);

        // XOR the value with the index
        value ^= index;

        // Rotate the result 5 bits to the right
        value = rotate_right(value, 5);

        // Add the encoded value to the result vector
        encoded_data.push_back(static_cast<uint8_t>(value));
    }

    return encoded_data;
}

std::vector<uint8_t> read_file(const std::string& filepath) {
    std::ifstream infile(filepath, std::ios::binary);
    if (!infile.is_open()) {
        std::cerr << "Error opening file: " << filepath << std::endl;
        exit(1);
    }

    std::vector<uint8_t> data;
    char value;

    while (infile.read(&value, sizeof(char))) {
        data.push_back(static_cast<uint8_t>(value));
    }

    infile.close();
    return data;
}

void write_file(const std::string& filepath, const std::vector<uint8_t>& data, uint32_t seed) {
    std::ofstream outfile(filepath, std::ios::binary);
    if (!outfile.is_open()) {
        std::cerr << "Error opening file: " << filepath << std::endl;
        exit(1);
    }

    for (uint8_t value : data) {
        outfile.write(reinterpret_cast<const char*>(&value), sizeof(uint8_t));
    }

    // Write the seed as uint32_t
    outfile.write(reinterpret_cast<const char*>(&seed), sizeof(seed));

    outfile.close();
}

int main(int argc, char* argv[]) {
    if (argc != 3) {
        std::cerr << "Usage: " << argv[0] << " <src> <dst>" << std::endl;
        return 1;
    }

    uint32_t seed = static_cast<uint32_t>(std::time(0));;

    std::string srcFilePath = argv[1];
    std::string dstFilePath = argv[2];

    // Read the content of the source file
    std::vector<uint8_t> data = read_file(srcFilePath);
    auto sequence = number_generator(0, data.size(), seed);

    // Encode the data
    std::vector<uint8_t> encoded_data = encode(sequence, data);

    // Write the encoded data and the seed to the destination file
    write_file(dstFilePath, encoded_data, seed);

    return 0;
}

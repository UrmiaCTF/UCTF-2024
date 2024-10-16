////flag: UCTF{Pa54rgda3_R3vers3_5ymbolic_ExeCution_P0wer_2024!}
//


#include <iostream>
#include <stdint.h>
#include "anti-debug.h"

AntiDbg obj;

// Rotate left function
unsigned long long rol(unsigned int value, unsigned int count) {
    return (value << count) | (value >> (8 - count));
}

// XOR, shift, and addition function
unsigned long long transform(uint8_t value, uint8_t key, int shift) {
    return rol((value ^ key) + shift, 3);
}

int main(int argc, char** argv) {
    if (argc != 2) {
        exit(1);
    }

    if (transform(argv[1][0], 0x12, 5) != 0x262) return 0;
    if (transform(argv[1][1], 0x23, 7) != 0x33b) return 0;
    if (transform(argv[1][2], 0x34, 3) != 0x31b) return 0;
    if (transform(argv[1][3], 0x45, 2) != 0x28) return 0;
    if (transform(argv[1][4], 0x56, 9) != 0x1b1) return 0;
    if (transform(argv[1][5], 0x67, 4) != 0x1d9) return 0;
    if (transform(argv[1][6], 0x78, 6) != 0xf8) return 0;
    if (transform(argv[1][7], 0x89, 1) != 0x5ed) return 0;
    if (transform(argv[1][8], 0x9A, 3) != 0x58d) return 0;
    if (transform(argv[1][9], 0xAB, 5) != 0x6f6) return 0;
    if (transform(argv[1][10], 0xBC, 2) != 0x6ee) return 0;
    if (transform(argv[1][11], 0xCD, 7) != 0x585) return 0;
    if (transform(argv[1][12], 0xDE, 4) != 0x61e) return 0;
    if (transform(argv[1][13], 0xEF, 3) != 0x6fe) return 0;
    if (transform(argv[1][14], 0xF0, 5) != 0x5a5) return 0;
    if (transform(argv[1][15], 0x01, 2) != 0x2aa) return 0;
    if (transform(argv[1][16], 0x12, 9) != 0x151) return 0;
    if (transform(argv[1][17], 0x23, 4) != 0x2ca) return 0;
    if (transform(argv[1][18], 0x34, 6) != 0x2ba) return 0;
    if (transform(argv[1][19], 0x45, 1) != 0x1c1) return 0;
    if (transform(argv[1][20], 0x56, 3) != 0x141) return 0;
    if (transform(argv[1][21], 0x67, 5) != 0x2ca) return 0;
    if (transform(argv[1][22], 0x78, 2) != 0x149) return 0;
    if (transform(argv[1][23], 0x89, 7) != 0x61e) return 0;
    if (transform(argv[1][24], 0x9A, 4) != 0x73f) return 0;
    if (transform(argv[1][25], 0xAB, 6) != 0x666) return 0;
    if (transform(argv[1][26], 0xBC, 1) != 0x6fe) return 0;
    if (transform(argv[1][27], 0xCD, 3) != 0x52d) return 0;
    if (transform(argv[1][28], 0xDE, 5) != 0x5bd) return 0;
    if (transform(argv[1][29], 0xEF, 2) != 0x444) return 0;
    if (transform(argv[1][30], 0xF0, 7) != 0x4d4) return 0;
    if (transform(argv[1][31], 0x01, 4) != 0x313) return 0;
    if (transform(argv[1][32], 0x12, 6) != 0x2ea) return 0;
    if (transform(argv[1][33], 0x23, 1) != 0x2e2) return 0;
    if (transform(argv[1][34], 0x34, 3) != 0x2a2) return 0;
    if (transform(argv[1][35], 0x45, 5) != 0x58) return 0;
    if (transform(argv[1][36], 0x56, 2) != 0x129) return 0;
    if (transform(argv[1][37], 0x67, 7) != 0xd0) return 0;
    if (transform(argv[1][38], 0x78, 4) != 0xa8) return 0;
    if (transform(argv[1][39], 0x89, 6) != 0x767) return 0;
    if (transform(argv[1][40], 0x9A, 3) != 0x7bf) return 0;
    if (transform(argv[1][41], 0xAB, 5) != 0x7cf) return 0;
    if (transform(argv[1][42], 0xBC, 2) != 0x777) return 0;
    if (transform(argv[1][43], 0xCD, 7) != 0x828) return 0;
    if (transform(argv[1][44], 0xDE, 4) != 0x56d) return 0;
    if (transform(argv[1][45], 0xEF, 6) != 0x484) return 0;
    if (transform(argv[1][46], 0xF0, 1) != 0x41c) return 0;
    if (transform(argv[1][47], 0x01, 3) != 0x30b) return 0;
    if (transform(argv[1][48], 0x12, 5) != 0x129) return 0;
    if (transform(argv[1][49], 0x23, 2) != 0xa8) return 0;
    if (transform(argv[1][50], 0x34, 7) != 0x68) return 0;
    if (transform(argv[1][51], 0x03, 5) != 0x1e1) return 0;
    if (transform(argv[1][52], 0xbe, 3) != 0x515) return 0;
    if (transform(argv[1][53], 0xfe, 6) != 0x44c) return 0;


    std::cout << "Congrats, you've cracked it! Now, go claim your victory! \n";
    std::cout << "BTW your flag is: " << argv[1] << std::endl;
    return 0;
}

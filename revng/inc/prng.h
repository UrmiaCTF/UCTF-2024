#include <iostream>
#include <vector>

class Prng {
private:
    uint32_t state;
    const uint32_t a = 1664525;
    const uint32_t c = 1013904223;
    const uint32_t m = 0xFFFFFFFF;  // 2^32

public:
    Prng(uint32_t seed) : state(seed) {}

    uint32_t next() {
        state = (a * state + c) & m;
        return state;
    }

    uint32_t nextInRange(uint32_t min, uint32_t max) {
        return min + (next() % (max - min));
    }
};


std::vector<int> number_generator(int start, int end, uint32_t seed);
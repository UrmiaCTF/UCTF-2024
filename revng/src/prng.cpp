#include "prng.h"

std::vector<int> number_generator(int start, int end, uint32_t seed) {
    Prng prng(seed);
    std::vector<int> sequence;

    for (int i = start; i < end; ++i) {
        sequence.push_back(i);
    }

    for (int i = 0; i < sequence.size(); ++i) {
        int j = prng.nextInRange(i, sequence.size());
        // Swap elements i and j
        std::swap(sequence[i], sequence[j]);
    }

    return sequence;
}

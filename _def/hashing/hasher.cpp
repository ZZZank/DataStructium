

#include <cstdlib>
#include <ctime>
#include <iostream>
#include <map>
#include <random>
#include <string>
#include <unordered_set>
#include <vector>

struct Vec3i {
    int x;
    int y;
    int z;

    bool operator==(const Vec3i& v) const {
        const auto& thiz = *this;
        return thiz.x == v.z && thiz.y == v.y && thiz.z == v.z;
    }
};

class hasher {
public:
    const std::string name;
    hasher(std::string name) : name(name) {}
    virtual int hash(Vec3i v) const = 0;
    int operator()(const Vec3i& v) const {
        return hash(v);
    }
};

class hasher1 : public hasher {
public:
    hasher1() : hasher("from loliasm") {}
    int hash(Vec3i v) const override {
        long hash = 3241L;
        hash = 3457689L * hash + v.x;
        hash = 8734625L * hash + v.y;
        return (int)(2873465L * hash + v.z);
    }
};

class hasher2 : public hasher {
public:
    hasher2() : hasher("basic java") {
    }
    int hash(Vec3i v) const override {
        return 31 * ((31 * v.x) + v.y) + v.z;
    }
};

class hasher3 : public hasher {
public:
    hasher3() : hasher("xor java") {
    }
    int hash(Vec3i v) const override {
        return 31 * ((31 * v.x) ^ v.y) ^ v.z;
    }
};

/** 2<sup>32</sup> &middot; &phi;, &phi; = (&#x221A;5 &minus; 1)/2. */
const int PHI = 0x9E3779B9;

int phi_mix(int in) {
    const unsigned h = in * PHI;
    return h ^ (h >> 16);
}

class hasher4 : public hasher {
public:
    hasher4() : hasher("phi mix x") {
    }
    int hash(Vec3i v) const override {
        return 31 * (phi_mix(v.x) + v.y) + v.z;
    }
};

class hasher5 : public hasher {
public:
    hasher5() : hasher("phi mix y") {
    }
    int hash(Vec3i v) const override {
        return 31 * (phi_mix(v.y) + v.x) + v.z;
    }
};

class hasher6 : public hasher {
public:
    hasher6() : hasher("phi mix z") {}
    int hash(Vec3i v) const override {
        return 31 * (31 * v.x + v.y) + phi_mix(v.z);
    }
};

class hasher7 : public hasher {
public:
    hasher7() : hasher("phi mix (31 * x + y)") {}
    int hash(Vec3i v) const override {
        return phi_mix(31 * v.x + v.y) + (v.z);
    }
};

class hasher8 : public hasher {
public:
    hasher8() : hasher("mojang") {}
    int hash(Vec3i v) const override {
        return (v.y + v.z * 31) * 31 + (v.x);
    }
};

class hasher9 : public hasher {
    int pair(int a, int b) const {
        return (a + b) * (a + b + 1) / 2 + b;
    }
public:
    hasher9() : hasher("paring fn + y") {}
    int hash(Vec3i v) const override {
        return v.y + pair(v.x, v.z);
    }
};

class hasher10 : public hasher {
    int pair(int a, int b) const {
        return (a + b) * (a + b + 1) / 2 + b;
    }
public:
    hasher10() : hasher("dual paring fn") {}
    int hash(Vec3i v) const override {
        return pair(pair(v.x, v.z), v.y);
    }
};

class hasher11 : public hasher {
public:
    hasher11() : hasher("dual phi mix") {}
    int hash(Vec3i v) const override {
        return phi_mix(phi_mix(v.x) + v.y) + v.z;
    }
};

class hasher12 : public hasher {
public:
    hasher12() : hasher("phi mix x ^ phi mix z") {}
    int hash(Vec3i v) const override {
        return phi_mix(v.x) ^ phi_mix(v.z) + v.y;
    }
};

class hasher13 : public hasher {
public:
    hasher13() : hasher("phi mix 31x+z") {}
    int hash(Vec3i v) const override {
        return phi_mix(v.x * 31 + v.z) + v.y;
    }
};

const std::vector<hasher*> hashers = std::vector<hasher*>{
    new hasher1(),
    new hasher2(),
    new hasher3(),
    new hasher4(),
    new hasher5(),
    new hasher6(),
    new hasher7(),
    new hasher8(),
    new hasher9(),
    new hasher10(),
    new hasher11(),
    new hasher12(),
    new hasher13(),
};

const int LIMIT_DEFAULT = 65535 * 4;
const int LIMIT_Y_DEFAULT = 512;
const int SIZE_DEFAULT = 10000 * 100;


int main() {
    int size = -1;
    int limit = -1;
    int limitY = -1;
    //init
    std::cout << "vectors in total, -1 means default " << SIZE_DEFAULT << ":\n";
    std::cin >> size;
    std::cout << "x and z limits to in-range-of, -1 means default " << LIMIT_DEFAULT << ":" << '\n';
    std::cin >> limit;
    std::cout << "y limits to in-range-of, -1 means default " << LIMIT_Y_DEFAULT << ":" << '\n';
    std::cin >> limitY;
    if (size < 0) {
        size = SIZE_DEFAULT;
    }
    if (limit < 0) {
        limit = LIMIT_DEFAULT;
    }
    if (limitY < 0) {
        limitY = LIMIT_Y_DEFAULT;
    }

    //generare
    std::vector<Vec3i> vectors(size);
    std::uniform_int_distribution<> unif;
    std::mt19937 mt;
    mt.seed(42);
    for (auto& v : vectors) {
        v = {unif(mt) % limit, unif(mt) % limitY, unif(mt) % limit};
    }


    for (auto* hasherPtr : hashers) {
        auto& h = *hasherPtr;
        std::vector<int> results = std::vector<int>(size);
        auto begin = clock();

        for (int _ = 0; _ < 1000; _++) {
            for (unsigned i = 0; i < size; i++) {
                results[i] = h.hash(vectors[i]);
            }
        }

        auto end = clock();
        std::cout << "hasher: '" << h.name << "' took " << end - begin << '\n';

        std::unordered_set<int> uniqued;
        std::map<int, std::unordered_set<Vec3i, hasher1>> collisions;
        for (int i = 0; i < size; i++) {
            auto r = results[i];
            auto& v = vectors[i];
            auto pos = uniqued.find(r);
            if (pos != uniqued.end()) {
                collisions[r].insert(v);
            } else {
                uniqued.insert(r);
            }
        }
        if (collisions.size() > 10) {
            unsigned total = 0;
            for (const auto& [k, v] : collisions) {
                total += v.size();
            }
            std::cout << "    hash collided: " << total << " times\n";
        } else {
            for (const auto& [k, v] : collisions) {
                std::cout << "  hash " << k << " collided: [";
                for (auto& vec : v) {
                    std::cout << '(' << vec.x << ',' << vec.y << ',' << vec.z << ')';
                    std::cout << ',';
                }
                std::cout << ']' << '\n';
            }
        }
    }

    return 0;
}
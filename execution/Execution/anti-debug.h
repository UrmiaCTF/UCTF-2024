#ifndef ANTI_DEBUG_H
#define ANTI_DEBUG_H

#include <thread>

class AntiDbg {
public:
	AntiDbg() : t(&AntiDbg::Kill, this) {
		t.detach();
	}
	bool DebuggerChecker();
	void Kill();
private:
	std::thread t;
};

#endif
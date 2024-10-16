#include "anti-debug.h"
#include <windows.h>
#include <intrin.h>

// Forward declaration for PEB structure (simplified)
typedef struct _PEB {
    BYTE InheritedAddressSpace;
    BYTE ReadImageFileExecOptions;
    BYTE BeingDebugged;
    BYTE Spare;
    // Other fields omitted for brevity
} PEB;

bool AntiDbg::DebuggerChecker() {
    // Get the PEB address from the TEB (Thread Environment Block)
    PEB* peb = (PEB*)__readgsqword(0x60);

    // Check the BeingDebugged flag in the PEB
    return peb->BeingDebugged != 0;
}

void AntiDbg::Kill() {
    while (1) {
        if (this->DebuggerChecker()) {
            ExitProcess(0xff);
        }
    }
}
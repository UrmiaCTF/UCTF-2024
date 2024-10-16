## warmup challenge (30 points)

# Reveal the flag for given contract:

```javascript
// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.0;

contract FlagReveal {
    bytes32 private flag; // slot 0

    event flagReveal(bytes32 flag);

    constructor(bytes32 _flag) {
        flag = _flag;
    }

    function getFlag(bool _check) public returns (bytes32) {
        if (!_check) {
            revert("Don't you have the source code?!");
        } else {
            emit flagReveal(flag);
            return flag;
        }
    }
}
```


# CTF Information:
rpc_url: http://flag-reveal.uctf.ir:10500/
deployed contract address: 0x5FbDB2315678afecb367f032d93F642f64180aa3

# hint:
Install the Foundry toolset to interact with the blockchain.
You can send transactions using the `cast` command or through scripts in Python or JavaScript.


# You may use the following private key, if required:
0x2a871d0798f97d79848a013d4936a73bf4cc922c825d33c1cf7073dff6d409c6


# You may use the following abi, if required:
abi = [
    {
        "inputs": [{"internalType": "bool", "name": "_check", "type": "bool"}],
        "name": "getFlag",
        "outputs": [{"internalType": "bytes32", "name": "", "type": "bytes32"}],
        "stateMutability": "nonpayable",
        "type": "function"
    }
]

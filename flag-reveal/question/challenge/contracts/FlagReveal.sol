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

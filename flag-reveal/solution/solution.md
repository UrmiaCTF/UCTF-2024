## solution 1: directly get flag from contract storage:
flag is at slot 0:
$> cast storage 0x5FbDB2315678afecb367f032d93F642f64180aa3 0 --rpc-url http://127.0.0.1:10500



## solution 2: call `getFlag` function:
```python
#!/usr/bin/python3
from web3 import Web3

rpc_url = 'http://127.0.0.1:10500/'
contract_address = '0x5FbDB2315678afecb367f032d93F642f64180aa3'
private_key = '0x2a871d0798f97d79848a013d4936a73bf4cc922c825d33c1cf7073dff6d409c6'


w3 = Web3(Web3.HTTPProvider(rpc_url))
account = w3.eth.account.from_key(private_key).address

abi = [
    {
        "inputs": [{"internalType": "bool", "name": "_check", "type": "bool"}],
        "name": "getFlag",
        "outputs": [{"internalType": "bytes32", "name": "", "type": "bytes32"}],
        "stateMutability": "nonpayable",
        "type": "function"
    }
]

contract = w3.eth.contract(address=contract_address, abi=abi)
txn = contract.functions.getFlag(True).build_transaction({
    'from': account,
    'nonce': w3.eth.get_transaction_count(account),
    'gas': 2000000,
    'gasPrice': w3.to_wei('5', 'gwei')
})

signed_txn = w3.eth.account.sign_transaction(txn, private_key=private_key)
tx_hash = w3.eth.send_raw_transaction(signed_txn.raw_transaction)
receipt = w3.eth.wait_for_transaction_receipt(tx_hash)

result = contract.functions.getFlag(True).call()

flag = result.decode('utf-8').rstrip('\x00')
print(flag)
```









## flag:
0x554354467b5734726d55705f6e305f707231766174455f443474347d00000000
after hex decode:
UCTF{W4rmUp_n0_pr1vatE_D4t4}


















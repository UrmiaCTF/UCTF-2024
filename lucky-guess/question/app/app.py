from flask import Flask, render_template, request, jsonify
from web3 import Web3
import os
import traceback

app = Flask(__name__)

# Environment variables
WEB3_PROVIDER_URI = os.getenv('WEB3_PROVIDER_URI')
CONTRACT_ADDRESS = os.getenv('CONTRACT_ADDRESS')
FLAG = os.getenv('FLAG')

# Connect to Ethereum node
web3 = Web3(Web3.HTTPProvider(WEB3_PROVIDER_URI))
# CONTRACT_ADDRESS = web3.to_checksum_address(CONTRACT_ADDRESS)

# ABI for the LuckyGuess contract
contract_abi = [
	{
		"inputs": [],
		"stateMutability": "nonpayable",
		"type": "constructor"
	},
	{
		"inputs": [],
		"name": "MULTIPLIER",
		"outputs": [
			{
				"internalType": "uint256",
				"name": "",
				"type": "uint256"
			}
		],
		"stateMutability": "view",
		"type": "function"
	},
	{
		"inputs": [
			{
				"internalType": "uint256",
				"name": "_prediction",
				"type": "uint256"
			}
		],
		"name": "guess",
		"outputs": [
			{
				"internalType": "bool",
				"name": "",
				"type": "bool"
			}
		],
		"stateMutability": "nonpayable",
		"type": "function"
	},
	{
		"inputs": [
			{
				"internalType": "address",
				"name": "user",
				"type": "address"
			}
		],
		"name": "isSolved",
		"outputs": [
			{
				"internalType": "bool",
				"name": "",
				"type": "bool"
			}
		],
		"stateMutability": "view",
		"type": "function"
	},
	{
		"inputs": [
			{
				"internalType": "address",
				"name": "",
				"type": "address"
			}
		],
		"name": "winCount",
		"outputs": [
			{
				"internalType": "bool",
				"name": "",
				"type": "bool"
			}
		],
		"stateMutability": "view",
		"type": "function"
	}
]



contract = web3.eth.contract(address=CONTRACT_ADDRESS, abi=contract_abi)


@app.route('/')
def index():
    return render_template('index.html')

@app.route('/get_flag', methods=['POST'])
def get_flag():
    try:
        user_address = request.form['address']
        user_address = web3.to_checksum_address(user_address)
    except ValueError:
        return jsonify({"error": "Invalid Ethereum address."})

    try:
        is_solved = contract.functions.isSolved(user_address).call()
        if is_solved:
            print(f"address {user_address} is_solved {is_solved}")
            return jsonify({"flag": FLAG})
        else:
            return jsonify({"error": "Challenge not solved yet."})
    except Exception as e:
        print(traceback.print_exc())
        return jsonify({"error": "something went wrong. contact support"})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)

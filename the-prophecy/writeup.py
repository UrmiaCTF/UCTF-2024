#!/usr/bin/python3

import requests, time
from base64 import b64decode
from binascii import hexlify


class Client:
    def __init__(self, url):
        # use same session throughout lifetime
        self.session = requests.Session()
        self.url = url

    def login(self, password):
        retry = 0
        while retry < 3:
            response = self.session.get(
                f"{self.url}?password={password}"
            )
            if response.ok:
                return response.json()['enc']
            retry += 1
            time.sleep(1)
        print("error sending request. retry exceeded.")
        print(f"request password: {password}")
        exit(-1)

def extract_blocks(res):
    """
    Takes the response of Client.login() and extracts the ciphertext blocks
    as a list of numbers
    """
    BLOCK_SIZE = 32
    return [int(res[i * BLOCK_SIZE:(i+1) * BLOCK_SIZE], 16) for i in range(len(res) // BLOCK_SIZE)]

if __name__ == "__main__":
    client = Client("http://127.0.0.1:8000")

    # make blocks minus 1 byte
    plaintext =  b"A" * 16 + b"B" * 15
    first_try = client.login(plaintext.hex())

    # extract 2 blocks
    blocks = extract_blocks(first_try)

    # 1 block
    neo_plaintext = b"B" * 15
    password = b''
    i = 0
    iv = blocks[-1]
    while len(password) < 16:
        # make a tuck which is
        # one block of 'B's + extracted passwords + one byte guess
        try:
            tuck = int.from_bytes(neo_plaintext + i.to_bytes(1, 'big'), 'big')
        except OverflowError:
            print("error: couldn't find character!")
            exit(-1)

        # XOR
        try:
            payload = hex(iv ^ blocks[1] ^ tuck).replace("0x", "").zfill(32)
        except IndexError:
            break
        # Try again
        payload_output = client.login(payload)
        print("\rTrying tuck:", hex(tuck), "Payload:", payload, end='')
        payload_output_blocks = extract_blocks(payload_output)
        
        # Check if we have found the plaintext
        if blocks[2] == payload_output_blocks[1]:
            password += i.to_bytes(1, 'big')
            print(" Password = ", password.hex(), end='')
            neo_plaintext = b"B" * (15 - len(password)) + password
            plaintext = b"A" * 16 + b"B" * (15 - len(password))
            next_try = client.login(plaintext.hex())
            blocks = extract_blocks(next_try)
            i = 0
        else:
            # set the IV to the latest block
            iv = payload_output_blocks[-1]
            i += 1

        s = requests.Session()
        URL = 'http://127.0.0.1:8000'
        response = s.get(f'{URL}?password={password.hex()}')
        if response.ok:
            body = response.json()
            if 'flag' in body:
                print("\n",body)
                break
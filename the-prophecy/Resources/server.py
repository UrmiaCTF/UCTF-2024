from typing import Annotated, Optional
from Crypto.Cipher import AES
from Crypto.Util.Padding import pad
from Crypto.Random import get_random_bytes
from fastapi import FastAPI, Response, Cookie, HTTPException

app = FastAPI()

FLAG = 'uctf{b3h157un}'
PASSWORD = b'nLnjjGdRhQTV'

sessions = {}

@app.get("/")
def login(password: str, response: Response, session: Annotated[Optional[str], Cookie()] = None):
    if password == PASSWORD.hex():
        return {"flag": FLAG}
    else:
        if not session:
            cookie = get_random_bytes(16)
            session = cookie.hex()
            response.set_cookie('session', session)
            sessions[session] = (get_random_bytes(16), get_random_bytes(16))
        try:
            iv, key = sessions[session]
        except KeyError:
            raise HTTPException(status_code=422)

        try:
            cipher = AES.new(key, AES.MODE_CBC, iv)
            result = iv + cipher.encrypt(pad(bytes.fromhex(password) + PASSWORD, AES.block_size))
        except ValueError:
            raise HTTPException(status_code=422)
        sessions[session] = (result[-16:], key,)
        return {"enc": result.hex()}

import socket
import threading
from ECC import Coord, Curve
from secrets import randbelow
from secret import flag
import json

valid_hosts = {'uctf.ir'}


class Server:

    def __init__(self, host, port) -> None:
        self.host = host
        self.port = port
        self.start_server()


    def handle_client(self, conn, addr):
        print(f"[NEW CONNECTION] {addr} connected.")
        while True:
            try:

                a = 0x0
                b = 0x7
                p = 0xfffffffffffffffffffffffffffffffffffffffffffffffffffffffefffffc2f
                Gx = 0x79be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798
                Gy = 0x483ada7726a3c4655da4fbfc0e1108a8fd17b448a68554199c47d08ffb10d4b8

                ecc = Curve(a, b, p)
                G = Coord(Gx, Gy)

                assert ecc.is_on_curve(G)
                signatures = {}


                for host in valid_hosts:
                    d = randbelow(p)
                    Q = ecc.sign(G, d)
                    signatures[host] = [hex(Q.x),hex(Q.y)]

                conn.sendall(f"{json.dumps(signatures)}\r\n".encode())
                conn.sendall(b"Give me your generator and private key for verification process\r\nG(x,y) in hex format: ")
                
                data0 = conn.recv(4096)
                if not data0:
                    break
                
                conn.sendall(b"d in hex format: ")
                data1 = conn.recv(4096)
                if not data1:
                    break
                
                try:
                    Coordinates = data0.decode().strip().split(',')
                    PrivateKey = data1.decode().strip()
                    G1 = Coord(int(Coordinates[0], 16), int(Coordinates[1], 16))
                    d1 = int(PrivateKey, 16)
                except Exception as e:
                    conn.sendall(b'Wrong format! try again.\r\n')
                    break


                if not ecc.is_on_curve(G1):
                    conn.sendall(b'Point is not on the curve!')
                    break
                
                if d1 < 2:
                    conn.sendall(b"Security Issues Discovered!\r\n")
                    break

                sig = ecc.sign(G1, d1)
                if sig.x == int(signatures['uctf.ir'][0], 16) and sig.y == int(signatures['uctf.ir'][1], 16):
                    conn.sendall(f"Access granted. Here is your reward : {flag}\r\n".encode())
                    break
                else:
                    conn.sendall(b"Verficication process failed!\r\n")
                    break

            except ConnectionResetError:
                break

        conn.close()
        print(f"[DISCONNECTED] {addr} disconnected.")


    def start_server(self):
        server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        server.bind((self.host, self.port))
        server.listen()
        print(f"[LISTENING] Server is listening on {self.host}:{self.port}")

        while True:
            conn, addr = server.accept()
            thread = threading.Thread(target=self.handle_client, args=(conn, addr))
            thread.start()
            print(f"[ACTIVE CONNECTIONS] {threading.active_count() - 1}")

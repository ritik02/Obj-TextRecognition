import socket
import struct
from model import *
import json
import os
import sys

app = ClarifaiApp(para1, para2)
model = app.models.get("Logo")


def captions():
    image = Img(file_obj=open('home.jpg', 'rb'))
    json_data = model.predict([image])
    info = json.dumps(json_data)
    info = json.loads(info)
    dct = info["outputs"][0]
    name = dct['data']['regions']
    word = name[0]['data']['concepts'][0]['name']
    result = "The predicted logo for the given image is " + word
    client.sendall(result)

    os.remove('home.jpg')
    print "Image deleted\n\n"


HOST = "192.168.43.163"
PORT = 7000
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
try:
    s.bind((HOST, PORT))
except socket.error as msg:
    print 'Bind failed. Error Code : ' + str(msg[0]) + ' Message ' + msg[1]
    sys.exit()
s.listen(5)
print 'SERVER STARTED RUNNING'
j = 1
while True:
    client, address = s.accept()
    buf = ""
    while len(buf) < 4:
        buf += client.recv(4 - len(buf))
    size = struct.unpack('!i', buf)[0]
    with open('home.jpg', 'wb') as f:
        while size > 0:
            data = client.recv(1024)
            f.write(data)
            size -= len(data)
    print "Image Saved", j
    j += 1
    captions()
    client.close()

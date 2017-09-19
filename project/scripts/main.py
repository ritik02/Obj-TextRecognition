from model import *

import socket
import struct
import json
import sys
import os

l = {'a': [ 'air conditioner', 'air cooler',  'almond', 'anaconda', 'animal', 'animals', 'ant', 'ape', 'apple', 'armor', 'arrow','atm', 'air bag', 'arm', ' angel', 'airplane', 'aquarium'] ,
'b': ['baboon', 'baby', 'badminton', 'bake',  'ball', 'banana', 'banyan','bag', 'baseball', 'basement', 'basketball', 'bat', 'bathroom', 'battlefield', 'bear', 'bedroom', 'bee', 'bicycle', 'bike', 'billiards', 'bird', 'blackboard', 'book', 'bookshelf', 'boot',	'bottle',  'boy', 'bread', 'breakfast', 'bride', 'bridge', 'bronze medal', 'broom', 'buffalo',  'building', 'bull', 'buns', 'bus',  'butter', 'butterfly', ],
'c': ['cable','cake', 'calculator','comb' 'calender', 'camel', 'clothes','candy', 'canteen', 'car', 'carpet', 'carrot', 'cashew',  'cat', 'caterpillar', 'cattle', 'ceiling', 'chair', 'chalk', 'champion', 'cheese', 'cheetah', 'cherry', 'chicken', 'child', 'children', 'chili', 'chips', 'chocolate', 'classroom', 'clothes', 'cloud', 'clouds', 'cobra', 'coconut', 'coffee','computer', 'cook', 'cookie', 'cop', 'corn',  'cow', 'cream','crocodile', 'crow', 'cucumber', 'cupcake', 'curry',  'cycle',  'cyclone'],
'd': ['dairy',  'deer', 'dentist',  'dessert', 'diamond', 'dinosaur', 'dog', 'dolphin', 'donkey', 'door', 'door bell', 'doughnut', 'dove', 'dragonfly', 'drain',  'duck', 'dustbin'],
'e': ['eagle', 'eel', 'egg', 'elephant',  'entryway', 'eraser'],
'f': ['foot','footwear', 'factory', 'fan', 'feast', 'fence', 'field', 'finch', 'fish', 'floor', 'flour', 'fly', 'fog', 'food', 'football', 'forest', 'fort', 'fossil', 'fox', 'french fries', 'frog', 'fruit', 'furniture', 'flower', 'flight', 'floor'],
'g': ['game', 'garage', 'garden',  'gate', 'giraffe', 'glasses', 'globe', 'goal', 'goat', 'gold', 'gold medal','glass', 'golf',  'goose', 'gorilla', 'grain', 'grape', 'grasshopper', 'groom', 'group', 'guard',  'gun', 'gutter', 'gym'],
'h': [ 'hall', 'heater', 'helmet', 'hen', 'hippo', 'hockey', 'home', 'horse', 'hot dog', 'house', 'human', 'hummingbird',],
'i': ['ice', 'ice cream', 'infant', 'ink', 'insects'],
'j': ['jaguar',  'jam', 'jeep', 'jelly', 'jellybeans', 'jellyfish', 'jug', 'juice', 'jungle'],
'k': ['kangaroo', 'key', 'keyboard', 'kitchen', 'kite', 'kiwi', 'knife', "kennel", "knot"],
'l': ['ladder', 'lake', 'lamp', 'laptop', 'lemon', 'leopard', 'lion', 'living room', 'lizard', 'lock', 'loft', 'lollipop',  'lunch box'],
'm': ['mailbox','mango', 'map', 'market', 'mat','milk', 'medal', 'men', 'metal', 'mice', 'mirror', 'mo+bile', 'mobilephone', 'mobile phone', 'mongoose', 'monitor', 'monkey', 'mosquito', 'motherboard', 'mouse', 'mousepad', 'muffin'],
'n': [ 'nest', 'newspaper', 'notebook',],
'o': ['oak',  'oil', 'onion', 'orange', 'owl', 'ox'],
'p': ['paddle', 'painting', 'pan', 'panda', 'pantry', 'paper','pant', 'paperweight', 'parachute', 'parrot', 'pasta', 'peacock', 'peanut', 'pen', 'pencil', 'penguin', 'pepper', 'person',  'pickle', 'picnic', 'picture', 'pig', 'pigeon', 'pilot', 'pineapple', 'pizza', 'plane', 'plant', 'plumber', 'police', 'polo', 'pool', 'popcorn', 'potato', 'protein', 'pumpkin', 'purse', 'python'],
'q': [],
'r': ['rabbit','racket', 'racquet', 'radio', 'rafting', 'railway station', 'rain', 'rain forest', 'rainbow', 'rat', 'river', 'road', 'rock', 'rod', 'roller', 'roof', 'room', 'rugby', 'ruler'],
's': ['sole', 'sandal', 'salad', 'salt', 'sandstorm', 'sandwich', 'satellite','shirt', 'sausage', 'school bus', 'school boy', 'school girl',  'scissors', 'scoreboard', 'scorpion', 'sea', 'seal', 'seeds','shark', 'sheep', 'shelf','shock', 'shocks', 'shoe', 'shoes', 'shop', 'silver', 'silver medal',  'skate', 'skates', 'ski',  'sky', 'slipper', 'slippers', 'snack', 'snail', 'snow', 'snowboard', 'snowfall', 'snowstorm', 'soccer',  'sparrow', 'spectacles', 'spices', 'spoon', 'spork', 'sport',  'spring', 'sprouts', 'squirrel', 'stadium', 'staff', 'stairs', 'starfish', 'station', 'strawberry', 'student', 'sugar', 'sunflower', 'sunrise', 'sunset', 'swan', 'swimming pool', 'switch'],
't': ['table', 'table tennis', 'tadpole', 'tea', 'teapot',  'telephone', 'tennis', 'thunderstorm', 'tiger', 'toffee', 'toilet', 'tomato', 'tornado', 'tortoise', 'track', 'train', 'tree', 'turkey', 'turmeric', 'turtle', 'typewriter', 'typhoon',],
'u': [ 'uniform', 'utensils'],
'v': ['vacuum cleaner', 'vapor', 'vegetable', 'vehicle', 'volley ball', 'vulture'],
'w': ['wafer',  'wall clock', 'washing machine', 'watch', 'water', 'watermelon', 'wave', 'weapon', 'whale', 'wheat', 'whiteboard', 'wicket', 'window',  'wire', 'wolf', 'wolverine', 'women', 'woodchuck',  'wrist watch',],
'x': [],
'y': ['yak', 'yard', 'yogurt'],
'z': ['zebra']
}
def get_tags(cap):
    tags = []
    for word in cap:
        key = word[0]
        ls = l[key]
        for element in ls:
            if element == word:
                tags.append(word)
    return tags

def college(image):
    json_data = model3.predict([image])
    info = json.dumps(json_data)
    info = json.loads(info)
    dct = info["outputs"][0]
    result = dct['data']['concepts'][0]['name']
    value = dct['data']['concepts'][0]['value']
    if value > 0.1:
        return result
    else:
        result = ""
        return result


# model to detect persons
def identify_persons(image):
    json_data = model2.predict([image])
    info = json.dumps(json_data)
    info = json.loads(info)
    list_of_persons = info['outputs'][0]['data']
    try:
        list_of_persons = list_of_persons['regions']
        persons = len(list_of_persons)
        man, woman = 0, 0

        for i in range(persons):
            lx = list_of_persons[i]['data']['face']['gender_appearance']['concepts']
            if lx[0]['value'] > lx[1]['value']:
                if lx[0]['name'] == 'masculine':
                    man += 1
                else:
                    woman += 1
            elif lx[1]['value'] > lx[0]['value']:
                if lx[1]['name'] == 'masculine':
                    man += 1
                else:
                    woman += 1

        if persons == 1:
            if man == 1:
                x = "There is a man."
            else:
                x = "There is a woman."
        else:
            if man == 1 and woman == 1:
                x = "There is a man and a woman."
            elif man == 1:
                x = "There are " + str(persons) + " persons. One man and " + str(woman) + " women."
            elif woman == 1:
                x = "There are " + str(persons) + " persons. " + str(man) + " men and one woman."
            else:
                x = "There are " + str(persons) + " persons. " + str(man) + " men and " + str(woman) + " women."
    except:
        x = "none"

    return x


# model to detect objects using general model
def captions():
    cap = []
    image = Img(file_obj=open('home.png', 'rb'))
    json_data = model.predict([image])
    info = json.dumps(json_data)
    info = json.loads(info)
    dct = info["outputs"][0]
    results = dct['data']['concepts']
    print (results)
    l = ['person', 'man', 'woman', 'boy', 'girl', 'adult', 'group']
    s, x = "", ""
    print ("The predicted captions for the given image are as follows:")
    #ho="".encode()
    #ho = college(image)
    #print (ho)
    f = 0
    for obj in results:
        if obj['name'] in l and f == 0:
            x = identify_persons(image)
            #print x
            if x == "none":
                x = ""
                cap.append(obj['name'].lower())
            else:
                f = 1
                print (x)
        elif obj['name'] not in l:
            cap.append(obj['name'].lower())

    tags = get_tags(cap)
    if len(tags) > 0:
        s = ""
        for word in tags:
            print (word)
            s = s + word + ". ,"
    '''
    elif len(cap)>0:
        print "No captions matched with general model"
    '''
    #ans=x+"  . ,"+ho+" . ,"+s
    ans = x + "  . ,"  + " . ," + s
    client.sendall(ans.encode())
    #os.remove('home.png')


# CREATING SOCKET
HOST = "192.168.43.218"
PORT = 7000
skt = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
skt.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
try:
    skt.bind((HOST, PORT))
except socket.error as msg:
    print ('Bind failed. Error Code : ' + str(msg[0]) + ' Message ' + msg[1])
    sys.exit("Program terminated !!")
skt.listen(5)
print ('SERVER STARTED RUNNING. Press Ctrl+C to terminate the code.')

# READY TO PROCESS..
j = 1

try:
    while True:
        client, address = skt.accept()
        buf = "".encode()
        while len(buf) < 4:
            buf += client.recv(4 - len(buf))
        size = struct.unpack('!i', buf)[0]
        with open('home.png', 'wb') as f:
            while size > 0:
                data = client.recv(1024)
                f.write(data)
                size -= len(data)
        print ("\nImage", j, "Saved")
        j += 1
        captions()
        client.close()

except KeyboardInterrupt:
    sys.exit("Program terminated !!")

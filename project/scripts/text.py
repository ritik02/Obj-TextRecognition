import Image
from pytesseract import image_to_string

s = image_to_string(Image.open('home.jpg'))
print s

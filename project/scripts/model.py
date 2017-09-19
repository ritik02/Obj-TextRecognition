#!/usr/bin/env python
# -*- coding: utf-8 -*-
#
#  model.py
#
#  Copyright 2017 Ashish <ashish@Ashish-Lenovo>
#
#  This program is free software; you can redistribute it and/or modify
#  it under the terms of the GNU General Public License as published by
#  the Free Software Foundation; either version 2 of the License, or
#  (at your option) any later version.
#
#  This program is distributed in the hope that it will be useful,
#  but WITHOUT ANY WARRANTY; without even the implied warranty of
#  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#  GNU General Public License for more details.
#
#  You should have received a copy of the GNU General Public License
#  along with this program; if not, write to the Free Software
#  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
#  MA 02110-1301, USA.c
#
#

from clarifai.rest import Image as ClImage
from clarifai.rest import ClarifaiApp
from parameters import *

# SELECTING MODEL
app = ClarifaiApp(para1, para2)
model = app.models.get("general-v1.3")
model2 = app.models.get("Demographics")
model3=app.models.get("College")
Img = ClImage
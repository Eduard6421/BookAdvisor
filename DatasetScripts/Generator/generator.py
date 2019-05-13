from PIL import Image
from PIL import ImageFont
from PIL import ImageDraw
from random import uniform
from random import randint


paths = []
words = []
fonts = []
colors = []

with open('colors.txt') as colorFile:
	for line in colorFile:
		colors.append(line[:-1])


with open('fontspath.txt') as fontFile:
	for line in fontFile:
		fonts.append(line[:-1])

with open('paths.txt') as pathFile:
	for line in pathFile:
		paths.append(line[:-1])

with open('words.txt') as wordFile:
	for line in wordFile:
		words.append(line[:-1])

with open('ground_truth.txt','a') as labelFile:

	for i in range(0,50000):

		if i%100 == 0:
			print(i)

		img_number = randint(0, len(paths)-1)
		image_path = paths[img_number]

		font_number = randint(0,len(fonts)-1)
		font_title = fonts[font_number]

		word_number = randint(0,len(words)-1)
		word = words[word_number]

		color_number = randint(0,len(colors)-1)
		color = colors[color_number]

		img = Image.open(image_path)

		width, height = img.size
		
		font_size = 1
		img_fraction = uniform(0.35,0.75)

		font = ImageFont.truetype(font_title, font_size)

		while font.getsize(word)[0] < img_fraction*width:
			font_size += 1
			font = ImageFont.truetype(font_title, font_size)

		font_size -= 1
		font = ImageFont.truetype(font_title, font_size)

		text_size = font.getsize(word)

		rangex = width - text_size[0]
		rangey = height - text_size[1]

		fx = randint(0,rangex+1)
		fy = randint(0,rangey+1)

		nfx = randint(0,20)
		nfy = randint(0,20)

		img = img.crop((fx,fy,text_size[0]+fx+nfx,text_size[1]+fy+nfy))

		draw = ImageDraw.Draw(img)

		draw.text((randint(0,nfx+1), randint(0,nfy+1)), word, font=font,fill=color) # put the text on the image
		img.save('images/' + 'img' + str(i) + '.jpg') # save it

		print('images/' + 'img' + str(i) + '.jpg' + ' ' + word,file=labelFile)
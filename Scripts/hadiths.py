import requests
from bs4 import BeautifulSoup
import HTMLParser
from openpyxl import Workbook
# import urllib

h = HTMLParser.HTMLParser()

# Get sunnah.com Hadith Qudsi page and make sure response is good
r = requests.get("http://sunnah.com/qudsi40")
if r.status_code == 200:
	print "Got page."
	# print r.text
	r.encoding = 'utf-8' # Required to handle arabic

	try:
		soup = BeautifulSoup(r.text)
	except:
		soup = BeautifulSoup(r.text, 'html.parser')
	# print soup.prettify()

	allHadith = soup.select('.AllHadith')
	hadithList = allHadith[0].find_all(class_='actualHadithContainer')
	# print hadithList[-1]

	cleanedHadith = [] # Cleaned list of hadith in english and arabic

	for hadith in hadithList:
		enHadith = hadith.find(class_="englishcontainer").text
		arHadith = hadith.find(class_="arabic_hadith_full").text
		cleanedHadith.append([enHadith, arHadith.encode('utf-8')])

  # Nice sample display of hadith results
	for i in range(0, len(cleanedHadith)):
		if i+1 < 10:
			print "###"
			print "#" + str(i+1) + "#"
			print "###\n"
		else:
			print "####"
			print "#" + str(i+1) + "#"
			print "####\n"
		print "English hadith:\n" + "---------------\n" + cleanedHadith[i][0]
		print "\n"
		print "Arabic hadith:\n" + "--------------\n" + cleanedHadith[i][1]
		print "\n\n"


wb = Workbook()
ws = wb.active

for i in range(0, len(cleanedHadith)):
	ws.append([i+1, cleanedHadith[i][0], cleanedHadith[i][1].decode('utf-8')])

wb.save("hadiths.xlsx")


'''
with open("hadiths.txt", "a") as myfile:
	text = ""
	for i in range(0, len(cleanedHadith)):
		if i+1 < 10:
			text += "###\n"
			text += "#" + str(i+1) + "#\n"
			text +=  "###\n\n"
		else:
			text += "####\n"
			text += "#" + str(i+1) + "#\n"
			text += "####\n\n"
		text += "English hadith:\n" + "---------------\n" + cleanedHadith[i][0]
		text += "\n\n"
		text += "Arabic hadith:\n" + "--------------\n" + cleanedHadith[i][1].decode('utf-8')
		text += "\n\n\n\n"
	text = text.encode('utf-8')
	myfile.write(text)	
'''

# print h.unescape(hadith)
# print urllib.unquote(hadith).decode('utf8') 


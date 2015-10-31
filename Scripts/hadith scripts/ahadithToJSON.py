import requests
from bs4 import BeautifulSoup
import io
import json


results = []

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
		newHadith = {}
		newHadith["number"] = i + 1
		newHadith["englishHadith"] = cleanedHadith[i][0]
		newHadith["arabicHadith"] = cleanedHadith[i][1].decode("utf-8")
		results.append(newHadith)

JSONToPrint = {"results": results}

with io.open('hadiths.json', 'w', encoding='utf8') as json_file:
    data = json.dumps(JSONToPrint, indent=4, ensure_ascii=False)
    # unicode(data) auto-decodes data to unicode if str
    json_file.write(unicode(data))


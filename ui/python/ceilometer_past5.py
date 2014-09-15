#!/usr/bin/python
from get_token import *
from get_tenantid import *
import urllib2
import datetime
import json
import cgi, cgitb
from common_function import *

def checkPadding(value):
    if(value < 10):
        value = "0"+ str(value)
        return value
    else:
        return str(value)
    
print "Content-type: text/html\r\n\r\n"
print '<html>'
print '<head>'
print '<title>Getting token from Openstack</title>'
print '</head>'
print '<body>'
print '<div>'

token = get_token()
tenantId = get_tenant_id()
apiPort = "8774"
hostIP = "localhost"
headers = {
            'Content-Type'  :   'application/json',
            'Accept'        :   'application/json',
            'X-Auth-Token'  :   token
        }


currentTime = datetime.datetime.now()
year = datetime.date.today().strftime("%Y")
month = datetime.date.today().strftime("%m")
currentDay = datetime.date.today().strftime("%d")

printHeaders()

numOfDays = 12
previousFive = (int(currentDay) - numOfDays) + 1

for day in range(previousFive, int(currentDay) + 1):
    strPreviousDay = checkPadding(day - 1)
    strCurrentDay = checkPadding(day);
    strCurrentMonth = checkPadding(int(month));
    apiPath = "/v2/" + tenantId + "/os-simple-tenant-usage?start=%s-%s-%sT00:00:00.000000&end=%s-%s-%sT00:00:00.000000" %(year,strCurrentMonth,strPreviousDay,year,strCurrentMonth,strCurrentDay)
    url = "http://" + hostIP + ":" + apiPort + apiPath
    jsonValue = get_json(url, headers)
    displayUsage(jsonValue)

print '</table>'
print '<br></br>'
print '</body>'
print '</html>'


#!/usr/bin/python
from get_token import *
from get_tenantid import *
import urllib2
import datetime
import json
import cgi, cgitb
from common_function import *

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
hostIP = get_hostip()
headers = {
            'Content-Type'  :   'application/json',
            'Accept'        :   'application/json',
            'X-Auth-Token'  :   token
        }


currentTime = datetime.datetime.now()
year = datetime.date.today().strftime("%Y")
month = datetime.date.today().strftime("%m")
currentDay = datetime.date.today().strftime("%d")

apiPath = "/v2/" + tenantId + "/os-simple-tenant-usage?start=%s-%s-%sT00:00:00.000000&end=%s-%s-%sT00:00:00.000000" %(year,month,"01",year,month,currentDay)
url = "http://" + hostIP + ":" + apiPort + apiPath
result = get_json(url, headers)
#formatted = json.dumps(result, sort_keys=True, indent=3)
#print formatted
printHeaders()
displayUsage(result)

print '</table>'
print '</div>'
print '<div>'
print '<br></br>'
print '<div align="left"> Usage for the last 7 days &nbsp &nbsp &nbsp\
        <a style="color:red;"><button type="button" id="last-five-usage" onclick="showLastFive()"> Show/Hide </button></a>'
print ' <div style="float:right;"> Click here to generate bill: &nbsp &nbsp &nbsp\
        <a style="color:red;"><button type="button" id="generate-bill" onclick="generateBill()"> Generate </button></a> </div></div>'
print '</div>'
print '</body>'
print '</html>'


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
print '</head>'
print '<body>'
print '<div id="total-bill">'

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

apiPath = "/v2/" + tenantId + "/os-simple-tenant-usage?start=%s-%s-%sT00:00:00.000000&end=%s-%s-%sT00:00:00.000000" %(year,month,"01",year,month,currentDay)
url = "http://" + hostIP + ":" + apiPort + apiPath
result = get_json(url, headers)
diskRate = 25
memRate = 10
vcpuRate = 50

disk = float(result['tenant_usages'][0]['total_local_gb_usage']) * diskRate
mem = float(result['tenant_usages'][0]['total_memory_mb_usage']) * ( float(memRate) / 100 )
vcpu = float(result['tenant_usages'][0]['total_vcpus_usage']) * vcpuRate

total = float((disk + mem + vcpu) / 100)
print "Total : $%.2f"  %total

print '</div>'
print '</body>'
print '</html>'


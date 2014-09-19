#!/usr/bin/python
from get_token import *
from get_tenantid import *
import urllib2
import datetime
import json
import cgi, cgitb

def checkPadding(value):
    if(value < 10):
        value = "0"+ str(value)
        return value
    else:
        return str(value)

def get_json(url, headers):
    request = urllib2.Request(url, None, headers)
    response = urllib2.urlopen(request).read()
    jsonOutput = json.loads(response.decode('utf8'))
    return(jsonOutput)

def displayUsage(data):
    if data['tenant_usages']:
        for i in range(0,len(data)):
            startDate = data['tenant_usages'][i]['start'].split('T')[0]
            endDate = data['tenant_usages'][i]['stop'].split('T')[0]
            #print "<tr> <td> %s </td> <td> %s </td> <td> %s </td> <td> %.2f </td> <td> %.2f </td> <td> %.2f </td> <td> %.2f </td> </tr>" %("admin",startDate,endDate,data['tenant_usages'][i]['total_hours'],data['tenant_usages'][i]['total_local_gb_usage'],data['tenant_usages'][i]['total_memory_mb_usage'],data['tenant_usages'][i]['total_vcpus_usage'])
            print "<tr> <td> %s </td> <td> %s </td> <td> %s </td> <td> %.2f </td> </tr>" %("admin",startDate,endDate,data['tenant_usages'][i]['total_vcpus_usage'])

def constructPath(tenantId, year, month, currentDay,hostIP, apiPort):
    apiPath = "/v2/" + tenantId + "/os-simple-tenant-usage?start=%s-%s-%sT00:00:00.000000&end=%s-%s-%sT00:00:00.000000" %(year,month,"01",year,month,currentDay)
    url = "http://" + hostIP + ":" + apiPort + apiPath
    decoded = get_json(url, headers)
    return decoded


def printHeaders():
    print '<table width="100%" class="table-align">'
    print '<tr> <th> TENANT </th> <th> START DATE </th> <th> END DATE </th> <th> TOTAL VCPUS USED (IN HOURS) </th> </tr>'


#!/usr/bin/python
from get_token import *
from get_tenantid import *
import urllib2
import json

def printOutput(url,headers, component, componentName):
    decoded = get_json(url, headers)
    print '<tr>'
    print "<td> <label for=\"" + component + "-id\"> Select " + component.title() + "</label> </td>"
    print "<td> <select id=\"" + component + "-list\">"
    print "<option value=\"-1\"> ----- Select" + component.title() + " ID ------ </option>"
    for i in range(0, len(decoded[componentName])):
        print "<option value=\"" + str(i) + "\">" + decoded[componentName][i]['name'] + " --> " + decoded[componentName][i]['id']  + "</option>"
    print "</select> </td>"
    print "</tr>"




def get_json(url, headers):
    request = urllib2.Request(url, None, headers)
    response = urllib2.urlopen(request).read()
    jsonValue = json.loads(response.decode('utf8'))
    return(jsonValue)

print "Content-type: text/html\r\n\r\n"
print '<html>'
print '<head>'
print '<title>Getting token from Openstack</title>'
print '</head>'
print '<body>'
print '<table border="0" width="60%">'
print '<tr> <td> <label for="instance-id"> Instance Name </label> </td>'
print '<td> <input type="text" name="instance-name" id="instance-id"> </td> </tr>'
token = get_token()
tenantId = get_tenant_id()

####  CONSTRUCT GET IMAGES REST URL ####

apiPath = "/v1/images/detail"
apiPort = "9292"
hostIP = get_hostip()
headers = {
            'Content-Type'  :   'application/json',
            'Accept'        :   'application/json',
            'X-Auth-Token'  :   token
        }

url = "http://" + hostIP + ":" + apiPort + apiPath
printOutput(url,headers,"image",'images')

####  CONSTRUCT GET FLAVOR REST URL ####

apiPath = "/v2/" + tenantId + "/flavors"
apiPort = "8774"
url = "http://" + hostIP + ":" + apiPort + apiPath
printOutput(url, headers, "flavor", 'flavors')

####  CONSTRUCT GET NETWORK REST URL ####

apiPath = "/v2.0/networks"
apiPort = "9696"
url = "http://" + hostIP + ":" + apiPort + apiPath
printOutput(url, headers, "network", 'networks')

print '</table>'
print '</body>'
print '</html>'

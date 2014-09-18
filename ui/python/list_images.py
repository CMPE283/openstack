#!/usr/bin/python
from get_token import *
from get_tenantid import *
import urllib2
import json
import cgi, cgitb

def get_images_count(decoded):
    return len(decoded['images'])

token = get_token()
tenantId = get_tenant_id()

apiPath = "/v2/images"
apiPort = "9292"
hostIP = get_hostip()
url = "http://" + hostIP + ":" + apiPort + apiPath
headers = {
            'Content-Type'  :   'application/json',
            'Accept'        :   'application/json',
            'X-Auth-Token'  :   token
        }

print "Content-type: text/html\n"
decoded = get_json(url,headers)
formatted = json.dumps(decoded, sort_keys = True, indent = 3)
#print formatted
#print '<table>'
#print '<html><head></head><body>'
#print '<table>'
get_images_count(decoded)
for i in range(0,len(decoded['images'])):
    print '<tr><td>'
    print decoded['images'][i]['name'] + "</td><td>"
    print decoded['images'][i]['id'] + "</td><td>" 
    print decoded['images'][i]['status'] + "</td><td>"
    print str(decoded['images'][i]['size']) + "</td><tr>"

#print '</table></body></html>'

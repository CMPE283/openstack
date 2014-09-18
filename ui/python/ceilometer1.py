#!/usr/bin/python
from get_token import *
from get_tenantid import *
import urllib2

token = get_token()
tenantId = get_tenant_id()
apiPath = "/v2/" + tenantId + "/os-simple-tenant-usage/" + tenantId
apiPort = "8774"
hostIP = "192.168.1.98"
headers = {
            'Content-Type'  :   'application/json',
            'Accept'        :   'application/json',
            'X-Auth-Token'  :   token
        }

url = "http://" + hostIP + ":" + apiPort + apiPath
request = urllib2.Request(url, None, headers)
response = urllib2.urlopen(request).read()
decoded = json.loads(response.decode('utf8'))
formatted = json.dumps(decoded, sort_keys = True, indent = 3)
print formatted


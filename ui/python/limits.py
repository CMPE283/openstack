#!/usr/bin/python
print "Content-type: text/json\n"
from get_token import *
import urllib2
import json
from get_tenantid import *

#mapping = {'VCPUs': 'totalCoresUsed', 'RAM (GB)': 'totalRAMUsed', 'Instances': 'totalInstancesUsed', 'Floating IPs': 'totalFloatingIpsUsed', 'Security Group': 'totalSecurityGroupsUsed' }
mapping = {'VCPUs': 'totalCoresUsed', 'RAM': 'totalRAMUsed', 'Instances': 'totalInstancesUsed'}
mapping_total = {'VCPUs': 'maxTotalCores', 'RAM': 'maxTotalRAMSize', 'Instances': 'maxTotalInstances'}
value = {}
tenantId = get_tenant_id()
apiPath = "/v2/" + tenantId + "/limits"
apiPort = "8774"
token = get_token()
headers = {
    'Content-Type'  :   'application/json',
    'Accept'        :   'application/json',
    'X-Auth-Token'  :   token
}
hostIP = "localhost"
url = "http://" + hostIP + ":" + apiPort + apiPath
decoded = get_json(url, headers)
#formatted = json.dumps(decoded, sort_keys=True, indent=3)
#print formatted
for key in mapping.keys():
    value[mapping[key]] = {}
    value[mapping[key]]["category"] = key
#    if(mapping[key] == "totalRAMUsed"):
#        #decoded['limits']['absolute'][mapping[key]] = int(decoded['limits']['absolute'][mapping[key]]) / 1000
    value[mapping[key]]['percentage'] = decoded['limits']['absolute'][mapping[key]] * 100 / decoded['limits']['absolute'][mapping_total[key]]
        
jsonValue = json.dumps(value, sort_keys=True)
print jsonValue

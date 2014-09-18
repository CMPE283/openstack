#!/usr/bin/python
from get_token import *
import urllib2
import json

def get_tenant_id():

    token = get_token()
    headers = {
            'Content-Type'  :   'application/json',
            'Accept'        :   'application/json',
            'X-Auth-Token'  :   token
        }

    hostIP = get_hostip()
    apiPort = "5000"
    apiPath = "/v2.0/tenants"
    url = "http://" + hostIP + ":" + apiPort + apiPath
    decoded = get_json(url, headers)
    if( decoded['tenants'][0]['name'] == "admin" ):
        return decoded['tenants'][0]['id']

def get_json(url, headers):
    request = urllib2.Request(url, None, headers)
    response = urllib2.urlopen(request).read()
    jsonValue = json.loads(response.decode('utf8'))
    return(jsonValue)

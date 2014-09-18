#!/usr/bin/python
import urllib2
import json
from os import environ as env

def get_token():
    password = "a28d36fd90074d0c"
    hostIP = get_hostip()
    tenantName = "admin"
    userName = "admin"

    apiPath = "/v2.0/tokens"
    apiPort = "5000"
    url = "http://" + hostIP + ":" + apiPort + apiPath
    headers = {
        'Content-Type'  :   'application/json',
        'Accept'        :   'application/json'
        }


    requestBody = '{"auth":{"tenantName":"' + tenantName + '","passwordCredentials":{"username":"' + userName + '","password":"' + password +'"}}}'
    request = urllib2.Request(url, requestBody, headers)
    response = urllib2.urlopen(request).read()
    decoded = json.loads(response.decode('utf8'))
    token = decoded['access']['token']['id']
    return token

def get_hostip():
    hostAddress = "10.0.2.15"
    return hostAddress


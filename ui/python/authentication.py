#!/usr/bin/python

print 'Content-Type: application/json\n\n'
import urllib2
import json
import re
import cgi, cgitb

form = cgi.FieldStorage()
userName=form.getvalue('username')
password=form.getvalue('userpd')

global msg
#print "Content-type: text/html\r\n\r\n"
#print '<html>'
#print '<head>'
#print '</head>'
#print '<body>'



hostIP = "localhost"
tenantName = "admin"


apiPath = "/v2.0/tokens"
apiPort = "5000"
url = "http://" + hostIP + ":" + apiPort + apiPath
headers = {
    'Content-Type'  :   'application/json',
    'Accept'        :   'application/json'
    }


requestBody = '{"auth":{"tenantName":"' + tenantName + '","passwordCredentials":{"username":"' + userName + '","password":"' + password +'"}}}'

try:
    request = urllib2.Request(url, requestBody, headers)
    response = urllib2.urlopen(request).read()
    
except IOError:
     msg = "Login Failed"    
else:
    msg="Login Success"

jsonmsg = '{"msg":"'+msg+'"}'
decoded = json.loads(jsonmsg.decode('utf8'))
print json.dumps(jsonmsg)
#print '<p id=\"insert-text\">' + msg + '</p>'

#print '</body>'
#print '</html>'

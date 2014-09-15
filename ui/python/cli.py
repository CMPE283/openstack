#!/usr/bin/python
import pxssh
import getpass
import re
from get_tenantid import *
import cgi, cgitb

tenant_id = get_tenant_id()
form = cgi.FieldStorage()
imageId = form.getvalue('image_id')
flavorId = form.getvalue('flavor_id')
networkId = form.getvalue('network_id')
instanceName = form.getvalue('instance_name')

'''
imageId = "7e7ebe98-a3ec-4c17-baeb-82847bfd0a52"
flavorId = "1"
networkId = "f7ac5fcf-a4f7-4873-931b-44719d5de3df"
instanceName = "test2"
'''

global message

print "Content-type: text/html\r\n\r\n"
print '<html>'
print '<head>'
print '<title>Getting token from Openstack</title>'
print '</head>'
print '<body>'

s = pxssh.pxssh();
hostname = "localhost"
username = "root"
password = "CMPE283"
s.login(hostname, username, password, port="2022")
cmd = "nova boot --image %s --flavor %s --nic net-id=%s %s | grep \"| id\"" %(imageId, flavorId, networkId, instanceName)
s.sendline(cmd)
s.prompt()
a = s.before
if(re.search(r'.*(ERROR.*)', a, re.M)):
    message = 'Failed to Create'
elif (re.search( r'(.*)(\| \w{8}-\w{4}-\w{4}-\w{4}-\w{12})(.*)', a, re.M|re.I)):
    message = "Instance Created Successfully"
else:
    message = "Instance Creation Failed"

print '<p id=\"insert-text\">' + message + '</p>'

s.logout

print '</body>'
print '</html>'


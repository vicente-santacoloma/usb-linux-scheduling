import random
import sys

numero_proc = sys.argv[1]

f = file("process.xml", "w")

f.write("<descriptions>\n")

for i in range(1, int(numero_proc) +1):
    f.write("\t<process>\n")
    f.write("\t\t<pid>"+ str(i)  +"</pid>\n")
    f.write("\t\t<isRealTime>"+ str(random.randint(0,1))  +"</isRealTime>\n")
    f.write("\t\t<priority>"+ str(random.randint(1, 10))  +"</priority>\n")

    for j in range(1, random.randint(1, 5) +1):
        f.write("\t\t\t<cycle>"+ str(random.randint(1,20))  +"</cycle>\n")
        
    f.write("\t\t<timeIn>"+ str(i)  +"</timeIn>\n")
    f.write("\t</process>\n")

f.write("</descriptions>\n")

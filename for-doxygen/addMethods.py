import copy






















def parseMethods(file):
	out = ''
	for line in file:
		if('class' not in line and ('public' in line or 'private' in line or 'static' in line or 'protected' in line)):
			out += line
			out += """
"""
	if '{' in out:
		out.replace('{','')
	return out


def main():
	plantuml = open("output.txt","r")
	newfile = []

	for line in plantuml:
		if( 'RE' not in line and 'ArrayList' not in line and 'Collections' not in line and 'ThreadLocalRandom' not in line and 'Application' not in line and 'FXMLLoader' not in line and 'Parent' not in line and 'Scene' not in line and 'Stage' not in line and 'BigDecimal' not in line and '//' not in line):
			if('class' in line):
				newline = copy.copy(line)
				# print newline
				if('{' in newline):
					newline.remove('{')
				# if('(' in newline):
				# 	newline.remove('(')
				# if(')' in newline):
				# 	newline.remove(')')
				filename = newline.split('.')[-1][:-1] + '.java'
				newline += '''
{'''
				newline += parseMethods(open(filename,'r'))
				newline += '''
}'''
				newfile.append(newline)
			else:
				newfile.append(line)
	final_out_string = ''
	for item in newfile:
		final_out_string += item
	print final_out_string
	# print newfile	
main()
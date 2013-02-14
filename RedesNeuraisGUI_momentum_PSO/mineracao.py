

import matplotlib.pyplot as plt

def plotarGrafico(res, label, v):
	import pylab


	plt.figure(label)

	plt.plot(xrange(len(res)), res)

	plt.xlabel('iteracao')
	plt.ylabel('Erro Medio')
	plt.title(' Erros para o IPSONET ')
	plt.suptitle(label)
	plt.grid(True)
	
	plt.savefig('output_'+str(label)+str(v))
	#plt.show()
	plt.close()

arq = open("janela_5_erro_medio.txt")
res = []
for l in arq:
	erro = float(l.split()[0])
#	print erro
	if (erro != 0):
		res.append(erro)
plotarGrafico(res, "com janela de tamanho 2 - camadas escondidas 50 ", 2)

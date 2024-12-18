# Le nom de votre classe principale
# Renommez si nécessaire
MAINCLASS=Main
## Le chemin vers où votre classe compilée est installée
# Renommez si nécessaire
INSTALLDIR=../out/production/TP3
MAINDIR=$(dir $(subst .,/,$(MAINCLASS)))
CLASSES=$(shell find $(INSTALLDIR)/$(MAINDIR) -name \*.class)

compile: $(subst .,/,$(MAINCLASS)).class

%.class: %.java
	javac -g -d $(INSTALLDIR) *.java

clean:
	## Rien à faire, tout à été compilé et installé dans INSTALLDIR


install:
	## Rien à faire, tout à été compilé et installé dans INSTALLDIR

cleanInstall:
	rm $(subst $$,\$$,$(CLASSES))

test:


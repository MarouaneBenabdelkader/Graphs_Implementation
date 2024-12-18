# Le nom de votre classe principale
# Renommez si nécessaire
MAINCLASS=Main
## Le chemin vers où votre classe compilée est installée
# Renommez si nécessaire
INSTALLDIR=out/production/TP3
JARFILE=TP3RandomTrees

all: compile install exec

# Cible pour compiler
compile:
	cd src ; make compile

jar: compile
	cd $(INSTALLDIR); \
	echo Main-Class: $(subst /,.,$(MAINCLASS)) > manifest.txt ; \
	jar cvfm $(JARFILE).jar manifest.txt ./
	mv $(INSTALLDIR)/$(JARFILE).jar ./

install:
	cd src ; make install

clean:
	cd src ; make clean ; make cleanInstall
	rm *.zip *.jar manifest.*

# Cible qui explique comment executer
exec: $(JARFILE).jar
	java -jar $(JARFILE).jar

# Ou autrement
#exec:
#	java -classpath $(INSTALLDIR) $(MAINCLASS)

# Demarre automatiquement une demonstration de votre programme
# Il faut que cette demo soit convaincante
demo:
	java -classpath $(INSTALLDIR) $(MAINCLASS)

# Executer automatiquent les test
# On s'attend (d'habitude) que pour claque classe MaClasse il y ait une
# classe TestMaClasse qui vorifie le bon comportment de chaque methode de la classe
# sur au moins une entrée
# A vous de completer
test:
	

# Cible pour créer son rendu de tp 
zip:
	moi=$$(whoami) ; zip -r $${moi}_renduTP2.zip *


# Cible pour vérifier le contenu de son rendu de tp 
zipVerify:
	moi=$$(whoami) ; unzip -l $${moi}_renduTP2.zip

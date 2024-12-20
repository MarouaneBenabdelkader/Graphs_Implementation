# Nom de la classe principale (sans .java, ni chemin)
MAINCLASS=Main

# Dossier où seront placées les classes compilées
INSTALLDIR=out/production/TP2

# Dossier des sources
SOURCEDIR=src

# Liste des sources .java
SOURCES := $(wildcard $(SOURCEDIR)/*.java) \
           $(wildcard $(SOURCEDIR)/Generators/*.java) \
           $(wildcard $(SOURCEDIR)/Graph/*.java) \
           $(wildcard $(SOURCEDIR)/GraphClasses/*.java) \
           $(wildcard $(SOURCEDIR)/Graphics/*.java) \
           $(wildcard $(SOURCEDIR)/MSTAlgorithms/*.java) \
           $(wildcard $(SOURCEDIR)/RandomTreeAlgos/*.java) \
           $(wildcard $(SOURCEDIR)/Utilities/*.java)

# Cible par défaut
default: compile

compile:
	@if not exist "$(INSTALLDIR)" mkdir "$(INSTALLDIR)"
	javac -g -d $(INSTALLDIR) $(SOURCES)

run: compile
	java -cp $(INSTALLDIR) $(MAINCLASS)

clean:
	@if exist "$(INSTALLDIR)" rmdir /S /Q "$(INSTALLDIR)"

install:
	@echo "Rien de spécial, tout est déjà compilé dans $(INSTALLDIR)."

cleanInstall:
	@echo "Pas de nettoyage spécifique."

test:
	@echo "Aucun test défini."

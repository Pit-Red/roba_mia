all: rm Output.class 
	java Output

Output.class: Output.j 
	java -jar jasmin.jar Output.j

Output.j: Translator.class
	java Translator

Translator.class: Translator.java
	javac Translator.java



rm:
	rm -f Translator.class Output.j Output.class
#### LEZIONE 3 STATISTICA DESCRITTIVA

library("UsingR")
data(math)
str(math)
hist(math, freq=F, col="red")
lines(density(math), col="blue",lwd=3)
# si usa lines se l'istogramma è ancora aperto per sovrapporre la linea della densità stimata
plot(density(math))
# rappresenta un grafico a parte rispetto all'istogramma

# Istogramma per variabili discrete

x<-rpois(100, 1)
# simula 1oo valori estratti da una distribuzione di Poisson di parametro lambda=1
hist(x, freq=F, col="green")
# non è corretto!

hist(math, freq=F, breaks=10)

boxplot(math, col="yellow", horizontal=T)

x<-rnorm(2000)
# genera 200 valori estratti dalla distribuzione normale standard
boxplot(x, col="pink", horizontal=T)

data(babies)
str(babies)
# Analisi statistica descrittiva di race
table(babies$race)
# la moda è 0
table(babies$race)/sum(table(babies$race))
table(babies$race)/sum(table(babies$race))*100

t<-table(babies$race)
barplot(t, col=rainbow(12))
barplot(t/sum(t), col=rainbow(23))
barplot(t/sum(t)*100, col=rainbow(10))
dotchart(t)
pie(t, col=rainbow(16))
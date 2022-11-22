########### LEZIONE 2 - STATISTICA DESCRITTIVA

data("Loblolly")
str(Loblolly)


mean(Loblolly$height)
median(Loblolly$height)

#### MODA
# E' il valore della variabile (discreta o qualitativa o
# quantitativa categorizzata) che si presenta con
# la frequenza più alta
# La frequenza assoluta è il numero di volte in cui si verifica
# il livello o valore dato della variabile nel campione

# Distribuzione della Frequenze di age
table(Loblolly$age)
# age non ha moda perché tutte le frequenze sono uguali

table(Loblolly$Seed)
# Seed non ha moda perché tutte le frequenze sono uguali

### Frequenze relative
# La frequenza relativa è la frequenza assoluta diviso
# la taglia del campione (o la taglia senza NA)

sum(is.na(Loblolly$age))
# is.na restituisce T o F se gli elementi sono rispettivamente NA o no
# T=1; F=0, quindi sum restituisce il numero di NA nel vettore

length(Loblolly$age)  # taglia del vettore
dim(Loblolly)  # dimensione del dataframe completo
str(dim(Loblolly))  # vettore di taglia 2
dim(Loblolly)[2] # selezione da un vettore (1 indice)
Loblolly[40,3] # selezione da un dataframe (2 indici, riga e colonna)

table(Loblolly$age)/length(Loblolly$age)

# Frequenze percentuali
# frequenze relative * 100
table(Loblolly$age)/length(Loblolly$age)*100
sum(table(Loblolly$age)/length(Loblolly$age)*100)

##### Misure di dispersione

sum(is.na(Loblolly$height))

rg<-range(Loblolly$height)
# min e max (vettore con 2 componenti)
rg[2]-rg[1]

quantile(Loblolly$height) # quartili
quantile(Loblolly$height, 0.90)
# il 90% dei valori di height non è maggiore di 59.595

var(Loblolly$height, na.rm=T)
mean(Loblolly$height, na.rm=T)
sd(Loblolly$height, na.rm=T)

# z-score
z<-(Loblolly$height-mean(Loblolly$height))/sd(Loblolly$height)
mean(z)
sd(z)

# CV
sd(Loblolly$height)/mean(Loblolly$height)*100

hist(Loblolly$height, freq=F)
abline(v=32, col="red", lwd=3)

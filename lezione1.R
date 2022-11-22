#### LEZIONE 1 8 novembre 2022

library("UsingR")

data("homeprice")
dim(homeprice)
str(homeprice)

mean(homeprice$sale)

dati<-read.csv2("DATASET.csv", header=T)
mean(dati$PGR)

dati2<-read.table("DATASET.csv", sep=";", dec=",", header = T)
mean(dati2$PGR)

# read.csv and read.csv2 are identical to read.table except for the defaults. They are intended for reading ‘comma separated value’ files (‘.csv’) or (read.csv2) the variant used in countries that use a comma as decimal point and a semicolon as field separator.

# Per file xls o xlsx
# my_data <- read.table("nomefile.extension", sep = "\t", header=TRUE)

# library("readxl")
# my_data <- read_excel("my_file.xlsx", sheet = "data")
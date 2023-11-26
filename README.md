# excel-java-iris

This repo contains a java routine to read an Excel workbook.

## Prerequisites
Make sure you have [git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git) and [Docker desktop](https://www.docker.com/products/docker-desktop) installed.

## Installation: Docker
Clone/git pull the repo into any local directory

```
$ git clone https://github.com/oliverwilms/excel-java-iris.git
```

Open the terminal in this directory and run:

```
$ docker-compose up -d
```

## Run java code to read an excel file

```
$ docker-compose exec -it iris java IRISNative
```

Press Enter to accept the default /opt/irisapp/excel/money.xls.

```
>>> Input File [/opt/irisapp/excel/money.xls]:
I got a label a1
I got a number 2
I got a number 11
I got a label b1
I got a number 3
I got a number 12
I got a label c1
I got a number 4
I got a number 13
I got a label d1
I got a number 5
I got a number 14
I got a label e1
I got a number 6
I got a number 15
I got a label f1
I got a number 7
I got a number 16
I got a label g1
I got a number 8
I got a number 17
I got a label h1
I got a number 9
I got a number 18
I got a label i1
I got a number 10
I got a number 19
I got a label j1
I got a number 11
```

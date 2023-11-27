# excel-java-iris

This repo contains a java routine to read data from Excel 95, 97, 2000, XP, and 2003 workbooks and write the data into IRIS globals using Java Native API library.

Install iris-globals-contest package via ZPM package deployment. This provides persistent class for transactions and CSP pages.

New otw.iris.excel class imports data from ^excel global into dc.iris.transact table.

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

## Run java code to read an excel file and populate IRIS global

```
$ docker-compose exec -it iris java IRISNative
```

Press Enter to accept the default /opt/irisapp/excel/money.xls.

```
>>> Input File [/opt/irisapp/excel/money.xls]
```

## IRIS session

```
$ docker-compose exec -it iris iris session iris
```

## Import Excel data from IRIS global into IRIS persistent class

```
USER>w ##class(otw.iris.excel).importExcel()
1
```

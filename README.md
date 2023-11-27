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

![screenshot](https://github.com/oliverwilms/bilder/blob/main/Money_xls.PNG)

## IRIS session

```
$ docker-compose exec -it iris iris session iris
```

## See data from Excel workbook in IRIS global

```
USER>zw ^excel
^excel(0,0,0)="Date"
^excel(0,0,1)="Check"
^excel(0,0,2)="Merchant"
^excel(0,0,3)="Category"
^excel(0,0,4)="SubCategory"
^excel(0,0,5)="Memo"
^excel(0,0,6)="Credit"
^excel(0,0,7)="Debit"
^excel(0,0,8)="Account"
^excel(0,0,9)="Status"
^excel(0,1,0)="27-Nov"
^excel(0,1,1)=101
^excel(0,1,2)="Landlord"
^excel(0,1,3)="Business Expense"
^excel(0,1,4)="Rent"
^excel(0,1,5)="December"
^excel(0,1,7)=250
^excel(0,1,8)="Checking"
^excel(0,2,0)="27-Nov"
^excel(0,2,2)="Aldi"
^excel(0,2,3)="Groceries"
^excel(0,2,5)=16
^excel(0,2,7)=54.35
^excel(0,2,8)="Checking"
```

## Import Excel data from IRIS global into IRIS persistent class

```
USER>w ##class(otw.iris.excel).importExcel(-1)
1
```

I added -1 above because I want to start importing with sheet 0. In my personal Excel workbook, I want to skip the first five workbooks.

## See data from Excel workbook in IRIS SQL table

![screenshot](https://github.com/oliverwilms/bilder/blob/main/Capture_SQL.PNG)

ARG IMAGE=intersystemsdc/iris-community
FROM $IMAGE

USER root

WORKDIR /opt/irisapp
RUN chown ${ISC_PACKAGE_MGRUSER}:${ISC_PACKAGE_IRISGROUP} /opt/irisapp

# Fix interactive input issues
RUN echo 'debconf debconf/frontend select Noninteractive' | debconf-set-selections

# Install Java 8
RUN apt-get update
RUN apt-get install -y openjdk-8-jdk
RUN apt-get install -y ant
RUN apt-get clean

# Setup for docker commandline
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64/
RUN export JAVA_HOME
ENV JRE_HOME /usr/lib/jvm/java-8-openjdk-amd64/
RUN export JRE_HOME
ENV CLASSPATH .:/usr/irissys/dev/java/lib/1.8/*
RUN export CLASSPATH

# COPY --chown=${ISC_PACKAGE_MGRUSER}:${ISC_PACKAGE_IRISGROUP} csp csp
# COPY --chown=${ISC_PACKAGE_MGRUSER}:${ISC_PACKAGE_IRISGROUP} excel excel
# COPY --chown=${ISC_PACKAGE_MGRUSER}:${ISC_PACKAGE_IRISGROUP} java java
COPY --chown=${ISC_PACKAGE_MGRUSER}:${ISC_PACKAGE_IRISGROUP} *.jar /usr/irissys/dev/java/lib/1.8/
COPY --chown=${ISC_PACKAGE_MGRUSER} . .
# COPY --chown=${ISC_PACKAGE_MGRUSER}:${ISC_PACKAGE_IRISGROUP} *.sh .

RUN chmod ugo+x /opt/irisapp/*.sh

USER ${ISC_PACKAGE_MGRUSER}

RUN pip3 install -r requirements.txt

COPY module.xml module.xml
COPY iris.script iris.script

RUN iris start IRIS && iris session IRIS < iris.script && iris stop IRIS quietly && \ 
  cp /usr/irissys/csp/irisapp/* /usr/irissys/csp/user/ && \ 
  /opt/irisapp/java.sh

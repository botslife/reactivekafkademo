FROM ubuntu:latest
RUN apt update
RUN apt upgrade
RUN apt install -y curl
RUN curl -L --http1.1 https://cnfl.io/cli 
RUN sh -s -- -b /usr/local/bin

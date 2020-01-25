#!/bin/bash

for i in {1..10}
do   
   curl "localhost:9000/blockingtask/$i" &
done
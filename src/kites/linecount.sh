#!/bin/bash

cd exceptions
echo -n "Exceptions: "
cat * | wc -l
cd ../logic
echo -n "Logic: "
cat * | wc -l
cd ../TRSModel
echo -n "Model: "
cat * | wc -l
cd ../visual
echo -n "Visual: "
cat * | wc -l
cd ..

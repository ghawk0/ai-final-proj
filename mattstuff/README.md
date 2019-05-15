Things to install before hand:
To install Pyomo:
conda install -c conda-forge pyomo

To install random name generator:
pip install names


#Step 1

Run python3 inputgenerator.py

#Step 2

cd team-assignment 

cd src

java CreateDivisions div-input.txt

#Step 3

cd .

cd .

(working directory is mattstuff)

cd solution

python3 createdivs.py

#Step 4

pyomo solve --solver=glpk comprating.py compdata{currentdivisionnumber}.dat

#Step 5

python3 parseresults.py {currentdivisionnumber}

#Step 6

python3 teammaking.py teams.txt {currentdivisionnumber}




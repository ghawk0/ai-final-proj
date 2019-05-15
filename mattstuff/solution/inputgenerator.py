"""
Script to generate sample input for the problem of organizing and scheduling a pickup
ultimate game. Writes # of players, # of fields and p1, ..., pn (permissible team sizes) to file div-input.txt.
Writes # of fields needed to field-input.txt
Pickles a dictionary in file playerof player numbers to player info. This dictionary has format
Player num -> [Name, gender, preferred gender matchup, competitiveness]
"""

import random
import names
import pickle

# total number of players
n = random.randint(50, 100)

# total number of fields
m = random.randint(3, 6)

# maximum number of allowable divisions
k = random.randint(3, 5)

# sample set of permissible team sizes
team_sizes = [5, 6, 7]

with open('div-input.txt', 'w+') as f:
    f.write("{}\n{}\n".format(n, k))

    for team_size in team_sizes:
        f.write("{}\n".format(team_size))

with open('field-input.txt', 'w+') as f:
    f.write("{}".format(m))

# percent of players that are female
f = random.randint(0, 23)/100

# percent of players that are NB
nb = random.randint(0, 5)/100
a = random.randint(40, 70)/100
b = f + nb
c = n*(1-b)

people = {}
for i in range(1, n+1):
    people[i] = []
    
for i in range(1, int(c)):
    people[i].append(names.get_first_name(gender='male'))
    people[i].append('M')
    if random.randint(0,100)/100 < a:
        people[i].append('M')
    else:
        people[i].append('A')
    people[i].append(random.randint(1,10))
    
for i in range(int(c), int(c+n*f)):
    people[i].append(names.get_first_name(gender='female'))
    people[i].append('F')
    if random.randint(0,100)/100 < a:
        people[i].append('F')
    else:
        people[i].append('A')
    people[i].append(random.randint(1,10))
        
for i in range(int(c+n*f), n+1):
    if i % 2 == 0:
        people[i].append(names.get_first_name(gender='female'))
    else:
        people[i].append(names.get_first_name(gender='male'))
    people[i].append('NB')
    if random.randint(0, 100)/100 < a:
        people[i].append('A')
    elif random.randint(0, 100)/100 < 0.8:
        people[i].append('F')
    else:
        people[i].append('M')
    people[i].append(random.randint(1, 10))
    
    
with open('player_info.pickle', 'wb') as f:
    pickle.dump(people, f)